package kane.genericGame.gameEvent.camera;

import static kane.Kane.GAME;
import static kane.renderer.Camera.CAMERA;
import static kane.renderer.ResolutionSpecification.RES_SPECS;

import kane.genericGame.GameEvent;

public class BindCameraToMap extends GameEvent{

    public BindCameraToMap(){
        super(2);
    }

    @Override
    public void start(){
        procedure();


    }

    @Override
    public void procedure(){
        int mapLen = GAME.mapLen;
        int mapHeight = GAME.mapHeight;
        if (CAMERA.pos.x - RES_SPECS.gameWidth * 0.5f < 0){
            CAMERA.pos.x = RES_SPECS.gameWidth * 0.5f;
            CAMERA.acc.x = 0;
            CAMERA.vel.x = 0;
        } else if (CAMERA.pos.x + RES_SPECS.gameWidth * 0.5f > mapLen){
            CAMERA.pos.x = mapLen - RES_SPECS.gameWidth * 0.5f;
            CAMERA.acc.x = 0;
            CAMERA.vel.x = 0;
        }
        if (CAMERA.pos.y - RES_SPECS.GAME_HEIGHT * 0.5f < 0){
            CAMERA.pos.y = RES_SPECS.GAME_HEIGHT * 0.5f;
            CAMERA.acc.y = 0;
            CAMERA.vel.y = 0;
        } else if (CAMERA.pos.y + RES_SPECS.GAME_HEIGHT * 0.5f > mapHeight){
            CAMERA.pos.y = mapHeight - RES_SPECS.GAME_HEIGHT * 0.5f;
            CAMERA.acc.y = 0;
            CAMERA.vel.y = 0;
        }

        reduceFrameCounter();

    }

    @Override
    public void end(){

    }

}
