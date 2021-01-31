package kane.renderer;

import java.awt.Color;

import kane.math.Vec2f;
import kane.physics.AABB;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.contacts.ActiveAttributes;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;

/**
 * The Camera is a body which is used for the area that is displayed.
 */
public class Camera extends Body {

	private Material mInterface = new Material(1, 0);
	private AABB window;
	private Vec2f windowRad;
	public Vec2f zeroPoint;
	private ResolutionSpecification resSpecs;

	public Camera(ResolutionSpecification resSpecs) {
		super(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);

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
		window = new AABB(min, max);
		zeroPoint = min;
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

}
