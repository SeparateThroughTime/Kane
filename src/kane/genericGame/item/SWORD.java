package kane.genericGame.item;

import static kane.Kane.GAME;

import kane.exceptions.WriteJsonException;
import kane.genericGame.Item;
import kane.genericGame.JsonManager;
import kane.genericGame.gameEvent.mob.MeleeAttack;
import kane.math.Vec2f;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

// This is used as a static outer-class. Therefore, that's not possible in java, I decided to write it uppercase...
// The only generation of this is in Inventory.
public class SWORD extends Item{
    public static final int STANDARD_AMOUNT = 5;

    public SWORD(){
        super("Sword", "json\\items\\sword\\itemSpriteController.json", "json\\items\\sword\\playerSpriteController.json");

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
