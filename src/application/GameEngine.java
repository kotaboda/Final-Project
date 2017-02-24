package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import character.Character;
import character.Player;
import floors.Floor;
import itemSystem.Item;
import itemSystem.Usable;
import viewInterface.Viewable;

public class GameEngine {
	private static Viewable view;
	private static Game game;

	public static void run() {
		//NOTE(andrew): This can be changed later, if it isn't done how we want it to be done
		int floorNum = game.getPlayer().getFloorNum();
		Floor[] floors = game.getFloors();
		view.displayGeneralView(floors[floorNum - 1]);

	}
	
	

	public static Game loadGame() {
		Game g = new Game(new Player("Jeffrey"), null);

		if (Files.exists(Paths.get("src/saves/savedGame.neu"))) {
			try (ObjectInputStream ois = new ObjectInputStream(
					Files.newInputStream(Paths.get("src/saves/savedGame.neu")))) {
				g = (Game) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return g;
	}
	
	public static void saveGame(Game state) {
		try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("src/saves/savedGame.neu")))){
			oos.writeObject(state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setView(Viewable newView) {
		view = newView;
		view.setPlayerSummary(game.getPlayer().getPlayerSummary());
	}

	
}