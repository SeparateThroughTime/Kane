package kane.genericGame.gameEvent.mob;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.math.Vec2f;

public class WalkAwayFromPos extends GameEvent{

    private final Mob mob;
    private final Vec2f pos;

    public WalkAwayFromPos(Mob mob, Vec2f pos){
        super(1);
        this.mob = mob;
        this.pos = pos;
    }

    @Override
    public void start(){
        float direction = pos.x - mob.pos.x;
        if (direction < 0){
            mob.walkRight();
        } else{
            mob.walkLeft();
        }
    }

    @Override
    public void procedure(){

    }

    @Override
    public void end(){

    }

}
