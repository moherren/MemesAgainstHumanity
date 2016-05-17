package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameServer extends Thread {

	private int port;
	private ServerSocket server;
	private HostPanel host;
	private Socket soc;
	private boolean connected=false;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private String address;
	private DiscoveryThread connector;

	public GameServer(HostPanel host, int port){
		this.host = host;
		this.port = port;
		try {
			server = new ServerSocket(port);
			System.out.println("Server waiting at "
					+ InetAddress.getLocalHost() + " on Port " + port+server+InetAddress.getLocalHost().getHostName());
			System.out.println(InetAddress.getByName(InetAddress.getLocalHost().getHostName()));
			address=InetAddress.getLocalHost().getHostName();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run(){
		Socket soc=null;
		connected=false;
		while(!connected){
			try{
				connector=new DiscoveryThread();
				connector.start();
				soc = server.accept();
				oos=new ObjectOutputStream(soc.getOutputStream());
				oos.writeObject(host.getGameName());
				System.out.println("Connection Requested");
				ois=new ObjectInputStream(soc.getInputStream());
				String playerName=(String)ois.readObject();
				if(host.connectPlayer(playerName)){
					oos.writeObject(Boolean.TRUE);
					connected=true;
				}
				else{
					oos.writeObject(Boolean.FALSE);
				}
			} catch (IOException | ClassNotFoundException error) {
				System.out.println(error);
			}
		}
	}
	
	public class DiscoveryThread extends Thread{
		public void run(){
			try{
				DatagramSocket socket=new DatagramSocket(port,InetAddress.getByName("0.0.0.0"));
				socket.setBroadcast(true);
				while(!connected){
					System.out.println(getClass().getName()+">>>Ready to receive broadcast packets!");
					byte[] recvBuf=new byte[15000];
					DatagramPacket packet=new DatagramPacket(recvBuf,recvBuf.length);
					socket.receive(packet);
					System.out.println(getClass().getName()+">>>Discovery packet received from: "+packet.getAddress().getHostAddress());
					System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));
					//See if the packet holds the right command (message)
					String message = new String(packet.getData()).trim();
					if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
		        		byte[] sendData = ("DISCOVER_FUIFSERVER_RESPONSE "+host.getGameName()+" "+host.getUserName()).getBytes();
		        		//Send a response
		        		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
		        		socket.send(sendPacket);
		        		System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
					}
				}
				socket.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public void startGame(){
		
	}
	
	public String getAddress(){
		return address;
	}
}
