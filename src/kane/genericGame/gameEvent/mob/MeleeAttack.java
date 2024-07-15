package kane.genericGame.gameEvent.mob;

import kane.genericGame.*;
import kane.physics.Shape;
import kane.sound.SoundSource;
import kane.sound.SoundType;

public class MeleeAttack extends GameEvent{

    protected Mob attacker;
    protected Shape attackShape;

    public MeleeAttack(int eventDuration, Mob attacker){
        super(eventDuration);
        this.attacker = attacker;
    }

    @Override
    public void start(){
        attacker.putActiveActions(MobActions.ATTACKING, true);
        attackShape = attacker.getShape(PassiveAttributes.ATTACKING_FIELD);
        attackShape.addActiveAttribute(ActiveAttributes.ATTACKING_FIELD);

        playSound();
    }

    @Override
    public void procedure(){

    }

    @Override
    public void end(){
        attacker.putActiveActions(MobActions.ATTACKING, false);
        attackShape.remActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
    }

    private void playSound(){
        SoundSource soundSource = attacker.getSoundSource(SoundType.ATTACK);
        if (soundSource != null){
            soundSource.play();
        }
    }

}
