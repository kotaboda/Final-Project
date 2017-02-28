package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import character.Player;
import floors.Floor;
import models.Coordinates;
import viewInterface.Viewable;

public class GameEngine {
	private static Viewable view;
	private static Game game = new Game(new Player());

	public static void run() {
		// NOTE(andrew): This can be changed later, if it isn't done how we want
		// it to be done
		int floorNum = game.getPlayer().getFloorNum();
		Floor[] floors = game.getFloors();
		view.displayGeneralView(/* floors[floorNum - 1] */null);


	}

	public static Game loadGame() {
		Game g = new Game(new Player("Jeffrey", 0));

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
		try (ObjectOutputStream oos = new ObjectOutputStream(
				Files.newOutputStream(Paths.get("src/saves/savedGame.neu")))) {
			oos.writeObject(state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setView(Viewable newView) {
		view = newView;
	}

	public static void setGame(Game g) {
		game = g;
	}

	public static Game getGame(){
		return game;
	}
	public static void updatePlayerPosition(int x, int y){
		Coordinates c = game.getPlayer().getCoordinates();
		c.setX(c.getX() + x);
		c.setY(c.getY() + y);
	}
	
}