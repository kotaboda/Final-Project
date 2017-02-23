package application;
	
import java.nio.file.Files;
import java.nio.file.Paths;

import battleSystem.Battle;
import itemSystem.Inventory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import viewInterface.Viewable;


public class GameGUI extends Application implements Viewable {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Parent battleView = loader.load(Files.newInputStream(Paths.get("src/BattleView.fxml")));
			Scene scene = new Scene(battleView);
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
