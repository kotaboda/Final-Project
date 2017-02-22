package application;
	
import battleSystem.Battle;
import itemSystem.Inventory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import viewInterface.Viewable;


public class GameGUI extends Application implements Viewable {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void displayMainMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayPauseMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayBattleView(Battle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGeneralView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayInventoryView(Inventory inv) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayCharacterManager() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayLootManager() {
		// TODO Auto-generated method stub
		
	}



}
