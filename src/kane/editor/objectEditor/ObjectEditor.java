package kane.editor.objectEditor;

import kane.Kane;
import kane.genericGame.Game;
import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.renderer.Camera;

import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;
import static kane.physics.Physics.PHYSICS;

public class ObjectEditor extends Game{

    public static ObjectEditor EDITOR;
    public static void main(String[] args){
        EDITOR = new ObjectEditor("Kane - Object Editor");
        EDITOR.run();

    }

    Body shapeMenu;
    Body attributeMenu;

    public ObjectEditor(String title){
        super(title);
    }

    @Override
    protected void initGame(){
        Camera.initializeCamera(false);

        PHYSICS.isGravityOn = false;

        createShapeMenu();
        createAttributeMenu();
    }

    private void createShapeMenu(){
        Vec2f center = Scalar.screenPercentToPseudoUnit(new Vec2f(50, 90));
        Vec2f radius = Scalar.screenPercentToPseudoUnit(new Vec2f(50, 10));
        shapeMenu = new Body(center);
//        shapeMenu.addShape(new Box(center.x, center.y));

    }

    private void createAttributeMenu(){

    }

    @Override
    protected void mechanicsLoop(){

    }

    @Override
    protected void postMechanicsLoops(){

    }

    @Override
    public void leftArrowClick(){

    }

    @Override
    public void leftArrowPressed(){

    }

    @Override
    public void leftArrowReleased(){

    }

    @Override
    public void rightArrowClick(){

    }

    @Override
    public void rightArrowPressed(){

    }

    @Override
    public void rightArrowReleased(){

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
    public void downArrowClick(){

    }

    @Override
    public void downArrowPressed(){

    }

    @Override
    public void downArrowReleased(){

    }

    @Override
    public void f1Click(){

    }

    @Override
    public void f2Click(){

    }

    @Override
    public void f3Click(){

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
    public void cPressed(){

    }

    @Override
    public void cReleased(){

    }

    @Override
    public void cClick(){

    }

    @Override
    public void spacePressed(){

    }

    @Override
    public void spaceReleased(){

    }

    @Override
    public void spaceClick(){

    }

    @Override
    public void shiftPressed(){

    }

    @Override
    public void shiftReleased(){

    }

    @Override
    public void shiftClick(){

    }

    @Override
    public void escClick(){

    }

    @Override
    public void iPressed(){

    }

    @Override
    public void iReleased(){

    }

    @Override
    public void iClick(){

    }

    @Override
    public void leftMousePressed(){

    }

    @Override
    public void leftMouseReleased(){

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
