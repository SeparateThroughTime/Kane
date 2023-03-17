package kane.genericGame.gameEvent.camera;

import static kane.renderer.Camera.CAMERA;

import kane.genericGame.GameEvent;

public class SlowCameraY extends GameEvent{

	public SlowCameraY() {
		super(1);
	}
	
	@Override
	public void start() {
		CAMERA.vel.y = CAMERA.vel.y * 0.1f;
		
	}

	@Override
	public void procedure() {
		
	}

	@Override
	public void end() {
		
	}

}
