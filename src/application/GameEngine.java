package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import battleSystem.Battle;
import character.Boss;
import character.Character;
import character.Enemy;
import character.Exercise;
import character.Lab;
import character.Player;
import character.Project;
import enums.Character.Direction;
import interfaces.note.Noteable;
import interfaces.tile.Collidable;
import interfaces.tile.Lootable;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import models.Coordinates;
import tiles.Chest;
import tiles.GroundTile;
import tiles.Tile;

public class GameEngine {
	private static final int BATTLE_CHANCE = 10;
	private static GameGUI view;
	private static Game game;

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
		if (newView != null) {
			view = newView;
		}
	}

	public static Game getGame() {
		return game;
	}

	public static boolean updatePlayerPosition(Direction direction) {
		int x = 0;
		int y = 0;
		boolean success = false;
		switch (direction) {
		case UP:
			y = -1;
			break;
		case RIGHT:
			x = 1;
			break;
		case DOWN:
			y = 1;
			break;
		case LEFT:
			x = -1;
			break;
		}
		Coordinates c = game.getPlayer().getCoordinates();
		if (checkMovement(direction)) {
			c.setX(c.getX() + x);
			c.setY(c.getY() + y);
			success = true;

		}
		return success;
	}

	public static boolean checkMovement(Direction direction) {
		int x = 0;
		int y = 0;
		switch (direction) {
		case UP:
			y = -1;
			break;
		case RIGHT:
			x = 1;
			break;
		case DOWN:
			y = 1;
			break;
		case LEFT:
			x = -1;
			break;
		}
		Coordinates c = game.getPlayer().getCoordinates();
		return (((c.getX() + x) >= 0
				&& (c.getX() + x) < game.getFloors().get(game.getPlayer().getFloorNum() - 1).getTiles().get(0).size())
				&& ((c.getY() + y) >= 0
						&& (c.getY() + y) < game.getFloors().get(game.getPlayer().getFloorNum() - 1).getTiles().size())
				&& !(game.getFloors().get(game.getPlayer().getFloorNum() - 1).getTiles().get(c.getY() + y)
						.get(c.getX() + x) instanceof Collidable));
	}

	public static Battle chanceABattle() {
		Random r = new Random();
		Battle battle = null;
		if(r.nextInt(100) + 1 <= BATTLE_CHANCE){
			int numOfEnemies = r.nextInt(3) + 1;
			Enemy[] enemies = new Enemy[numOfEnemies];
			for(int i = 0; i < numOfEnemies; i++){
				Enemy e = null;
				int spawn = r.nextInt(100) + 1;
				if(spawn >= 60){
					e = new Exercise();
				}
				else if(spawn < 60 && spawn > 10){
					e = new Lab();
				}
				else if(spawn <= 10){
					e = new Project();
				}
				
				enemies[i] = e;
			}
			battle = new Battle(game.getPlayer(), enemies);
		}
		return battle;

	}

	public static String checkNote() {
		Player p = GameEngine.getGame().getPlayer();
		Direction d = p.getDirectionFacing();
		int x = 0;
		int y = 0;
		switch (d) {
		case UP:
			y = -1;
			break;
		case RIGHT:
			x = 1;
			break;
		case DOWN:
			y = 1;
			break;
		case LEFT:
			x = -1;
			break;
		}

		// TODO(andrew): null pointer on the second floor, and only the two in
		// the top left corner display anything?
		Tile t = GameEngine.getGame().getFloors().get(p.getFloorNum() - 1).getTiles().get(p.getCoordinates().getY() + y)
				.get(p.getCoordinates().getX() + x);
		if (t instanceof Noteable) {
			// retreive the notes at those coordinates in order to display it
			view.displayMessage(((Noteable) t).getMessage());
		}
		return null;
	}

	public static String checkLoot() {
		Player p = GameEngine.getGame().getPlayer();
		Direction d = p.getDirectionFacing();
		int x = 0;
		int y = 0;
		switch (d) {
		case UP:
			y = -1;
			break;
		case RIGHT:
			x = 1;
			break;
		case DOWN:
			y = 1;
			break;
		case LEFT:
			x = -1;
			break;
		}

		// TODO(andrew): null pointer on the second floor, and only the two in
		// the top left corner display anything?
		Tile t = GameEngine.getGame().getFloors().get(p.getFloorNum() - 1).getTiles().get(p.getCoordinates().getY() + y)
				.get(p.getCoordinates().getX() + x);
		if (t instanceof Chest) {
			view.displayLootManager((Lootable) t);
		}
		return null;
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

	public static void displayEndBattle(Battle b, boolean leveledUp) {
		view.displayEndBattle(b, leveledUp);
	}

	public static void setGame(Game g) {
		game = g;
	}

	public static boolean checkForBoss() {
		Player p = GameEngine.getGame().getPlayer();
		Direction d = p.getDirectionFacing();
		int x = 0;
		int y = 0;
		switch (d) {
		case UP:
			y = -1;
			break;
		case RIGHT:
			x = 1;
			break;
		case DOWN:
			y = 1;
			break;
		case LEFT:
			x = -1;
			break;
		}

		// TODO(andrew): null pointer on the second floor, and only the two in
		// the top left corner display anything?
		Tile t = GameEngine.getGame().getFloors().get(p.getFloorNum() - 1).getTiles().get(p.getCoordinates().getY() + y)
				.get(p.getCoordinates().getX() + x);
		if (t instanceof Boss
				&& !game.getFloors().get(game.getPlayer().getFloorNum() - 1).getBossBattle().isCompleted()) {
			view.displayBattleView(game.getFloors().get(game.getPlayer().getFloorNum() - 1).getBossBattle());
			game.getFloors().get(p.getFloorNum() - 1).changeTile(
					game.getFloors().get(p.getFloorNum() - 1).getBoss().getCoordinates().getY(),
					game.getFloors().get(p.getFloorNum() - 1).getBoss().getCoordinates().getX(), new GroundTile(5));
			return true;
		}
		return false;
	}

	public static void playTakeDamageAnimation(Image animation, Character character) {
		view.playTakeDamageAnimation(animation, character);
	}

	public static void playAttackAnimation(Image animation, Character character) {
		view.playTakeDamageAnimation(animation, character);
	}

}