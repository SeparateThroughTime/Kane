package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.renderer.Camera;

public class MoveCameraRight extends GameEvent {
	
	private Camera camera;
	private Mob player;

	public MoveCameraRight(Game g, Camera camera, Mob player) {
		super(g, 2);
		this.camera = camera;
		this.player = player;
	}

	@Override
	public void start() {
		camera.getAcc().add(camera.getMovementAccX());
		if (camera.getVel().getX() > player.getWalkSpeed()) {
			camera.getVel().setX(player.getWalkSpeed());
		}
	}

	@Override
	public void procedure() {
		
	}

	@Override
	public void end() {
	}

}