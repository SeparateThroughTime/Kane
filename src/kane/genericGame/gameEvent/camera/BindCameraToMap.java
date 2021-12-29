package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.math.Vec2f;
import kane.renderer.Camera;
import kane.renderer.ResolutionSpecification;

public class BindCameraToMap extends GameEvent{

	private Camera camera;
	private Vec2f cameraPos;
	private ResolutionSpecification resSpecs;

	public BindCameraToMap(Game g, Camera camera) {
		super(g, 2);
		this.camera = camera;
	}
	
	@Override
	public void start() {
		cameraPos = camera.getPos();
		resSpecs = g.getResSpecs();
		procedure();
		
		
		
	}

	@Override
	public void procedure() {
		int mapLen = g.getMapLen();
		int mapHeight = g.getMapHeight();
		if (cameraPos.getX() - resSpecs.gameWidth * 0.5f < 0) {
			cameraPos.setX(resSpecs.gameWidth * 0.5f);
			camera.getAcc().setX(0);
			camera.getVel().setX(0);
		} else if (cameraPos.getX() + resSpecs.gameWidth * 0.5f > mapLen) {
			cameraPos.setX(mapLen - resSpecs.gameWidth * 0.5f);
			camera.getAcc().setX(0);
			camera.getVel().setX(0);
		}
		if (cameraPos.getY() - resSpecs.GAME_HEIGHT * 0.5f < 0) {
			cameraPos.setY(resSpecs.GAME_HEIGHT * 0.5f);
			camera.getAcc().setY(0);
			camera.getVel().setY(0);
		} else if (cameraPos.getY() + resSpecs.GAME_HEIGHT * 0.5f > mapHeight) {
			cameraPos.setY(mapHeight - resSpecs.GAME_HEIGHT * 0.5f);
			camera.getAcc().setY(0);
			camera.getVel().setY(0);
		}
		
		reduceFrameCounter();
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
