package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import battleSystem.Battle;
import character.Player;
import floors.Floor;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import models.Coordinates;
import tileinterfaces.Collidable;

public class GameEngine {
	private static GameGUI view;
	private static Game game = new Game(new Player());

	public static void run() {
		// NOTE(andrew): This can be changed later, if it isn't done how we want
		// it to be done
		view.displayGeneralView();

	}

	public static Game loadGame() {
		Game g = null;

		if (Files.exists(Paths.get("resources/saves/savedGame.neu"))) {
			try (ObjectInputStream ois = new ObjectInputStream(
					Files.newInputStream(Paths.get("resources/saves/savedGame.neu")))) {
				g = (Game) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("Failed Load");
				e.printStackTrace();
			}
		}
		game = g;
		return g;
	}

	public static void saveGame(Game state) {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				Files.newOutputStream(Paths.get("resources/saves/savedGame.neu"), StandardOpenOption.CREATE))) {
			oos.writeObject(state);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setView(GameGUI newView) {
		view = newView;
	}

	public static Game getGame() {
		return game;
	}

	public static void updatePlayerPosition(int x, int y) {
		Coordinates c = game.getPlayer().getCoordinates();
		if (((c.getX() + x)	 >= 0 && (c.getX() + x) < game.getFloors()[game.getPlayer().getFloorNum() - 1].getTiles()[0].length)
				&& ((c.getY() + y) >= 0 && (c.getY() + y) < game.getFloors()[game.getPlayer().getFloorNum() - 1].getTiles().length)
				&& !(game.getFloors()[game.getPlayer().getFloorNum() - 1].getTiles()[c.getY() + y][c.getX()
						+ x] instanceof Collidable)) {
			c.setX(c.getX() + x);
			c.setY(c.getY() + y);
		}
	}

	public static Battle checkForBattle(Floor currentFloor) {
		Coordinates playerC = game.getPlayer().getCoordinates();
		Battle[] battlesC = currentFloor.getBattles();
		Battle battle = null;
		for (int i = 0; i < battlesC.length; i++) {
			Coordinates currentC = battlesC[i].getCoordinates();
			if (playerC.equals(currentC)) {
				battle = battlesC[i];
			}
		}

		return battle;
	}
	
	public static String checkNote() {
//		Player p = GameEngine.getGame().getPlayer();
//		Tile t = GameEngine.getGame().getFloors()[p.getFloorNum()].getTiles()[p.getCoordinates().getX()][p.getCoordinates()
//		                                                                                 				.getY()];
//		if (t instanceof FloorMessage || t instanceof WallMessage) {
//			// retreive the notes at those coordinates in order to display it
//			String message = GameEngine.getGame().getFloors()[p.getFloorNum()].getNotes().get(p.getCoordinates()).getMessage();
//			return message;
//		} else {
			return null;
//		}
	}
	

	public static void playerBattleInput(Battle battle) {
		

		view.waitForPlayerSelection(battle);
	}

	public static void startBattle(Battle b) {
		Service<Void> battleService = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						b.start();
						return null;
					}
				};
			}

		};

		battleService.start();
	}

	public static void displayEndBattle(Battle b){
		view.displayEndBattle(b);
	}

	public static void setGame(Game g) {
		game = g;
	}
	
}