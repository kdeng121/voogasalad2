package game_player;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class SettingsMenu {
	
	private Button settingsButton;
	private VBox settingsMenu;
	private Stage settingsStage;
	private Slider volumeSlider;
	private Slider brightnessSlider;
	private Label volumeLabel;
	private Label brightnessLabel;
	
	protected void makeSettingsMenu(HBox pane) {
		settingsButton = new Button("Settings");
		settingsButton.getStyleClass().add("button-nav");
		settingsButton.setOnAction(click->{showSettingsMenu();});
		pane.getChildren().add(settingsButton);
		settingsMenu = new VBox(50);
		settingsMenu.setPrefWidth(300);
		BackgroundImage back = new BackgroundImage(new Image("background.png"), BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		settingsMenu.setBackground(new Background(back));
		settingsButton.setOnAction(click->{showSettingsMenu();});	
	}
	protected void makeSettingsStage() {
		settingsStage = new Stage();
		Scene scene = new Scene(settingsMenu);
		scene.getStylesheets().add(getClass().getResource("/main/aesthetic.css").toString());
		makeBrightnessSlider();
		makeVolumeSlider();
		makeBrightnessLabel();
		makeVolumeLabel();
		settingsMenu.getChildren().add(brightnessLabel);
		settingsMenu.getChildren().add(brightnessSlider);
		settingsMenu.getChildren().add(volumeLabel);
		settingsMenu.getChildren().add(volumeSlider);
		settingsStage.setScene(scene);
	}
	
	private void makeBrightnessSlider() {
		brightnessSlider = new Slider();
		brightnessSlider.getStyleClass().add("slider");
		brightnessSlider.setMin(-1);
		brightnessSlider.setMax(1);
		
	}
	
	private void makeVolumeSlider() {
		volumeSlider = new Slider();
		volumeSlider.getStyleClass().add("slider");
		volumeSlider.setMin(0);
		volumeSlider.setMax(1);
	}
	
	private void makeBrightnessLabel() {
		brightnessLabel = new Label("Change Brightness:");
		brightnessLabel.getStyleClass().add("button-nav");
	}
	
	private void makeVolumeLabel() {
		volumeLabel = new Label("Change Volume:");
		volumeLabel.getStyleClass().add("button-nav");
		
	}
	/**
	 * method to show settings Menu
	 * 
	 */
	protected void showSettingsMenu() {
		settingsStage.show();
		
	}
	/**
	 * getter method for the Brightness Slider on the Settings Stage
	 * 
	 */
	public Slider getBrightnessSlider() {
		return brightnessSlider;
	}
	/**
	 * getter method for the Volume Slider on the Settings Stage
	 * 
	 */
	public Slider getVolumeSlider() {
		return volumeSlider;
	}
	

}
