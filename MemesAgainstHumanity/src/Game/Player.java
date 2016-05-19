package Game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.event.ListSelectionEvent;

import Graphics.Display;
import Graphics.SpriteHolder;

public class Player {
	CardPile hand=new CardPile();
	String name="memer";
	int id=-1;
	int score=0;
	Card[] selected;
	boolean isClient=false;
	int curSelect=0;
	boolean isSelecting=true;
	
	BufferedImage pepe;
	
	public Player(String name,boolean isClient){
		this.name=name;
		this.isClient=isClient;
		
		try {
			pepe= ImageIO.read(new File("pepes/pepe.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		selected=new Card[]{null,null,null};
	}
	
	public void step(Point p){
		for(Card c:hand.getCards())
			c.step(p);
	}
	
	public void draw(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		if(isClient){
			int x=0;
			for(Card c:hand.getCards()){
				x+=(int)(c.getRectangle().getWidth()+5);
			}
			x=Display.WIDTH/2-x/2;
		
			int y=Display.frame.getHeight()-150;
			for(Card c:hand.getCards()){
				x+=c.getRectangle().getWidth()/2+5;
				c.setPosition(x,y);
				x+=c.getRectangle().getWidth()/2+5;
				c.draw(g);
			}
		}
	}
	
	public void drawStats(Graphics g, int x, int y,int width,int height){
		Rectangle rect=new Rectangle(x,y,height,height);
		
		Display.drawFitToRectangle(pepe, rect,g);
		
		g.setFont(g.getFont().deriveFont((float)height));
		g.drawString(name, x+height+10, y+height);
	}
	
	public void addToHand(Card c){
		hand.addCard(c);
		c.setOwnerID(id);
	}
	
	public void click(Point mouse){
		if(isSelecting){
			selected[curSelect]=null;
			for(Card c:hand.getCards()){
				if(c.click(mouse))
					selected[curSelect]=c;
			}
		}
	}
	
	public Card[] getSelected(){
		return selected;
	}

	public void submit() {
		if(isSelecting)
		if(selected[curSelect]!=null){
			hand.removeCard(selected[curSelect]);
			curSelect++;
			if(curSelect==selected.length){
				isSelecting=false;
			}
		}
	}
	
	public void reset(Card t){
		selected=new Card[t.templateSize];
		Arrays.fill(selected,null);
		isSelecting=true;
		curSelect=0;
	}
}
