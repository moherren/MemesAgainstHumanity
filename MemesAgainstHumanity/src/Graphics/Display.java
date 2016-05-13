package Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	
	Game g=new Game();
	
	public static void main(String[] args){
		frame=new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		
		display=new Display();
		
		timer=new Timer((int) (1000/fps),display);
		timer.start();
		
		frame.add(display);
		frame.addMouseListener(display);
		display.setVisible(true);
		frame.validate();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Display(){

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
