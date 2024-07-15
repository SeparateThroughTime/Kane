package kane.genericGame.item;

import static kane.Kane.GAME;

import kane.genericGame.Item;
import kane.genericGame.gameEvent.mob.MeleeAttack;
import kane.math.Vec2f;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

// This is used as a static outer-class. Therefore, that's not possible in java, I decided to write it uppercase...
// The only generation of this is in Inventory.
public class SWORD extends Item{
    public static final int STANDARD_AMOUNT = 5;

    private static final Sprite ITEM_SPRITE = new Sprite("sprites\\\\items\\\\sword.png", 16, 16);

    private static final SpriteController[] ITEM_SPRITE_CONTROLLERS = {new SpriteController(ITEM_SPRITE)};

    private static final Sprite[] PLAYER_SPRITES = {new Sprite("sprites\\player\\playerHead.png", 32, 32),
            new Sprite("sprites\\player\\playerUpperSword.png", 32, 32),
            new Sprite("sprites\\player\\playerLower.png", 32, 32)};

    private static final SpriteController[] PLAYER_SPRITE_CONTROLLERS =
            {new SpriteController(PLAYER_SPRITES[0]), new SpriteController(PLAYER_SPRITES[1]),
                    new SpriteController(PLAYER_SPRITES[2])};

    private static final Vec2f playerSpritePosOffset = new Vec2f(-32, -32);

    public SWORD(){
        super("Sword", ITEM_SPRITE_CONTROLLERS, PLAYER_SPRITE_CONTROLLERS);

        initItemSpriteStates();

        initHeadSpriteStates();
        initUpperSpriteStates();
        initLowerSpriteStates();

    }

    private static void initLowerSpriteStates(){
        PLAYER_SPRITES[2].addState(SpriteState.STANDING_RIGHT, new int[]{5});
        PLAYER_SPRITES[2].addState(SpriteState.RUNNING_RIGHT, new int[]{6, 7, 8, 9,});
        PLAYER_SPRITES[2].addState(SpriteState.HIT_RIGHT, new int[]{5});
        PLAYER_SPRITES[2].addState(SpriteState.JUMP_RIGHT, new int[]{5});
        PLAYER_SPRITES[2].addState(SpriteState.FALL_RIGHT, new int[]{5});
        PLAYER_SPRITES[2].addState(SpriteState.STANDING_LEFT, new int[]{0});
        PLAYER_SPRITES[2].addState(SpriteState.RUNNING_LEFT, new int[]{6, 7, 8, 9});
        PLAYER_SPRITES[2].addState(SpriteState.HIT_LEFT, new int[]{0});
        PLAYER_SPRITES[2].addState(SpriteState.JUMP_LEFT, new int[]{0});
        PLAYER_SPRITES[2].addState(SpriteState.FALL_LEFT, new int[]{0});

        PLAYER_SPRITE_CONTROLLERS[2].spritePosOffset = playerSpritePosOffset;
    }

    private static void initUpperSpriteStates(){
        PLAYER_SPRITES[1].addState(SpriteState.STANDING_RIGHT, new int[]{6});
        PLAYER_SPRITES[1].addState(SpriteState.RUNNING_RIGHT, new int[]{7, 8, 9, 10});
        PLAYER_SPRITES[1].addState(SpriteState.STANDING_LEFT, new int[]{0});
        PLAYER_SPRITES[1].addState(SpriteState.HIT_RIGHT, new int[]{7});
        PLAYER_SPRITES[1].addState(SpriteState.JUMP_RIGHT, new int[]{7});
        PLAYER_SPRITES[1].addState(SpriteState.FALL_RIGHT, new int[]{7});
        PLAYER_SPRITES[1].addState(SpriteState.RUNNING_LEFT, new int[]{1, 2, 3, 4});
        PLAYER_SPRITES[1].addState(SpriteState.ATTACK_RIGHT, new int[]{11});
        PLAYER_SPRITES[1].addState(SpriteState.ATTACK_LEFT, new int[]{5});
        PLAYER_SPRITES[1].addState(SpriteState.HIT_LEFT, new int[]{0});
        PLAYER_SPRITES[1].addState(SpriteState.JUMP_LEFT, new int[]{0});
        PLAYER_SPRITES[1].addState(SpriteState.FALL_LEFT, new int[]{0});

        PLAYER_SPRITE_CONTROLLERS[1].spritePosOffset = playerSpritePosOffset;
    }

    private static void initHeadSpriteStates(){
        PLAYER_SPRITES[0].addState(SpriteState.STANDING_RIGHT, new int[]{0});
        PLAYER_SPRITES[0].addState(SpriteState.RUNNING_RIGHT, new int[]{0});
        PLAYER_SPRITES[0].addState(SpriteState.HIT_RIGHT, new int[]{0});
        PLAYER_SPRITES[0].addState(SpriteState.JUMP_RIGHT, new int[]{0});
        PLAYER_SPRITES[0].addState(SpriteState.FALL_RIGHT, new int[]{0});
        PLAYER_SPRITES[0].addState(SpriteState.STANDING_LEFT, new int[]{1});
        PLAYER_SPRITES[0].addState(SpriteState.RUNNING_LEFT, new int[]{1});
        PLAYER_SPRITES[0].addState(SpriteState.HIT_LEFT, new int[]{1});
        PLAYER_SPRITES[0].addState(SpriteState.JUMP_LEFT, new int[]{1});
        PLAYER_SPRITES[0].addState(SpriteState.FALL_LEFT, new int[]{1});

        PLAYER_SPRITE_CONTROLLERS[0].spritePosOffset = playerSpritePosOffset;
    }

    private static void initItemSpriteStates(){
        ITEM_SPRITE.addState(SpriteState.STATIC, new int[]{0});
        ITEM_SPRITE_CONTROLLERS[0].setCurrentSpriteState(SpriteState.STATIC);
        ITEM_SPRITE_CONTROLLERS[0].spritePosOffset = new Vec2f(-128, -32);
        ITEM_SPRITE_CONTROLLERS[0].scale = new Vec2f(2, 2);
    }

    @Override
    public void attack(){
        MeleeAttack attack = new MeleeAttack(20, GAME.player);
        GAME.addEvent(attack);
    }

    @Override
    public void react(){

    }

    @Override
    public void jump(){

    }

    @Override
    public void move(){

    }

}
