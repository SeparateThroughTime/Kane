package kane.renderer;

import java.awt.Color;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.Game;
import kane.genericGame.gameEvent.camera.BindCameraToMap;
import kane.genericGame.gameEvent.camera.MoveCameraDown;
import kane.genericGame.gameEvent.camera.MoveCameraLeft;
import kane.genericGame.gameEvent.camera.MoveCameraRight;
import kane.genericGame.gameEvent.camera.MoveCameraUp;
import kane.genericGame.gameEvent.camera.SlowCameraX;
import kane.genericGame.gameEvent.camera.SlowCameraY;
import kane.math.Vec2f;
import kane.physics.AABB;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;

/**
 * The Camera is a body which is used for the area that is displayed.
 */
public class Camera extends Body {

	private Material mInterface = new Material(1, 0);
	private AABB window;
	private AABB aiRange;
	private Vec2f windowRad;
	public Vec2f zeroPoint;
	private ResolutionSpecification resSpecs;
	private Game g;
	private Vec2f movementAccX;
	private Vec2f movementAccY;
	private int movementSpeedY;

	public Camera(ResolutionSpecification resSpecs, Game g) {
		super(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);
		this.g = g;

		movementAccX = new Vec2f(g.getPlayer().getWalkAcc()).mult(0.5f);
		movementAccY = new Vec2f(movementAccX).perpLeft();
		movementSpeedY = g.getPlayer().getWalkSpeed() * 2;

		this.resSpecs = resSpecs;
		this.zeroPoint = new Vec2f();
		setReactToGravity(false);
		createCamera();
	}

	/**
	 * create the actual camera. Only need to run in constructor.
	 */
	private void createCamera() {
		this.windowRad = new Vec2f(resSpecs.gameWidth * 0.5f, resSpecs.GAME_HEIGHT * 0.5f);

		// Create camera
		addShape(new Point(0, 0, this, Color.BLUE, mInterface, 0));
		getShape(0).setCollision(false);
		getShape(0).setVisible(false);
		// Left Box
		addShape(new Box(-(int) (resSpecs.gameWidth * 0.3125f), 0, this,
				new Vec2f(resSpecs.gameWidth * 0.1875f, resSpecs.GAME_HEIGHT * 0.5f), Color.GREEN, mInterface, 0));
		getShape(1).setCollision(false);
		getShape(1).addActiveAttribute(ActiveAttributes.CAMERA_LEFT);
		getShape(1).setVisible(false);
		// Right Box
		addShape(new Box((int) (resSpecs.gameWidth * 0.3125f), 0, this,
				new Vec2f(resSpecs.gameWidth * 0.1875f, resSpecs.GAME_HEIGHT * 0.5f), Color.GREEN, mInterface, 0));
		getShape(2).setCollision(false);
		getShape(2).addActiveAttribute(ActiveAttributes.CAMERA_RIGHT);
		getShape(2).setVisible(false);
		// Lower Box
		addShape(new Box(0, -(int) (resSpecs.GAME_HEIGHT * 0.3125f), this,
				new Vec2f(resSpecs.gameWidth * 0.5f, resSpecs.GAME_HEIGHT * 0.1875f), Color.GREEN, mInterface, 0));
		getShape(3).setCollision(false);
		getShape(3).addActiveAttribute(ActiveAttributes.CAMERA_DOWN);
		getShape(3).setVisible(false);
		// Upper Box
		addShape(new Box(0, (int) (resSpecs.GAME_HEIGHT * 0.3125f), this,
				new Vec2f(resSpecs.gameWidth * 0.5f, resSpecs.GAME_HEIGHT * 0.1875f), Color.GREEN, mInterface, 0));
		getShape(4).setCollision(false);
		getShape(4).addActiveAttribute(ActiveAttributes.CAMERA_UP);
		getShape(4).setVisible(false);
		// Mid X Box
		addShape(new Box(0, 0, this, new Vec2f(resSpecs.gameWidth * 0.125f, resSpecs.GAME_HEIGHT * 0.5f), Color.RED,
				mInterface, 0));
		getShape(5).setCollision(false);
		getShape(5).addActiveAttribute(ActiveAttributes.CAMERA_MID_X);
		getShape(5).setVisible(false);
		// Mid Y Box
		addShape(new Box(0, 0, this, new Vec2f(resSpecs.gameWidth * 0.5f, resSpecs.GAME_HEIGHT * 0.125f), Color.RED,
				mInterface, 0));
		getShape(6).setCollision(false);
		getShape(6).addActiveAttribute(ActiveAttributes.CAMERA_MID_Y);
		getShape(6).setVisible(false);
	}

	/**
	 * update the window of the camera. Needs to run every frame.
	 */
	public void update() {
		Vec2f min = new Vec2f(getPos()).sub(windowRad);
		Vec2f max = new Vec2f(getPos()).add(windowRad);
		Vec2f aiMin = new Vec2f(min).add(100, 100);
		Vec2f aiMax = new Vec2f(max).add(100, 100);
		window = new AABB(min, max);
		aiRange = new AABB(aiMin, aiMax);
		zeroPoint = min;
	}

	public AABB getAiRange() {
		return aiRange;
	}

	/**
	 * Needs to run, after resolution of game has changed.
	 * 
	 * @param gameWidth
	 */
	public void changeResolution() {
		clearBody();
		createCamera();
	}

	/**
	 * Get window.
	 * 
	 * @return
	 */
	public AABB getWindow() {
		return window;
	}

	public void bindCameraToMap() {
		g.addEvent(new BindCameraToMap(g, this));
	}

	public Vec2f getMovementAccX() {
		return movementAccX;
	}

	public void moveCameraLeft() {
		g.addEvent(new MoveCameraLeft(g, this, g.getPlayer()));
	}

	public void moveCameraRight() {
		g.addEvent(new MoveCameraRight(g, this, g.getPlayer()));
	}

	public void moveCameraUp() {
		g.addEvent(new MoveCameraUp(g, this));
	}

	public void moveCameraDown() {
		g.addEvent(new MoveCameraDown(g, this));
	}

	public void SlowCameraX() {
		g.addEvent(new SlowCameraX(g, this));
	}

	public void SlowCameraY() {
		g.addEvent(new SlowCameraY(g, this));
	}

	public Vec2f getMovementAccY() {
		return movementAccY;
	}

	public void setMovementAccY(Vec2f movementAccY) {
		this.movementAccY = movementAccY;
	}

	public int getMovementSpeedY() {
		return movementSpeedY;
	}

	public void setMovementSpeedY(int movementSpeedY) {
		this.movementSpeedY = movementSpeedY;
	}

}
