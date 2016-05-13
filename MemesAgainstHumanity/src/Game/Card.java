package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	
	
	public Card(BufferedImage sprite){
		this.sprite=sprite;
		sWidth=sprite.getWidth();
		sHeight=sprite.getHeight();
		sRatio=sHeight/(sWidth*1.00);
	}
	
	public Rectangle2D getRectangle(){
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
			
		int width=this.width;
		int height=(int) (width/Math.pow(sRatio, -1));
		g2.drawImage(sprite, (int)rect.getCenterX()-width/2, (int)rect.getCenterY()-height/2,width,height,null);
		}
		else{
			int height=this.width;
			int width=(int) (height/sRatio);
			g2.drawImage(sprite, (int) (rect.getCenterX()-width/2.0), (int)rect.getCenterY()-height/2,width,height,null);
		}
		
		g2.setColor(Color.BLACK);
		g2.drawRoundRect((int)rect.getMinX(), (int)rect.getMinY(), width, height, 14, 14);
		
		g2.rotate(rotation, x, y);
		
	}
	
	public void setPosition(int x,int y){
		this.x=x;
		this.y=y;
	}
}
