package kane.genericGame.gameEvent.camera;

import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.math.Vec2f;

public class MoveBackground extends GameEvent{

	public MoveBackground() {
		super(2);
	}
	
	@Override
	public void start() {
		procedure();
		
	}

	@Override
	public void procedure() {
		if (RENDERER.background != null) {
			int backgroundPos = (int) ((CAMERA.pos.dot(new Vec2f(1, 0)) - RES_SPECS.gameWidth * 0.5f) * Game.BACKGROUND_SPEED);
			RENDERER.background.setOffsetX(backgroundPos);
		}
		
		reduceFrameCounter();
	}

	@Override
	public void end() {
		
	}

}