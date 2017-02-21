package tiles;

import tileinterfaces.Interactable;

public class WallMessage extends Wall implements Interactable {

	public WallMessage(int tileSheetNum) {
		super(tileSheetNum);
	}
}
