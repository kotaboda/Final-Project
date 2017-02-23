package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import character.Player;
import floors.Floor;
import viewInterface.Viewable;

public class GameEngine {
	private Viewable view;
	private Player player;
	private Floor[] floors;

	public GameEngine(Player player, Viewable view, Floor[] floors) {
		this.player = player;
		this.view = view;
		this.floors = floors;
	}

	public void run() {
		
		

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

	public static void saveGame(GameEngine gameEngine) {

		try (ObjectOutputStream oos = new ObjectOutputStream(
				Files.newOutputStream(Paths.get("src/saves/savedGame.neu")))) {
			oos.writeObject(gameEngine);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}