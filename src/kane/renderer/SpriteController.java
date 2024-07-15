package kane.renderer;

import kane.genericGame.gameEvent.animation.AnimationStep;
import kane.math.Vec2f;

import static kane.Kane.GAME;

public class SpriteController{
    public static final int ANIMATION_RATE = 10;
    public static final SpriteState[] LEFT_SPRITE_STATES =
            {SpriteState.ATTACK_LEFT, SpriteState.RUNNING_LEFT, SpriteState.STANDING_LEFT};
    public static final SpriteState[] RIGHT_SPRITE_STATES =
            {SpriteState.ATTACK_RIGHT, SpriteState.RUNNING_RIGHT, SpriteState.STANDING_RIGHT};

    public SpriteState currentSpriteState;
    public int currentSpriteStateFrameNo;
    public Vec2f spritePosOffset;
    private int frameCounter;
    public Vec2f scale;

    public Sprite sprite;

    private AnimationStep animation;

    public SpriteController(Sprite sprite){
        this.spritePosOffset = new Vec2f();
        this.sprite = sprite;
        this.scale = new Vec2f(1, 1);
    }

    public void startAnimation(){
        animation = new AnimationStep(this);
        GAME.addEvent(animation);
    }

    public void stopAnimation(){
        animation.killEvent();
    }

    public Vec2f[] getFrameTexCoords(){
        return sprite.getTexCoords(currentSpriteState, currentSpriteStateFrameNo);
    }

    public void setCurrentSpriteState(SpriteState state){
        if (!sprite.stateIsAssigned(state)){
            return;
        }

        currentSpriteState = state;
        currentSpriteStateFrameNo = 0;
        frameCounter = 0;
    }

    public void step(){
        if (frameCounter >= ANIMATION_RATE){
            frameCounter = 0;
            stepCurrentSpriteStateFrame();
        }
        frameCounter++;
    }

    private void stepCurrentSpriteStateFrame(){
        currentSpriteStateFrameNo++;
        if (currentSpriteStateFrameNo >= sprite.getFrameCount(currentSpriteState)){
            currentSpriteStateFrameNo = 0;
        }
    }

}
