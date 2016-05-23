package Game;

import java.io.Serializable;

public class GameCommand implements Serializable{

	public static final int GC_DRAW_CARD=0,GC_SUBMIT_CARD=1,GC_NEW_TEMPLATE=2;

	public static final int GC_REMOVE_PLAYER = 3,GC_CREATE_PLAYER = 4,GC_CHOOSE_CARD = 5;
	public static final int GC_INTRODUCE_PLAYER=6;
	
	public String name;
	public int type,id;
	public Card[] cards;
	
	private GameCommand(){
		
	}

	public static GameCommand createPlayer(int id2) {
		
		GameCommand gc=new GameCommand();
		
		gc.id=id2;
		gc.type=GC_CREATE_PLAYER;
		
		return gc;
	}
	
	public static GameCommand introducePlayer(int id, String name){
		GameCommand gc=new GameCommand();
		gc.type=GC_INTRODUCE_PLAYER;
		gc.id=id;
		gc.name=name;
		return gc;
	}

	public static GameCommand createTemplate(Card c, int jID) {
		
		GameCommand gc=new GameCommand();
		
		gc.cards=new Card[]{c};
		gc.id=jID;
		gc.type=GC_NEW_TEMPLATE;
		
		return gc;
	}

	public static GameCommand removePlayer(int id2) {
		GameCommand gc=new GameCommand();
		
		gc.id=id2;
		gc.type=GC_REMOVE_PLAYER;
		
		return gc;
	}

	public static GameCommand drawCard(Card drawCard) {
		GameCommand gc=new GameCommand();
		
		gc.cards=new Card[]{drawCard};
		gc.type=GameCommand.GC_DRAW_CARD;
		
		return gc;
	}
	
}
