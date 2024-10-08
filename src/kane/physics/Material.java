package kane.physics;

public record Material(float density, float friction){

    public final static Material STATIC = new Material(0, 1f);
    public final static Material DYNAMIC = new Material(1, 0.9f);
    public final static Material EVENT = new Material(0, 0);
    public final static Material INTERFACE = new Material(1, 0);

    @Override
    public float density(){
        return density;
    }

    @Override
    public float friction(){
        return friction;
    }


}
