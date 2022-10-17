/*TODO
	Hud Flickering while camera Movement
	Jump/Walk/Stop Mechanics
	Visual Effects
	Sounds
	Object Editor
		(Ermitteln des besten Mittelpunkts)
	Level Ends/ Player dies -> Next level/ Restart
	Level Editor
	Event Editor
	Campaign Editor
	StartMenu
	Save
	
	Events:
		WalkAI
			KoopaWalk
			WalkToPlayer
			BooHooWalk
			MosquitoWalk
			BatWalk
			TeleWalk
			JumpWalk
			PiercingMissile
			ThrowMissile
			SearchingMissile
		TurnLeft
		TurnRight
		Attack
			MissileAttack
			JumpAttack
			FlyingAttack
			ElectricField
			Pushback
		RotateWorld
		JumpingShoes
		Heal
		MushroomEffect
		Transform(Vampire)
		Sanity(Fire)
		Sanity(Ice)
		
	Mechanics:
		Tauch-Level
		Lohrenfahrt
		Eis
		Flug-Level
		Kanonen-Transport
	
	Dead End To Do:
	Rotation
	
	Wait for relevance To Do:
	Separation of shapepair is not executed when shapePair separates too fast -> Workaround is increasing aabb_tolerance in physics.
	ContactPoint: BoxPolygon, PolygonPolygon -> Ghost Contacts
	Speed-Adjuster (Jumping against dynamics increases jump heigth)
		Its probably not the cause of friction but something else)

*/
package kane;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import kane.genericGame.AIs;
import kane.genericGame.ActiveAttributes;
import kane.genericGame.Game;
import kane.genericGame.Item;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.genericGame.PassiveAttributes;
import kane.genericGame.hud.HudBar;
import kane.genericGame.item.SWORD;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.physics.shapes.LineSegment;
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

	Material mStatic = new Material(0, 1f);
	Material mDynamic = new Material(1, 0.9f);
	Material mEvent = new Material(0, 0);
	Material mInterface = new Material(1, 0);
	Body sword;
	Item currentItem;

	@Override
	protected void initGame() {

		mapLen = 400 * 3;
		mapHeight = resSpecs.GAME_HEIGHT;

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
		player = new Mob(this, 100, 130, 3, 1);
		player.setWalkAcc(new Vec2f(40 / DELTATIME, 0));
		player.setJumpAcc(new Vec2f(0, 800 / DELTATIME));
		player.setWalkSpeed(300);
		physics.addBody(player);

		// camera
		renderer.createCamera();
		renderer.getCamera().bindCameraToMap();
		renderer.moveBackground();
		mouseListener.addCamera(renderer.getCamera());
		inventory = renderer.getCamera().initInventory();
		
		// Set player Item
		currentItem = inventory.getItem("None");
		player.addShape(new Box(0, 0, player, new Vec2f(16, 32), Color.GREEN, mDynamic, 2));
		player.getShape(0).addPassiveAttribute(PassiveAttributes.PLAYER_ALL);
		player.getShape(0).addPassiveAttribute(PassiveAttributes.MOB_ALL);
		player.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		player.addShape(new Box(0, -22, player, new Vec2f(15, 10), Color.WHITE, mEvent, 2));
		player.getShape(1).setCollision(false);
		player.getShape(1).addActiveAttribute(ActiveAttributes.MOB_FEETS);
		player.getShape(1).addActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
		player.getShape(1).setVisible(true);
		player.addShape(new Box(32, 0, player, new Vec2f(8, 32), Color.RED, mEvent, 2));
		player.getShape(2).setCollision(false);
		player.getShape(2).addPassiveAttribute(PassiveAttributes.ATTACKING_FIELD);
		player.getShape(2).setVisible(true);
		SpriteController[] spriteControllers = currentItem.getPlayerSpriteControllers();
		player.getShape(0).setSpriteControllers(spriteControllers);

		// Sword
		sword = new Body(200, 130);
		Vec2f points[] = new Vec2f[4];
		points[0] = new Vec2f(-16, -16);
		points[1] = new Vec2f(16, -16);
		points[2] = new Vec2f(16, 16);
		points[3] = new Vec2f(-16, 16);
		sword.addShape(new Polygon(0, 0, sword, Color.YELLOW, points, mDynamic, 2));
		sword.getShape(0).addActiveAttribute(ActiveAttributes.SWORD);
		File file = new File("sprites\\items\\sword.png");
		Sprite sprite = new Sprite(file, 16, 16);
		sprite.addState(SpriteState.STATIC, new int[] { 0 });
		spriteControllers = new SpriteController[1];
		spriteControllers[0] = new SpriteController(sprite);
		spriteControllers[0].setSpritePosOffset(new Vec2f(-16, -16));
		spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
		sword.getShape(0).setSpriteControllers(spriteControllers);
		physics.addBody(sword);

		// Create Blob
		Mob blob = new Mob(this, 300, 130, 3, 1);
		points = new Vec2f[4];
		blob.addShape(new Box(0, 0, blob, new Vec2f(32, 16), Color.YELLOW, mDynamic, 2));
		blob.getShape(0).addPassiveAttribute(PassiveAttributes.MOB_ALL);
		blob.getShape(0).addPassiveAttribute(PassiveAttributes.PHYSICAL);
		blob.addShape(new Box(31, 0, blob, new Vec2f(1, 15), Color.YELLOW, mEvent, 2));
		blob.getShape(1).addPassiveAttribute(PassiveAttributes.MOB_RIGHT);
		blob.getShape(1).setCollision(false);
		blob.addShape(new Box(-31, 0, blob, new Vec2f(1, 15), Color.YELLOW, mEvent, 2));
		blob.getShape(2).addPassiveAttribute(PassiveAttributes.MOB_LEFT);
		blob.getShape(2).setCollision(false);
		blob.addShape(new Box(0, -2, blob, new Vec2f(32, 15), Color.YELLOW, mEvent, 2));
		blob.getShape(3).addActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
		blob.getShape(3).setCollision(false);
		sprite = new Sprite(new File("sprites\\Mobs\\Blob\\Blob.png"), 32, 32);
		sprite.addState(SpriteState.STATIC, new int[] { 0 });
		spriteControllers = new SpriteController[1];
		spriteControllers[0] = new SpriteController(sprite);
		spriteControllers[0].setSpritePosOffset(new Vec2f(-32, -16));
		spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
		blob.getShape(0).setSpriteControllers(spriteControllers);
		blob.getActiveActions().put(MobActions.GUMBA_WALK, true);
		blob.setAI(AIs.GUMBA);
		blob.setWalkAcc(new Vec2f(40 / DELTATIME, 0));
		blob.setJumpAcc(new Vec2f(0, 200 / DELTATIME));
		blob.setWalkSpeed(50);
		physics.addBody(blob);

		// Create Background
		file = new File("sprites\\backgrounds\\background.png");
		renderer.changeBackground(file);

//		changeResolution(Resolution.SOL1176x664);

		// healthBar
		file = new File("sprites\\interface\\HealthBar.png");
		healthBar = renderer.getCamera().addHudBar(file);
		refreshHealthBar();
		
		
	}

	public void refreshHealthBar() {
		if (player.getHealth() > 0) {
			healthBar.refresh((float) player.getHealth() / HudBar.MAX_PLAYER_HEALTH);
		} else {
			healthBar.refresh(0);

		}

	}

	@Override
	protected void mechanicsLoop() {
	}

	@Override
	protected void postMechanicsLoops() {

	}

	@Override
	public void leftMousePressed() {

	}

	@Override
	public void leftMouseReleased() {
		if (showInventory) {
			for (int i = 0; i < inventory.NUM_SLOTS; i++) {
				Shape slot = inventory.getSlot(i);
				if (slot.isPointInShape(mouseListener.getMousePos())) {
					Item item = inventory.getItem(i);
					if (item != null) {
						currentItem = item;
						SpriteController[] spriteControllers = item.getPlayerSpriteControllers();
						SpriteState spriteState = player.getShape(PassiveAttributes.MOB_ALL).getCurrentSpriteState();
						player.getShape(PassiveAttributes.MOB_ALL).setSpriteControllers(spriteControllers);
						player.getShape(PassiveAttributes.MOB_ALL).setCurrentSpriteState(spriteState);
					}
				}
			}
		}
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
		player.walkLeft();
	}

	@Override
	public void leftArrowPressed() {
	}

	@Override
	public void leftArrowReleased() {
		player.stopWalkLeft();
	}

	@Override
	public void rightArrowClick() {
		player.walkRight();
	}

	@Override
	public void rightArrowPressed() {
	}

	@Override
	public void rightArrowReleased() {
		player.stopWalkRight();

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
			player.jump();
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
		currentItem.attack(this);
	}

	@Override
	public void escClick() {
		if (showInventory) {
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

	boolean showInventory = false;

	@Override
	public void iClick() {
		showInventory = !showInventory;
		pause = showInventory;
		inventory.setVisible(showInventory);

	}

	@Override
	public void playerTouchCameraLeft(Shape cameraLeft, Shape playerAll) {
		renderer.getCamera().moveCameraLeft();
	}

	@Override
	public void playerTouchCameraRight(Shape cameraRight, Shape playerAll) {
		renderer.getCamera().moveCameraRight();
	}

	@Override
	public void playerTouchCameraUp(Shape cameraUp, Shape playerAll) {
		renderer.getCamera().moveCameraUp();
	}

	@Override
	public void playerTouchCameraDown(Shape cameraDown, Shape playerAll) {
		renderer.getCamera().moveCameraDown();
	}

	@Override
	public void playerTouchCameraMidX(Shape cameraMidX, Shape playerAll) {
		renderer.getCamera().SlowCameraX();
	}

	@Override
	public void playerTouchCameraMidY(Shape cameraMidY, Shape playerAll) {
		renderer.getCamera().SlowCameraY();
	}

	@Override
	public void mobStandsOnPhysical(Shape mobFeet, Shape physical) {
		Mob mob = (Mob) mobFeet.getBody();
		mob.setOnGround(true);
	}

	@Override
	public void playerCollectsSword(Shape sword, Shape playerAll) {
		sword.getBody().remove();
		inventory.getItem("Sword").addAmount(SWORD.STANDARD_AMOUNT);
	}

	@Override
	public void mobFeetLeavePhysical(Shape mobFeet, Shape physical) {
		Mob mob = (Mob) mobFeet.getBody();
		mob.setOnGround(false);
	}

	@Override
	public void mobAttacksMob(Shape attackingField, Shape attackedMobAll) {
		Mob attackedMob = (Mob) attackedMobAll.getBody();
		Mob attackingMob = (Mob) attackingField.getBody();
		int damage = attackingMob.getDamage();
		attackedMob.hit(damage, attackingMob.getPos());

		if (attackedMob.hasShapeWithPassiveAttribute(PassiveAttributes.PLAYER_ALL)) {
			refreshHealthBar();
		}
	}

	@Override
	public void mobJumpsOnMob(Shape mobFeet, Shape mobAll) {
		Mob jumpingMob = (Mob) mobFeet.getBody();
		jumpingMob.getVel().setY(300);
	}
}
