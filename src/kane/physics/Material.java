package kane.physics;

/**
 * Every Shape has a Material. The material is relevant for mass and friction of the Shape.
 */
public class Material {
	
	private final float density;
	private final float friction;
	
	/**
	 * 
	 * @param density -for calculation mass. High density -> High mass
	 * @param friction -determines how clingy the shape is.
	 */
	public Material(float density, float friction) {
		this.density = density;
		this.friction = friction;
		
	}

	/**
	 * Get density.
	 * @return
	 */
	public float getDensity() {
		return density;
	}

	/**
	 * Get friction.
	 * @return
	 */
	public float getFriction() {
		return friction;
	}
	
	
}
