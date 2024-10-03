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

import java.awt.Color;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kane.genericGame.*;
import kane.genericGame.gameEvent.mob.DamageHandler;
import kane.genericGame.hud.HudBar;
import kane.genericGame.hud.Inventory;
import kane.genericGame.item.SWORD;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Polygon;
import kane.renderer.*;
import kane.serialization.BodySerialization;
import kane.serialization.ColorSerialization;
import kane.serialization.MobSerialization;
import kane.serialization.Vec2fSerialization;
import kane.sound.SoundType;

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

    Material mStatic = new Material(0, 1f);
    Material mDynamic = new Material(1, 0.9f);
    Material mEvent = new Material(0, 0);
    Body sword;
    Item currentItem;

    @Override
    protected void initGame(){

        mapLen = 400 * 3;
        mapHeight = RES_SPECS.GAME_HEIGHT;

        // Create player
        player = new Mob(100, 130, 3, 1, MobDirection.RIGHT);
        player.setWalkAcc(new Vec2f(40 / DELTATIME, 0));
        player.setJumpAcc(new Vec2f(0, 800 / DELTATIME));
        player.setWalkSpeed(300);

        // camera
        Camera.initializeCamera(true);
        CAMERA.bindCameraToMap();
        RENDERER.moveBackground();
        CAMERA.initInventory();

        // Create World
        Body body = new Body(0, 0);
        LineSegment line =
                new LineSegment(new Vec2f(30, 0), new Vec2f(30, RES_SPECS.GAME_HEIGHT), body, Color.BLUE, mStatic, 2);
        body.addShape(line);
        line.addToRenderer();
        body.shapes[0].addPassiveAttribute(PassiveAttributes.PHYSICAL);

        body = new Body(0, 0);
        line = new LineSegment(new Vec2f(0, 30), new Vec2f(mapLen, 30), body, Color.BLUE, mStatic, 2);
        body.addShape(line);
        line.addToRenderer();
        body.shapes[0].addPassiveAttribute(PassiveAttributes.PHYSICAL);

        body = new Body(0, 0);
        line = new LineSegment(new Vec2f(mapLen - 30, 0), new Vec2f(mapLen - 30, RES_SPECS.GAME_HEIGHT), body,
                Color.BLUE, mStatic, 2);
        body.addShape(line);
        line.addToRenderer();
        body.shapes[0].addPassiveAttribute(PassiveAttributes.PHYSICAL);

        // Set player Item
        currentItem = INVENTORY.getItem("None");
        player.addShape(new Box(0, 0, player, new Vec2f(16, 32), Color.WHITE, mDynamic, 2));
        player.shapes[0].addPassiveAttribute(PassiveAttributes.PLAYER_ALL);
        player.shapes[0].addPassiveAttribute(PassiveAttributes.MOB_ALL);
        player.shapes[0].addPassiveAttribute(PassiveAttributes.PHYSICAL);
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


        // Sword
        sword = new Body(200, 130);
        Vec2f[] points = new Vec2f[4];
        points[0] = new Vec2f(-16, -16);
        points[1] = new Vec2f(16, -16);
        points[2] = new Vec2f(16, 16);
        points[3] = new Vec2f(-16, 16);
        sword.addShape(new Polygon(0, 0, sword, Color.YELLOW, points, mDynamic, 2));
        sword.shapes[0].addActiveAttribute(ActiveAttributes.SWORD);
        Sprite sprite = new Sprite("sprites\\items\\sword.png", 16, 16);
        sprite.addState(SpriteState.STATIC, new int[]{0});
        spriteControllers = new SpriteController[1];
        spriteControllers[0] = new SpriteController(sprite);
        spriteControllers[0].spritePosOffset = new Vec2f(-16, -16);
        spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
        sword.shapes[0].setSpriteControllers(spriteControllers);

        // Create Blob
        Mob blob = new Mob(300, 130, 3, 1, MobDirection.LEFT);
        points = new Vec2f[4];
        blob.addShape(new Box(0, 0, blob, new Vec2f(32, 16), Color.YELLOW, mDynamic, 2));
        blob.shapes[0].addPassiveAttribute(PassiveAttributes.MOB_ALL);
        blob.shapes[0].addPassiveAttribute(PassiveAttributes.PHYSICAL);
        blob.addShape(new Box(31, 0, blob, new Vec2f(1, 15), Color.YELLOW, mEvent, 2));
        blob.shapes[1].addPassiveAttribute(PassiveAttributes.MOB_RIGHT);
        blob.shapes[1].collision = false;
        blob.shapes[1].visible = false;
        blob.addShape(new Box(-31, 0, blob, new Vec2f(1, 15), Color.YELLOW, mEvent, 2));
        blob.shapes[2].addPassiveAttribute(PassiveAttributes.MOB_LEFT);
        blob.shapes[2].collision = false;
        blob.shapes[2].visible = false;
        blob.addShape(new Box(0, -2, blob, new Vec2f(32, 15), Color.YELLOW, mEvent, 2));
        blob.shapes[3].addActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
        blob.shapes[3].collision = false;
        blob.shapes[3].visible = false;
        sprite = new Sprite("sprites\\Mobs\\Blob\\Blob.png", 32, 32);
        sprite.addState(SpriteState.STATIC, new int[]{0});
        spriteControllers = new SpriteController[1];
        spriteControllers[0] = new SpriteController(sprite);
        spriteControllers[0].spritePosOffset = new Vec2f(-32, -16);
        spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
        blob.shapes[0].setSpriteControllers(spriteControllers);
        blob.putActiveActions(MobActions.GUMBA_WALK, true);
        blob.setAI(AIs.GUMBA);
        blob.setWalkAcc(new Vec2f(40 / DELTATIME, 0));
        blob.setJumpAcc(new Vec2f(0, 200 / DELTATIME));
        blob.setWalkSpeed(50);
        //        blob.addSoundSource("sound//mobs//blob//death.ogg", SoundType.DEATH);
        //        blob.addSoundSource("sound//mobs//blob//damage.ogg", SoundType.DAMAGE);
        //        blob.addSoundSource("sound//player//walk.ogg", SoundType.WALK);


        // Create Background
        RENDERER.changeBackground("sprites\\backgrounds\\background.png");

        //		changeResolution(Resolution.SOL1176x664);

        // healthBar
        healthBar = CAMERA.addHudBar("sprites\\interface\\HealthBar.png");
        refreshHealthBar();

        // Spooky testing
        this.setSanity(0.0f);

        //		GAME.changeResolution(Resolution.SOL1024x768);

        // background music
        //        addEvent(new PlaySound(RESOURCE_MANAGER.getSoundBuffer("sound//music//01//main.ogg"), true, true));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Mob.class, new MobSerialization());
        gsonBuilder.registerTypeAdapter(Body.class, new BodySerialization());
        gsonBuilder.registerTypeAdapter(Vec2f.class, new Vec2fSerialization());
        gsonBuilder.registerTypeAdapter(Color.class, new ColorSerialization());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        String json = gson.toJson(player);
        System.out.println(json);
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
