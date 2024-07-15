package kane.genericGame.gameEvent.mob;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobDirection;
import kane.genericGame.PassiveAttributes;

public class GumbaWalk extends GameEvent{

    private final Mob walker;

    public GumbaWalk(Mob walker){
        super(2);
        this.walker = walker;
    }

    @Override
    public void start(){
        walker.walkLeft();

    }

    @Override
    public void procedure(){
        if (walker.getDirection().equals(MobDirection.LEFT)){
            if (walker.getShape(PassiveAttributes.MOB_LEFT).getCollidedShapes(PassiveAttributes.PHYSICAL).length > 0){
                walker.walkRight();
            }
        } else if (walker.getShape(PassiveAttributes.MOB_RIGHT).getCollidedShapes(PassiveAttributes.PHYSICAL).length
                > 0){
            walker.walkLeft();
        }

        reduceFrameCounter();
    }

    @Override
    public void end(){
        walker.stopWalkLeft();
        walker.stopWalkRight();
    }

}
