package tiles;

import tileinterfaces.Interactable;

public class WallMessage extends Wall implements Interactable {

	//TODO(andrew): set message sometime
	private String message;
	
	public WallMessage(int tileSheetNum) {
		super(tileSheetNum);
	}

	@Override
	public void interact(Player player) {
		//TODO(andrew): Display text from message here
	}
}
