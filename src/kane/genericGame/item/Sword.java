package kane.genericGame.item;

import java.io.File;

import kane.genericGame.Item;
import kane.math.Vec2f;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

// This is used as a static outer-class. Therefore thats not possible in java, I decided to write it uppercase...
// The only generation of this is in Inventory.
public class SWORD extends Item {
	public static final int STANDARD_AMOUNT = 5;
	
	private static final File ITEM_FILE = new File("sprites\\items\\sword.png");
	private static final Sprite ITEM_SPRITE = new Sprite(ITEM_FILE, 1, 1);
	private static final SpriteController ITEM_SPRITE_CONTROLLER = new SpriteController(ITEM_SPRITE);
	private static final File PLAYER_FILE = new File("sprites\\player\\playerSword.png");
	private static final Sprite PLAYER_SPRITE = new Sprite(PLAYER_FILE, 2, 2);
	private static final SpriteController PLAYER_SPRITE_CONTROLLER = new SpriteController(PLAYER_SPRITE);

	public SWORD() {
		super("Sword", ITEM_SPRITE_CONTROLLER, PLAYER_SPRITE_CONTROLLER);
		
		ITEM_SPRITE.addState(SpriteState.STATIC, new int[] { 0 });
		ITEM_SPRITE_CONTROLLER.setCurrentSpriteState(SpriteState.STATIC, true);
		ITEM_SPRITE_CONTROLLER.setSpritePosOffset(new Vec2f(-32, -32));
		ITEM_SPRITE_CONTROLLER.setScale(2f);
		
		PLAYER_SPRITE.addState(SpriteState.STANDING, new int[] { 0 });
		PLAYER_SPRITE.addState(SpriteState.RUNNING, new int[] { 1, 2, 3, 4 });
		PLAYER_SPRITE.addState(SpriteState.STANDING_ATTACK, new int[] { 5, 6, 7, 8 });
		PLAYER_SPRITE.addState(SpriteState.RUNNING_ATTACK, new int[] { 9, 10, 11, 12 });
		PLAYER_SPRITE_CONTROLLER.setCurrentSpriteState(SpriteState.STANDING, true);
		PLAYER_SPRITE_CONTROLLER.setSpritePosOffset(new Vec2f(-32, -32));

	}

	@Override
	public void attack() {

	}

	@Override
	public void react() {

	}

	@Override
	public void jump() {

	}

	@Override
	public void move() {

	}

}
