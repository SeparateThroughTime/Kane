package kane.sound;

public enum SoundType{
    WALK(0),
    ATTACK(1),
    JUMP(2),
    STAND(3),
    DAMAGE(4),
    DEATH(5),
    VARIABLE_1(6),
    VARIABLE_2(7),
    VARIABLE_3(8);

    public final int id;

    SoundType(int id){
        this.id = id;
    }
}
