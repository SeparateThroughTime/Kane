/*TODO

	ContactPoint: BoxPolygon, PolygonPolygon -> Ghost Contacts
	Rotation
	SpriteAnimator: Shape has SpriteAnimator has Sprite -> So Sprites dont need to load for every entity
	Different Sprites for same shape -> wearing item
	Items/Inventory
	Visual Effects
	Sounds
	Object Editor
		Ermitteln des besten Mittelpunkts
	Level Ends/ Player dies -> Next level/ Restart
	Level Editor
	Event Editor?
	Campaign Editor
	StartMenu
	Save
	

*/
package kane;

import java.awt.Color;
import java.io.File;

import kane.genericGame.Game;
import kane.genericGame.Inventory;
import kane.math.Scalar;
import kane.math.Vec2f;
import kane.math.Vec2i;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.contacts.ActiveAttributes;
import kane.physics.contacts.PassiveAttributes;
import kane.physics.shapes.Box;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Point;
import kane.physics.shapes.Polygon;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

/**
 * This is the game "Kane".
 */
public class Kane extends Game {

	public Kane(String title) {
		super(title);
	}

	public static void main(String[] args) {
		Kane game = new Kane("Kane");
		game.run();

	}

	public static float BACKGROUND_SPEED = 0.5f;

	Material mStatic = new Material(0, 1f);
	Material mDynamic = new Material(1, 0.9f);
	Material mEvent = new Material(0, 0);
	Material mInterface = new Material(1, 0);
	Body player;
	Body sword;
	Vec2f playerRunAcc;
	int playerRunSpeed;
	Vec2f playerJumpAcc;
	Vec2f cameraMovementAccX;
	Vec2f cameraMovementAccY;
	int cameraMovementSpeedY;
	int mapLen;
	int mapHeight;

	boolean playerCanJump;

	@Override
	protected void initGame() {

		mapLen = 400 * 3;
		mapHeight = resSpecs.GAME_HEIGHT;

		// Set Vars
		playerRunAcc = new Vec2f(40 / DELTATIME, 0);
		playerJumpAcc = new Vec2f(0, 200 / DELTATIME);
		playerRunSpeed = 300;
		cameraMovementAccX = new Vec2f(playerRunAcc).mult(0.5f);
		cameraMovementAccY = new Vec2f(cameraMovementAccX).perpLeft();
		cameraMovementSpeedY = playerRunSpeed * 2;

		// Create World
		Body body = new Body(0, 0);
		body.addShape(
				new LineSegment(new Vec2f(30, 0), new Vec2f(30, resSpecs.GAME_HEIGHT), body, Color.BLUE, mStatic, 2));
		body.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		physics.addBody(body);

		body = new Body(0, 0);
		body.addShape(new LineSegment(new Vec2f(0, 30), new Vec2f(mapLen, 30), body, Color.BLUE, mStatic, 2));
		body.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		physics.addBody(body);

		body = new Body(0, 0);
		body.addShape(new LineSegment(new Vec2f(mapLen - 30, 0), new Vec2f(mapLen - 30, resSpecs.GAME_HEIGHT), body,
				Color.BLUE, mStatic, 2));
		body.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		physics.addBody(body);

		// Create player
		player = new Body(100, 130);
		player.addShape(new Box(0, 0, player, new Vec2f(32, 32), Color.GREEN, mDynamic, 2));
		player.getShape(0).addPassiveAttribute(PassiveAttributes.PLAYER_ALL);
		body.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		player.addShape(new Box(0, -22, player, new Vec2f(30, 10), Color.WHITE, mEvent, 2));
		player.getShape(1).setCollision(false);
		player.getShape(1).addActiveAttribute(ActiveAttributes.PLAYER_FEETS);
		player.getShape(1).setVisible(false);
		File file = new File("sprites\\player.png");
		Sprite sprite = new Sprite(file, 1, 1);
		sprite.addState(SpriteState.Standing, new int[] { 0 });
		sprite.addState(SpriteState.Running, new int[] { 1, 2, 3, 4 });
		SpriteController spriteController = new SpriteController(sprite);
		player.getShape(0).setSpriteController(spriteController);
		spriteController.setCurrentSpriteState(SpriteState.Standing, true);
		spriteController.setSpritePosOffset(new Vec2f(-32, -32));
		physics.addBody(player);

		// Sword
		sword = new Body(200, 130);
		Vec2f points[] = new Vec2f[4];
		points[0] = new Vec2f(-3, -10);
		points[1] = new Vec2f(3, -10);
		points[2] = new Vec2f(3, 10);
		points[3] = new Vec2f(-3, 10);
		sword.addShape(new Polygon(0, 0, sword, Color.YELLOW, points, mDynamic, 2));
		sword.getShape(0).addActiveAttribute(ActiveAttributes.SWORD);
		physics.addBody(sword);

		// Create Background
		file = new File("sprites\\background.png");
		renderer.changeBackground(file);

//		changeResolution(Resolution.SOL1176x664);

	}

	@Override
	protected void mechanicsLoop() {
		Vec2f cameraPos = renderer.getCamera().getPos();
		if (cameraPos.getX() - resSpecs.gameWidth * 0.5f < 0) {
			cameraPos.setX(resSpecs.gameWidth * 0.5f);
			renderer.getCamera().getAcc().setX(0);
			renderer.getCamera().getVel().setX(0);
		} else if (cameraPos.getX() + resSpecs.gameWidth * 0.5f > mapLen) {
			cameraPos.setX(mapLen - resSpecs.gameWidth * 0.5f);
			renderer.getCamera().getAcc().setX(0);
			renderer.getCamera().getVel().setX(0);
		}
		if (cameraPos.getY() - resSpecs.GAME_HEIGHT * 0.5f < 0) {
			cameraPos.setY(resSpecs.GAME_HEIGHT * 0.5f);
			renderer.getCamera().getAcc().setY(0);
			renderer.getCamera().getVel().setY(0);
		} else if (cameraPos.getY() + resSpecs.GAME_HEIGHT * 0.5f > mapHeight) {
			cameraPos.setY(mapHeight - resSpecs.GAME_HEIGHT * 0.5f);
			renderer.getCamera().getAcc().setY(0);
			renderer.getCamera().getVel().setY(0);
		}

		if (renderer.getGameBackground() != null) {
			int backgroundPos = (int) ((cameraPos.dot(new Vec2f(1, 0)) - resSpecs.gameWidth * 0.5f) * BACKGROUND_SPEED);
			renderer.getGameBackground().setOffsetX(backgroundPos);
		}
	}

	@Override
	protected void postMechanicsLoops() {
		Vec2f cameraPos = new Vec2f(Scalar.round(renderer.getCamera().getPos().getX()),
				Scalar.round(renderer.getCamera().getPos().getY()));
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			for (int j = 0; j < body.getNumShapes(); j++) {
				Shape shape = body.getShape(j);
				if (shape.hasPassiveAtrribute(PassiveAttributes.INVENTORY)) {
					body.getPos().set(new Vec2f(cameraPos));
				}
			}
		}
	}

	@Override
	public void leftMousePressed() {

	}

	@Override
	public void leftMouseReleased() {
	}

	@Override
	public void leftMouseClick() {

	}

	@Override
	public void rightMousePressed() {
	}

	@Override
	public void rightMouseReleased() {

	}

	@Override
	public void rightMouseClick() {
	}

	@Override
	public void leftArrowClick() {
		player.getShape(0).getSpriteController().setCurrentSpriteState(SpriteState.Running, true);
	}

	@Override
	public void leftArrowPressed() {
		if (!pause) {
			player.getAcc().setX(-playerRunAcc.getX());
			if (-player.getVel().getX() > playerRunSpeed) {
				player.getVel().setX(-playerRunSpeed);
			}
		}
	}

	@Override
	public void leftArrowReleased() {
		player.getShape(0).getSpriteController().setCurrentSpriteState(SpriteState.Standing, true);
	}

	@Override
	public void rightArrowClick() {
		player.getShape(0).getSpriteController().setCurrentSpriteState(SpriteState.Running, true);
	}

	@Override
	public void rightArrowPressed() {
		if (!pause) {
			player.getAcc().setX(playerRunAcc.getX());
			if (player.getVel().getX() > playerRunSpeed) {
				player.getVel().setX(playerRunSpeed);
			}
		}
	}

	@Override
	public void rightArrowReleased() {
		player.getShape(0).getSpriteController().setCurrentSpriteState(SpriteState.Standing, true);
	}

	@Override
	public void upArrowClick() {

	}

	@Override
	public void upArrowPressed() {

	}

	@Override
	public void upArrowReleased() {

	}

	@Override
	public void downArrowPressed() {

	}

	@Override
	public void downArrowClick() {

	}

	@Override
	public void downArrowReleased() {

	}

	@Override
	public void f1Click() {

	}

	@Override
	// show Contacts
	public void f2Click() {
		renderer.showContacts = !renderer.showContacts;

	}

	@Override
	// show AABBs
	public void f3Click() {
		renderer.showAABBs = !renderer.showAABBs;

	}

	@Override
	public void f4Click() {

	}

	@Override
	public void f5Click() {

	}

	@Override
	public void f6Click() {
	}

	@Override
	public void f7Click() {
	}

	@Override
	public void f8Click() {

	}

	@Override
	public void f9Click() {

	}

	@Override
	public void f10Click() {

	}

	@Override
	public void f11Click() {

	}

	@Override
	public void f12Click() {

	}

	@Override
	public void spacePressed() {

	}

	@Override
	public void spaceReleased() {

	}

	@Override
	public void spaceClick() {
		if (!pause) {
			if (playerCanJump) {
				player.getAcc().add(playerJumpAcc);
			}
		}
	}

	@Override
	public void shiftPressed() {

	}

	@Override
	public void shiftReleased() {

	}

	@Override
	public void shiftClick() {

	}

	@Override
	public void escClick() {
		if (showInterface) {
			iClick();
		} else {
			pause = !pause;
		}
	}

	@Override
	public void cPressed() {

	}

	@Override
	public void cReleased() {

	}

	@Override
	public void cClick() {

	}

	@Override
	public void iPressed() {

	}

	@Override
	public void iReleased() {

	}

	boolean showInterface = false;

	@Override
	public void iClick() {
		showInterface = !showInterface;
		pause = showInterface;
		for (int i = 0; i < inventory.getNumShapes(); i++) {
			Shape shape = inventory.getShape(i);
			if (shape.hasPassiveAtrribute(PassiveAttributes.INVENTORY)) {
				shape.setVisible(showInterface);
			}
		}

	}

	@Override
	public void penetration(ShapePair pair) {
		for (int i = 0; i < 2; i++) {
			Shape activeShape;
			Shape passiveShape;
			if (i == 0) {
				activeShape = pair.getShapeA();
				passiveShape = pair.getShapeB();
			} else {
				activeShape = pair.getShapeB();
				passiveShape = pair.getShapeA();
			}
			for (int j = 0; j < activeShape.getNumActiveAttributes(); j++) {
				ActiveAttributes activeE = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.getNumPassiveAttributes(); k++) {
					PassiveAttributes passiveE = passiveShape.getPassiveAttribute(j);
					// Here starts the Eventmanagement
					if (activeE == ActiveAttributes.CAMERA_RIGHT && passiveE == PassiveAttributes.PLAYER_ALL) {
						renderer.getCamera().getAcc().add(cameraMovementAccX);
						if (renderer.getCamera().getVel().getX() > playerRunSpeed) {
							renderer.getCamera().getVel().setX(playerRunSpeed);
						}
					} else if (activeE == ActiveAttributes.CAMERA_LEFT && passiveE == PassiveAttributes.PLAYER_ALL) {
						renderer.getCamera().getAcc().sub(cameraMovementAccX);
						if (-renderer.getCamera().getVel().getX() > playerRunSpeed) {
							renderer.getCamera().getVel().setX(-playerRunSpeed);
						}
					} else if (activeE == ActiveAttributes.CAMERA_MID_X && passiveE == PassiveAttributes.PLAYER_ALL) {
						renderer.getCamera().getVel().setX(inventory.getVel().getX() * 0.9f);
					}
					if (activeE == ActiveAttributes.CAMERA_UP && passiveE == PassiveAttributes.PLAYER_ALL) {
						renderer.getCamera().getAcc().add(cameraMovementAccY);
						if (renderer.getCamera().getVel().getY() > cameraMovementSpeedY) {
							renderer.getCamera().getVel().setY(cameraMovementSpeedY);
						}
					} else if (activeE == ActiveAttributes.CAMERA_DOWN && passiveE == PassiveAttributes.PLAYER_ALL) {
						renderer.getCamera().getAcc().sub(cameraMovementAccY);
						if (-renderer.getCamera().getVel().getY() > cameraMovementSpeedY) {
							renderer.getCamera().getVel().setY(-cameraMovementSpeedY);
						}
					} else if (activeE == ActiveAttributes.CAMERA_MID_Y && passiveE == PassiveAttributes.PLAYER_ALL) {
						renderer.getCamera().getVel().setY(inventory.getVel().getY() * 0.5f);
					} else if (activeE == ActiveAttributes.SWORD && passiveE == PassiveAttributes.PLAYER_ALL) {
						activeShape.getBody().remove();

					}
				}

			}
		}
	}

	@Override
	public void penetrated(ShapePair pair) {
		for (int i = 0; i < 2; i++) {
			Shape activeShape;
			Shape passiveShape;
			if (i == 0) {
				activeShape = pair.getShapeA();
				passiveShape = pair.getShapeB();
			} else {
				activeShape = pair.getShapeB();
				passiveShape = pair.getShapeA();
			}
			for (int j = 0; j < activeShape.getNumActiveAttributes(); j++) {
				ActiveAttributes activeE = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.getNumPassiveAttributes(); k++) {
					PassiveAttributes passiveE = passiveShape.getPassiveAttribute(j);
					// Here starts the Eventmanagement
					if (activeE == ActiveAttributes.PLAYER_FEETS && passiveE == PassiveAttributes.PHYSICAL) {
						playerCanJump = true;
					}
				}
			}
		}
	}

	@Override
	public void separated(ShapePair pair) {
		for (int i = 0; i < 2; i++) {
			Shape activeShape;
			Shape passiveShape;
			if (i == 0) {
				activeShape = pair.getShapeA();
				passiveShape = pair.getShapeB();
			} else {
				activeShape = pair.getShapeB();
				passiveShape = pair.getShapeA();
			}
			for (int j = 0; j < activeShape.getNumActiveAttributes(); j++) {
				ActiveAttributes activeE = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.getNumPassiveAttributes(); k++) {
					PassiveAttributes passiveE = passiveShape.getPassiveAttribute(j);
					// Here starts the Eventmanagement
					if (activeE == ActiveAttributes.PLAYER_FEETS && passiveE == PassiveAttributes.PHYSICAL) {
						playerCanJump = false;
					}
				}
			}
		}
	}
}
