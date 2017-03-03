package tiles;

import character.Player;
import tileinterfaces.Interactable;

public class WallMessage extends Wall implements Interactable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5857787835770994013L;
	//TODO(andrew): set message sometime
//	private String message;
	
	public WallMessage(int tileSheetNum) {
		super(tileSheetNum);
	}

	@Override
	public void interact(Player player) {
		//TODO(andrew): Display text from message here
	}
}
