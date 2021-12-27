package kane.genericGame.item;

import java.io.File;

import kane.genericGame.Game;
import kane.genericGame.Item;
import kane.genericGame.gameEvent.MeleeAttack;
import kane.math.Vec2f;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

// This is used as a static outer-class. Therefore thats not possible in java, I decided to write it uppercase...
// The only generation of this is in Inventory.
public class SWORD extends Item {
	public static final int STANDARD_AMOUNT = 5;
	
	private static final Sprite ITEM_SPRITE = new Sprite(new File("sprites\\\\items\\\\sword.png"), 1, 1);
	
	private static final SpriteController[] ITEM_SPRITE_CONTROLLERS = {
			new SpriteController(ITEM_SPRITE)
	};
	
	private static final Sprite[] PLAYER_SPRITES = {
			new Sprite(new File("sprites\\player\\playerHead.png"), 2, 2),
			new Sprite(new File("sprites\\player\\playerUpperSword.png"), 2, 2),
			new Sprite(new File("sprites\\player\\playerLower.png"), 2, 2)
	};

	private static final SpriteController[] PLAYER_SPRITE_CONTROLLERS = {
			new SpriteController(PLAYER_SPRITES[0]),
			new SpriteController(PLAYER_SPRITES[1]),
			new SpriteController(PLAYER_SPRITES[2])
	};

	public SWORD() {
		super("Sword", ITEM_SPRITE_CONTROLLERS, PLAYER_SPRITE_CONTROLLERS);
		
		ITEM_SPRITE.addState(SpriteState.STATIC, new int[] { 0 });
		ITEM_SPRITE_CONTROLLERS[0].setCurrentSpriteState(SpriteState.STATIC, true);
		ITEM_SPRITE_CONTROLLERS[0].setSpritePosOffset(new Vec2f(-32, -32));
		ITEM_SPRITE_CONTROLLERS[0].setScale(2f);
		
		PLAYER_SPRITES[0].addState(SpriteState.STANDING_RIGHT, new int[] { 0 });
		PLAYER_SPRITES[0].addState(SpriteState.RUNNING_RIGHT, new int[] { 0 });
		PLAYER_SPRITES[0].addState(SpriteState.STANDING_LEFT, new int[] { 1 });
		PLAYER_SPRITES[0].addState(SpriteState.RUNNING_LEFT, new int[] { 1 });
		
		PLAYER_SPRITES[1].addState(SpriteState.STANDING_RIGHT, new int[] { 0 });
		PLAYER_SPRITES[1].addState(SpriteState.RUNNING_RIGHT, new int[] { 1, 2, 3, 4 });
		PLAYER_SPRITES[1].addState(SpriteState.STANDING_LEFT, new int[] { 6 });
		PLAYER_SPRITES[1].addState(SpriteState.RUNNING_LEFT, new int[] { 7, 8, 9, 10 });
		PLAYER_SPRITES[1].addState(SpriteState.ATTACK_RIGHT, new int[] { 5 });
		PLAYER_SPRITES[1].addState(SpriteState.ATTACK_LEFT, new int[] { 11 });
		
		PLAYER_SPRITES[2].addState(SpriteState.STANDING_RIGHT, new int[] { 0 });
		PLAYER_SPRITES[2].addState(SpriteState.RUNNING_RIGHT, new int[] { 1, 2, 3, 4, });
		PLAYER_SPRITES[2].addState(SpriteState.STANDING_LEFT, new int[] { 5 });
		PLAYER_SPRITES[2].addState(SpriteState.RUNNING_LEFT, new int[] { 6, 7, 8, 9 });
		
		PLAYER_SPRITE_CONTROLLERS[0].setCurrentSpriteState(SpriteState.STANDING_RIGHT, true);
		PLAYER_SPRITE_CONTROLLERS[0].setSpritePosOffset(new Vec2f(-32, -32));
		PLAYER_SPRITE_CONTROLLERS[1].setCurrentSpriteState(SpriteState.STANDING_RIGHT, true);
		PLAYER_SPRITE_CONTROLLERS[1].setSpritePosOffset(new Vec2f(-32, -32));
		PLAYER_SPRITE_CONTROLLERS[2].setCurrentSpriteState(SpriteState.STANDING_RIGHT, true);
		PLAYER_SPRITE_CONTROLLERS[2].setSpritePosOffset(new Vec2f(-32, -32));

	}

	@Override
	public void attack(Game g) {
		MeleeAttack attack = new MeleeAttack(g, 20, g.getPlayer());
		g.addEvent(attack);
	}

	@Override
	public void react(Game g) {

	}

	@Override
	public void jump(Game g) {

	}

	@Override
	public void move(Game g) {

	}

}
