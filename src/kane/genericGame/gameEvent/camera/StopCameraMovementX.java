package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.math.Scalar;
import kane.renderer.Camera;

public class StopCameraMovementX extends GameEvent{
	
	private Camera camera;
	
	public StopCameraMovementX(Game g, Camera camera) {
		super(g, 2);
		this.camera = camera;
	}
	
	@Override
	public void start() {
		procedure();
		
	}

	@Override
	public void procedure() {
		camera.getVel().setX(camera.getVel().getX() * 0.9f);
		reduceFrameCounter();
		if (Scalar.equals(camera.getVel().getX(), 0f)) {
			killEvent();
		}
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
