package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Graphics.Display;
import Graphics.SpriteHolder;

public class Game {
	Player player=new Player("bob",true);
	CardPile deck,discard,templates;
	
	BufferedImage templateDisplay;
	Card template;
	ArrayList<Player> players=new ArrayList<Player>();
	
	SubmitButton sb=new SubmitButton();
	
	public Game(){
		deck=new CardPile();
		templates=new CardPile();
		loadDeck();
		for(int i=0;i<7;i++){
			player.addToHand(deck.drawCard());
		}
		
		template=templates.drawCard();
		templateDisplay=drawTemplate(template);
		
		player.reset(template);
		
		discard=new CardPile();
		
	}
	
	public void loadDeck(){
		File sourcePath = new File("sourceImages");
		File[] sourceFiles = sourcePath.listFiles();
		for (File i : sourceFiles) {
			try {
				deck.addCard(new Card(new SpriteHolder(i).get(0)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		deck.shuffle();
		
		ArrayList<String> pathArray = new ArrayList<String>();
		
		Scanner scan;
		try {
			File f = new File("TemplatesText");
			scan = new Scanner(f);
			while (scan.hasNextLine()) {
				pathArray.add(scan.nextLine());
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		
		for (int i = 0; i < pathArray.size(); i++) {
			ArrayList<Integer> numnums = new ArrayList<Integer>();

			String[] split = pathArray.get(i).split("~");
			pathArray.set(i, split[0]);
			pathArray.set(i, pathArray.get(i).trim());
			for (int z = 1; z < split.length; z++) {
				numnums.add(Integer.parseInt(split[z]));
			}

			int[] recCoords=new int[numnums.size()];
			
			for (int s = 0; s < numnums.size(); s++) {
				recCoords = new int[numnums.size()];
				for (int f = 0; f < recCoords.length; f++) {
					recCoords[f] = numnums.get(0);
					numnums.remove(0);
				}
				

			}try {
					
					templates.addCard(new Card(ImageIO.read(new File(pathArray.get(i))),recCoords));
				} catch (IOException e) {
					System.out.println(pathArray.get(i));
					e.printStackTrace();
				}
		}
		templates.shuffle();
	}
	
	public void draw(Graphics g){
		player.draw(g);
		Rectangle rect=new Rectangle(10,10,(int)(Display.frame.getWidth()*0.5-15),(int)(Display.frame.getHeight()*0.6-15));
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
		Display.drawFitToRectangle(templateDisplay, rect,g);
		player.drawStats(g, (int)(rect.getWidth()+10), 10, (int)(Display.frame.getWidth()-(rect.getWidth()+10)), 24);
		
		sb.setPosition((int)(rect.getWidth()+10), (int)(Display.frame.getHeight()*0.6-48), (int)(Display.frame.getWidth()-(rect.getWidth()+10)),32);
		sb.draw(g);
	}

	public void step(Point mouse) {
		player.step(mouse);
		
	}

	public void click(Point mouse) {
		if(!sb.click(mouse, this)){
			player.click(mouse);
			templateDisplay=drawTemplate(template);
		}
	}
	
	public BufferedImage drawTemplate(Card c){
		BufferedImage bi=c.getImage();
		BufferedImage template=new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g=template.createGraphics();
		g.drawImage(bi,0,0,null);
		for(int i=0;i<c.templateSize;i++){
			Rectangle2D rect=new Rectangle2D.Double(c.tempX[i],c.tempY[i],c.tempWidth[i],c.tempHeight[i]);
			
			if(player.getSelected()[i]!=null){
				Display.drawFitToRectangle(player.getSelected()[i].getImage(), rect,g);
			}
		}
		return template;
	}

	public void submit() {
		player.submit();
		
	}
}
