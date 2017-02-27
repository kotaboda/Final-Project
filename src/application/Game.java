package application;

import character.Character;
import character.Player;
import floors.Floor;
import floors.Floor1;
import floors.Floor2;
import floors.Floor3;
import itemSystem.Item;
import itemSystem.Usable;

public class Game {

	private Player player;
	private Floor[] floors;
	
	public Game(Player player) {
		this.player = player;
		this.floors = new Floor[] {new Floor1(player), new Floor2(), new Floor3()};
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
