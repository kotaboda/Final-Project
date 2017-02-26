package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import battleSystem.Battle;
import character.Player;
import character.PlayerSummary;
import enums.GUILayouts;
import floors.Floor;
import floors.Floor1;
import itemSystem.Inventory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.Coordinates;
import tiles.TileManager;
import viewInterface.Viewable;

public class GameGUI extends Application implements Viewable {

	private Stage primaryStage;
	private PlayerSummary playerSummary = new PlayerSummary(new Player("Test", 0));
	private GUILayouts currentLayout = GUILayouts.MAIN_MENU;

	@FXML
	private Button newGameButton;
	@FXML
	private Button loadGameButton;
	@FXML
	private Button exitButton;
	@FXML
	private Button menuButton;
	@FXML
	private Canvas playerImage;
	@FXML
	private Label playerName;
	@FXML
	private ProgressBar playerHealthBar;
	@FXML
	private Canvas canvas;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void init() {
		GameEngine.setView(this);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			// displayMainMenu();
			displayGeneralView(new Floor1());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void displayMainMenu() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();

		loader.setController(this);

		try {
			Parent p = loader.load(Files.newInputStream(Paths.get("src/MainMenuView.fxml")));
			newGameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					GameEngine.setGame(new Game(null, null));
					GameEngine.run();
				}
			});

			loadGameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					GameEngine.setGame(GameEngine.loadGame());
					GameEngine.run();
				}

			});
			primaryStage.setScene(new Scene(p));
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void displayPauseMenu() {
		// TODO Auto-generated method stub
		this.currentLayout = GUILayouts.PAUSE;
		FXMLLoader loader = new FXMLLoader();
	}

	@Override
	public void displayBattleView(Battle b) {
		// TODO Auto-generated method stub
		this.currentLayout = GUILayouts.BATTLE;
		FXMLLoader loader = new FXMLLoader();

		loader.setController(this);
		try {
			Parent p = loader.load(Files.newInputStream(Paths.get("src/BattleView.fxml")));
			playerName.setText(b.getPlayerSummary().playerName);
			playerHealthBar.progressProperty()
					.bind(playerSummary.hpProperty.divide(playerSummary.maxHPProperty.doubleValue()));
			primaryStage.setScene(new Scene(p));
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void displayGeneralView(Floor currentFloor) {
		this.currentLayout = GUILayouts.GENERAL;
		FXMLLoader loader = new FXMLLoader();

		loader.setController(this);
		try {
			Parent parent = loader.load(Files.newInputStream(Paths.get("src/GeneralView.fxml")));
			currentFloor.getPlayer().getCoordinates().setCoordinates(0, 0);
			parent.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (currentLayout == GUILayouts.GENERAL) {
						KeyCode keyEvent = event.getCode();
						Coordinates playerCoordinates = currentFloor.getPlayer().getCoordinates();

						switch (keyEvent) {
						case W:
							playerCoordinates.setY(playerCoordinates.getY() - 1);
							drawToGeneralCanvas(currentFloor);
							break;
						case S:
							playerCoordinates.setY(playerCoordinates.getY() + 1);
							drawToGeneralCanvas(currentFloor);

							break;
						case A:
							playerCoordinates.setX(playerCoordinates.getX() - 1);
							drawToGeneralCanvas(currentFloor);

							break;
						case D:
							playerCoordinates.setX(playerCoordinates.getX() + 1);
							drawToGeneralCanvas(currentFloor);
							break;
						default:
							break;
						}
					}
				}
			});
			menuButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					displayPauseMenu();
				}

			});
			playerHealthBar.progressProperty()
					.bind(playerSummary.hpProperty.divide(playerSummary.maxHPProperty.doubleValue()));

			playerName.setText(playerSummary.playerName);

			// Drawing testing
			GraphicsContext gc = canvas.getGraphicsContext2D();
			WritableImage image = TileManager.getImageToDraw(currentFloor.getTiles(),
					currentFloor.getPlayer().getCoordinates());
			gc.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
			primaryStage.setScene(new Scene(parent));
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void drawToGeneralCanvas(Floor currentFloor) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		WritableImage image = TileManager.getImageToDraw(currentFloor.getTiles(),
				currentFloor.getPlayer().getCoordinates());
		gc.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
	}

	@Override
	public void displayInventoryView(Inventory inv) {
		// TODO Auto-generated method stub
		this.currentLayout = GUILayouts.INVENTORY;

	}

	@Override
	public void displayCharacterManager() {
		// TODO Auto-generated method stub
		currentLayout = GUILayouts.PLAYER_MENU;

	}

	@Override
	public void displayLootManager() {
		// TODO Auto-generated method stub
		currentLayout = GUILayouts.LOOT_MANAGER;

	}

	@Override
	public void setPlayerSummary(PlayerSummary playerSummary) {
		this.playerSummary = playerSummary;
	}

}
