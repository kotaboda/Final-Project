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
	private static Player player;
	private static Floor[] floors;

	public GameEngine(Player player, Viewable view, Floor[] floors) {
		this.player = player;
		this.view = view;
		this.floors = floors;
	}

	void init() {
	}

	public static void run() {
		
		

	}
	
	public static Character pickTarget() {
		//when use picks an enemy, tell the battle class which enemy it is
		return null;
	}
	
	public static void useItem(Item item) {
		//when the user chooses an item to use, let the player know which item they chose
		Item[] items = player.getInventoryContents();
		for(Item thing : items) {
			if(item == thing && item instanceof Usable) {
				((Usable) item).use(player);
			}
		}
	}

	public static GameEngine loadGame() {
		GameEngine ge = null;

		if (Files.exists(Paths.get("src/saves/savedGame.neu"))) {
			try (ObjectInputStream ois = new ObjectInputStream(
					Files.newInputStream(Paths.get("src/saves/savedGame.neu")))) {
				ge = (GameEngine) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return ge;
	}
	
	public void saveGame(GameEngine state) {
		try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("src/saves/savedGame.neu")))){
			oos.writeObject(state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}