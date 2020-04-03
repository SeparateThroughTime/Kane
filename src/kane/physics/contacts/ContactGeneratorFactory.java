package kane.physics.contacts;

import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.ShapeType;
import kane.physics.contacts.generators.BoxBoxContactGenerator;
import kane.physics.contacts.generators.BoxPolygonContactGenerator;
import kane.physics.contacts.generators.CircleBoxContactGenerator;
import kane.physics.contacts.generators.CircleCircleContactGenerator;
import kane.physics.contacts.generators.CirclePolygonContactGenerator;
import kane.physics.contacts.generators.LineSegmentBoxContactGenerator;
import kane.physics.contacts.generators.LineSegmentCircleContactGenerator;
import kane.physics.contacts.generators.LineSegmentPolygonContactGenerator;
import kane.physics.contacts.generators.NoContactGenerator;
import kane.physics.contacts.generators.PlaneBoxContactGenerator;
import kane.physics.contacts.generators.PlaneCircleContactGenerator;
import kane.physics.contacts.generators.PlanePolygonContactGenerator;
import kane.physics.contacts.generators.PolygonPolygonContactGenerator;

public class ContactGeneratorFactory {
//This class manages all the different Contact Generations between the different Body Types.

	private ContactGenerator[][] generators;

	public ContactGeneratorFactory() {
		int numShapeTypes = ShapeType.values().length;
		generators = new ContactGenerator[numShapeTypes][numShapeTypes];
		for (int i = 0; i < generators.length; i++) {
			for (int j = 0; j < generators[0].length; j++) {
				generators[i][j] = new NoContactGenerator();
			}
		}
		generators[ShapeType.PLANE.id][ShapeType.CIRCLE.id] = new PlaneCircleContactGenerator();
		generators[ShapeType.CIRCLE.id][ShapeType.CIRCLE.id] = new CircleCircleContactGenerator();
		generators[ShapeType.LINESEGMENT.id][ShapeType.CIRCLE.id] = new LineSegmentCircleContactGenerator();
		generators[ShapeType.BOX.id][ShapeType.BOX.id] = new BoxBoxContactGenerator();
		generators[ShapeType.CIRCLE.id][ShapeType.BOX.id] = new CircleBoxContactGenerator();
		generators[ShapeType.PLANE.id][ShapeType.BOX.id] = new PlaneBoxContactGenerator();
		generators[ShapeType.LINESEGMENT.id][ShapeType.BOX.id] = new LineSegmentBoxContactGenerator();
		generators[ShapeType.CIRCLE.id][ShapeType.POLYGON.id] = new CirclePolygonContactGenerator();
		generators[ShapeType.BOX.id][ShapeType.POLYGON.id] = new BoxPolygonContactGenerator();
		generators[ShapeType.LINESEGMENT.id][ShapeType.POLYGON.id] = new LineSegmentPolygonContactGenerator();
		generators[ShapeType.POLYGON.id][ShapeType.POLYGON.id] = new PolygonPolygonContactGenerator();
		generators[ShapeType.PLANE.id][ShapeType.POLYGON.id] = new PlanePolygonContactGenerator();
	}

	public void generate(ContactAcceptor acceptor, ShapePair[] bodypairs, int numBodyPairs) {
		// This method needs to run every frame. It updates the contacts array.

		for (int i = 0; i < numBodyPairs; i++) {
			ShapePair shapePair = bodypairs[i];
			Shape shapeA = shapePair.getShapeA();
			Shape shapeB = shapePair.getShapeB();
			if(shapeA.getType().id > shapeB.getType().id) {
				shapePair.flipShapes();
			}
			if (shapePair.getShapeA().getAABB().overlaps(shapePair.getShapeB().getAABB())) {
				generators[shapePair.getShapeA().getType().id][shapePair.getShapeB().getType().id]
						.generate(shapePair, acceptor);
			}

		}

	}

}
