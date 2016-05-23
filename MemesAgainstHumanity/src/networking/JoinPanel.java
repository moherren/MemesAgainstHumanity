package networking;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.*;

import Graphics.Display;

@SuppressWarnings("serial")
public class JoinPanel extends JPanel implements ActionListener{

	SetUp frame;
	
	JPanel infoPanel;
	JPanel buttonPanel;
	JTextField userName,hostName;
	JTextArea response;
	JButton find,join;
	DefaultListModel<String> serverListModel;
	JList<String> serverList;
	
	ArrayList<String[]> servers;
	
	Socket soc;
	
	public static final int port=2000;
	
	boolean joinedGame=false;
	boolean waiting=false;

	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;
	
	public JoinPanel(SetUp frame){
		this.frame=frame;
//		setPreferredSize(new Dimension(500,500));
		setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		infoPanel=new JPanel();
		infoPanel.setLayout(new GridLayout(1,1));
		infoPanel.add(new JLabel("User Name:"));
		userName=new JTextField(20);
		infoPanel.add(userName);
		c.gridx=0;
		c.gridy=0;
		add(infoPanel,c);
		buttonPanel=new JPanel();
		find=new JButton("Find Games");
		find.addActionListener(this);
		buttonPanel.add(find);
		join=new JButton("Join Game");
		join.addActionListener(this);
		buttonPanel.add(join);
		c.gridy=1;
		add(buttonPanel,c);
		c.gridy=2;
		add(new JLabel("Host                               Game                                    "),c);
		c.gridy=3;
		serverListModel=new DefaultListModel<String>();
		serverList=new JList<String>(serverListModel);
		
		serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serverList.setVisibleRowCount(5);
		add(new JScrollPane(serverList),c);
		response=new JTextArea(5,42);
		response.setEditable(false);
		c.gridy=4;
		add(response,c);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==find){
//			if(!joinedGame && !userName.getText().equals("")&&!hostName.getText().equals("")){
			if(!joinedGame && !waiting){
				try{
					System.out.println(InetAddress.getLoopbackAddress());
					servers=discoverHosts();
					serverListModel.removeAllElements();
					for(String[] server:servers){
						String string=server[1];
						for(int i=0;i<33-server[1].length();i++){
							string+=" ";
						}
						string+=server[0];
						serverListModel.addElement(string);
					}
					if(servers.size()>0){
						for(String[] server:servers)
							System.out.println(Arrays.toString(server));
					}
					else{
						System.out.println("No servers found");
						response.setText("No game found");
						return;
					}
				} catch(Exception e1){
					e1.printStackTrace();
					response.setText("No game found");
				}
			}
		}
		else if(e.getSource()==join){
			if(!joinedGame&&!waiting&&!userName.getText().equals("")){
				try{
					int index=serverList.getSelectedIndex();
					if(index<0)
						return;
					String[] server=servers.get(index);
					soc=new Socket(InetAddress.getByName(server[2]),port);
					ois=new ObjectInputStream(soc.getInputStream());
					String game=(String)ois.readObject();
					response.setText(response.getText()+"\nRequesting Join: "+server[0]+" hosted by "+server[1]);
					response.setText(response.getText()+"\nWaiting for response...");
					waiting=true;
					oos=new ObjectOutputStream(soc.getOutputStream());
					oos.writeObject(userName.getText());
					//start a thread that will listen for a response
					new ResponseThread(server).start();
				}catch(IOException | ClassNotFoundException e1){
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void startGame(String host){
		//set up the game
		frame.dispose();
		new Display(host);
	}
	
	public ArrayList<String[]> discoverHosts(){
		try{
			DatagramSocket c=new DatagramSocket();
			c.setBroadcast(true);
			c.setSoTimeout(100);
			byte[] sendData="DISCOVER_FUIFSERVER_REQUEST".getBytes();
			try{
				DatagramPacket sendPacket=new DatagramPacket(sendData,sendData.length,InetAddress.getByName("255.255.255.255"),port);
				c.send(sendPacket);
				System.out.println(getClass().getName()+">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
			}catch(Exception e){
			}
			Enumeration<NetworkInterface> interfaces=NetworkInterface.getNetworkInterfaces();
			while(interfaces.hasMoreElements()){
				NetworkInterface networkInterface=interfaces.nextElement();
				if(networkInterface.isLoopback()||networkInterface.isUp())
					continue;
				for(InterfaceAddress interfaceAddress:networkInterface.getInterfaceAddresses()){
					InetAddress broadcast=interfaceAddress.getBroadcast();
					if(broadcast==null)
						continue;
					try{
						DatagramPacket sendPacket=new DatagramPacket(sendData,sendData.length,InetAddress.getByName("255.255.255.255"),port);
						c.send(sendPacket);
						System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
					}catch(Exception e){
					}
				}
			}
			System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");
			System.out.println(c.getSoTimeout());
			ArrayList<String[]> servers=new ArrayList<String[]>();
			boolean timeout=false;
			while(!timeout){
				try{
					byte[] recvBuf=new byte[15000];
					DatagramPacket receivePacket=new DatagramPacket(recvBuf,recvBuf.length);
					c.receive(receivePacket);
					System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());
					String message=new String(receivePacket.getData()).trim();
					if(message.contains("DISCOVER_FUIFSERVER_RESPONSE")){
						String[] split=message.split(" ");
						servers.add(new String[]{split[1],split[2],receivePacket.getAddress().getHostName()});
					}
				} catch(SocketTimeoutException e){
					timeout=true;
				}
			}
			c.close();
			return servers;
		} catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	private class ResponseThread extends Thread{
		
		String[] server;
		
		public ResponseThread(String[] server){
			this.server=server;
		}
		public void run(){
			try{
				if((Boolean)(ois.readObject())==true){
					response.setText(response.getText()+"\nJoined game");
					joinedGame=true;
					waiting=false;
					boolean end=false;
					while(!end){
						try {
							Object w=ois.readObject();
							if(w!=null){
								end=true;
								startGame(server[2]);
							}
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
				else{
					waiting=false;
					response.setText(response.getText()+"\nJoin Denied");
				}
			} catch(IOException | ClassNotFoundException e){
				e.printStackTrace();
				waiting=false;
			}
		}
	}
	
}
