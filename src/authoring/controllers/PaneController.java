package authoring.controllers;

import java.io.File;

import authoring.Canvas;
import authoring.right_components.LevelPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class PaneController {

	private LevelPane levelPane;
	private Canvas canvas;
	// more instance variables can be added as pane controller develops
	// more responsibilities
	
	public PaneController(LevelPane lp, Canvas c){
		levelPane = lp;
		canvas = c;
	}
	
	public void setBackground(File image){
		System.out.println(image.getPath());
		System.out.println(image.getName());
		Image im = new Image(image.getName());
		canvas.updateBackground(im);
	}
}