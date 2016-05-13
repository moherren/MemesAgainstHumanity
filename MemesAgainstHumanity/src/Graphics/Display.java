package Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Game.Card;
import Game.Player;

public class Display extends JPanel implements ActionListener{
	
	public static JFrame frame;
	public static final int WIDTH=800,HEIGHT=600;
	private static final int fps = 30;
	
	public static Display display;
	static Timer timer;
	
	Player p=new Player("Bill");
	
	public static void main(String[] args){
		frame=new JFrame();
		frame.setSize(800, 600);
		frame.setVisible(true);
		
		display=new Display();
		
		timer=new Timer((int) (1000/fps),display);
		timer.start();
		
		frame.add(display);
		display.setVisible(true);
		frame.validate();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Display(){

	}

	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	
	public void paintComponent(Graphics g){
		p.draw(g);
	}
}
