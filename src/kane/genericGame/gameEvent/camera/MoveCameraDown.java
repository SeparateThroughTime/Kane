package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.renderer.Camera;

public class MoveCameraDown extends GameEvent{

	private Camera camera;
	
	public MoveCameraDown(Game g, Camera camera) {
		super(g, 2);
		this.camera = camera;
	}
	
	@Override
	public void start() {
		procedure();
		
	}

	@Override
	public void procedure() {
		camera.getAcc().sub(camera.getMovementAccY());
		if (-camera.getVel().getY() > camera.getMovementSpeedY()) {
			camera.getVel().setY(-camera.getMovementSpeedY());
		}
		reduceFrameCounter();
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
