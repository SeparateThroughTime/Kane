package kane.renderer;

import static kane.Kane.GAME;
import static kane.genericGame.hud.Inventory.INVENTORY;
import static kane.physics.Physics.PHYSICS;
import static kane.renderer.ResolutionSpecification.RES_SPECS;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.gameEvent.camera.BindCameraToMap;
import kane.genericGame.gameEvent.camera.MoveCameraDown;
import kane.genericGame.gameEvent.camera.MoveCameraLeft;
import kane.genericGame.gameEvent.camera.MoveCameraRight;
import kane.genericGame.gameEvent.camera.MoveCameraUp;
import kane.genericGame.gameEvent.camera.SlowCameraX;
import kane.genericGame.gameEvent.camera.SlowCameraY;
import kane.genericGame.hud.HudBar;
import kane.genericGame.hud.Inventory;
import kane.math.Vec2f;
import kane.physics.AABB;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;

/**
 * The Camera is a body which is used for the area that is displayed.
 */
public class Camera extends Body {
	
	public static Camera CAMERA;

	private Material mInterface = new Material(1, 0);
	public AABB window;
	public AABB aiRange;
	public Vec2f windowRad;
	public Vec2f zeroPoint;
	public Vec2f movementAccX;
	public Vec2f movementAccY;
	public int movementSpeedY;
	private ArrayList<HudBar> hudBars = new ArrayList<HudBar>();

	private Camera() {
		super((int)RES_SPECS.halfGameWidth, (int)RES_SPECS.halfGameHeight);

		movementAccX = new Vec2f(GAME.player.getWalkAcc()).mult(0.5f);
		movementAccY = new Vec2f(movementAccX).perpLeft();
		movementSpeedY = GAME.player.getWalkSpeed() * 2;

		this.zeroPoint = new Vec2f();
		reactToGravity = false;
		createCamera();
		
		PHYSICS.addBody(this);
	}
	
	public static void initializateCamera() {
		if(CAMERA == null) {
			CAMERA = new Camera();
		}
	}

	/**
	 * create the actual camera. Only need to run in constructor.
	 */
	private void createCamera() {
		this.windowRad = new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.5f);

		// Create camera
		addShape(new Point(0, 0, this, Color.BLUE, mInterface, 0));
		shapes[0].collision = false;
		shapes[0].visible = false;
		// Left Box
		addShape(new Box(-(int) (RES_SPECS.gameWidth * 0.3125f), 0, this,
				new Vec2f(RES_SPECS.gameWidth * 0.1875f, RES_SPECS.GAME_HEIGHT * 0.5f), Color.GREEN, mInterface, 0));
		shapes[1].collision = false;
		shapes[1].addActiveAttribute(ActiveAttributes.CAMERA_LEFT);
		shapes[1].visible = false;
		// Right Box
		addShape(new Box((int) (RES_SPECS.gameWidth * 0.3125f), 0, this,
				new Vec2f(RES_SPECS.gameWidth * 0.1875f, RES_SPECS.GAME_HEIGHT * 0.5f), Color.GREEN, mInterface, 0));
		shapes[2].collision = false;
		shapes[2].addActiveAttribute(ActiveAttributes.CAMERA_RIGHT);
		shapes[2].visible = false;
		// Lower Box
		addShape(new Box(0, -(int) (RES_SPECS.GAME_HEIGHT * 0.3125f), this,
				new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.1875f), Color.GREEN, mInterface, 0));
		shapes[3].collision = false;
		shapes[3].addActiveAttribute(ActiveAttributes.CAMERA_DOWN);
		shapes[3].visible = false;
		// Upper Box
		addShape(new Box(0, (int) (RES_SPECS.GAME_HEIGHT * 0.3125f), this,
				new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.1875f), Color.GREEN, mInterface, 0));
		shapes[4].collision = false;
		shapes[4].addActiveAttribute(ActiveAttributes.CAMERA_UP);
		shapes[4].visible = false;
		// Mid X Box
		addShape(new Box(0, 0, this, new Vec2f(RES_SPECS.gameWidth * 0.125f, RES_SPECS.GAME_HEIGHT * 0.5f), Color.RED,
				mInterface, 0));
		shapes[5].collision = false;
		shapes[5].addActiveAttribute(ActiveAttributes.CAMERA_MID_X);
		shapes[5].visible = false;
		// Mid Y Box
		addShape(new Box(0, 0, this, new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.125f), Color.RED,
				mInterface, 0));
		shapes[6].collision = false;
		shapes[6].addActiveAttribute(ActiveAttributes.CAMERA_MID_Y);
		shapes[6].visible = false;
	}

	/**
	 * update the window of the camera. Needs to run every frame.
	 */
	public void update() {
		Vec2f min = new Vec2f(pos).sub(windowRad);
		Vec2f max = new Vec2f(pos).add(windowRad);
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
		INVENTORY.changeResolution();
	}

	public void bindCameraToMap() {
		GAME.addEvent(new BindCameraToMap());
	}

	public void moveCameraLeft() {
		GAME.addEvent(new MoveCameraLeft());
	}

	public void moveCameraRight() {
		GAME.addEvent(new MoveCameraRight());
	}

	public void moveCameraUp() {
		GAME.addEvent(new MoveCameraUp());
	}

	public void moveCameraDown() {
		GAME.addEvent(new MoveCameraDown());
	}

	public void SlowCameraX() {
		GAME.addEvent(new SlowCameraX());
	}

	public void SlowCameraY() {
		GAME.addEvent(new SlowCameraY());
	}

	public void initInventory() {
		Shape mainShape = addShape(new Point(0, 0, this, Color.BLUE, mInterface, 3));
		Shape[] slotShapes = new Shape[8];
		slotShapes[0] = addShape(new Box(-144, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		slotShapes[1] = addShape(new Box(-48, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		slotShapes[2] = addShape(new Box(48, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		slotShapes[3] = addShape(new Box(144, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		slotShapes[4] = addShape(new Box(-144, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		slotShapes[5] = addShape(new Box(-48, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		slotShapes[6] = addShape(new Box(48, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		slotShapes[7] = addShape(new Box(144, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		Inventory.initializateInventory(mainShape, slotShapes, RES_SPECS);
	}

	public HudBar addHudBar(String filepath) {
		int hudPos = hudBars.size();
		Shape hudShape = addShape(new Point(-RES_SPECS.gameWidth / 2 + HudBar.HUD_HEIGHT + HudBar.HUD_WIDTH / 2,
				-RES_SPECS.GAME_HEIGHT / 2 + RES_SPECS.GAME_HEIGHT - (int) (HudBar.HUD_HEIGHT * (hudPos + 1) * 1.5), this,
				Color.BLUE, mInterface, 3));
		HudBar hudBar = new HudBar(filepath, hudShape);

		hudBars.add(hudBar);
		return hudBar;

	}

}
