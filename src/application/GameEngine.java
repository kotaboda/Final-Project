package application;

import character.Player;
import floors.Floor;

public class GameEngine {
	private Viewable view;
	private Player player;
	private Floor[] floors;
	
	public GameEngine(Player player, Viewable view, Floor[] floors) {
		this.player = player;
		this.view = view;
		this.floors = floors;
	}
	
	public static void run() {
		
	}
	
	public GameEngine loadGame() {
		return null;
	}
	
	public void saveGame(GameEngine state) {
		
	}
}