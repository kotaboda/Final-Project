package tiles;

import tileinterfaces.Collidable;
import tileinterfaces.Interactable;

public class Chest extends Tile implements Interactable, Collidable {

	public Chest(int tileSheetNum){
		super(tileSheetNum);
	}
}
