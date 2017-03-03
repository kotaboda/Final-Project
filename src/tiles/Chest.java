package tiles;

import character.Player;
import characterInterfaces.Lootable;
import floors.Note;
import itemSystem.Item;
import tileinterfaces.Collidable;
import tileinterfaces.Interactable;

public class Chest extends Tile implements Interactable, Collidable, Lootable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6248188691328046026L;
	private Note note;
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

	@Override
	public String getMessage() {
		return note.getMessage();
	}

	@Override
	public void setNote(Note note) {
		this.note = note;
	}
}
