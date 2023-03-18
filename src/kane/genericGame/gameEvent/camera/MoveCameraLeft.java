package kane.genericGame.gameEvent.camera;

import static kane.renderer.Camera.CAMERA;
import static kane.Kane.GAME;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;

public class MoveCameraLeft extends GameEvent{
	
	private Mob player;

	public MoveCameraLeft() {
		super(1);
		player = GAME.player;
	}
	
	@Override
	public void start() {
		CAMERA.acc.sub(CAMERA.movementAccX);
		if (-CAMERA.vel.x > player.getWalkSpeed()) {
			CAMERA.vel.x = -player.getWalkSpeed();
		}
		
	}

	@Override
	public void procedure() {
		
		
	}

	@Override
	public void end() {
		
	}

}