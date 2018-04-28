package authoring.right_components.EntityComponent;

import java.util.ArrayList;
import java.util.List;

import authoring.controllers.EntityController;
import authoring.right_components.BasePane;
import frontend_utilities.ButtonFactory;
import frontend_utilities.ImageBuilder;
import game_engine.Entity;
import game_engine.components.sprite.FilenameComponent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EntityPane extends BasePane{

	private EntityWrapper current;
	private VBox box;
	private VBox menuBox;
	private ImageView sprite;
	private List<HBox> createButtonArray;
	private List<HBox> editButtonArray;

	public void setController(EntityController controller) {
		this.controller = controller;
	}

	private EntityController controller;

	public EntityPane(){
		current = new EntityWrapper(new Entity(), this);
	}

	public Pane getView(){
		controller.listenCanvas();
		createButtonArray = instantiateCreateButtonArray();
		editButtonArray = instantiateEditButtonArray();
		box = buildBasicView("Entity Creator");
		box.setPadding(new Insets(10,10,10,10));
		box.setAlignment(Pos.TOP_CENTER);
		box.getChildren().add(getSprite());
		menuBox = getMenuBox();
		box.getChildren().add(menuBox);
		box.getChildren().addAll(createButtonArray);
		return box;
	}

	private List<HBox> instantiateCreateButtonArray() {
		List<HBox> list = new ArrayList<>();
		list.add(ButtonFactory.makeHBox("Create Entity", null,
				controller.getButton()));
		return list;
	}
	private List<HBox> instantiateEditButtonArray() {
		List<HBox> list = new ArrayList<>();
		//list.add(ButtonFactory.makeHBox("Create Entity", null, controller.getButton()));
		list.add(ButtonFactory.makeHBox("Delete Entity", null, controller.getRemoveButton()));
		list.add(ButtonFactory.makeHBox("Return to new entity creation", null,
				ButtonFactory.makeButton(e -> newWrapper())));

		return list;
	}
	public VBox getMenuBox(){
		VBox newBox = new VBox();
		newBox.setAlignment(Pos.CENTER);
		newBox.setSpacing(10);
		newBox.getChildren().addAll(current.getView());
		HBox addBox = ButtonFactory.makeHBox("Add Component", null,
				ButtonFactory.makeButton(e -> newComponent()));
		addBox.setMaxHeight(20);
		addBox.setMaxWidth(20);
		Button addComponentButton = new Button("Add a new component");
		addComponentButton.setOnAction(e -> newComponent());
		newBox.getChildren().add(addComponentButton);

		return newBox;
	}

	private void newComponent() {
		ComponentSelectionWindow window = new ComponentSelectionWindow(this.getPureCurrent(), this);
		window.display();
	}

	public void updateSprite() {
		sprite.setImage(new Image(current.getEntity().getComponent(FilenameComponent.class).getValue(), 130, 130, true, true));
		ImageBuilder.resize(sprite, 130);
	}

	public Pane getSprite(){
		StackPane pane = new StackPane();
		sprite = ImageBuilder.getImageView(current.getEntity().getComponent(FilenameComponent.class).getValue(),
				130, 130);
		pane.getChildren().add(sprite);
		return pane;
	}

	public EntityWrapper getCurrent() {
		current.getMenuList().stream().forEach(a -> a.getElements().stream()
			.forEach(b -> b.setComponentValue()));
		current.updateImage();
		return current;
	}

	public void clearImageViews() {
		controller.resetImageViews();
	}
	public void setActiveWrapper(EntityWrapper wrapper){
		//controller.listenCanvas();
		box.getChildren().remove(menuBox);
		box.getChildren().removeAll(createButtonArray);
		box.getChildren().removeAll(editButtonArray);
		current = wrapper;
		menuBox = getMenuBox();
		box.getChildren().add(menuBox);
		box.getChildren().addAll(editButtonArray);
		updateSprite();
	}
	public void refresh(){
		controller.updateCanvas();
		current.updateImage();
		box.getChildren().remove(menuBox);
		ArrayList<HBox> list = new ArrayList<>();
		if (box.getChildren().removeAll(createButtonArray)){
			list.addAll(createButtonArray);
		}
		else {
			box.getChildren().removeAll(editButtonArray);
			list.addAll(editButtonArray);
		}
//		box.getChildren().removeAll(createButtonArray);
//		box.getChildren().removeAll(editButtonArray);
		menuBox = getMenuBox();
		box.getChildren().add(menuBox);
		box.getChildren().addAll(list);
		//controller.resetImageViews();
		updateSprite();
	}
	public void newWrapper(){
		controller.listenCanvas();
		box.getChildren().remove(menuBox);
		box.getChildren().removeAll(createButtonArray);
		box.getChildren().removeAll(editButtonArray);
		current = new EntityWrapper(new Entity(), this);
		menuBox = getMenuBox();
		box.getChildren().add(menuBox);
		box.getChildren().addAll(createButtonArray);
		controller.resetImageViews();
		updateSprite();
	}
	public EntityWrapper getPureCurrent(){
		return current;
	}

	public void newDuplicateEntity() {
		current = new EntityWrapper(current, this);
		refresh();
	}
}
