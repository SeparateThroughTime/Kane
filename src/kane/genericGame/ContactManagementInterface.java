package kane.genericGame;

import kane.physics.Shape;

public interface ContactManagementInterface{
    void playerTouchCameraLeft(Shape cameraLeft, Shape playerAll);

    void playerTouchCameraRight(Shape cameraRight, Shape playerAll);

    void playerTouchCameraUp(Shape cameraUp, Shape playerAll);

    void playerTouchCameraDown(Shape cameraDown, Shape playerAll);

    void playerTouchCameraMidX(Shape cameraMidX, Shape playerAll);

    void playerTouchCameraMidY(Shape cameraMidY, Shape playerAll);

    void playerCollectsSword(Shape sword, Shape playerAll);

    void mobJumpsOnMob(Shape playerFeet, Shape mobAll);

    void mobStandsOnPhysical(Shape mobFeet, Shape physical);

    void mobFeetLeavePhysical(Shape mobFeet, Shape physical);

    void mobAttacksMob(Shape attackingField, Shape attackedMobAll);
}
