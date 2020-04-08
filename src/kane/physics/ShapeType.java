package kane.physics;

public enum ShapeType {
//Different Types for Shapes
	
	//static shapes
	PLANE(0),
	LINESEGMENT(1),
	
	//moving shapes
	CIRCLE(2),
	BOX(3),
	POINT(4),
	POLYGON(5);
	
	public int id;
	
	private ShapeType(int id) {
		this.id = id;
	}
}
