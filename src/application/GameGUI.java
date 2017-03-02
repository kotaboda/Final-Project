package application;

import java.io.IOException;
import java.util.ArrayList;

import abilityInterfaces.Ability;
import battleSystem.Battle;
import character.Enemy;
import character.Player;
import characterEnums.Stats;
import enums.GUILayouts;
import floors.Floor;
import itemSystem.Inventory;
import itemSystem.Item;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Coordinates;
import tiles.TileManager;

//Setup Character screen fxml
//Wire button for pauseView
public class GameGUI extends Application {

	private Stage primaryStage;
	private GUILayouts currentLayout = GUILayouts.MAIN_MENU;
	private Game TESTINGGAME = GameEngine.getGame();
	private boolean isPlayersTurn = false;
	private Object lock = new Object();
	private Parent p;

	@FXML
	private Button exitButton;
	@FXML
	private ImageView playerImageView;
	@FXML
	private Label playerName;
	@FXML
	private ProgressBar playerEnergyBar;
	@FXML
	private ProgressBar playerHealthBar;
	@FXML
	private Canvas canvas;
	@FXML
	private GridPane inventoryGrid;
	@FXML
	private GridPane statGrid;

	public static void main(String[] args) {
		// Font.loadFont((new
		// Object()).getClass().getResourceAsStream("Orbitron-Bold.ttf"), 16);
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

	// MainMenu specific elements
	@FXML
	private Button newGameButton;
	@FXML
	private Button loadGameButton;
	
	public void displayMainMenu() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenuView.fxml"));

		loader.setController(this);

		try {

			p = loader.load();

			newGameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					TESTINGGAME = new Game(new Player());
					GameEngine.setGame(TESTINGGAME);
					GameEngine.run();
				}
			});

			loadGameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					TESTINGGAME = GameEngine.loadGame();
					GameEngine.run();
				}
			});

			exitButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Platform.exit();
				}

			});
			Scene scene = new Scene(p);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// PauseMenu specific elements
	@FXML
	private Button mainMenuButton;
	@FXML
	private Button saveGameButton;
	@FXML
	private Button characterButton;

	public void displayPauseMenu() {
		// TODO Auto-generated method stub
		this.currentLayout = GUILayouts.PAUSE;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/PauseView.fxml"));
		loader.setController(this);
		try {
			p = loader.load();

			mainMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					displayMainMenu();
				}

			});

			saveGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					GameEngine.saveGame(TESTINGGAME);
				}

			});

			characterButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					displayCharacterManager();
				}

			});

			exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					displayGeneralView();

				}

			});
			Scene scene = new Scene(p);
			String css = getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Battle Specific Elements
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

	public void displayBattleView(Battle b) {

		this.currentLayout = GUILayouts.BATTLE;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/BattleView.fxml"));

		loader.setController(this);
		try {
			p = loader.load();

			playerName.setText(TESTINGGAME.getPlayer().NAME);
			enemies.setAlignment(Pos.CENTER);
			ArrayList<Label> enemyNames = new ArrayList<Label>();
			for (int i = 0; i < b.getEnemies().length; i++) {
				Enemy currentEnemy = b.getEnemies()[i];
				Label enemyName = new Label(currentEnemy.NAME);
				enemyNames.add(enemyName);
				ProgressBar enemyHealth = new ProgressBar();
				Group child = new Group();
				enemyHealth.progressProperty().addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (newValue.intValue() == 0) {
									int newSelection = enemies.getChildren().indexOf(child);
									enemies.getChildren().remove(child);
									Node n = null;
									try {
										n = enemies.getChildren().get(newSelection);
									} catch (IndexOutOfBoundsException e) {

									}
									if (n != null) {
										n.getOnMouseClicked().handle(null);
									}
								}
							}
						});
					}

				});

				enemyHealth.progressProperty()
						.bind(currentEnemy.getHPProperty().divide(currentEnemy.getMaxHPProperty().doubleValue()));
				VBox mainContainer = new VBox(new Canvas(100, 100), enemyName, enemyHealth);
				child.getChildren().add(mainContainer);
				enemies.getChildren().add(child);
				child.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						b.setPlayerTarget(currentEnemy);

						for (Label name : enemyNames) {
							name.styleProperty().setValue("");
						}
						enemyName.setStyle("-fx-background-color : blue;");
					};
				});
				if (i == 0) {
					child.getOnMouseClicked().handle(null);
					;
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
								public void changed(ObservableValue<? extends Ability> observable, Ability oldValue,
										Ability newValue) {
									rightBattleVBox.getChildren().clear();
									if (abilityList.getSelectionModel().getSelectedItem() != null) {
										Label abilityDescription = new Label(
												abilityList.getSelectionModel().getSelectedItem().getDescription());
										abilityDescription.wrapTextProperty().set(true);
										rightBattleVBox.getChildren().add(abilityDescription);
										//
										if (isPlayersTurn) {
											submitButton.setDisable(false);
										}
										//
									}
								}
								//
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
			playerEnergyBar.progressProperty().bind(TESTINGGAME.getPlayer().getEnergyProperty()
					.divide(TESTINGGAME.getPlayer().getMaxEnergyProperty().doubleValue()));
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty()
					.divide(TESTINGGAME.getPlayer().getHPProperty().doubleValue()));
			Scene scene = new Scene(p);
			String css = getClass().getResource("application.css").toExternalForm();
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
			// NOTE(andrew): targets are set in the enemy onclick method
			break;

		case 1:
			// ability
			battle.setPlayerNextAbility(abilityList.getSelectionModel().getSelectedItem());
			break;

		case 2:
			// items

			break;

		default:
			break;
		}
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				leftActionList.getSelectionModel().clearSelection();
				middleBattleVBox.getChildren().clear();
				rightBattleVBox.getChildren().clear();
			}

		});
	}

	// TODO(dakota): I'm not sure how to do the whole canvas drawing thing so
	// it's just an overlay over the map
	// or if we just want the messsage to take up the whole screen
	public void displayMessageView(String message) {

	}

	// GeneralView specific elements
	@FXML
	private Button menuButton;

	public void displayGeneralView() {
		this.currentLayout = GUILayouts.GENERAL;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GeneralView.fxml"));

		loader.setController(this);
		try {
			p = loader.load();
			playerImageView.setImage(new Image(getClass().getResourceAsStream("/hero.jpg")));
			p.setOnKeyPressed(new EventHandler<KeyEvent>() {

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
						case ESCAPE:
							displayPauseMenu();
							break;
						default:
							break;
						}

						drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]);
						Battle b = GameEngine
								.checkForBattle(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]);
						//String message = GameEngine.checkNote();
						//TODO(andrew): add a boolean check here so this only checks when a movement key is pressed
						Coordinates playerCoord = TESTINGGAME.getPlayer().getCoordinates();
						System.out.println("Tile: " + TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]
								.getTiles()[playerCoord.getY()][playerCoord.getX()].getTileSheetNum());
						if (b != null) {
							displayBattleView(b);
							GameEngine.startBattle(b);
							//FIXME(andrew): commented out until the exceptions are resolved
//						} else if(message != null) {
//							displayMessageView(message);
						} else if (TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]
								.getTiles()[playerCoord.getY()][playerCoord.getX()].getTileSheetNum() == 4) {
							TESTINGGAME.getPlayer().setFloorNum(TESTINGGAME.getPlayer().getFloorNum() + 1);
							TESTINGGAME.getPlayer().getCoordinates().setCoordinates(
									TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1].getPlayerStart()
											.getX(),
									TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1].getPlayerStart()
											.getY());
						}
					}
				}
			});
			// TODO
			for (int i = 0; i < Stats.values().length; i++) {
				Label stat = new Label(Stats.values()[i].toString().substring(0, 3));
				Label statNum = new Label(TESTINGGAME.getPlayer().getStat(Stats.values()[i]) + "");
				statGrid.addRow(i, stat, statNum);
			}
			menuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					displayPauseMenu();
				}

			});
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty()
					.divide(TESTINGGAME.getPlayer().getMaxHPProperty().doubleValue()));
			playerName.setText(TESTINGGAME.getPlayer().NAME);

			// Drawing testing
			// GraphicsContext gc = canvas.getGraphicsContext2D();
			drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]);
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
			Scene scene = new Scene(p);
			String css = getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void displayEndBattle(Battle b) {
		Player player = b.getPlayer();
//		Enemy[] enemiesArray = b.getEnemies();
		if (player.getHPProperty().get() > 0) {
			// TODO(andrew): pop a text view displaying loot and exp/level gain
			// stats
			Text display = new Text(100, 100, "You beat the dudes mango im real prouda you goodjob");

			// TODO(andrew): This is called when they quit out of the
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					((AnchorPane) primaryStage.getScene().getRoot()).getChildren().add(display);
					displayGeneralView();
				}

			});
		} else {
			// TODO(andrew): pop a text view displaying "YOU SUCK" or something
			// along those lines.
		}
	}

	private void drawToGeneralCanvas(Floor currentFloor) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		WritableImage image = TileManager.getImageToDraw(currentFloor.getTiles(),
				currentFloor.getPlayer().getCoordinates());
		gc.drawImage(image, 0, 0, image.getWidth() * (canvas.getWidth() / image.getWidth()),
				image.getHeight() * (canvas.getHeight() / image.getHeight()));

	}

	public void displayInventoryView(Inventory inv) {
		this.currentLayout = GUILayouts.INVENTORY;
	}

	public void displayCharacterManager() {
		currentLayout = GUILayouts.PLAYER_MENU;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/CharacterView.fxml"));
		loader.setController(this);

		try {
			p = loader.load();

			exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					displayPauseMenu();
				}
			});

			playerName.setText(TESTINGGAME.getPlayer().NAME);

			for (int i = 0; i < Stats.values().length; i++) {
				Label stat = new Label(Stats.values()[i].toString());
				Label statNum = new Label(TESTINGGAME.getPlayer().getStat(Stats.values()[i]) + "");
				statGrid.addRow(i, stat, statNum);
			}

			// TODO Figure out if items are going to have graphic or just text

			Scene scene = new Scene(p);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayLootManager() {
		currentLayout = GUILayouts.LOOT_MANAGER;
	}

}
