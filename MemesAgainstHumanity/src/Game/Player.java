package Game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Graphics.Display;
import Graphics.SpriteHolder;

public class Player {
	ArrayList<Card> hand=new ArrayList<Card>();
	String name="memer";
	int id=-1;
	int score=0;
	
	public Player(String name){
		this.name=name;
	}
	
	public void step(Point p){
		for(Card c:hand)
			c.step(p);
	}
	
	public void draw(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		int x=0;
		for(Card c:hand){
			x+=(int)(c.getRectangle().getWidth()+5);
		}
		x=Display.WIDTH/2-x/2;
		
		int y=450;
		for(Card c:hand){
			x+=c.getRectangle().getWidth()/2+5;
			c.setPosition(x,y);
			x+=c.getRectangle().getWidth()/2+5;
			c.draw(g);
		}
	}
	
	public void addToHand(Card c){
		hand.add(c);
	}
}
