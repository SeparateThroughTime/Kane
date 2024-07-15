package kane.genericGame.gameEvent.mob;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;

public class Fall extends GameEvent{

    private final Mob mob;

    public Fall(Mob mob){
        super(2);
        this.mob = mob;
    }

    @Override
    public void start(){
        mob.putActiveActions(MobActions.FALLING, true);
    }

    @Override
    public void procedure(){
        if (!mob.isOnGround()){
            reduceFrameCounter();
        }
    }

    @Override
    public void end(){
        mob.putActiveActions(MobActions.FALLING, false);
    }
}
