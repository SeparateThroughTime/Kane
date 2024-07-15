package kane.genericGame.gameEvent.camera;

import static kane.renderer.Camera.CAMERA;
import static kane.Kane.GAME;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;


public class MoveCameraRight extends GameEvent{

    private final Mob player;

    public MoveCameraRight(){
        super(1);
        player = GAME.player;
    }

    @Override
    public void start(){
        CAMERA.acc.add(CAMERA.movementAccX);
        if (CAMERA.vel.x > player.getWalkSpeed()){
            CAMERA.vel.x = player.getWalkSpeed();
        }
    }

    @Override
    public void procedure(){

    }

    @Override
    public void end(){
    }

}