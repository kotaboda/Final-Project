package application;

import java.io.IOException;
import java.util.ArrayList;

import abilityInterfaces.Ability;
import battleSystem.Battle;
import character.Enemy;
import character.Player;
import characterEnums.Direction;
import characterEnums.InventoryAction;
import characterEnums.Stats;
import characterInterfaces.Lootable;
import enums.GUILayouts;
import floors.Floor;
import itemSystem.Item;
import itemSystem.Usable;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
import javafx.util.Duration;
import models.Coordinates;
import publisherSubscriberInterfaces.Listener;
import tiles.TileManager;

public class GameGUI extends Application {

	private Stage primaryStage;
	private GUILayouts currentLayout = GUILayouts.MAIN_MENU;
	private Game TESTINGGAME = GameEngine.getGame();
	private boolean isPlayersTurn = false;
	private Object lock = new Object();
	private Parent p;
	private TextArea displayText = new TextArea("");
	private boolean isAnimating = false;
	{
		displayText.setId("displayText");
		// displayText.setMouseTransparent(true);
		displayText.setWrapText(true);
		displayText.setFocusTraversable(false);
		displayText.setEditable(false);
	}

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
	private GridPane playerInventoryGrid;
	@FXML
	private ListView<Item> otherInventoryGrid;
	@FXML
	private Button lootManagerButton;
	@FXML
	private Button exitLootButton;
	@FXML
	private GridPane statGrid;

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/MainMenuView.fxml"));

		loader.setController(this);

		try {

			p = loader.load();

			newGameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					TESTINGGAME = new Game(new Player());
					GameEngine.setGame(TESTINGGAME);
					displayGeneralView();
				}
			});

			loadGameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					TESTINGGAME = GameEngine.loadGame();
					displayGeneralView();
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/PauseView.fxml"));
		loader.setController(this);
		try {
			p = loader.load();
			p.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					KeyCode k = event.getCode();
					switch (k) {
					case ESCAPE:
						displayGeneralView();
						break;
					default:
						break;
					}
				}
			});
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
	@FXML
	private VBox battleLogVBox;
	@FXML
	private ScrollPane bLogScrollPane;
	private ListView<Usable> itemList;

	public void displayBattleView(Battle b) {

		this.currentLayout = GUILayouts.BATTLE;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/BattleView.fxml"));

		loader.setController(this);
		try {
			p = loader.load();

			playerName.setText(TESTINGGAME.getPlayer().NAME + " Lvl. " + TESTINGGAME.getPlayer().getLevel());
			enemies.setAlignment(Pos.CENTER);
			Listener<Battle> s = new Listener<Battle>() {

				@Override
				public void update() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Text l = new Text(b.getLoggedAction());
							l.maxWidth(120);
							l.setWrappingWidth(130);
							battleLogVBox.getChildren().add(l);
							bLogScrollPane.vvalueProperty().bind(battleLogVBox.heightProperty());
						}
					});
				}
			};
			b.addSubscriber(s);
			ArrayList<Label> enemyNames = new ArrayList<Label>();
			for (int i = 0; i < b.getEnemies().length; i++) {
				Enemy currentEnemy = b.getEnemies()[i];
				Label enemyName = new Label(currentEnemy.NAME);
				enemyNames.add(enemyName);
				ProgressBar enemyHealth = new ProgressBar();
				enemyHealth.progressProperty()
						.bind(currentEnemy.getHPProperty().divide(currentEnemy.getMaxHPProperty().doubleValue()));
				Group child = new Group();
				enemies.getChildren().add(child);
				enemyHealth.progressProperty().addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (newValue.doubleValue() <= 0) {
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
				VBox mainContainer = new VBox(new Canvas(100, 100), enemyName, enemyHealth);
				child.getChildren().add(mainContainer);
				child.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						b.setPlayerTarget(currentEnemy);

						for (Label name : enemyNames) {
							name.styleProperty().setValue("");
						}
						enemyName.setStyle("-fx-background-color : lightblue;");
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
							ArrayList<Usable> usable = new ArrayList<>();
							for (int i = 0; i < TESTINGGAME.getPlayer().getInventoryContents().length; i++) {
								if (TESTINGGAME.getPlayer().getInventoryContents()[i] instanceof Usable) {
									usable.add((Usable) TESTINGGAME.getPlayer().getInventoryContents()[i]);
								}
							}
							itemList = new ListView<Usable>(FXCollections.observableArrayList(usable));
							itemList.getSelectionModel().selectedItemProperty()
									.addListener(new ChangeListener<Usable>() {

										@Override
										public void changed(ObservableValue<? extends Usable> observable,
												Usable oldValue, Usable newValue) {
											if (isPlayersTurn) {
												submitButton.setDisable(false);
											}
											Label l = new Label(((Item) newValue).getDescription());
											l.wrapTextProperty().set(true);
											rightBattleVBox.getChildren().clear();
											rightBattleVBox.getChildren().add(l);
										}

									});
							itemList.getSelectionModel().select(0);
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
					isPlayersTurn = false;
					synchronized (lock) {
						lock.notifyAll();
					}
				}

			});
			playerEnergyBar.progressProperty().bind(TESTINGGAME.getPlayer().getEnergyProperty()
					.divide(TESTINGGAME.getPlayer().getMaxEnergyProperty().doubleValue()));
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty()
					.divide(TESTINGGAME.getPlayer().getMaxHPProperty().doubleValue()));
			Scene scene = new Scene(p);
			String css = getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);

			primaryStage.setScene(scene);
			primaryStage.show();
			GameEngine.startBattle(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void waitForPlayerSelection(Battle battle) {
		isPlayersTurn = true;
		try {
			synchronized (lock) {
				lock.wait();
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
			battle.setPlayerNextItemUse(itemList.getSelectionModel().getSelectedItem());
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

	// GeneralView specific elements
	@FXML
	private Button menuButton;

	public void displayGeneralView() {
		this.currentLayout = GUILayouts.GENERAL;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/GeneralView.fxml"));

		loader.setController(this);
		try {
			p = loader.load();
			playerImageView.setImage(new Image(getClass().getResourceAsStream("/hero.jpg")));
			drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], 0, 0);
			p.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (currentLayout == GUILayouts.GENERAL) {
						KeyCode keyEvent = event.getCode();

						switch (keyEvent) {
						case W:
							if (!((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
								if(!isAnimating && GameEngine.checkMovement(Direction.UP)){
									playAnimation(Direction.UP);
//									GameEngine.updatePlayerPosition(Direction.UP);
									TESTINGGAME.getPlayer().setDirectionFacing(Direction.UP);
								}else if(!isAnimating){
									TESTINGGAME.getPlayer().setDirectionFacing(Direction.UP);
									drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], 0, 0);
								}
							}
							break;
						case S:
							if (!((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
								if(!isAnimating && GameEngine.checkMovement(Direction.DOWN)){
									playAnimation(Direction.DOWN);
//									GameEngine.updatePlayerPosition(Direction.DOWN);
									TESTINGGAME.getPlayer().setDirectionFacing(Direction.DOWN);
								}else if(!isAnimating){
									TESTINGGAME.getPlayer().setDirectionFacing(Direction.DOWN);
									drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], 0, 0);
								}
							}
							break;
						case A:
							if (!((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
								if(!isAnimating && GameEngine.checkMovement(Direction.LEFT)){
									playAnimation(Direction.LEFT);
//									GameEngine.updatePlayerPosition(Direction.LEFT);
									TESTINGGAME.getPlayer().setDirectionFacing(Direction.LEFT);
								}else if(!isAnimating){
									TESTINGGAME.getPlayer().setDirectionFacing(Direction.LEFT);
									drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], 0, 0);
								}
							}
							break;
						case D:
							if (!((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
								if(!isAnimating && GameEngine.checkMovement(Direction.RIGHT)){
									playAnimation(Direction.RIGHT);
//									GameEngine.updatePlayerPosition(Direction.RIGHT);
									TESTINGGAME.getPlayer().setDirectionFacing(Direction.RIGHT);
								}else if(!isAnimating){
									TESTINGGAME.getPlayer().setDirectionFacing(Direction.RIGHT);
									drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], 0, 0);
								}
							}
							break;
						case E:
							if (((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
								((AnchorPane) primaryStage.getScene().getRoot()).getChildren().remove(displayText);
								GameEngine.checkLoot();
							} else {
								GameEngine.checkNote();
							}
							
							break;
						case ESCAPE:
							displayPauseMenu();
							break;
						default:
							break;
						}
						// NOTE(andrew): added this if statement to ensure that
						// this code only runs when it needs to.
						if (keyEvent.equals(KeyCode.W) || keyEvent.equals(KeyCode.A) || keyEvent.equals(KeyCode.S)
								|| keyEvent.equals(KeyCode.D)) {
//							drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], 0, 0);
							Battle b = GameEngine
									.checkForBattle(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]);
							// String message = GameEngine.checkNote();

							Coordinates playerCoord = TESTINGGAME.getPlayer().getCoordinates();
							if (b != null) {
								displayBattleView(b);

							} else if (TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]
									.getTiles()[playerCoord.getY()][playerCoord.getX()].getTileSheetNum() == 4) {
								TESTINGGAME.getPlayer().setFloorNum(TESTINGGAME.getPlayer().getFloorNum() + 1);
								TESTINGGAME.getPlayer().getCoordinates().setCoordinates(
										TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]
												.getPlayerStart().getX(),
										TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]
												.getPlayerStart().getY());
								drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum()-1], 0, 0);
							}
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
			statGrid.addRow(Stats.values().length, new Label("LVL"),
					new Label("" + TESTINGGAME.getPlayer().getLevel()));
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
			//NOTE(andrew): animation testing!!
//			drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1]);
//			Animation testing
//			isAnimating = true;
//			Timeline gameLoop = new Timeline();
//			gameLoop.setCycleCount( 64 );
//			
//			KeyFrame kf = new KeyFrame(
//				Duration.seconds(0.01666667), // 60 FPS
//				new EventHandler<ActionEvent>()
//				{
//					int x = 0;
//					public void handle(ActionEvent ae)
//					{
//						x += 1;
//						int tempX = x % 64;
//						drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], x, 0);
//						if(tempX == 0){
//							x = 0;
//							isAnimating = false;
//						}
//					}
//				}
//			);
//			gameLoop.getKeyFrames().add( kf );
//			gameLoop.play();
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

	public void displayMessage(String message) {
		displayText.setText(message);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				((AnchorPane) primaryStage.getScene().getRoot()).getChildren().add(displayText);

				// NOTE(andrew): this must be an event filter that is passed in
				// the type of event and its function, rather than using the
				// setOnKeyPressed() method, because there are selections made
				// in the battle view, and those selections eat up the ESCAPE
				// event. Using this filter allows us to read the keycode before
				// it is eaten up.
				p.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					if ((event.getCode().equals(KeyCode.ESCAPE) || event.getCode().equals(KeyCode.E))
							&& currentLayout.equals(GUILayouts.BATTLE)) {
						// ((AnchorPane)
						// primaryStage.getScene().getRoot()).getChildren().remove(displayText);
						displayGeneralView();
					}
				});
			}

		});
	}

	public void displayEndBattle(Battle b) {
		Player player = b.getPlayer();
		// Enemy[] enemiesArray = b.getEnemies();
		if (player.getHPProperty().get() > 0) {
			// TODO(andrew): pop a text view displaying loot and exp/level gain
			// stats

			displayMessage("You beat the dudes mango im real prouda you goodjob\nb\nu\nt\ni\n'\nm\nn\no\nt");
			// displayText.setText("You beat the dudes mango im real prouda you
			// goodjob\nb\nu\nt\ni\n'\nm\nn\no\nt");
			//
			// Platform.runLater(new Runnable() {
			// @Override
			// public void run() {
			//
			// ((AnchorPane)
			// primaryStage.getScene().getRoot()).getChildren().add(displayText);
			//
			// //NOTE(andrew): this must be an event filter that is passed in
			// the type of event and its function, rather than using the
			// //setOnKeyPressed() method, because there are selections made in
			// the battle view, and those selections eat up the escape
			// //event. Using this filter allows us to read the keycode before
			// it is eaten up.
			// p.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			// if (event.getCode().equals(KeyCode.ESCAPE) &&
			// currentLayout.equals(GUILayouts.BATTLE)) {
			// displayGeneralView();
			// }
			// });
			// }
			//
			// });
		} else {
			// TODO(andrew): pop a text view displaying "YOU SUCK" or something
			// along those lines.
			displayText.setText("You real bad at this videogame thign");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					((AnchorPane) primaryStage.getScene().getRoot()).getChildren().add(displayText);

					// NOTE(andrew): this must be an event filter that is passed
					// in the type of event and its function, rather than using
					// the
					// setOnKeyPressed() method, because there are selections
					// made in the battle view, and those selections eat up the
					// escape
					// event. Using this filter allows us to read the keycode
					// before it is eaten up.
					p.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
						if (event.getCode().equals(KeyCode.ESCAPE) && currentLayout.equals(GUILayouts.BATTLE)) {
							displayMainMenu();
						}
					});
				}

			});
		}
	}

	private void drawToGeneralCanvas(Floor currentFloor, int offsetX, int offsetY) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		WritableImage image = TileManager.getImageToDraw(currentFloor.getTiles(),
				currentFloor.getPlayer().getCoordinates());
		int currentImageIndex = 0;
		int imageRow = 0;
		switch(currentFloor.getPlayer().getDirectionFacing()){
		
		case UP:
			currentImageIndex = Math.abs((offsetY % 32) / 8);
			imageRow = 0;
			break;
		case DOWN:
			currentImageIndex = Math.abs((offsetY % 32) / 8);
			imageRow = 2;
			break;
			
		case LEFT:
			currentImageIndex = Math.abs((offsetX % 32) / 8);
			imageRow = 3;
			break;
		case RIGHT:
			currentImageIndex = Math.abs((offsetX % 32) / 8);
			imageRow = 1;
			break;
		}
		Image playerImg = new Image(getClass().getResourceAsStream("/images/MaleWalk.png"));
//		gc.drawImage(image, 0 + offsetX, 0 + offsetY, image.getWidth() * (canvas.getWidth() / image.getWidth()),
//				image.getHeight() * (canvas.getHeight() / image.getHeight()));
		gc.drawImage(image, -64 + offsetX, -64 + offsetY, image.getWidth() * 2, image.getHeight() * 2);
//		gc.drawImage(playerImg, (canvas.getWidth() / 2) - 16, (canvas.getHeight() / 2) - 16, 32, 32);
//		gc.drawImage(playerImg, (canvas.getWidth() / 2) - 32, (canvas.getHeight() / 2) - 32, 64, 64);
		gc.drawImage(playerImg, currentImageIndex * 32, imageRow * 32, 32, 32, (canvas.getWidth() / 2) - 32, (canvas.getHeight() / 2) - 32, 64, 64);
//		System.out.println(currentFloor.getPlayer().getCoordinates());
//		System.out.println("X: " + offsetX + "Y: " + offsetY);
	}

	public void displayCharacterManager() {
		currentLayout = GUILayouts.PLAYER_MENU;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/CharacterView.fxml"));
		loader.setController(this);

		try {
			p = loader.load();
			p.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					KeyCode k = event.getCode();
					switch (k) {
					case ESCAPE:
						displayPauseMenu();
						break;
					default:
						break;
					}
				}

			});

			exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					displayPauseMenu();
				}
			});

			playerName.setText(TESTINGGAME.getPlayer().NAME + " Lvl. " + TESTINGGAME.getPlayer().getLevel());
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty().divide(TESTINGGAME.getPlayer().getMaxHPProperty().doubleValue()));
			playerEnergyBar.progressProperty().bind(TESTINGGAME.getPlayer().getEnergyProperty().divide(TESTINGGAME.getPlayer().getMaxEnergyProperty().doubleValue()));
			for (int i = 0; i < Stats.values().length; i++) {
				Label stat = new Label(Stats.values()[i].toString());
				Label statNum = new Label(TESTINGGAME.getPlayer().getStat(Stats.values()[i]) + "");
				statGrid.addRow(i, stat, statNum);
			}
			for (int i = 0; i < TESTINGGAME.getPlayer().getInventoryContents().length; i++) {
				Item theItem = TESTINGGAME.getPlayer().getInventoryContents()[i];
				Menu m = new Menu(theItem.toString() + ": " + theItem.getDescription());
				MenuBar item = new MenuBar(m);
				GridPane.setHalignment(item, HPos.CENTER);
				if (theItem instanceof Usable) {
					MenuItem mi = new MenuItem("Use");
					mi.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							((Usable) theItem).use(TESTINGGAME.getPlayer());
							TESTINGGAME.getPlayer().modifyInventory(InventoryAction.TAKE, theItem);
							playerInventoryGrid.getChildren().remove(item);
						}

					});
					m.getItems().add(mi);
				}
				playerInventoryGrid.addRow(i, item);
			}

			Scene scene = new Scene(p);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayLootManager(Lootable l) {
		currentLayout = GUILayouts.LOOT_MANAGER;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/LootManagerView.fxml"));
		loader.setController(this);

		try {
			p = loader.load();

			for (int i = 0; i < l.obtainLoot().length; i++) {
				// Label item = new Label(l.obtainLoot()[i].toString() + ": " +
				// l.obtainLoot()[i].getDescription());
				// GridPane.setHalignment(item, HPos.CENTER);
				otherInventoryGrid.getItems().add(l.obtainLoot()[i]);

			}
			lootManagerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					Item item = otherInventoryGrid.getSelectionModel().getSelectedItem();
					boolean successful = false;
					if (item != null) {
						successful = GameEngine.givePlayerItem(item);
					}
					if (!successful && !((AnchorPane) p).getChildren().contains(displayText)) {
						displayMessage("You can't take the item!");
					}
					if (successful) {
						otherInventoryGrid.getItems().remove(item);
						l.removeItem(item);
						Label label = new Label(item.toString() + ": " + item.getDescription());
						GridPane.setHalignment(label, HPos.CENTER);
						playerInventoryGrid.addRow(TESTINGGAME.getPlayer().getInventoryContents().length, label);
					}
				}
			});
			exitLootButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					displayGeneralView();
				}
			});
			for (int i = 0; i < TESTINGGAME.getPlayer().getInventoryContents().length; i++) {
				Label item = new Label(TESTINGGAME.getPlayer().getInventoryContents()[i].toString() + ": "
						+ TESTINGGAME.getPlayer().getInventoryContents()[i].getDescription());
				GridPane.setHalignment(item, HPos.CENTER);
				playerInventoryGrid.addRow(i, item);
			}

			Scene scene = new Scene(p);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void playAnimation(Direction direction){
		if(!isAnimating){
			switch(direction){
			case UP:
				isAnimating = true;
				new AnimationTimer()
				{
					int y = 0;
					public void handle(long currentNanoTime) {
						y += 2;
						int temp = y % 64;
						drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], 0, y);
						if(temp == 0){
							isAnimating = false;
							y = 0;
							GameEngine.updatePlayerPosition(Direction.UP);
							this.stop();
						}
					
					}
				}.start();
			
				break;
			case RIGHT:
				isAnimating = true;
				new AnimationTimer()
				{
					int x = 0;
					public void handle(long currentNanoTime) {
						x -= 2;
						int temp = x % 64;
						drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], x, 0);
						if(temp == 0){
							isAnimating = false;
							x = 0;
							GameEngine.updatePlayerPosition(Direction.RIGHT);
							this.stop();
						}
					
					}
				}.start();
				break;
				
			case DOWN:
				isAnimating = true;
				new AnimationTimer()
				{
					int y = 0;
					public void handle(long currentNanoTime) {
						y -= 2;
						int temp = y % 64;
						drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], 0, y);
						if(temp == 0){
							isAnimating = false;
							y = 0;
							GameEngine.updatePlayerPosition(Direction.DOWN);
							this.stop();
						}
					
					}
				}.start();
				//
				
				break;
				
			case LEFT:
				isAnimating = true;
				new AnimationTimer()
				{
					int x = 0;
					public void handle(long currentNanoTime) {
						x += 2;
						int temp = x % 64;
						drawToGeneralCanvas(TESTINGGAME.getFloors()[TESTINGGAME.getPlayer().getFloorNum() - 1], x, 0);
						if(temp == 0){
							isAnimating = false;
							x = 0;
							GameEngine.updatePlayerPosition(Direction.LEFT);
							this.stop();
						}
					
					}
				}.start();
	
				break;
				
			default:
				break;
			}
		}
	}
}
