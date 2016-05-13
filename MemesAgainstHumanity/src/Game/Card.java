package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Card {
	BufferedImage sprite;
	int sWidth,sHeight;
	double sRatio;
	public double rotation=0;
	
	public int x,y,z;
	public int width=100, height=140;
	
	boolean selected=false;
	boolean hover=false;
	long timeHover=0;
	long resizeTime=100;
	
	public Card(BufferedImage sprite){
		this.sprite=sprite;
		sWidth=sprite.getWidth();
		sHeight=sprite.getHeight();
		sRatio=sHeight/(sWidth*1.00);
	}
	
	public void step(Point mouse){
		Rectangle2D rect=getRectangle();
		boolean curHover=rect.contains(mouse.x, mouse.y);
		long time=System.currentTimeMillis();
		
		if(hover!=curHover){
			System.out.println("mah");
			if(time-timeHover>resizeTime){
				timeHover=time;
			}
			else{
				timeHover=time-((timeHover+resizeTime)-time);
			}
			hover=curHover;
		}
		
	}
	
	public Rectangle2D getRectangle(){
		long time=System.currentTimeMillis();
		double multiplier=Math.max(1, Math.min(2,(time-timeHover)/(resizeTime*1.0)+1));
		if(!hover)
			multiplier=Math.max(1, Math.min(2,2-(time-timeHover)/(resizeTime*1.0)));
		
		int width=(int) (this.width*multiplier),height=(int) (this.height*multiplier);
		Rectangle2D rect=new Rectangle2D.Double(x-width/2.0,y-height/2.0,width,height);
		return rect;
	}
	
	public void draw(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		
		Rectangle2D rect=getRectangle();
		g2.rotate(rotation, x, y);
		g2.setColor(Color.WHITE);
		g2.fillRoundRect((int)rect.getMinX(), (int)rect.getMinY(),(int) rect.getWidth(),(int) rect.getHeight(), 14, 14);
		
		if(sRatio<1){
			
		int width=(int) rect.getWidth();
		int height=(int) (rect.getWidth()/Math.pow(sRatio, -1));
		g2.drawImage(sprite, (int)rect.getCenterX()-width/2, (int)rect.getCenterY()-height/2,width,height,null);
		}
		else{
			int height=(int) rect.getWidth();
			int width=(int) (rect.getWidth()/sRatio);
			g2.drawImage(sprite, (int) (rect.getCenterX()-width/2.0), (int)rect.getCenterY()-height/2,width,height,null);
		}
		
		g2.setColor(Color.BLACK);
		g2.drawRoundRect((int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getWidth(), (int)rect.getHeight(), 14, 14);
		
		g2.rotate(rotation, x, y);
		
	}
	
	public void setPosition(int x,int y){
		this.x=x;
		this.y=y;
	}
}
