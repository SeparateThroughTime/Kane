package kane.genericGame;

import kane.physics.Shape;

/**
 * Methods for every relevant combination.
 * First parameter is the activeShape
 * Second parameter is the passiveShape
 */
public interface ContactManagementInterface {
	public void playerTouchCameraLeft(Shape cameraLeft, Shape playerAll);
	public void playerTouchCameraRight(Shape cameraRight, Shape playerAll);
	public void playerTouchCameraUp(Shape cameraUp, Shape playerAll);
	public void playerTouchCameraDown(Shape cameraDown, Shape playerAll);
	public void playerTouchCameraMidX(Shape cameraMidX, Shape playerAll);
	public void playerTouchCameraMidY(Shape cameraMidY, Shape playerAll);
	
	public void playerCollectsSword(Shape sword, Shape playerAll);
	
	public void mobStandsOnPhysical(Shape mobFeet, Shape physical);
	public void mobFeetLeavePhysical(Shape mobFeet, Shape physical);
	public void playerAttacksMob(Shape attackingField, Shape mobAll);
}
