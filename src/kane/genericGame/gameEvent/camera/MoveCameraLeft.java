package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.renderer.Camera;

public class MoveCameraLeft extends GameEvent{
	
	private Camera camera;
	private Mob player;

	public MoveCameraLeft(Game g, Camera camera, Mob player) {
		super(g, 2);
		this.camera = camera;
		this.player = player;
	}
	
	@Override
	public void start() {
		procedure();
		
	}

	@Override
	public void procedure() {
		camera.getAcc().sub(camera.getMovementAccX());
		if (-camera.getVel().getX() > player.getWalkSpeed()) {
			camera.getVel().setX(-player.getWalkSpeed());
		}
		reduceFrameCounter();
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
