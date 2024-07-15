package kane.genericGame.gameEvent.mob;

import kane.Kane;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.PassiveAttributes;
import kane.sound.SoundSource;
import kane.sound.SoundType;

public class DamageHandler extends GameEvent{

    Mob attackedMob;
    Mob attackingMob;

    public DamageHandler(Mob attackingMob, Mob attackedMob){
        super(1);
        this.attackedMob = attackedMob;
        this.attackingMob = attackingMob;
    }

    @Override
    public void start(){
        int damage = attackingMob.getDamage();
        attackedMob.hit(damage, attackingMob.pos);

        if (attackedMob.hasShapeWithPassiveAttribute(PassiveAttributes.PLAYER_ALL)){
            Kane.GAME.refreshHealthBar();
        }

        SoundSource soundSource = attackedMob.getSoundSource(SoundType.DAMAGE);
        if (soundSource != null){
            soundSource.play();
        }
    }

    @Override
    public void procedure(){

    }

    @Override
    public void end(){

    }
}
