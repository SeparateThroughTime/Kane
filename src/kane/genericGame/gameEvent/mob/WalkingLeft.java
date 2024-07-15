package kane.genericGame.gameEvent.mob;

import kane.genericGame.*;
import kane.math.ArrayOperations;
import kane.renderer.SpriteController;
import kane.sound.SoundSource;
import kane.sound.SoundType;

public class WalkingLeft extends GameEvent{

    private final Mob walker;
    private SoundSource soundSource;

    public WalkingLeft(Mob walker){
        super(2);
        this.walker = walker;
    }

    @Override
    public void start(){
        refreshBodyDirection();
        setMobAttributesStart();
        playSound();
    }

    @Override
    public void end(){
        setMobAttributesEnd();
        stopSound();
    }

    @Override
    public void procedure(){
        move();
        checkPauseSound();
        reduceFrameCounter();
    }

    private void refreshBodyDirection(){
        if (ArrayOperations.contains(SpriteController.RIGHT_SPRITE_STATES,
                walker.getShape(PassiveAttributes.MOB_ALL).getCurrentSpriteState())){
            walker.mirrorX();
        }
    }

    private void setMobAttributesStart(){
        walker.angle = 0f;
        walker.setDirection(MobDirection.LEFT);
        walker.putActiveActions(MobActions.WALK, true);
        walker.putActiveActions(MobActions.STAND, false);
    }

    private void playSound(){
        soundSource = walker.getSoundSource(SoundType.WALK);
        if (soundSource != null){
            soundSource.play();
        }
    }


    private void move(){
        walker.acc.x = -walker.getWalkAcc().x;
        if (-walker.vel.x > walker.getWalkSpeed()){
            walker.vel.x = -walker.getWalkSpeed();
        }
    }

    private void checkPauseSound(){
        if (soundSource == null){
            return;
        }

        if (soundSource.isPlaying()){
            if (!walker.isOnGround()){
                soundSource.stop();
            }
        } else{
            if (walker.isOnGround()){
                soundSource.play();
            }
        }
    }

    private void setMobAttributesEnd(){
        walker.putActiveActions(MobActions.WALK, false);
        walker.putActiveActions(MobActions.STAND, true);
    }

    private void stopSound(){
        if (soundSource != null){
            soundSource.stop();
        }
    }


}
