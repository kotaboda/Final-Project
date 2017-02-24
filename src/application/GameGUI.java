package application;
	
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import battleSystem.Battle;
import itemSystem.Inventory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import viewInterface.Viewable;


public class GameGUI extends Application implements Viewable {
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			displayGeneralView();
			primaryStage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

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
		System.out.println("Pause Button Clicked!");
	}

	@Override
	public void displayBattleView(Battle b) {
		// TODO Auto-generated method stub
		
		FXMLLoader loader = new FXMLLoader();
		
		loader.setController(this);
		try {
			Parent p = loader.load(Files.newInputStream(Paths.get("src/BattleView.fxml")));
			primaryStage.setScene(new Scene(p));
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
	private Button menuButton;
	@Override
	public void displayGeneralView() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		
		loader.setController(this);
		try {
			Parent p = loader.load(Files.newInputStream(Paths.get("src/GeneralView.fxml")));
			menuButton.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					displayBattleView(null);
				}
				
			});
			primaryStage.setScene(new Scene(p));
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
