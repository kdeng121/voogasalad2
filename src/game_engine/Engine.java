package game_engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import game_engine.level.Level;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Engine {
	private Map<Integer, Level> myLevels;
	private int myCurrentLevel;
	private int myIdCounter;
	private List<GameSystem> mySystems;
	private LinkedList<KeyEvent> myInputs;

	public Engine() {
		myLevels = new HashMap<Integer, Level>();
		myCurrentLevel = 0;
		myIdCounter = 0;
		myInputs = new LinkedList<KeyEvent>();

		mySystems = initSystems();
	}

	public void update(double elapsedTime) {
		System.out.println("Getting called!");
		Level currentLevel = getLevel();
		for (GameSystem system : mySystems) {
			system.act(elapsedTime, currentLevel);
		}

		currentLevel.checkEvents();

		myInputs.clear();
	}

	public Level createLevel() {
		Level createdLevel = new Level(myIdCounter);
		myLevels.put(myIdCounter, createdLevel);
		myIdCounter++;
		return createdLevel;
	}

	public void removeLevel(int id) {
		myLevels.remove(id);
	}

	public Level getLevel() {
		return myLevels.get(myCurrentLevel);
	}

	public void setLevel(int dex) {
		myCurrentLevel = dex;
	}

	public List<KeyEvent> getInput(Component<KeyCode> keyInput) {
		return myInputs.stream().filter(keyEvent -> keyInput.getValue().equals(keyEvent.getCode()))
				.collect(Collectors.toList());
	}

	public void receiveInput(KeyEvent event) {
		System.out.println("added input!");
		myInputs.add(event);
	}

	// WROTE FOR TEMPORARY TESTING -- REMOVE LATER!!
	public void clearInputs() {
		myInputs.clear();
	}

	private List<GameSystem> initSystems() {
		Reflections reflections = new Reflections("game_engine", new SubTypesScanner(true));
		Set<Class<? extends GameSystem>> allClasses = reflections.getSubTypesOf(GameSystem.class);

		List<GameSystem> systems = new ArrayList<>();

		allClasses.stream().filter(clazz -> !Modifier.isAbstract(clazz.getModifiers())).forEach(clazz -> {
			try {
				Parameter[] params = clazz.getDeclaredConstructors()[0].getParameters();
				Constructor<?> ctor;
				GameSystem system;
				if (params.length > 0) {
					ctor = clazz.getDeclaredConstructor(new Class[] { Engine.class });
					system = (GameSystem) ctor.newInstance(new Engine());
				} else {
					ctor = clazz.getDeclaredConstructor(new Class[] {});
					system = (GameSystem) ctor.newInstance();
				}
				systems.add(system);
			} catch (Exception e) {
				// do nothing: just continue without this system
			}
		});

		return systems;
	}

}
