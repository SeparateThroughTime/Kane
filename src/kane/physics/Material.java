package kane.physics;

public record Material(float density, float friction){

    @Override
    public float density(){
        return density;
    }

    @Override
    public float friction(){
        return friction;
    }


}
