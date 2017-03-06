package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import battleSystem.Battle;
import character.Player;
import characterEnums.Direction;
import characterEnums.InventoryAction;
import characterInterfaces.Lootable;
import floors.Floor;
import itemSystem.Item;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import models.Coordinates;
import tileinterfaces.Collidable;
import tileinterfaces.Interactable;
import tiles.BossTile;
import tiles.Chest;
import tiles.Tile;

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

	public static boolean updatePlayerPosition(Direction direction) {
		int x = 0;
		int y = 0;
		boolean success = false;
		switch(direction){
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
		game.getPlayer().setDirectionFacing(direction);
		return success;
	}
	
	public static boolean checkMovement(Direction direction){
		int x = 0;
		int y = 0;
		switch(direction){
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
		 return (((c.getX() + x) >= 0 && (c.getX() + x) < game.getFloors()[game.getPlayer().getFloorNum() - 1].getTiles()[0].length)
				&& ((c.getY() + y) >= 0 && (c.getY() + y) < game.getFloors()[game.getPlayer().getFloorNum() - 1].getTiles().length)
				&& !(game.getFloors()[game.getPlayer().getFloorNum() - 1].getTiles()[c.getY() + y][c.getX()
						+ x] instanceof Collidable));
	}

	public static Battle checkForBattle(Floor currentFloor) {
		Coordinates playerC = game.getPlayer().getCoordinates();
		Battle[] battlesC = currentFloor.getBattles();
		Battle battle = null;
		for (int i = 0; i < battlesC.length; i++) {
			Coordinates currentC = battlesC[i].getCoordinates();
			if (playerC.equals(currentC) && !battlesC[i].isCompleted()) {
				battle = battlesC[i];
			}
		}
		if(battle != null && battle.isCompleted()){
			battle = null;
		}
		return battle;
	}
	
	public static String checkNote() {
		Player p = GameEngine.getGame().getPlayer();
		Direction d = p.getDirectionFacing();
		int x = 0;
		int y = 0;
		switch(d){
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
		
		//TODO(andrew): null pointer on the second floor, and only the two in the top left corner display anything?
		Tile t = GameEngine.getGame().getFloors()[p.getFloorNum() - 1].getTiles()[p.getCoordinates().getY() + y][p.getCoordinates().getX() + x];
		if (t instanceof Interactable) {
			// retreive the notes at those coordinates in order to display it
			view.displayMessage(((Interactable) t).getMessage());
		}
		return null;
	}
	
	public static String checkLoot() {
		Player p = GameEngine.getGame().getPlayer();
		Direction d = p.getDirectionFacing();
		int x = 0;
		int y = 0;
		switch(d){
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
		
		//TODO(andrew): null pointer on the second floor, and only the two in the top left corner display anything?
		Tile t = GameEngine.getGame().getFloors()[p.getFloorNum() - 1].getTiles()[p.getCoordinates().getY() + y][p.getCoordinates().getX() + x];
		if(t instanceof Chest){
			view.displayLootManager((Lootable)t); 
		} else if(t instanceof BossTile){
			view.displayBattleView(game.getFloors()[game.getPlayer().getFloorNum() - 1].getBossBattle());
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

	public static boolean givePlayerItem(Item item){
		return game.getPlayer().modifyInventory(InventoryAction.GIVE, item);
	}
	
	public static void displayEndBattle(Battle b){
		view.displayEndBattle(b);
	}

	public static void setGame(Game g) {
		game = g;
	}
	
}