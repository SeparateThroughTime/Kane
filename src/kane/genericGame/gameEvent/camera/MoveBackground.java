package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.math.Vec2f;
import kane.renderer.Renderer;
import kane.renderer.ResolutionSpecification;

public class MoveBackground extends GameEvent{
	private Renderer renderer;
	private Vec2f cameraPos;
	private ResolutionSpecification resSpecs;

	public MoveBackground(Game g, Renderer renderer) {
		super(g, 2);
		this.renderer = renderer;
	}
	
	@Override
	public void start() {
		cameraPos = renderer.getCamera().getPos();
		resSpecs = g.getResSpecs();
		procedure();
		
	}

	@Override
	public void procedure() {
		if (renderer.getGameBackground() != null) {
			int backgroundPos = (int) ((cameraPos.dot(new Vec2f(1, 0)) - resSpecs.gameWidth * 0.5f) * Game.BACKGROUND_SPEED);
			renderer.getGameBackground().setOffsetX(backgroundPos);
		}
		
		reduceFrameCounter();
	}

	@Override
	public void end() {
		
	}

}