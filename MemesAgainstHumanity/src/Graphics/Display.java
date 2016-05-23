package Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Game.Card;
import Game.Game;
import Game.Player;

public class Display extends JPanel implements ActionListener,MouseListener{
	
	public static JFrame frame;
	public static final int WIDTH=1000,HEIGHT=600;
	private static final int fps = 30;
	
	public static Display display;
	static Timer timer;
	
	Game g;
	
	public static void main(String[] args){
		
	}
	
	public Display(String server){
		frame=new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display=this;
		
		g=new Game(server);
		timer=new Timer((int) (1000/fps),display);
		timer.start();
		
		frame.add(display);
		frame.addMouseListener(display);
		display.setVisible(true);
		frame.validate();
	}

	public void actionPerformed(ActionEvent arg0) {
		
		Point mouse=frame.getMousePosition();
		if(mouse!=null)
			g.step(mouse);
		
		repaint();
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		this.g.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		g.click(frame.getMousePosition());
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static Rectangle drawFitToRectangle(BufferedImage bi,Rectangle r,Graphics g){
		return drawFitToRectangle(bi,(Rectangle2D)r,g);
	}
	
	public static Rectangle drawFitToRectangle(BufferedImage sprite,Rectangle2D rect,Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		int sWidth=sprite.getWidth(),sHeight=sprite.getHeight();
		double sRatio=sHeight/(sWidth*1.00);
		
		int tWidth=(int) rect.getWidth(),tHeight=(int) rect.getHeight();
		double tRatio=tHeight/(tWidth*1.00);
		
		if(sRatio<tRatio){
			int width=(int) rect.getWidth();
			int height=(int) (rect.getWidth()/Math.pow(sRatio, -1));
			g2.drawImage(sprite, (int)rect.getCenterX()-width/2, (int)rect.getCenterY()-height/2,width,height,null);
			return new Rectangle((int)rect.getCenterX()-width/2, (int)rect.getCenterY()-height/2,width,height);
		}
		else {
			int height=(int) rect.getHeight();
			int width=(int) (rect.getHeight()/sRatio);
			g2.drawImage(sprite, (int) (rect.getCenterX()-width/2.0), (int)rect.getCenterY()-height/2,width,height,null);
			return new Rectangle((int)rect.getCenterX()-width/2, (int)rect.getCenterY()-height/2,width,height);
		}
	}

	public static Rectangle2D drawFitToRectangle(BufferedImage sprite, Rectangle2D rect) {
		int sWidth=sprite.getWidth(),sHeight=sprite.getHeight();
		double sRatio=sHeight/(sWidth*1.00);
		
		int tWidth=(int) rect.getWidth(),tHeight=(int) rect.getHeight();
		double tRatio=tHeight/(tWidth*1.00);
		
		if(sRatio<tRatio){
			int width=(int) rect.getWidth();
			int height=(int) (rect.getWidth()/Math.pow(sRatio, -1));
			return new Rectangle((int)rect.getCenterX()-width/2, (int)rect.getCenterY()-height/2,width,height);
		}
		else {
			int height=(int) rect.getHeight();
			int width=(int) (rect.getHeight()/sRatio);
			return new Rectangle((int)rect.getCenterX()-width/2, (int)rect.getCenterY()-height/2,width,height);
		}
	}
}
