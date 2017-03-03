package tiles;

import character.Player;
import floors.Note;
import tileinterfaces.Interactable;

public class WallMessage extends Wall implements Interactable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5857787835770994013L;
	//TODO(andrew): set message sometime
	private Note note;
	
	public WallMessage(int tileSheetNum) {
		super(tileSheetNum);
	}

	@Override
	public void interact(Player player) {
		//TODO(andrew): Display text from message here
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
