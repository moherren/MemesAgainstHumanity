package Game;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import Graphics.SpriteHolder;

public class Game {
	Player player=new Player("bob");
	CardPile deck,discard;
	
	public Game(){
		deck=new CardPile();
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
	}
	
	public void draw(Graphics g){
		player.draw(g);
	}

	public void step(Point mouse) {
		player.step(mouse);
		
	}
}
