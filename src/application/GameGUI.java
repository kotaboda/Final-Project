package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import abilityInterfaces.Ability;
import battleSystem.Battle;
import character.Enemy;
import enums.GUILayouts;
import floors.Floor;
import itemSystem.Inventory;
import itemSystem.Item;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tiles.TileManager;
import viewInterface.Viewable;

public class GameGUI extends Application implements Viewable {

	private Stage primaryStage;
	private GUILayouts currentLayout = GUILayouts.MAIN_MENU;
	private final Game TESTINGGAME = GameEngine.getGame();
	private Object lock = new Object();

	@FXML
	private Pane rightBattleVBox;
	@FXML
	private ListView<String> leftActionList;
	//
	private ListView<Ability> abilityList;
	//
	@FXML
	private Button submitButton;
	@FXML
	private HBox enemies;
	@FXML
	private VBox middleBattleVBox;
	@FXML
	private Label battleTextLabel;
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
		Font.loadFont((new Object()).getClass().getResourceAsStream("Orbitron-Bold.ttf"), 16);
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
			displayMainMenu();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void displayMainMenu() {
		FXMLLoader loader = new FXMLLoader();

		loader.setController(this);

		try {

			Parent p = loader.load(Files.newInputStream(Paths.get("src/MainMenuView.fxml")));
			newGameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					GameEngine.run();
				}
			});

			loadGameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Service<Void> s = new Service<Void>() {
						@Override
						protected Task<Void> createTask() {
							return new Task<Void>() {
								@Override
								protected Void call() throws Exception {
									GameEngine.setGame(TESTINGGAME);
									GameEngine.run();
									return null;
								}
							};
						}

					};
					s.start();
				}

			});

			exitButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Platform.exit();
				}

			});
			Scene scene = new Scene(p);
			scene.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
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
		loader.setController(this);
		try {
			Parent p = loader.load(Files.newInputStream(Paths.get("src/PauseView.fxml")));

			exitButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					displayGeneralView(TESTINGGAME.getFloors()[0]);
				}

			});
			Scene scene = new Scene(p);
			String css = this.getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void displayBattleView(Battle b) {

		this.currentLayout = GUILayouts.BATTLE;
		FXMLLoader loader = new FXMLLoader();

		loader.setController(this);
		try {
			Parent p = loader.load(Files.newInputStream(Paths.get("src/BattleView.fxml")));
			playerName.setText(TESTINGGAME.getPlayer().name);
			enemies.setAlignment(Pos.CENTER);
			for (int i = 0; i < b.getEnemies().length; i++) {
				int enemyNum = i + 1;
				Enemy currentEnemy = b.getEnemies()[i];
				Label enemyName = new Label(currentEnemy.name);
				ProgressBar enemyHealth = new ProgressBar();
				enemyHealth.progressProperty().bind(currentEnemy.getHPProperty().divide(currentEnemy.getMaxHPProperty().doubleValue()));
				VBox mainContainer = new VBox(new Canvas(100, 100), enemyName, enemyHealth);
				Node child = new Group(mainContainer);
				enemies.getChildren().add(child);
				child.setOnMouseClicked(new EventHandler<MouseEvent>() {				
					@Override
					public void handle(MouseEvent event) {
						b.setPlayerTarget(currentEnemy);
						System.out.println(enemyNum);
					};
				});
				if(i == 0){
					child.getOnMouseClicked().handle(null);;
				}
			}
			//
			submitButton.setDisable(true);
			//
			leftActionList.setItems(FXCollections.observableArrayList("Attack", "Abilities", "Items"));
			leftActionList.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					MouseButton b = event.getButton();
					switch (b) {
					case PRIMARY:
						switch (leftActionList.getSelectionModel().getSelectedIndex()) {
						case 0:
							middleBattleVBox.getChildren().clear();
							rightBattleVBox.getChildren().clear();
							//
							if (isPlayersTurn) {
								submitButton.setDisable(false);
								abilityList = null;
							}
							//
							break;
						case 1:
							//
							if (isPlayersTurn) {
								submitButton.setDisable(true);
							}
							//
							middleBattleVBox.getChildren().clear();
							abilityList = new ListView<>(TESTINGGAME.getPlayer().getAbilities());
							middleBattleVBox.getChildren().add(abilityList);
							abilityList.getSelectionModel().selectedItemProperty()
									.addListener(new ChangeListener<Ability>() {

										@Override
										public void changed(ObservableValue<? extends Ability> observable,
												Ability oldValue, Ability newValue) {
											rightBattleVBox.getChildren().clear();
											if (abilityList.getSelectionModel().getSelectedItem() != null) {
												Label abilityDescription = new Label(abilityList.getSelectionModel()
														.getSelectedItem().getDescription());
												abilityDescription.wrapTextProperty().set(true);
												rightBattleVBox.getChildren().add(abilityDescription);
												//
												if (isPlayersTurn) {
													submitButton.setDisable(false);
												}
												//
											}
										}
									});
							abilityList.getSelectionModel().selectFirst();
							break;
						case 2:
							//
							if (isPlayersTurn) {
								submitButton.setDisable(true);
								abilityList = null;
							}

							//
							middleBattleVBox.getChildren().clear();
							rightBattleVBox.getChildren().clear();
							ListView<Item> itemList = new ListView<Item>(
									FXCollections.observableArrayList(TESTINGGAME.getPlayer().getInventoryContents()));
							middleBattleVBox.getChildren().add(itemList);
							break;
						}
						break;
					default:
						break;
					}

				}

			});

			// submitButton.setDisable(true);
			submitButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// Ability a =
					// abilityList.getSelectionModel().getSelectedItem();
					// Tell Game Engine about selections here.

					submitButton.setDisable(true);
					synchronized (lock) {
						isPlayersTurn = false;
						lock.notifyAll();
					}

				}

			});
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty()
					.divide(TESTINGGAME.getPlayer().getHPProperty().doubleValue()));
			Scene scene = new Scene(p);
			String css = this.getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// TODO(andrew): these are for an example of something that could be done to
	// wait for the submit button to be clicked. Could also be accomplished by
	// getting the
	// battle loop thread to pause somehow, not entirely sure how.
	private boolean isPlayersTurn = false;

	public void waitForPlayerSelection(Battle battle) {
		isPlayersTurn = true;
		try {
			synchronized (lock) {
				while (isPlayersTurn) {
					lock.wait();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		switch (leftActionList.getSelectionModel().getSelectedIndex()) {
		case 0:
			// attack
			battle.setPlayerNextAbility(null);
			// then set the targets here
			break;

		case 1:
			// ability
			battle.setPlayerNextAbility(abilityList.getSelectionModel().getSelectedItem());
			// then set the targets here
			break;

		case 2:
			// items

			break;

		default:
			break;
		}

	}

	@Override
	public void displayGeneralView(Floor currentFloor) {
		this.currentLayout = GUILayouts.GENERAL;
		FXMLLoader loader = new FXMLLoader();

		loader.setController(this);
		try {
			Parent parent = loader.load(Files.newInputStream(Paths.get("src/GeneralView.fxml")));
			parent.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (currentLayout == GUILayouts.GENERAL) {
						KeyCode keyEvent = event.getCode();

						switch (keyEvent) {
						case W:
							GameEngine.updatePlayerPosition(0, -1);
							break;
						case S:
							GameEngine.updatePlayerPosition(0, 1);
							break;
						case A:
							GameEngine.updatePlayerPosition(-1, 0);
							break;
						case D:
							GameEngine.updatePlayerPosition(1, 0);
							break;
						default:
							break;
						}
						drawToGeneralCanvas(currentFloor);
						Battle b = GameEngine.checkForBattle(currentFloor);
						if (b != null) {
							displayBattleView(b);
							GameEngine.startBattle(b);
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
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty()
					.divide(TESTINGGAME.getPlayer().getMaxHPProperty().doubleValue()));
			playerName.setText(TESTINGGAME.getPlayer().name);

			// Drawing testing
			// GraphicsContext gc = canvas.getGraphicsContext2D();
			drawToGeneralCanvas(currentFloor);
			// WritableImage image =
			// TileManager.getImageToDraw(currentFloor.getTiles(),
			// playerSummary.coordinates);
			// gc.drawImage(image, 0, 0, image.getWidth() * 2, image.getHeight()
			// * 2);
			// Animation testing
			// Timeline gameLoop = new Timeline();
			// gameLoop.setCycleCount( 64 );
			//
			// KeyFrame kf = new KeyFrame(
			// Duration.seconds(0.01666667), // 60 FPS
			// new EventHandler<ActionEvent>()
			// {
			// int x = 0;
			// public void handle(ActionEvent ae)
			// {
			// x -= 1;
			// int tempX = x % 64;
			// gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			// if(tempX == 0){
			// playerSummary.coordinates.setX(playerSummary.coordinates.getX() +
			// 1);
			// x = 0;
			// }
			// WritableImage image =
			// TileManager.getImageToDraw(currentFloor.getTiles(),
			// playerSummary.coordinates);
			// System.out.println(x);
			// gc.drawImage(image, tempX - 64, 0, image.getWidth() * 2,
			// image.getHeight() * 2);
			// }
			// });
			//
			// gameLoop.getKeyFrames().add( kf );
			// gameLoop.play();
			// gameLoop.getKeyFrames().add( kf );
			// gameLoop.play();
			// split
			// new AnimationTimer()
			// {
			// int x = 0;
			// public void handle(long currentNanoTime)
			// {
			// x -= 1;
			// int tempX = x % 64;
			// gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			// if(tempX == 0){
			// playerSummary.coordinates.setX(playerSummary.coordinates.getX() +
			// 1);
			// x = 0;
			// }
			// WritableImage image =
			// TileManager.getImageToDraw(currentFloor.getTiles(),
			// playerSummary.coordinates);
			// gc.drawImage(image, tempX - 64, 0, image.getWidth() * 2,
			// image.getHeight() * 2);
			//
			//
			// }
			// }.start();
			Scene scene = new Scene(parent);
			String css = this.getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			primaryStage.setScene(scene);
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
		System.out.println(canvas.getWidth());
		System.out.println(image.getWidth() * 1.6);
		gc.drawImage(image, 0, 0, image.getWidth() * 1.6, image.getHeight() * 1.6);
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

}
