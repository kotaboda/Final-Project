package application;

import character.Character;
import character.Player;
import floors.Floor;
import itemSystem.Item;
import itemSystem.Usable;
import viewInterface.Viewable;

public class Game {

	private Player player;
	private Floor[] floors;
	
	public Game(Player player, Floor[] floors) {
		this.player = player;
		this.player = new Player("Jeffrey");
		this.floors = floors;
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
	
	public Floor[] getFloors(){
		return this.floors;
	}
}
