package kane;

import com.google.gson.Gson;
import kane.genericGame.*;
import kane.genericGame.hud.Inventory;
import kane.math.Vec2f;
import kane.physics.Material;
import kane.physics.Physics;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.renderer.ResolutionSpecification;
import kane.renderer.SpriteController;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;

import static kane.genericGame.Game.DELTATIME;
import static kane.genericGame.hud.Inventory.INVENTORY;

public class ExternalizeTest implements ContactManagementInterface{

    String test;
    int number;


    public ExternalizeTest(){
        test = "Test!";
        number = 20;
    }


    public static void main(String[] args){
        ExternalizeTest a = new ExternalizeTest();
        Physics.initializePhysics(a);


        Mob player = new Mob(100, 130, 3, 1, MobDirection.RIGHT);
        player.setWalkAcc(new Vec2f(40 / DELTATIME, 0));
        player.setJumpAcc(new Vec2f(0, 800 / DELTATIME));
        player.setWalkSpeed(300);

        Material mDynamic = new Material(0, 0);
        Box box = new Box(0,0,player, new Vec2f(), Color.RED, mDynamic, 1);
        ResolutionSpecification.initializeResSpecs(1,1,1,1);
        Inventory.initializeInventory(box, new Box[]{box}, ResolutionSpecification.RES_SPECS);

        Item currentItem = INVENTORY.getItem("None");
        player.addShape(new Box(0, 0, player, new Vec2f(16, 32), Color.WHITE, mDynamic, 2));
        player.shapes[0].addPassiveAttribute(PassiveAttributes.PLAYER_ALL);
        player.shapes[0].addPassiveAttribute(PassiveAttributes.MOB_ALL);
        player.shapes[0].addPassiveAttribute(PassiveAttributes.PHYSICAL);
        Material mEvent = new Material(0, 0);
        player.addShape(new Box(0, -22, player, new Vec2f(15, 10), Color.WHITE, mEvent, 2));
        player.shapes[1].collision = false;
        player.shapes[1].addActiveAttribute(ActiveAttributes.MOB_FEETS);
        player.shapes[1].addActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
        player.shapes[1].visible = false;
        player.addShape(new Box(32, 0, player, new Vec2f(8, 32), Color.RED, mEvent, 2));
        player.shapes[2].collision = false;
        player.shapes[2].addPassiveAttribute(PassiveAttributes.ATTACKING_FIELD);
        player.shapes[2].visible = false;
        SpriteController[] spriteControllers = currentItem.getPlayerSpriteControllers();
        player.shapes[0].setSpriteControllers(spriteControllers);
        //        player.addSoundSource("sound//player//jump.ogg", SoundType.JUMP);
        //        player.addSoundSource("sound//player//damage.ogg", SoundType.DAMAGE);
        //        player.addSound("sound//player//walk.ogg", SoundType.WALK);
        //        player.addSoundSource("sound//player//attack.ogg", SoundType.ATTACK);
        player.setDirection(MobDirection.RIGHT);

        Gson gson = new Gson();
        String json = gson.toJson(player);
        System.out.println(json);

    }

    @Override
    public void playerTouchCameraLeft(Shape cameraLeft, Shape playerAll){

    }

    @Override
    public void playerTouchCameraRight(Shape cameraRight, Shape playerAll){

    }

    @Override
    public void playerTouchCameraUp(Shape cameraUp, Shape playerAll){

    }

    @Override
    public void playerTouchCameraDown(Shape cameraDown, Shape playerAll){

    }

    @Override
    public void playerTouchCameraMidX(Shape cameraMidX, Shape playerAll){

    }

    @Override
    public void playerTouchCameraMidY(Shape cameraMidY, Shape playerAll){

    }

    @Override
    public void playerCollectsSword(Shape sword, Shape playerAll){

    }

    @Override
    public void mobJumpsOnMob(Shape playerFeet, Shape mobAll){

    }

    @Override
    public void mobStandsOnPhysical(Shape mobFeet, Shape physical){

    }

    @Override
    public void mobFeetLeavePhysical(Shape mobFeet, Shape physical){

    }

    @Override
    public void mobAttacksMob(Shape attackingField, Shape attackedMobAll){

    }
}
