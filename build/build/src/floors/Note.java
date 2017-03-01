package floors;

import models.Coordinates;

public class Note {
	private final String message;
	private final Coordinates coord;
	
	public Note(String message, Coordinates coord) {
		this.message = message;
		this.coord = coord;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Coordinates getCoord() {
		return coord;
	}
}
