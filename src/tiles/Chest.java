package tiles;

import character.Player;
import characterInterfaces.Lootable;
import itemSystem.Item;
import tileinterfaces.Collidable;
import tileinterfaces.Interactable;

public class Chest extends Tile implements Interactable, Collidable, Lootable {

	private Item[] loot;
	
	public Chest(int tileSheetNum){
		super(tileSheetNum);
		//TODO(andrew): initialize items here
	}

	@Override
	public void interact(Player player) {
		//TODO(andrew):
		obtainLoot();
	}
	
	@Override
	public Item[] obtainLoot(){
		return loot;
		//TODO(andrew): what exactly should this method do?
	}
}
