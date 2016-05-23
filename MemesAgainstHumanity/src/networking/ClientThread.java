package networking;

import java.io.IOException;

import javax.swing.JOptionPane;

import Game.Game;

public class ClientThread implements Runnable{
	boolean running=true;
	Game game;
	
	public ClientThread(Game m){
		game=m;
	}
	
	public void start(){
		Thread t=new Thread(this);
		t.start();
	}
	
	public void stop(){
		running=false;
	}
	
	public void run() {
		while(running){
			try {
				game.recieveState();
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(null,e.toString()+"\nDisconnected","Error",JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				e.printStackTrace();
			}
		}
	}

}
