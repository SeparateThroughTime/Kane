package kane.physics.contacts;

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

import static kane.physics.Physics.PHYSICS;
import static kane.physics.contacts.ContactListener.CONTACT_LISTENER;

/**
 * The ContactGeneratorFactory manages all the different Contact Generations between the different Body Types.
 */
public class ContactGeneratorFactory {

	public static ContactGeneratorFactory CONTACT_GENERATOR_FACTORY;
	
	private ContactGenerator[][] generators;

	/**
	 * 
	 * @param contactListener
	 */
	private ContactGeneratorFactory() {

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
	
	public static void initializateContactGeneratorFactory() {
		if (CONTACT_GENERATOR_FACTORY == null) {
			CONTACT_GENERATOR_FACTORY = new ContactGeneratorFactory();
		}
	}

	/**
	 * Needs to run every frame. It updates the contacts array.
	 * @param acceptor
	 */
	public void generate(ContactAcceptor acceptor) {
		for (int i = 0; i < PHYSICS.numShapePairs; i++) {
			ShapePair shapePair = PHYSICS.shapePairs[i];
			Shape shapeA = shapePair.shapeA;
			Shape shapeB = shapePair.shapeB;
			if (shapeA.type.id > shapeB.type.id) {
				shapePair.flipShapes();
			}
			if (shapePair.shapeA.aabb.overlaps(shapePair.shapeB.aabb)) {
				generators[shapePair.shapeA.type.id][shapePair.shapeB.type.id].generate(shapePair,
						acceptor);
				
				if (shapePair.contact != null) {
					if (shapePair.contact.distance <= 0) {
						CONTACT_LISTENER.penetrated(shapePair);
						if (!shapePair.penetration) {
							CONTACT_LISTENER.penetration(shapePair);
							shapeA.addColidedShape(shapeB);
							shapeB.addColidedShape(shapeA);
							shapePair.penetration = true;
						}
					} else if (shapePair.penetration) {
						CONTACT_LISTENER.separation(shapePair);
						shapeA.remColidedShape(shapeB);
						shapeB.remColidedShape(shapeA);
						shapePair.penetration = false;
					}
				}
			}

		}

	}

}
