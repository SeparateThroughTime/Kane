package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.renderer.Camera;

public class MoveCameraUp extends GameEvent{

	private Camera camera;
	
	public MoveCameraUp(Game g, Camera camera) {
		super(g, 1);
		this.camera = camera;
	}
	
	@Override
	public void start() {
		camera.getAcc().add(camera.getMovementAccY());
		if (camera.getVel().getY() > camera.getMovementSpeedY()) {
			camera.getVel().setY(camera.getMovementSpeedY());
		}
		
	}

	@Override
	public void procedure() {
		
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}