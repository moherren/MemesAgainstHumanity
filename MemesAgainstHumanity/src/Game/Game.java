package Game;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Graphics.SpriteHolder;

public class Game {
	Player player=new Player("bob");
	CardPile deck,discard,templates;
	
	Card template;
	
	public Game(){
		deck=new CardPile();
		templates=new CardPile();
		loadDeck();
		for(int i=0;i<7;i++){
			player.addToHand(deck.drawCard());
		}
		
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

			int[] recCoords=new int[4];
			
			for (int s = 0; s < numnums.size(); s++) {
				recCoords = new int[4];
				for (int f = 0; f < recCoords.length; f++) {
					recCoords[f] = numnums.get(0);
					numnums.remove(0);
				}
				

			}try {
					
					templates.addCard(new Card(ImageIO.read(new File(pathArray.get(i))),recCoords[0], recCoords[1], recCoords[2], recCoords[3]));
				} catch (IOException e) {
					System.out.println(pathArray.get(i));
					e.printStackTrace();
				}
		}
		templates.shuffle();
	}
	
	public void draw(Graphics g){
		player.draw(g);
	}

	public void step(Point mouse) {
		player.step(mouse);
		
	}

	public void click(Point mouse) {
		player.click(mouse);
	}
	
}
