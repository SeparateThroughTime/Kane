package kane.physics;

public interface ContactListener {

	//This runs every frame the pair is penetrating
	public void penetration(ShapePair pair);
	
	//This runs once the pair gets from a separation to a penetration
	public void penetrated(ShapePair pair);
	
	//This runs once the pair gets from a penetration to a separation
	public void separated(ShapePair pair);
}
