package Game;

import java.util.ArrayList;
import java.util.Collections;

public class CardPile {
	ArrayList<Card> pile;
	public CardPile(){
		pile=new ArrayList<Card>();
	}
	
	public void addCard(Card card){
		pile.add(card);
	}
	
	public Card drawCard(){
		Card c=pile.get(0);
		pile.remove(0);
		return c;
	}
	
	public void shuffle(){
		Collections.shuffle(pile);
	}
	
	public int getSize(){
		return pile.size();
	}
}
