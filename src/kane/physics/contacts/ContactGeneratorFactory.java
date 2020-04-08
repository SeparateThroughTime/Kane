package kane.physics.contacts;

import kane.physics.ContactListener;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.ShapeType;
import kane.physics.contacts.generators.BoxBoxContactGenerator;
import kane.physics.contacts.generators.BoxPointContactGenerator;
import kane.physics.contacts.generators.BoxPolygonContactGenerator;
import kane.physics.contacts.generators.CircleBoxContactGenerator;
import kane.physics.contacts.generators.CircleCircleContactGenerator;
import kane.physics.contacts.generators.CirclePointContactGenerator;
import kane.physics.contacts.generators.CirclePolygonContactGenerator;
import kane.physics.contacts.generators.LineSegmentBoxContactGenerator;
import kane.physics.contacts.generators.LineSegmentCircleContactGenerator;
import kane.physics.contacts.generators.LineSegmentPolygonContactGenerator;
import kane.physics.contacts.generators.NoContactGenerator;
import kane.physics.contacts.generators.PlaneBoxContactGenerator;
import kane.physics.contacts.generators.PlaneCircleContactGenerator;
import kane.physics.contacts.generators.PlanePointContactGenerator;
import kane.physics.contacts.generators.PlanePolygonContactGenerator;
import kane.physics.contacts.generators.PointPolygonContactGenerator;
import kane.physics.contacts.generators.PolygonPolygonContactGenerator;

public class ContactGeneratorFactory {
//This class manages all the different Contact Generations between the different Body Types.

	private ContactGenerator[][] generators;
	private ContactListener contactListener;

	public ContactGeneratorFactory(ContactListener contactListener) {
		this.contactListener = contactListener;

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
		generators[ShapeType.BOX.id][ShapeType.POINT.id] = new BoxPointContactGenerator();
		generators[ShapeType.CIRCLE.id][ShapeType.POINT.id] = new CirclePointContactGenerator();
		generators[ShapeType.PLANE.id][ShapeType.POINT.id] = new PlanePointContactGenerator();
		generators[ShapeType.POINT.id][ShapeType.POLYGON.id] = new PointPolygonContactGenerator();
	}

	public void generate(ContactAcceptor acceptor, ShapePair[] shapepairs, int numShapePairs) {
		// This method needs to run every frame. It updates the contacts array.

		for (int i = 0; i < numShapePairs; i++) {
			ShapePair shapePair = shapepairs[i];
			Shape shapeA = shapePair.getShapeA();
			Shape shapeB = shapePair.getShapeB();
			if (shapeA.getType().id > shapeB.getType().id) {
				shapePair.flipShapes();
			}
			if (shapePair.getShapeA().getAABB().overlaps(shapePair.getShapeB().getAABB())) {
				generators[shapePair.getShapeA().getType().id][shapePair.getShapeB().getType().id].generate(shapePair,
						acceptor);
				
				if (shapePair.getContact() != null) {
					if (shapePair.getContact().getDistance() <= 0) {
						contactListener.penetration(shapePair);
						if (!shapePair.isPenetration()) {
							contactListener.penetrated(shapePair);
							shapePair.setPenetration(true);
						}
					} else if (shapePair.isPenetration()) {
						contactListener.separated(shapePair);
						shapePair.setPenetration(false);
					}
				}
			}

		}

	}

}
