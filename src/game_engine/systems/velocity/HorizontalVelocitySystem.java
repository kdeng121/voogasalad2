package game_engine.systems.velocity;

import game_engine.Component;
import game_engine.components.physics.XAccelComponent;
import game_engine.components.physics.XVelComponent;

public class HorizontalVelocitySystem extends VelocitySystem {
	
	private static final Class<? extends Component<Double>> X_VEL = XVelComponent.class;
	private static final Class<? extends Component<Double>> X_ACCEL = XAccelComponent.class;
	
	public HorizontalVelocitySystem() {
		super(X_VEL, X_ACCEL);
	}

}
