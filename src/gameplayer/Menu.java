package gameplayer;

import java.util.ArrayList;
import authoring.GameChooserScreen;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author Brandon Dalla Rosa, Dana Park
 *
 */

public class Menu {
	
	private HBox pane;
	private PulldownFactory pullDownFactory = new PulldownFactory();
	private VBox keyPrefMenu;
	private Button keyPrefButton;
	private Button gameSelectionButton;
	private Stage keyPrefStage;
	private Stage gameSelectionStage;
	private DataManager dataManager;
	private KeyCode currentKey;
	private Button currentPrefButton;	
	private String currentPrefString;
	
	public Menu(DataManager data) {
		pane = new HBox(20);
		pane.setAlignment(Pos.CENTER);
		dataManager = data;
		currentKey = KeyCode.ENTER;
		currentPrefButton = new Button();
		makePullDownMenus();
		makeKeyPrefMenu();
		makeGameSelectionMenu();
	}
	/**
	 * Method to add the menu into the VBox for the View Manager
	 * 
	 * @param root
	 */
	public void addMenu(Pane root) {
		root.getChildren().add(pane);
	}
	
	private void makePullDownMenus() {
		pane.getChildren().add(pullDownFactory.SpeedBox());
		pane.getChildren().add(pullDownFactory.StatusBox());
		pane.getChildren().add(pullDownFactory.SaveLoadBox());
		
	}
	private void makeKeyPrefMenu() {
		keyPrefMenu = new VBox(25);
		keyPrefButton = new Button("Key Prefs");
		keyPrefButton.setOnAction(click->{showPrefMenu();});
		keyPrefButton.setPrefSize(160, 20);
		keyPrefButton.getStyleClass().add("button-nav");
		pane.getChildren().add(keyPrefButton);
		initKeyPrefMenu();
		keyPrefStage = new Stage();
		Scene scene = new Scene(keyPrefMenu);
		scene.getStylesheets().add(getClass().getResource("playerAesthetic.css").toString());
		scene.setOnKeyPressed(click->checkForInput(click.getCode()));
		keyPrefStage.setScene(scene);
		keyPrefMenu.getStyleClass().add("pane-back");
		
	}
	private void makeGameSelectionMenu() {
		gameSelectionButton = new Button("Game Selection");
		gameSelectionButton.getStyleClass().add("button-nav");
		gameSelectionButton.setOnAction(click->{showGameSelectionMenu();});
		pane.getChildren().add(gameSelectionButton);
		
	}
	

	
	private void initKeyPrefMenu() {
		ArrayList<String> inputs = (ArrayList<String>) dataManager.getInputCommands();
		for(String s : inputs) {
			HBox toAdd = new HBox(10);
			toAdd.setAlignment(Pos.CENTER);
			Label label = new Label(s);
			label.getStyleClass().add("text-keypref");

			Button button = new Button("ENTER");
			button.getStyleClass().add("button-keypref");
			button.setOnAction(click->{setPref(button,s);});
			toAdd.getChildren().add(label);
			toAdd.getChildren().add(button);
			keyPrefMenu.getChildren().add(toAdd);
		}
	}
	
	private void setPref(Button button,  String string) {
		currentPrefButton = button;
		currentPrefString = string;
	}
	private void showPrefMenu() {
		keyPrefStage.show();
	}
	
	public void showGameSelectionMenu() {
		gameSelectionStage = new Stage();
		GameChooserScreen gc = new GameChooserScreen(gameSelectionStage);
		gameSelectionStage.setScene(gc.display());
		gameSelectionStage.show();
	}
	
	public void checkForInput(KeyCode code) {
		currentKey = code;
		currentPrefButton.getStyleClass().add("button-keypref");

		currentPrefButton.setText(""+currentKey);
		dataManager.setKey(currentPrefString, code);
	}
	

}
