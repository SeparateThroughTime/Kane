/*T O D O
	Object Editor
		(Ermitteln des besten Mittelpunkts)
	Level Ends/ Player dies -> Next level/ Restart
	Level Editor
	Event Editor?
	Campaign Editor
	StartMenu
	Save
	Camera Adjustments
	Key-Mapping

	Shader
	    Make Distortion move with camera
	
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
	Speed-Adjuster (Jumping against dynamics increases jump height)
		Its probably not the cause of friction but something else)
	Hud Flickering while camera Movement
	Lines disappear when opening inventory
		Only on my PC, not on my Laptop... -> Seems to be a bug of lwjgl or openGL
	Switch to lwjgl
		inventory edges blue
		player sprite has blue pixels when on Edge of map

*/
package kane;

import static kane.genericGame.hud.Inventory.INVENTORY;
import static kane.genericGame.userInteraction.Mouse.MOUSE;
import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.sound.SoundEngine.SOUND;
import static kane.genericGame.JsonManager.JSON_MANAGER;

import java.awt.Color;
import java.util.Arrays;

import kane.exceptions.LoadJsonException;
import kane.exceptions.WriteJsonException;
import kane.genericGame.*;
import kane.genericGame.gameEvent.mob.DamageHandler;
import kane.genericGame.hud.HudBar;
import kane.genericGame.hud.Inventory;
import kane.genericGame.item.SWORD;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.*;
import kane.renderer.*;

public class Kane extends Game{

    public static Kane GAME;
    public boolean showInventory = false;

    private Kane(String title){
        super(title);
    }

    public static void main(String[] args){
        GAME = new Kane("Kane");
        GAME.run();

    }


    Body sword;
    Item currentItem;

    @Override
    protected void initGame(){

        mapLen = 400 * 3;
        mapHeight = RES_SPECS.GAME_HEIGHT;

        try{
            player = JSON_MANAGER.loadMob("json/player/player.json");
            player.pos.set(100, 130);
        } catch (LoadJsonException e){
            e.printStackTrace();
        }

        Camera.initializeCamera(true);
        CAMERA.bindCameraToMap();
        RENDERER.moveBackground();
        Inventory.initializeInventory();

        // Create World
        try{
            Body body = JSON_MANAGER.loadBody("json\\levels\\test\\world.json");
            LineSegment a = (LineSegment) (body.shapes[0]);
            a.addToRenderer();
            a = (LineSegment) (body.shapes[1]);
            a.addToRenderer();
            a = (LineSegment) (body.shapes[2]);
            a.addToRenderer();
        } catch (LoadJsonException e){
            throw new RuntimeException(e);
        }

        // Set player Item
        currentItem = INVENTORY.getItem("None");

        // Create Sword
        try{
            sword = JSON_MANAGER.loadBody("json\\levels\\test\\sword.json");
            sword.pos.set(200, 130);
            sword.setCurrentSpriteState(SpriteState.STATIC);
        } catch (LoadJsonException e){
            throw new RuntimeException(e);
        }

        // Create Blob
        try{
            Mob blob = JSON_MANAGER.loadMob("json\\levels\\test\\blob.json");
            blob.pos.set(300, 130);
            blob.setCurrentSpriteState(SpriteState.STATIC);
        } catch (LoadJsonException e){
            throw new RuntimeException(e);
        }


        // Create Background
        RENDERER.changeBackground("sprites\\backgrounds\\background.png");

        //		changeResolution(Resolution.SOL1176x664);

        // healthBar
        healthBar = new HudBar("sprites\\interface\\HealthBar.png");
        refreshHealthBar();

        // Spooky testing
        this.setSanity(0.0f);

        //		GAME.changeResolution(Resolution.SOL1024x768);

        // background music
        //        addEvent(new PlaySound(RESOURCE_MANAGER.getSoundBuffer("sound//music//01//main.ogg"), true, true));

    }

    public void refreshHealthBar(){
        if (player.getHealth() > 0){
            healthBar.refresh((float) player.getHealth() / HudBar.MAX_PLAYER_HEALTH);
        } else{
            healthBar.refresh(0);

        }

    }

    @Override
    public void mechanicsLoop(){
    }

    @Override
    public void postMechanicsLoops(){

    }

    @Override
    public void leftMousePressed(){

    }

    @Override
    public void leftMouseReleased(){
        checkInventoryClick();
    }

    private void checkInventoryClick(){
        if (showInventory){
            for (int i = 0; i < Inventory.NUM_SLOTS; i++){
                Shape slot = INVENTORY.getSlot(i);
                if (slot.isPointInShape(MOUSE.mousePos)){

                    inventoryClick(i);
                }
            }
        }
    }

    private void inventoryClick(int i){
        Item item = INVENTORY.getItem(i);
        if (item != null){
            currentItem = item;
            SpriteController[] spriteControllers = item.getPlayerSpriteControllers();
            player.getShape(PassiveAttributes.MOB_ALL).setSpriteControllers(spriteControllers);
            player.refreshSpriteStates();
        }
    }

    @Override
    public void leftMouseClick(){

    }

    @Override
    public void rightMousePressed(){
    }

    @Override
    public void rightMouseReleased(){

    }

    @Override
    public void rightMouseClick(){
    }

    @Override
    public void leftArrowClick(){
        player.walkLeft();
    }

    @Override
    public void leftArrowPressed(){
    }

    @Override
    public void leftArrowReleased(){
        player.stopWalkLeft();
    }

    @Override
    public void rightArrowClick(){
        player.walkRight();
    }

    @Override
    public void rightArrowPressed(){
    }

    @Override
    public void rightArrowReleased(){
        player.stopWalkRight();

    }

    @Override
    public void upArrowClick(){

    }

    @Override
    public void upArrowPressed(){

    }

    @Override
    public void upArrowReleased(){

    }

    @Override
    public void downArrowPressed(){

    }

    @Override
    public void downArrowClick(){

    }

    @Override
    public void downArrowReleased(){

    }

    @Override
    public void f1Click(){

    }

    @Override
    // show Contacts
    public void f2Click(){
        RENDERER.showContacts = !RENDERER.showContacts;

    }

    @Override
    // show AABBs
    public void f3Click(){
        RENDERER.showAABBs = !RENDERER.showAABBs;

    }

    @Override
    public void f4Click(){

    }

    @Override
    public void f5Click(){

    }

    @Override
    public void f6Click(){
    }

    @Override
    public void f7Click(){
    }

    @Override
    public void f8Click(){

    }

    @Override
    public void f9Click(){

    }

    @Override
    public void f10Click(){

    }

    @Override
    public void f11Click(){

    }

    @Override
    public void f12Click(){

    }

    @Override
    public void spacePressed(){

    }

    @Override
    public void spaceReleased(){

    }

    @Override
    public void spaceClick(){
        if (!pause){
            player.jump();
        }
    }

    @Override
    public void shiftPressed(){

    }

    @Override
    public void shiftReleased(){

    }

    @Override
    public void shiftClick(){
        currentItem.attack();
    }

    @Override
    public void escClick(){
        if (showInventory){
            iClick();
        } else{
            pause = !pause;
        }
    }

    @Override
    public void cPressed(){
    }

    @Override
    public void cReleased(){

    }

    @Override
    public void cClick(){

    }

    @Override
    public void iPressed(){

    }

    @Override
    public void iReleased(){

    }

    @Override
    public void iClick(){
        showInventory = !showInventory;
        pause = showInventory;
        INVENTORY.setVisible(showInventory);

        if (pause){
            SOUND.pause();
        } else{
            SOUND.resume();
        }
    }

    @Override
    public void playerTouchCameraLeft(Shape cameraLeft, Shape playerAll){
        CAMERA.moveCameraLeft();
    }

    @Override
    public void playerTouchCameraRight(Shape cameraRight, Shape playerAll){
        CAMERA.moveCameraRight();
    }

    @Override
    public void playerTouchCameraUp(Shape cameraUp, Shape playerAll){
        CAMERA.moveCameraUp();
    }

    @Override
    public void playerTouchCameraDown(Shape cameraDown, Shape playerAll){
        CAMERA.moveCameraDown();
    }

    @Override
    public void playerTouchCameraMidX(Shape cameraMidX, Shape playerAll){
        CAMERA.SlowCameraX();
    }

    @Override
    public void playerTouchCameraMidY(Shape cameraMidY, Shape playerAll){
        CAMERA.SlowCameraY();
    }

    @Override
    public void mobStandsOnPhysical(Shape mobFeet, Shape physical){
        Mob mob = (Mob) mobFeet.body;
        mob.setOnGround(true);
    }

    @Override
    public void playerCollectsSword(Shape sword, Shape playerAll){
        sword.body.remove();
        INVENTORY.getItem("Sword").addAmount(SWORD.STANDARD_AMOUNT);
    }

    @Override
    public void mobFeetLeavePhysical(Shape mobFeet, Shape physical){
        Mob mob = (Mob) mobFeet.body;
        mob.setOnGround(false);
    }

    @Override
    public void mobAttacksMob(Shape attackingField, Shape attackedMobAll){
        Mob attackedMob = (Mob) attackedMobAll.body;
        Mob attackingMob = (Mob) attackingField.body;
        addEvent(new DamageHandler(attackingMob, attackedMob));
    }

    @Override
    public void mobJumpsOnMob(Shape mobFeet, Shape mobAll){
        Mob jumpingMob = (Mob) mobFeet.body;
        jumpingMob.vel.y = 300;
    }
}
