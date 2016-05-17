package networking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SetUp extends JFrame{

	private JPanel panel;
	
	public SetUp(){
		super("Triad Set Up");
		setBackground(Color.WHITE);
//		setPreferredSize(new Dimension(500,500));
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel=new ChoosePanel(this);
		add(panel);
		pack();
		setVisible(true);
	}

	public static void main(String[] args){
		new Thread(){
			public void run(){
				new SetUp();
			}
		}.start();
	}
	
	public void changePanel(JPanel newPanel){
		remove(panel);
		add(newPanel);
		panel=newPanel;
		pack();
	}
}
