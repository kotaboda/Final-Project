package tiles;

import character.Player;
import tileinterfaces.Interactable;

public class FloorMessage extends Tile implements Interactable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 19751879742944256L;
	//TODO(andrew): set message sometime
//	private String message;
	
	public FloorMessage(int tileSheetNum) {
		super(tileSheetNum);
	}

	@Override
	public void interact(Player player) {
		//TODO(andrew): Display message from tile here
	}
}
