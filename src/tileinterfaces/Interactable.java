package tileinterfaces;

import character.Player;
import floors.Note;

public interface Interactable {

	public void interact(Player player);
	public String getMessage();
	public void setNote(Note note);
}
