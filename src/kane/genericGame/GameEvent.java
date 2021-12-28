package kane.genericGame;

public abstract class GameEvent {
	public abstract void start();

	public abstract void procedure();

	public abstract void end();

	private int frameCounter;
	public final int EVENT_DURATION;

	protected Game g;

	public GameEvent(Game g, int eventDuration) {
		this.g = g;
		this.EVENT_DURATION = eventDuration;
	}

	public int getFrameCounter() {
		return frameCounter;
	}

	public void countFrame() {
		frameCounter++;
	}
	
	public void reduceFrameCounter() {
		frameCounter--;
	}
}
