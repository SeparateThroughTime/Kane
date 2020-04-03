package kane.physics;

public class Material {
	
	private final float density;
	private final float friction;
	
	public Material(float density, float friction) {
		this.density = density;
		this.friction = friction;
		
	}

	public float getDensity() {
		return density;
	}

	public float getFriction() {
		return friction;
	}
	
	
}
