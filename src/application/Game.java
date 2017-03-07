package application;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import character.Character;
import character.Player;
import floors.Floor;
import floors.Floor1;
import floors.Floor2;
import floors.Floor3;
import itemSystem.Item;
import itemSystem.Usable;

public class Game implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2750081260774478492L;
	private Player player;
	private Floor[] floors;
	
	public Game(Player player) {
		this.player = player;
		this.player.setFloorNum(1);
		this.floors = new Floor[] {new Floor1(player), new Floor2(player), new Floor3()};
	}
	
	public Character pickTarget() {
		//when use picks an enemy, tell the battle class which enemy it is
		return null;
	}
	
	public void useItem(Item item) {
		//when the user chooses an item to use, let the player know which item they chose
		Item[] items = player.getInventoryContents();
		for(Item thing : items) {
			if(item == thing && item instanceof Usable) {
				((Usable) item).use(player);
			}
		}
	}
	
	
	
	public Player getPlayer(){
		return this.player;
	}
	
	public List<Floor> getFloors(){
		return Collections.unmodifiableList(Arrays.asList(floors));
	}
}
