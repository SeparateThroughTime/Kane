package kane.genericGame.gameEvent.animation;

import kane.genericGame.GameEvent;
import kane.renderer.SpriteController;

public class AnimationStep extends GameEvent{

    private final SpriteController animatedSprite;

    public AnimationStep(SpriteController animatedSprite){
        super(2);
        this.animatedSprite = animatedSprite;
    }

    @Override
    public void start(){

    }

    @Override
    public void procedure(){
        animatedSprite.step();
        reduceFrameCounter();

    }

    @Override
    public void end(){

    }

}
