package networking;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Game.Card;
import Game.CardPile;
import Game.GameCommand;
import Graphics.SpriteHolder;

public class Server extends Thread{
	Server server;
	Socket socket;
	HashMap<Socket,ObjectOutputStream> map=new HashMap<Socket,ObjectOutputStream>();
	ServerThread thread;
	int num=0;
	GameCommand[] ids=new GameCommand[]{null,null,null,null};
	
	CardPile deck,discard,templates;
	Card template=null;
	Random r;
	int jID=0;
	
	
	public static void main(String[] args) throws IOException {
		int port = /* Integer.parseInt(args[0]) */4998;
		System.out.println("New server started");
		new Server(port,"George","Michaels",false);
	}
	
	public Server(int port,String gameName,String userName,boolean bm) throws IOException{
		r=new Random();
		deck=new CardPile();
		templates=new CardPile();
		
		loadDeck();
		
		template=templates.drawCard();
		
		discard=new CardPile();
		
		ServerSocket ss=new ServerSocket(port);
		Socket ns=null;
		System.out.println(InetAddress.getLocalHost().getHostName());
		new DiscoveryThread(port,gameName,userName).start();
		while(true){
			try {
				ns = ss.accept();
				changeMap(ns,1);
				thread=new ServerThread(this,ns);
				System.out.println("new Client added!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadDeck(){
		File sourcePath = new File("sourceImages");
		File[] sourceFiles = sourcePath.listFiles();
		for (File i : sourceFiles) {
			try {
				deck.addCard(new Card(new SpriteHolder(i).get(0)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		deck.shuffle();
		
		ArrayList<String> pathArray = new ArrayList<String>();
		
		Scanner scan;
		try {
			File f = new File("TemplatesText");
			scan = new Scanner(f);
			while (scan.hasNextLine()) {
				pathArray.add(scan.nextLine());
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		
		for (int i = 0; i < pathArray.size(); i++) {
			ArrayList<Integer> numnums = new ArrayList<Integer>();

			String[] split = pathArray.get(i).split("~");
			pathArray.set(i, split[0]);
			pathArray.set(i, pathArray.get(i).trim());
			for (int z = 1; z < split.length; z++) {
				numnums.add(Integer.parseInt(split[z]));
			}

			int[] recCoords=new int[numnums.size()];
			
			for (int s = 0; s < numnums.size(); s++) {
				recCoords = new int[numnums.size()];
				for (int f = 0; f < recCoords.length; f++) {
					recCoords[f] = numnums.get(0);
					numnums.remove(0);
				}
				

			}try {
					
					templates.addCard(new Card(ImageIO.read(new File(pathArray.get(i))),recCoords));
				} catch (IOException e) {
					System.out.println(pathArray.get(i));
					e.printStackTrace();
				}
		}
		templates.shuffle();
	}

	public synchronized void sendToAll(GameCommand message) throws IOException, SocketException {
		
		if(true){
		if(message.id==GameCommand.GC_REMOVE_PLAYER)
			ids[message.id]=null;	
		HashMap<Socket,ObjectOutputStream> map=changeMap(null,0);
		ObjectOutputStream[] col=new ObjectOutputStream[map.size()];
		map.values().toArray(col);
		for(int i=0;i<col.length;i++){
			try {
				col[i].writeObject(message);
				col[i].flush();
			} catch (IOException e) {
				e.printStackTrace();
				}
			}
		}
	}

	public void removeConnection(ServerThread st){
		
		try {
			changeMap(st.socket,0);
			ids[st.id]=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Client disconnected!");
	}

	public synchronized HashMap<Socket,ObjectOutputStream> changeMap(Socket ns,int action) throws IOException{
		if(ns!=null){
			if(action==0)
				map.remove(ns);
			else if(action == 1){
				ObjectOutputStream out=new ObjectOutputStream(ns.getOutputStream());
				introduceSocket(out);
				map.put(ns, out);
				map.get(ns).flush();
				}
			}
		
		return (HashMap<Socket, ObjectOutputStream>) map.clone();
	}
	
	public void introduceSocket(ObjectOutputStream out){
		int id=nextID();
		GameCommand newPlayer=GameCommand.createPlayer(id);
		ids[id]=newPlayer;
		try {
			out.writeObject(newPlayer);
			out.writeObject(GameCommand.createTemplate(template,jID));
			for(int i=0;i<7;i++){
				out.writeObject(GameCommand.drawCard(deck.drawCard()));
			}
			for(int i=0;i<ids.length;i++){
				if(ids[i]!=null)
					out.writeObject(ids[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int nextID(){
		for(int i=0;i<ids.length;i++){
			if(ids[i]==null){
				return i;
			}
		}
		return ids.length;
	}
	
	public void newTemplate(){
			template=templates.drawCard();
			GameCommand gm=GameCommand.createTemplate(template,jID);
			try {
				sendToAll(gm);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
class ServerThread extends Thread {

	Server server;
	Socket socket;
	ObjectInputStream input;
	boolean running=true;
	int id=-1;
	
	public ServerThread(Server server,Socket socket) {
		this.server=server;
		this.socket=socket;
		try {
			input=new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	public void run() {
		int i=0;
		try {
			
			while (running) {
				GameCommand gs=(GameCommand)input.readObject();
				if(gs.type==GameCommand.GC_REMOVE_PLAYER){
					running=false;
				}
				if(gs.type==GameCommand.GC_CHOOSE_CARD){
					server.newTemplate();
				}
				else
					server.sendToAll(gs);
				if(gs.id!=-1)
					id=gs.id;
			}
		} catch (EOFException | SocketException ie ) {

		}catch(OptionalDataException ie){
			try {
				server.sendToAll(GameCommand.removePlayer(id));
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (IOException ie) {
			ie.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			removeConnection(this);
			}
		}

	}

}
class DiscoveryThread extends Thread{
	
	private int port;
	private String gameName;
	private String hostName;

	public DiscoveryThread(int port,String gameName,String hostName){
		this.port=port;
		this.gameName=gameName;
		this.hostName=hostName;
	}
	
	public void run(){
		DatagramSocket socket = null;
		try{	
			socket=new DatagramSocket(port,InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);
			while(true){
				System.out.println(getClass().getName()+">>>Ready to receive broadcast packets!");
				byte[] recvBuf=new byte[15000];
				DatagramPacket packet=new DatagramPacket(recvBuf,recvBuf.length);
				socket.receive(packet);
				System.out.println(getClass().getName()+">>>Discovery packet received from: "+packet.getAddress().getHostAddress());
				System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));
				//See if the packet holds the right command (message)
				String message = new String(packet.getData()).trim();
				if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
	        		byte[] sendData = ("DISCOVER_FUIFSERVER_RESPONSE "+gameName+" "+hostName).getBytes();
	        		//Send a response
	        		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
	        		socket.send(sendPacket);
	        		System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			if(socket!=null){
				socket.close();
			}
		}
	}
}