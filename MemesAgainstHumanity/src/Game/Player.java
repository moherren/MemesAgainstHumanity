package Game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Graphics.SpriteHolder;

public class Player {
	ArrayList<Card> hand=new ArrayList<Card>();
	String name="memer";
	int id=-1;
	int score=0;
	
	public Player(String name){
		this.name=name;
		try {
			Card c=new Card(new SpriteHolder(new File("yee/yee.jpg")).get(0));
			hand.add(c);
			c=new Card(new SpriteHolder(new File("yee/yee.jpg")).get(0));
			hand.add(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		int x=150,y=500;
		for(Card c:hand){
			c.setPosition(x,y);
			x+=c.getRectangle().getWidth()+10;
			c.draw(g);
		}
	}
}
