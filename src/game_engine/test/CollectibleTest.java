package game_engine.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game_engine.Engine;
import game_engine.Entity;
import game_engine.components.DamageComponent;
import game_engine.components.HealthComponent;
import game_engine.components.collect.CollectibleComponent;
import game_engine.components.collect.CollectorComponent;
import game_engine.components.collect.ScoreComponent;
import game_engine.components.collision.CollidableComponent;
import game_engine.components.collision.CollidedComponent;
import game_engine.components.collision.PassableComponent;
import game_engine.components.collision.edge_collided.BottomCollidedComponent;
import game_engine.components.collision.edge_collided.LeftCollidedComponent;
import game_engine.components.collision.edge_collided.RightCollidedComponent;
import game_engine.components.collision.edge_collided.TopCollidedComponent;
import game_engine.components.collision.hitbox.HitboxHeightComponent;
import game_engine.components.collision.hitbox.HitboxWidthComponent;
import game_engine.components.collision.hitbox.HitboxXOffsetComponent;
import game_engine.components.collision.hitbox.HitboxYOffsetComponent;
import game_engine.components.keyboard.DownKeyboardComponent;
import game_engine.components.keyboard.LeftKeyboardComponent;
import game_engine.components.keyboard.RightKeyboardComponent;
import game_engine.components.keyboard.UpKeyboardComponent;
import game_engine.components.physics.DefaultXVelComponent;
import game_engine.components.physics.DefaultYVelComponent;
import game_engine.components.physics.XAccelComponent;
import game_engine.components.physics.XVelComponent;
import game_engine.components.physics.YAccelComponent;
import game_engine.components.physics.YVelComponent;
import game_engine.components.position.AngleComponent;
import game_engine.components.position.XPosComponent;
import game_engine.components.position.YPosComponent;
import game_engine.components.projectile.ProjectileCollidableComponent;
import game_engine.components.projectile.ProjectileDamageComponent;
import game_engine.components.projectile.ProjectileFilenameComponent;
import game_engine.components.projectile.ProjectileHeightComponent;
import game_engine.components.projectile.ProjectileHitboxHeightComponent;
import game_engine.components.projectile.ProjectileHitboxWidthComponent;
import game_engine.components.projectile.ProjectileHitboxXOffsetComponent;
import game_engine.components.projectile.ProjectileHitboxYOffsetComponent;
import game_engine.components.projectile.ProjectileKeyboardInputComponent;
import game_engine.components.projectile.ProjectileWidthComponent;
import game_engine.components.projectile.ProjectileXVelComponent;
import game_engine.components.projectile.ProjectileYVelComponent;
import game_engine.components.sprite.SpritePolarityComponent;
import game_engine.event.Event;
import game_engine.event.actions.macro.LevelChangeAction;
import game_engine.event.actions.micro.DataChangeAction;
import game_engine.event.conditions.DataCondition;
import game_engine.event.conditions.EntityCollisionCondition;
import game_engine.level.Level;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CollectibleTest extends Application {

    private Entity mainCharacter; //smol rect
    private Entity coin; //BIG rect
    
    Map<Entity, Rectangle> spritesMap = new HashMap<Entity, Rectangle>();

    private Engine engine;

    private Rectangle mainCharacterRect;
    private Rectangle coinRect;

    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;    

    private static final int WIDTH = 1600;
    private static final int HEIGHT= 800;
    private static final Paint BACKGROUND = Color.rgb(36, 36, 36);

    private Group root;
    private Scene myScene;
    
    @Override
    public void start(Stage stage) throws Exception {
        setup();
        stage.setScene(myScene);
        stage.show();

        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    /**
     * @param elapsedTime
     */
    private void step(double elapsedTime) {
    	engine.update(elapsedTime);
		updateAllEntities();
        //updateRectPos();
    }

    private void setup(){
        root = new Group();
        myScene = new Scene(root, WIDTH, HEIGHT, BACKGROUND);
        myScene.setOnKeyPressed(b -> engine.receiveInput(b));
		myScene.setOnKeyReleased(b -> engine.receiveInput(b));

    	buildEntities();
        initRects();
    }
    
    private void updateAllEntities() {
    	for (Entity entity : engine.getLevel().getEntities()) {
    		if (!spritesMap.containsKey(entity)) {
    			double x = entity.getComponent(XPosComponent.class).getValue();
    			double y = entity.getComponent(YPosComponent.class).getValue();
    			double width = entity.getComponent(HitboxWidthComponent.class).getValue();
    			double height = entity.getComponent(HitboxHeightComponent.class).getValue();

    			Rectangle bullet = new Rectangle(x - width/2, y - height/2, width, height);
    			spritesMap.put(entity, bullet);
    			root.getChildren().add(bullet);
    		}
    		else if (!spritesMap.isEmpty()){
	    		Rectangle r = spritesMap.get(entity);
	    		double xPos = entity.getComponent(XPosComponent.class).getValue();
	    		double yPos = entity.getComponent(YPosComponent.class).getValue();
	    		double width = entity.getComponent(HitboxWidthComponent.class).getValue();
    			double height = entity.getComponent(HitboxHeightComponent.class).getValue();
	    		r.setX(xPos - width/2);
	    		r.setY(yPos - height/2);
    		}
    	}
    }

    private void updateRectPos(){
        double x = mainCharacter.getComponent(XPosComponent.class).getValue();
        double y = mainCharacter.getComponent(YPosComponent.class).getValue();
        double width = mainCharacter.getComponent(HitboxWidthComponent.class).getValue();
		double height = mainCharacter.getComponent(HitboxHeightComponent.class).getValue();
        
        double theta = mainCharacter.getComponent(AngleComponent.class).getValue();

        mainCharacterRect.setX(x - width/2);
        mainCharacterRect.setY(y - height/2);
        mainCharacterRect.setRotate(theta);
    }

    private void updateRectColor(){
        if(mainCharacter.getComponent(TopCollidedComponent.class)!=null
        		|| mainCharacter.getComponent(BottomCollidedComponent.class)!=null
        		|| mainCharacter.getComponent(LeftCollidedComponent.class)!=null
        		|| mainCharacter.getComponent(RightCollidedComponent.class)!=null){
            mainCharacterRect.setFill(Color.BLUE);
        }
        else{
            if(mainCharacterRect.getFill()!=Color.GRAY){
                mainCharacterRect.setFill(Color.GRAY);
            }
        }
    }

    private void buildEntities(){
    	mainCharacter = new Entity();
        coin = new Entity();

    	LeftKeyboardComponent keyLeftComp = new LeftKeyboardComponent(KeyCode.LEFT.toString());
    	RightKeyboardComponent keyRightComp = new RightKeyboardComponent(KeyCode.RIGHT.toString());
    	UpKeyboardComponent keyUpComp = new UpKeyboardComponent(KeyCode.UP.toString());
    	DownKeyboardComponent keyDownComp = new DownKeyboardComponent(KeyCode.DOWN.toString());
    	mainCharacter.addComponent(keyLeftComp);
    	mainCharacter.addComponent(keyRightComp);
    	mainCharacter.addComponent(keyUpComp);
    	mainCharacter.addComponent(keyDownComp);
    	mainCharacter.addComponent(new SpritePolarityComponent("1"));
    	
    	mainCharacter.addComponent(new YAccelComponent("0"));
    	mainCharacter.addComponent(new DefaultYVelComponent("30"));
    	mainCharacter.addComponent(new YVelComponent("0"));
    	
    	mainCharacter.addComponent(new XVelComponent("0"));
    	mainCharacter.addComponent(new DefaultXVelComponent("400"));
    	mainCharacter.addComponent(new XAccelComponent("0"));
    	
    	mainCharacter.addComponent(new XPosComponent("500"));
    	mainCharacter.addComponent(new YPosComponent("350"));
    	mainCharacter.addComponent(new AngleComponent("0"));
    	
    	coin.addComponent(new XPosComponent("800"));
    	coin.addComponent(new YPosComponent("300"));
    	coin.addComponent(new AngleComponent("0"));
    	
    	
    	mainCharacter.addComponent(new HitboxHeightComponent("100.0"));
    	mainCharacter.addComponent(new HitboxWidthComponent("100.0"));
    	mainCharacter.addComponent(new HitboxXOffsetComponent("0.0"));
    	mainCharacter.addComponent(new HitboxYOffsetComponent("0.0"));
    	
    	coin.addComponent(new HitboxHeightComponent("500.0"));
    	coin.addComponent(new HitboxWidthComponent("100.0"));
    	coin.addComponent(new HitboxXOffsetComponent("0.0"));
    	coin.addComponent(new HitboxYOffsetComponent("0.0"));
    	
    	mainCharacter.addComponent(new CollidableComponent("true"));
    	//e1.addComponent(new PassableComponent("true"));

    	
    	coin.addComponent(new CollidableComponent("true"));
    	//e3.addComponent(new PassableComponent("true"));
    	
    	//Add Health Component
    	mainCharacter.addComponent(new HealthComponent("1000")); //100 health points for e1
    	coin.addComponent(new HealthComponent("2000"));
    	coin.addComponent(new DamageComponent("100")); //e3 does 100 damage
    	
    	//Add Project Components to Entity e1
    	mainCharacter.addComponent(new ProjectileWidthComponent("20.0"));
    	mainCharacter.addComponent(new ProjectileHeightComponent("20.0"));
    	mainCharacter.addComponent(new ProjectileHitboxWidthComponent("20.0"));
    	mainCharacter.addComponent(new ProjectileHitboxHeightComponent("20.0"));
    	mainCharacter.addComponent(new ProjectileHitboxXOffsetComponent("0.0"));
    	mainCharacter.addComponent(new ProjectileHitboxYOffsetComponent("0.0"));
    	mainCharacter.addComponent(new ProjectileCollidableComponent("true"));
    	mainCharacter.addComponent(new ProjectileDamageComponent("2"));
    	mainCharacter.addComponent(new ProjectileKeyboardInputComponent(KeyCode.SPACE.toString()));
    	mainCharacter.addComponent(new ProjectileXVelComponent("200"));
    	mainCharacter.addComponent(new ProjectileYVelComponent("0"));
    	mainCharacter.addComponent(new ProjectileFilenameComponent("Mario.GIF"));
    	
    	//Add Collectible/Collector components
    	mainCharacter.addComponent(new CollectorComponent());
    	coin.addComponent(new CollectibleComponent("50"));
    	
    	//Add Score component to entity1
    	mainCharacter.addComponent(new ScoreComponent("0")); // 0 is default score
    	
    	
    	engine = new Engine();
    	
    	Level lvl0 = engine.createLevel();
    	//asdf.addEntity(e2);
    	lvl0.addEntity(mainCharacter);
    	lvl0.addEntity(coin);
    	
    	Level lvl1 = engine.createLevel();
    	
    	List<Level> levels = new ArrayList<Level>();
    	levels.add(lvl0);
    	levels.add(lvl1);


    }

    private void initRects(){
        double x1 = mainCharacter.getComponent(XPosComponent.class).getValue();
        double y1 = mainCharacter.getComponent(YPosComponent.class).getValue();
        
        double x3 = coin.getComponent(XPosComponent.class).getValue();
        double y3 = coin.getComponent(YPosComponent.class).getValue();
        
        double hh1 = mainCharacter.getComponent(HitboxHeightComponent.class).getValue();
        double hw1 = mainCharacter.getComponent(HitboxWidthComponent.class).getValue();
        
        double hh3 = coin.getComponent(HitboxHeightComponent.class).getValue();
        double hw3 = coin.getComponent(HitboxWidthComponent.class).getValue();

        mainCharacterRect = new Rectangle(x1 - hw1/2, y1 - hh1/2, hw1, hh1);
        //r2 = new Rectangle(x2 - hw2/2, y2 - hh2/2, hw2, hh2);
        coinRect = new Rectangle(x3 - hw3/2, y3 - hh3/2, hw3, hh3);
        
        spritesMap.put(mainCharacter, mainCharacterRect);
        //spritesMap.put(e2, r2);
        spritesMap.put(coin, coinRect);

        
        
        root.getChildren().add(mainCharacterRect);
        //root.getChildren().add(r2);
        root.getChildren().add(coinRect);
    }
    

    /**
     * Starts the program
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}