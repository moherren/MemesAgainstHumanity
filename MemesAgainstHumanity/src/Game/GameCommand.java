package Game;

import java.io.Serializable;

public class GameCommand implements Serializable{

	public static final int GC_DRAW_CARD=0,GC_SUBMIT_CARD=1,GC_NEW_TEMPLATE=2;
	
	public int type,id;
	public Card[] cards;
	
	public GameCommand(int type,Card[] cards,int id){
		this.type=type;
		this.cards=cards;
		this.id=id;
	}
	
}
