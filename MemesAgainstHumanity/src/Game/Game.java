package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Graphics.Display;
import Graphics.SpriteHolder;

public class Game {
	Player player=new Player("bob",true,0),judge;
	
	public static final int port=4998;
	
	BufferedImage templateDisplay;
	Card template;
	ArrayList<Player> players=new ArrayList<Player>();
	CardPile submittedCards=new CardPile();
	
	SubmitButton sb=new SubmitButton();

	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	public Game(String host,String userName){
		
		try {
			socket=new Socket(InetAddress.getByName(host),port);
			out=new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in= new ObjectInputStream(socket.getInputStream());
			GameCommand com=(GameCommand)in.readObject();
			player=new Player(com);
			sendState(com);
			boolean isHost=false;
			if(InetAddress.getLocalHost()==InetAddress.getByName(host))
				isHost=true;
			player=new Player(userName,isHost,com.id);
			sendState(GameCommand.introducePlayer(player.id, userName));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendState(GameCommand com) {
		try {
			out.writeObject(com);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g){
		if(showHand())
			player.draw(g);
		else
			drawJudge(g);
		Rectangle rect=new Rectangle(10,10,(int)(Display.frame.getWidth()*0.5-15),(int)(Display.frame.getHeight()*0.6-15));
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
		if(showHand()&&templateDisplay!=null)
			Display.drawFitToRectangle(templateDisplay, rect,g);
		else if(getHoverCard()!=null)
			Display.drawFitToRectangle(getHoverCard().getImage(), rect,g);
		player.drawStats(g, (int)(rect.getWidth()+10), 10, (int)(Display.frame.getWidth()-(rect.getWidth()+10)), 24);
		
		int dy=1;
		for(Player p:getPlayers()){
			player.drawStats(g, (int)(rect.getWidth()+10), 10+dy*12, (int)(Display.frame.getWidth()-(rect.getWidth()+10)), 24);
			dy++;
		}
		
		sb.setPosition((int)(rect.getWidth()+10), (int)(Display.frame.getHeight()*0.6-48), (int)(Display.frame.getWidth()-(rect.getWidth()+10)),32);
		sb.draw(g);
	}

	public boolean showHand(){
		return (player.isSelecting&&!player.isJudge);
	}
	
	private Card getHoverCard() {		
		if(showHand())
			return player.getHover();
		else{
			Point p=Display.frame.getMousePosition();
			Card hover=null;
			for(Card c:submittedCards.getCards()){
				c.step(p);
				if(c.hover)
					hover=c;
			}
			return hover;
		}
	}

	private void drawJudge(Graphics g) {
		int x=0;
		for(Card c:submittedCards.getCards()){
			x+=(int)(c.getRectangle().getWidth()+5);
		}
		x=Display.WIDTH/2-x/2;
	
		int y=Display.frame.getHeight()-150;
		for(Card c:submittedCards.getCards()){
			x+=c.getRectangle().getWidth()/2+5;
			c.setPosition(x,y);
			x+=c.getRectangle().getWidth()/2+5;
			if(c.ownerID==player.id)
				c.draw(g);
			else
				c.drawBack(g);
		}
	}

	public void step(Point mouse) {
		player.step(mouse);
		
	}

	public void click(Point mouse) {
		if(!sb.click(mouse, this)){
			player.click(mouse);
			templateDisplay=drawTemplate(template);
		}
	}
	
	public BufferedImage drawTemplate(Card c){
		BufferedImage bi=c.getImage();
		BufferedImage template=new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g=template.createGraphics();
		g.drawImage(bi,0,0,null);
		for(int i=0;i<c.templateSize;i++){
			Rectangle2D rect=new Rectangle2D.Double(c.tempX[i],c.tempY[i],c.tempWidth[i],c.tempHeight[i]);
			
			if(player.getSelected()[i]!=null){
				Display.drawFitToRectangle(player.getSelected()[i].getImage(), rect,g);
			}
		}
		return template;
	}

	public void submit() {
		player.submit(this);
	}
	
	public void submit(Card[] c){
		player.isSelecting=false;
		if(showHand()){
			sendState(GameCommand.submitCard(new Card(drawTemplate(template)),false));
		}
		else if(player.isJudge){
			sendState(GameCommand.submitCard(new Card(drawTemplate(template)),true));
		}
	}
	
	public synchronized void recieveCommand(GameCommand gc){
		switch(gc.type){
		case GameCommand.GC_DRAW_CARD:{
			if(gc.id==player.id)
			for(Card c:gc.cards)
				player.addToHand(c);
			break;
			}
		
		case GameCommand.GC_NEW_TEMPLATE:{
			template=gc.cards[0];
			reset(template);
			drawTemplate(template);
			for(Player p:getPlayers()){
				if(p.id==gc.id){
					judge=p;
					p.isJudge=true;
					p.isSelecting=false;
					}
				}
			break;
			}
		
		case GameCommand.GC_SUBMIT_CARD:{
			submittedCards.addCard(gc.cards[0]);
			for(Player p:getPlayers()){
				if(p.id==gc.id){
					p.isSelecting=false;
					}
				}
			break;
			}
		case GameCommand.GC_INTRODUCE_PLAYER:{
			Player p=new Player(gc);
			addPlayer(p);
			break;
			}
		case GameCommand.GC_REMOVE_PLAYER:{
			for(Player p:getPlayers()){
				if(p.id==gc.id){
					removePlayer(p);
					break;
					}
				}
			break;
			}
		}
	}
	
	private void addPlayer(Player p){
		alterPlayers(p,0);
	}
	
	private void removePlayer(Player p){
		alterPlayers(p,1);
	}
	
	private ArrayList<Player> getPlayers(){
		return alterPlayers(null,2);
	}
	
	public synchronized ArrayList<Player> alterPlayers(Player p,int action){
		
		switch(action){
			case 0:{
				players.add(p);
				break;
			}
			case 1:{
				players.remove(p);
				break;
			}
		}
		return (ArrayList<Player>) players.clone();
	}

	private void reset(Card t) {
		player.reset(t);
		for(Player p:players){
			p.reset(t);
		}
		submittedCards.clear();
	}

	public void recieveState() throws ClassNotFoundException, IOException {
		GameCommand gs=(GameCommand) in.readObject();
		recieveCommand(gs);
		
	}
}
