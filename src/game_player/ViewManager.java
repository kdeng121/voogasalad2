package game_player;

import java.awt.event.MouseAdapter;
import java.io.File;

import authoring.GameChooserScreen;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * 
 * @author Brandon Dalla Rosa, Dana Park
 *
 */
public class ViewManager {
	private Menu menu;
	private Stage gameStage;
	private double sceneWidth = 1200;
	private double sceneHeight = 900;
	private Paint backColor = Color.BLACK;
	private Pane view;
	private Scene gameScene;
	private PulldownFactory pullDownFactory;
	private ImageView gameImageView;
	private Image gameBackground;
	private BackgroundImage game;
	private SubScene subScene;
	private Group subRoot;
	private ColorAdjust colorAdjust = new ColorAdjust();
	
	public ViewManager(Menu menu, Stage stage, PulldownFactory pdf) {
		this.menu = menu;
		this.pullDownFactory = pdf;
		pullDownFactory.setViewManager(this);
		this.gameStage = stage;
		setScene();
		gameStage.setTitle("CALL US SALAD");
		gameStage.show();
		changeBrightness();
		changeVolume();
	}

	private void setScene() {
		Pane pane = setObjects();
		gameScene = new Scene(pane,sceneWidth,sceneHeight);
		gameScene.getStylesheets().add(getClass().getResource("/main/aesthetic.css").toString());
		gameStage.setScene(gameScene);
	}

	public Scene getScene() {
		return gameScene;
	}

	private Pane setObjects() {
		HBox center = new HBox(30);
		center.setAlignment(Pos.CENTER);
		
		gameBackground = new Image("mountain.png");
		gameImageView = new ImageView();
		gameImageView.setImage(gameBackground);

		BackgroundImage back = new BackgroundImage(new Image("background.png"), BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		center.setBackground(new Background(back));

		VBox order = new VBox(20);
		order.getStyleClass().add("pane-back");

		order.setAlignment(Pos.CENTER);
		center.getChildren().add(order);
		
		menu.addMenu(order);
		view = new Pane();
		view.setPrefSize(1000, 730);
		subRoot = new Group();
		subScene = new SubScene(subRoot, 770, 530, false, null);

		game = new BackgroundImage(gameBackground, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		view.setBackground(new Background(game));

		order.getChildren().add(subScene);
		subRoot.getChildren().add(view);
		order.setBackground(new Background(new BackgroundFill(backColor,null,null)));
		return center;
	}
	
	public SubScene getSubScene() {
		return subScene;
	}
	
	public Group getSubRoot() {
		return subRoot;
	}
	
	
	
	public void changeBackground() {
		BackgroundImage back = new BackgroundImage(new Image("mountain.png"), BackgroundRepeat.NO_REPEAT, 
		BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		view.setBackground(new Background(back));
	}


	
	public void changeBrightness() {
		this.menu.getBrightnessSlider().valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				
				colorAdjust.setBrightness((double) new_val);
				gameImageView.setEffect(colorAdjust);
				System.out.println(new_val);
			}
		});
	}

	public void changeVolume() {
		this.menu.getVolumeSlider().valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				String path = "/resources/baby.mp3";
				Media media = new Media(new File(path).toURI().toString());

				MediaPlayer mediaPlayer = new MediaPlayer(media);
				mediaPlayer.setVolume((double) new_val);
				mediaPlayer.play();

			}
		});
	}

	public void showGameSelectionMenu() {
		GameChooserScreen gc = new GameChooserScreen(gameStage);
		gameStage.setScene(gc.display());
		gameStage.show();
	}

	public Pane getNode() {
		return view;
	}
}