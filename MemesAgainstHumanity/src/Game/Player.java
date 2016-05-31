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
	
	Card hover=null;
	BufferedImage pepe;
	public boolean isJudge=false;
	
	public Player(GameCommand gc){
		name=gc.name;
		id=gc.id;
	}
	
	public Player(String name,boolean isClient,int id){
		this.name=name;
		this.isClient=isClient;
		
		try {
			pepe= ImageIO.read(new File("pepes/pepe.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		selected=new Card[]{null,null,null};
	}
	
	public void step(Point p){
		hover=null;
		for(Card c:accessHand(null,0)){
			c.step(p);
			if(c.hover)
				hover=c;
		}
	}
	
	public void draw(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		if(isClient){
			int x=0;
			for(Card c:accessHand(null,0)){
				x+=(int)(c.getRectangle().getWidth()+5);
			}
			x=Display.WIDTH/2-x/2;
		
			int y=Display.frame.getHeight()-150;
			for(Card c:accessHand(null,0)){
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
		c.setOwnerID(id);
		accessHand(c,0);
	}
	
	public void click(Point mouse){
		if(isSelecting){
			selected[curSelect]=null;
			for(Card c:accessHand(null,0)){
				if(c.click(mouse))
					selected[curSelect]=c;
			}
		}
	}
	
	public Card[] getSelected(){
		return selected;
	}

	public void submit(Game g) {
		if(isSelecting)
		if(selected[curSelect]!=null){
			accessHand(selected[curSelect],1);
			curSelect++;
			if(curSelect==selected.length){
				isSelecting=false;
				g.submit(getSelected());
			}
		}
	}
	
	public void reset(Card t){
		selected=new Card[t.templateSize];
		Arrays.fill(selected,null);
		isSelecting=true;
		curSelect=0;
		isJudge=false;
	}
	
	public synchronized Card[] accessHand(Card c,int action){
		if(c!=null){
			if(action==0){
				hand.addCard(c);
			}
			else if(action==1){
				hand.removeCard(c);
			}
		}
		else{
			return hand.getCards();
		}
		return null;
	}

	public Card getHover() {
		
		return null;
	}
}
