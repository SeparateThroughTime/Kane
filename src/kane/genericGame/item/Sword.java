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
	
	private static final File FILE = new File("sprites\\items\\sword.png");
	private static final Sprite SPRITE = new Sprite(FILE, 1, 1);
	private static final SpriteController SPRITE_CONTROLLER = new SpriteController(SPRITE);

	public SWORD() {
		super("Sword", SPRITE_CONTROLLER);
		SPRITE.addState(SpriteState.Static, new int[] { 0 });
		SPRITE_CONTROLLER.setCurrentSpriteState(SpriteState.Static, true);
		SPRITE_CONTROLLER.setSpritePosOffset(new Vec2f(-32, -32));
		SPRITE_CONTROLLER.setScale(2f);

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
