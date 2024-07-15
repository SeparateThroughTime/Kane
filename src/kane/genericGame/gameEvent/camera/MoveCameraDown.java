package kane.genericGame.gameEvent.camera;

import static kane.renderer.Camera.CAMERA;

import kane.genericGame.GameEvent;

public class MoveCameraDown extends GameEvent{


    public MoveCameraDown(){
        super(1);
    }

    @Override
    public void start(){
        CAMERA.acc.sub(CAMERA.movementAccY);
        if (-CAMERA.vel.y > CAMERA.movementSpeedY){
            CAMERA.vel.y = -CAMERA.movementSpeedY;
        }

    }

    @Override
    public void procedure(){


    }

    @Override
    public void end(){
        // TODO Auto-generated method stub

    }

}