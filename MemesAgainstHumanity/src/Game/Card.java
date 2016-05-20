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
	int ownerID=-1;
	
	int[] tempX=new int[1],tempY=new int[1],tempWidth=new int[1],tempHeight=new int[1];
	int templateSize=0;
	
	public Card(BufferedImage sprite){
		this.sprite=sprite;
		sWidth=sprite.getWidth();
		sHeight=sprite.getHeight();
		sRatio=sHeight/(sWidth*1.00);
	}
	
	public Card(BufferedImage sprite, int tX,int tY,int tW,int tH){
		this.sprite=sprite;
		sWidth=sprite.getWidth();
		sHeight=sprite.getHeight();
		sRatio=sHeight/(sWidth*1.00);
		
		this.tempX[0]=tX;
		this.tempY[0]=tY;
		this.tempWidth[0]=tW;
		this.tempHeight[0]=tH;
		templateSize=1;
	}
	
	public Card(BufferedImage sprite, int[] sizes){
		this.sprite=sprite;
		sWidth=sprite.getWidth();
		sHeight=sprite.getHeight();
		sRatio=sHeight/(sWidth*1.00);
		
		templateSize=sizes.length/4;
		tempX=new int[templateSize];
		tempY=new int[templateSize];
		tempWidth=new int[templateSize];
		tempHeight=new int[templateSize];
		for(int i=0;i<templateSize;i++){
			tempX[i]=sizes[i*4];
			tempY[i]=sizes[i*4+1];
			tempWidth[i]=sizes[i*4+2];
			tempHeight[i]=sizes[i*4+3];
		}
	}
	
	public void step(Point mouse){
		Rectangle2D rect=getRectangle();
		boolean curHover=rect.contains(mouse.x, mouse.y);
		long time=System.currentTimeMillis();
		
		if(hover!=curHover){
			if(time-timeHover>resizeTime){
				timeHover=time;
			}
			else{
				timeHover=time-((timeHover+resizeTime)-time);
			}
			hover=curHover;
		}
		
	}
	
	public boolean click(Point mouse){
		Rectangle2D rect=getRectangle();
		
		if(rect.contains(mouse.x, mouse.y))
			selected=true;
		else
			selected=false;
		
		return selected;
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
		
		if(!selected)
			g2.setColor(Color.WHITE);
		else
			g2.setColor(Color.BLUE);
			
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
	
	public void drawBack(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		
		Rectangle2D rect=getRectangle();
		g2.rotate(rotation, x, y);
		
		if(!selected)
			g2.setColor(Color.WHITE);
		else
			g2.setColor(Color.BLUE);
			
		g2.fillRoundRect((int)rect.getMinX(), (int)rect.getMinY(),(int) rect.getWidth(),(int) rect.getHeight(), 14, 14);
		
		g2.setColor(Color.BLACK);
		g2.drawRoundRect((int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getWidth(), (int)rect.getHeight(), 14, 14);
		
		g2.rotate(rotation, x, y);
		
	}
	
	public void setPosition(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public BufferedImage getImage(){
		return sprite;
	}
	
	public void setOwnerID(int id){
		ownerID=id;
	}
}

