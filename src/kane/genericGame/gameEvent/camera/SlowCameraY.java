package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.renderer.Camera;

public class SlowCameraY extends GameEvent{
	private Camera camera;

	public SlowCameraY(Game g, Camera camera) {
		super(g, 1);
		this.camera = camera;
	}
	
	@Override
	public void start() {
		camera.getVel().setY(camera.getVel().getY() * 0.1f);
		
	}

	@Override
	public void procedure() {
		
	}

	@Override
	public void end() {
		
	}

}
