package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.math.Scalar;
import kane.renderer.Camera;

public class StopCameraMovementY extends GameEvent{

	private Camera camera;
	
	public StopCameraMovementY(Game g, Camera camera) {
		super(g, 2);
		this.camera = camera;
	}
	
	@Override
	public void start() {
		procedure();
		
	}

	@Override
	public void procedure() {
		camera.getVel().setY(camera.getVel().getY() * 0.9f);
		reduceFrameCounter();
		if (Scalar.equals(camera.getVel().getY(), 0f)) {
			killEvent();
		}
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
