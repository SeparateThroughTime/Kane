package kane.renderer;

/**
 * This is a list of the states a sprite can has.
 */
public enum SpriteState{
    STATIC(0),
    RUNNING_LEFT(1),
    RUNNING_RIGHT(2),
    STANDING_LEFT(3),
    STANDING_RIGHT(4),
    ATTACK_LEFT(5),
    ATTACK_RIGHT(6),
    HIT_LEFT(7),
    HIT_RIGHT(8),
    JUMP_LEFT(9),
    JUMP_RIGHT(10),
    FALL_LEFT(11),
    FALL_RIGHT(12);

    public final int id;

    private SpriteState(int id){
        this.id = id;
    }
}

