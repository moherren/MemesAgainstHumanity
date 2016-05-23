package networking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Graphics.Display;
import networking.GameServer;

public class HostPanel extends JPanel implements ActionListener{

	SetUp frame;
	
	JPanel infoPanel;
	JPanel buttonPanel;
	JTextField userName,gameName;
	JTextArea response;
	JButton create,start;
	
	public static final int port=2000;
	
	GameServer server;
	
	boolean gameCreated;
	boolean joined;
	
	public HostPanel(SetUp frame){
		this.frame=frame;
//		setPreferredSize(new Dimension(500,500));
		setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		infoPanel=new JPanel();
		infoPanel.setLayout(new GridLayout(3,2));
		infoPanel.add(new JLabel("User Name:"));
		userName=new JTextField(20);
		infoPanel.add(userName);
		infoPanel.add(new JLabel("Game Name:"));
		gameName=new JTextField(20);
		infoPanel.add(gameName);
		c.gridx=0;
		c.gridy=0;
		add(infoPanel,c);
		buttonPanel=new JPanel();
		create=new JButton("Create Game");
		create.addActionListener(this);
		buttonPanel.add(create);
		start=new JButton("Start Game");
		start.addActionListener(this);
		start.setEnabled(false);
		buttonPanel.add(start);
		c.gridy=1;
		add(buttonPanel,c);
		c.gridy=2;
		add(new JLabel(),c);
		response=new JTextArea(5,42);
		response.setEditable(false);
		c.gridy=3;
		add(response,c);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==create){
			if(!gameCreated && !userName.getText().equals("") && !gameName.getText().equals("")){
				response.setText(response.getText()+"creating game ... ");
				createGame();
				response.setText(response.getText()+"created game at host "+server.getAddress()+"\n");
				response.setText(response.getText()+"waiting for other player\n");
				gameCreated=true;
				start.setEnabled(true);
			}
		}
		if(e.getSource()==start){
			if(gameCreated){
				frame.dispose();
				server.startGame();
				System.out.println("halmflksmdlfkmsdlkfmsdklfmdsklmf");
				startGame();
			}
		}
	}
	
	public void createGame(){
		server=new GameServer(this,port);
		server.start();
		gameCreated=true;
	}
	
	public void startGame(){
		//set up the game
		frame.dispose();
		try {
			new Display(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		gameCreated=true;
	}
	
	public boolean connectPlayer(String playerName){
		if(JOptionPane.showConfirmDialog(null, playerName+" would like to join. Confirm?", "Join?", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
			response.setText(response.getText()+"\n"+playerName+" joined");
			joined=true;
			return true;
		}
		else{
			return false;
		}
	}

	public String getGameName() {
		return gameName.getText();
	}

	public String getUserName() {
		return userName.getText();
	}
}
