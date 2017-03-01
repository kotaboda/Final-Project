package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import battleSystem.Battle;
import character.Enemy;
import character.Player;
import floors.Floor;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import models.Coordinates;

public class GameEngine {
	private static GameGUI view;
	private static Game game = new Game(new Player());
	private static Service<Void> battleService;

	public static void run() {
		// NOTE(andrew): This can be changed later, if it isn't done how we want
		// it to be done
		Battle thisGameSucks = new Battle(game.getPlayer(), new Enemy(), new Enemy(), new Enemy());
		battleService = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						thisGameSucks.start();
						return null;
					}
				};
			}

		};

		battleService.start();
		view.displayBattleView(thisGameSucks);
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

	public static void setView(GameGUI newView) {
		view = newView;
	}

	public static void setGame(Game g) {
		game = g;
	}

	public static Game getGame() {
		return game;
	}

	public static void updatePlayerPosition(int x, int y) {
		Coordinates c = game.getPlayer().getCoordinates();
		c.setX(c.getX() + x);
		c.setY(c.getY() + y);
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

	public static void playerBattleInput(Battle battle) {
		// TODO(andrew): This should take the selected options from the view,
		// but I am not sure on how to get that data from the view

		view.waitForPlayerSelection(battle);
	}

}