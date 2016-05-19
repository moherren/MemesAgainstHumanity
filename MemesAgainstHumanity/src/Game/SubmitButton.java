package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class SubmitButton {
	int x,y,width,height;
	
	public boolean click(Point p,Game g){
		Rectangle rect=new Rectangle(x,y+height,width,height);
		if(rect.contains(p)){
			g.submit();
			System.out.println("click");
			return true;
		}
		return false;
	}
	
	public void setPosition(int x,int y,int width,int height){
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public void draw(Graphics g){
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(Color.DARK_GRAY);
		g2.fill3DRect(x, y, width, height, true);
		g2.setFont(g2.getFont().deriveFont((float)height));
		g2.setColor(Color.BLACK);
		g2.drawString("Submit", x, y+height);
	}
}
