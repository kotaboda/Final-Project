package tiles;

import character.Player;
import tileinterfaces.Interactable;

public class FloorMessage extends Tile implements Interactable {

	//TODO(andrew): set message sometime
	private String message;
	
	public FloorMessage(int tileSheetNum) {
		super(tileSheetNum);
	}

	@Override
	public void interact(Player player) {
		//TODO(andrew): Display message from tile here
	}
}
