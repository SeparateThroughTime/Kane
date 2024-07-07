package kane.genericGame.gameEvent.mob;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.genericGame.MobDirection;
import kane.genericGame.PassiveAttributes;
import kane.math.ArrayOperations;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;
import kane.sound.SoundSource;
import kane.sound.SoundType;

public class WalkingRight extends GameEvent{

    private Mob walker;
    private SoundSource soundSource;

    public WalkingRight(Mob walker){
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
    public void procedure(){
        move();
    }

    @Override
    public void end(){
        setMobAttributesEnd();
        stopSound();
    }

    private void refreshBodyDirection(){
        if (ArrayOperations.contains(SpriteController.LEFT_SPRITE_STATES,
                walker.getShape(PassiveAttributes.MOB_ALL).getCurrentSpriteState())){
            walker.mirrorX();
        }
    }

    private void setMobAttributesStart(){
        walker.angle = 0f;
        walker.setDirection(MobDirection.RIGHT);
        walker.getActiveActions().put(MobActions.WALK, true);
        walker.getActiveActions().put(MobActions.STAND, false);
        walker.refreshSpriteStates();
    }

    private void playSound(){
        soundSource = walker.getSoundSource(SoundType.WALK);
        if (soundSource != null){
            soundSource.play();
        }
    }

    private void move(){
        walker.acc.x = walker.getWalkAcc().x;
        if (walker.vel.x > walker.getWalkSpeed()){
            walker.vel.x = walker.getWalkSpeed();
        }
        reduceFrameCounter();
    }

    private void setMobAttributesEnd(){
        walker.getActiveActions().put(MobActions.WALK, false);
        walker.getActiveActions().put(MobActions.STAND, true);
        walker.refreshSpriteStates();
    }

    private void stopSound(){
        if (soundSource != null){
            soundSource.stop();
        }
    }

}
