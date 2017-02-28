package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import abilityInterfaces.Ability;
import battleSystem.Battle;
import enums.GUILayouts;
import floors.Floor;
import itemSystem.Inventory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tiles.TileManager;
import viewInterface.Viewable;

public class GameGUI extends Application implements Viewable {

	private Stage primaryStage;
	private GUILayouts currentLayout = GUILayouts.MAIN_MENU;
	private final Game TESTINGGAME = GameEngine.getGame();

	
	@FXML
	private HBox enemies;
	@FXML
	private ListView<Ability> abilityList;
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
//			displayBattleView(new Battle(TESTINGGAME.getPlayer()));
			displayGeneralView(TESTINGGAME.getFloors()[0]);
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
					GameEngine.setGame(new Game(null));
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
			Scene scene = new Scene(p); 
			scene.getStylesheets().add("application.css");
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
		try{
			Parent p = loader.load(Files.newInputStream(Paths.get("src/PauseView.fxml")));
			primaryStage.setScene(new Scene(p));
			primaryStage.show();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void displayBattleView(Battle b) {
		// TODO Auto-generated method stub
		this.currentLayout = GUILayouts.BATTLE;
		FXMLLoader loader = new FXMLLoader();

		loader.setController(this);
		try {
			Parent p = loader.load(Files.newInputStream(Paths.get("src/BattleView.fxml")));
			playerName.setText(TESTINGGAME.getPlayer().name);
			
			for(int i = 0; i < b.getEnemies().length; i++){
				Node child = new Group(new VBox(new Canvas(), new Label(b.getEnemies()[i].name), new ProgressBar()));
				enemies.getChildren().add(child);
			}
			
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty().divide(TESTINGGAME.getPlayer().getHPProperty().doubleValue()));
			Scene scene = new Scene(p);
			String css = this.getClass().getResource("application.css").toExternalForm(); 
			scene.getStylesheets().add(css);
			abilityList.setItems(TESTINGGAME.getPlayer().getAbilities());
			primaryStage.setScene(scene);
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

						switch (keyEvent) {
						case W:
							GameEngine.updatePlayerPosition(0, -1);
							drawToGeneralCanvas(currentFloor);
							break;
						case S:
							GameEngine.updatePlayerPosition(0, 1);
							drawToGeneralCanvas(currentFloor);

							break;
						case A:
							GameEngine.updatePlayerPosition(-1, 0);
							drawToGeneralCanvas(currentFloor);

							break;
						case D:
							GameEngine.updatePlayerPosition(1, 0);
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
				.bind(TESTINGGAME.getPlayer().getHPProperty().divide(TESTINGGAME.getPlayer().getMaxHPProperty().doubleValue()));
			playerName.setText(TESTINGGAME.getPlayer().name);
			
			
			//Drawing testing
			GraphicsContext gc = canvas.getGraphicsContext2D();
			drawToGeneralCanvas(currentFloor);
			//WritableImage image = TileManager.getImageToDraw(currentFloor.getTiles(), playerSummary.coordinates);
			//gc.drawImage(image, 0, 0, image.getWidth() * 2, image.getHeight() * 2);
			//Animation testing
//			Timeline gameLoop = new Timeline();
//	        gameLoop.setCycleCount( 64 );
//	        
//	        KeyFrame kf = new KeyFrame(
//	            Duration.seconds(0.01666667),                // 60 FPS
//	            new EventHandler<ActionEvent>()
//	            {
//	            	int x = 0;
//	                public void handle(ActionEvent ae)
//	                {
//	                	x -= 1;
//			        	int tempX = x % 64;
//			        	gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//			        	if(tempX == 0){
//			        		playerSummary.coordinates.setX(playerSummary.coordinates.getX() + 1);
//			        		x = 0;
//			        	}
//			        	WritableImage image = TileManager.getImageToDraw(currentFloor.getTiles(), playerSummary.coordinates);
//			        	System.out.println(x);
//			        	gc.drawImage(image, tempX - 64, 0, image.getWidth() * 2, image.getHeight() * 2);
//	                }
//	            });
//	        
//	        gameLoop.getKeyFrames().add( kf );
//	        gameLoop.play();
//	        gameLoop.getKeyFrames().add( kf );
//	        gameLoop.play();
			//split
//			new AnimationTimer()
//		    {
//				int x = 0;
//		        public void handle(long currentNanoTime)
//		        {
//		        	x -= 1;
//		        	int tempX = x % 64;
//		        	gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//		        	if(tempX == 0){
//		        		playerSummary.coordinates.setX(playerSummary.coordinates.getX() + 1);
//		        		x = 0;
//		        	}
//		        	WritableImage image = TileManager.getImageToDraw(currentFloor.getTiles(), playerSummary.coordinates);
//		        	gc.drawImage(image, tempX - 64, 0, image.getWidth() * 2, image.getHeight() * 2);
//		        	
//		        	
//		        }
//		    }.start();
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
		gc.drawImage(image, -64, -64, image.getWidth() * 2, image.getHeight() * 2);
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
