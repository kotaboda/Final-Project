package tiles;

import character.Player;
import floors.Note;
import tileinterfaces.Collidable;
import tileinterfaces.Interactable;

public class BossTile extends Tile implements Collidable, Interactable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1313480413875060301L;

	public BossTile(int tileSheetNum) {
		super(tileSheetNum);
	}

	@Override
	public void interact(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public void setNote(Note note) {
		
	}

}
