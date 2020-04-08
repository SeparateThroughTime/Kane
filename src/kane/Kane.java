/*TODO
	Rotation
	Events (Contact Listener)
		World Events
		Items
		Inventory
	Level Ends/ Player dies -> Next level/ Restart
	Resizable Window
	Sprites
	Visual Effects
	Sounds
	Object Editor
		Ermitteln des besten Mittelpunkts
	Level Editor
	Event Editor?
	Campaign Editor
	StartMenu
	

*/
package kane;

import kane.genericGame.Game;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.contacts.AvtiveAttributes;
import kane.physics.contacts.PassiveAttributes;
import kane.physics.shapes.Box;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Point;

public class Kane extends Game {

	public Kane(String title) {
		super(title);
	}

	public static void main(String[] args) {
		Kane game = new Kane("Kane");
		game.run();

	}

	Material mStatic = new Material(0, 1f);
	Material mDynamic = new Material(1, 0.7f);
	Material mEvent = new Material(0, 0);
	Material mCamera = new Material(1, 0);
	Body player;
	Body camera;
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

//		physics.setGravity(new Vec2f(0, 0));

		mapLen = WIDTH * 3;
		mapHeight = HEIGHT;

		// Set Vars
		playerRunAcc = new Vec2f(40 / DELTATIME, 0);
		playerJumpAcc = new Vec2f(0, 200 / DELTATIME);
		playerRunSpeed = 300;
		cameraMovementAccX = new Vec2f(playerRunAcc).mult(0.5f);
		cameraMovementAccY = new Vec2f(cameraMovementAccX).perpLeft();
		cameraMovementSpeedY = playerRunSpeed * 2;

		// Create World
		Body body = new Body(0, 0);
		body.addShape(new LineSegment(new Vec2f(30, 0), new Vec2f(30, HEIGHT), body, 0x0000ff, mStatic));
		body.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		physics.addBody(body);

		body = new Body(0, 0);
		body.addShape(new LineSegment(new Vec2f(0, 30), new Vec2f(mapLen, 30), body, 0x0000ff, mStatic));
		body.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		physics.addBody(body);

		body = new Body(0, 0);
		body.addShape(
				new LineSegment(new Vec2f(mapLen - 30, 0), new Vec2f(mapLen - 30, HEIGHT), body, 0x0000ff, mStatic));
		body.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		physics.addBody(body);

		// Create player
		player = new Body(100, 130);
		player.addShape(new Box(0, 0, player, new Vec2f(10, 20), 0x00ff00, mDynamic));
		player.getShape(0).addPassiveAttribute(PassiveAttributes.PLAYER_ALL);
		body.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		player.addShape(new Box(0, -15, player, new Vec2f(9, 5), 0xffffff, mEvent));
		player.getShape(1).setCollision(false);
		player.getShape(1).addActiveAttribute(AvtiveAttributes.PLAYER_FEETS);
		physics.addBody(player);

		// Create camera
		camera = new Body(WIDTH / 2, HEIGHT / 2);
//		camera.setVisible(false);
		camera.addShape(new Point(0, 0, camera, 0x0000ff, mCamera));
		camera.getShape(0).setCollision(false);
		camera.getShape(0).setVisible(false);
		// Left Box
		camera.addShape(new Box(-(int) (WIDTH * 0.3125f), 0, camera, new Vec2f(WIDTH * 0.1875f, HEIGHT * 0.5f),
				0x00ff00, mCamera));
		camera.getShape(1).setCollision(false);
		camera.getShape(1).addActiveAttribute(AvtiveAttributes.CAMERA_LEFT);
		camera.getShape(1).setVisible(false);
		// Right Box
		camera.addShape(new Box((int) (WIDTH * 0.3125f), 0, camera, new Vec2f(WIDTH * 0.1875f, HEIGHT * 0.5f), 0x00ff00,
				mCamera));
		camera.getShape(2).setCollision(false);
		camera.getShape(2).addActiveAttribute(AvtiveAttributes.CAMERA_RIGHT);
		camera.getShape(2).setVisible(false);
		// Lower Box
		camera.addShape(new Box(0, -(int) (HEIGHT * 0.3125f), camera, new Vec2f(WIDTH * 0.5f, HEIGHT * 0.1875f),
				0x00ff00, mCamera));
		camera.getShape(3).setCollision(false);
		camera.getShape(3).addActiveAttribute(AvtiveAttributes.CAMERA_DOWN);
		camera.getShape(3).setVisible(false);
		// Upper Box
		camera.addShape(new Box(0, (int) (HEIGHT * 0.3125f), camera, new Vec2f(WIDTH * 0.5f, HEIGHT * 0.1875f),
				0x00ff00, mCamera));
		camera.getShape(4).setCollision(false);
		camera.getShape(4).addActiveAttribute(AvtiveAttributes.CAMERA_UP);
		camera.getShape(4).setVisible(false);
		// Mid X Box
		camera.addShape(new Box(0, 0, camera, new Vec2f(WIDTH * 0.125f, HEIGHT * 0.5f), 0xff0000, mCamera));
		camera.getShape(5).setCollision(false);
		camera.getShape(5).addActiveAttribute(AvtiveAttributes.CAMERA_MID_X);
		camera.getShape(5).setVisible(false);
		// Mid Y Box
		camera.addShape(new Box(0, 0, camera, new Vec2f(WIDTH * 0.5f, HEIGHT * 0.125f), 0xff0000, mCamera));
		camera.getShape(6).setCollision(false);
		camera.getShape(6).addActiveAttribute(AvtiveAttributes.CAMERA_MID_Y);
		camera.getShape(6).setVisible(false);
		physics.addBody(camera);
		renderer.setCamera(camera);
	}
	
	@Override
	protected void mechanicsLoop() {
		Vec2f cameraPos = camera.getPos();
		if (cameraPos.getX() - WIDTH * 0.5f < 0) {
			cameraPos.setX(WIDTH * 0.5f);
			camera.getAcc().setX(0);
		}
		else if (cameraPos.getX() + WIDTH * 0.5f > mapLen) {
			cameraPos.setX(mapLen - WIDTH * 0.5f);
			camera.getAcc().setX(0);
		}
		if (cameraPos.getY() - HEIGHT * 0.5f < 0) {
			cameraPos.setY(HEIGHT * 0.5f);
			camera.getAcc().setY(0);
		}
		else if (cameraPos.getY() + HEIGHT * 0.5f > mapHeight) {
			cameraPos.setY(mapHeight - HEIGHT * 0.5f);
			camera.getAcc().setY(0);
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
	public void leftArrowPressed() {
		player.getAcc().sub(playerRunAcc);
		if (-player.getVel().getX() > playerRunSpeed) {
			player.getVel().setX(-playerRunSpeed);
		}

	}

	@Override
	public void leftArrowReleased() {

	}

	@Override
	public void rightArrowPressed() {
		player.getAcc().add(playerRunAcc);
		if (player.getVel().getX() > playerRunSpeed) {
			player.getVel().setX(playerRunSpeed);
		}
	}

	@Override
	public void rightArrowReleased() {

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
		if (playerCanJump) {
			player.getAcc().add(playerJumpAcc);
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
				AvtiveAttributes activeE = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.getNumPassiveAttributes(); k++) {
					PassiveAttributes passiveE = passiveShape.getPassiveAttribute(j);
					// Here starts the Eventmanagement
					if (activeE == AvtiveAttributes.CAMERA_RIGHT && passiveE == PassiveAttributes.PLAYER_ALL) {
						camera.getAcc().add(cameraMovementAccX);
						if (camera.getVel().getX() > playerRunSpeed) {
							camera.getVel().setX(playerRunSpeed);
						}
					}
					if (activeE == AvtiveAttributes.CAMERA_LEFT && passiveE == PassiveAttributes.PLAYER_ALL) {
						camera.getAcc().sub(cameraMovementAccX);
						if (-camera.getVel().getX() > playerRunSpeed) {
							camera.getVel().setX(-playerRunSpeed);
						}
					}
					if (activeE == AvtiveAttributes.CAMERA_MID_X && passiveE == PassiveAttributes.PLAYER_ALL) {
						camera.getVel().setX(camera.getVel().getX()*0.5f);
					}
					if (activeE == AvtiveAttributes.CAMERA_UP && passiveE == PassiveAttributes.PLAYER_ALL) {
						camera.getAcc().add(cameraMovementAccY);
						if (camera.getVel().getY() > cameraMovementSpeedY) {
							camera.getVel().setY(cameraMovementSpeedY);
						}
					}
					if (activeE == AvtiveAttributes.CAMERA_DOWN && passiveE == PassiveAttributes.PLAYER_ALL) {
						camera.getAcc().sub(cameraMovementAccY);
						if (-camera.getVel().getY() > cameraMovementSpeedY) {
							camera.getVel().setY(-cameraMovementSpeedY);
						}
					}
					if (activeE == AvtiveAttributes.CAMERA_MID_Y && passiveE == PassiveAttributes.PLAYER_ALL) {
						camera.getVel().setY(camera.getVel().getY()*0.5f);
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
				AvtiveAttributes activeE = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.getNumPassiveAttributes(); k++) {
					PassiveAttributes passiveE = passiveShape.getPassiveAttribute(j);
					// Here starts the Eventmanagement
					if (activeE == AvtiveAttributes.PLAYER_FEETS && passiveE == PassiveAttributes.PHYSICAL) {
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
				AvtiveAttributes activeE = activeShape.getActiveAttribute(j);
				for (int k = 0; k < passiveShape.getNumPassiveAttributes(); k++) {
					PassiveAttributes passiveE = passiveShape.getPassiveAttribute(j);
					// Here starts the Eventmanagement
					if (activeE == AvtiveAttributes.PLAYER_FEETS && passiveE == PassiveAttributes.PHYSICAL) {
						playerCanJump = false;
					}
				}
			}
		}

	}
}
