package kane.genericGame.gameEvent.camera;

import static kane.renderer.Camera.CAMERA;

import kane.genericGame.GameEvent;

public class SlowCameraX extends GameEvent{

	public SlowCameraX() {
		super(1);
	}
	
	@Override
	public void start() {
		CAMERA.vel.x = CAMERA.vel.x  * 0.1f;
		
	}

	@Override
	public void procedure() {
		
	}

	@Override
	public void end() {
		
	}

}
