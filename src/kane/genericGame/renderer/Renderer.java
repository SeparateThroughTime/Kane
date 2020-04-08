package kane.genericGame.renderer;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.AABB;
import kane.physics.Body;
import kane.physics.Physics;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.ShapeType;
import kane.physics.contacts.Contact;
import kane.physics.shapes.Box;
import kane.physics.shapes.Circle;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Plane;
import kane.physics.shapes.Point;
import kane.physics.shapes.Polygon;

public class Renderer {

	private final int WIDTH;
	private final int HEIGHT;
	private int[] frameBufferData;
	private final Physics physics;
	private Shape[] renderedShapes;
	private int numRenderedShapes;
	private AABB window;
	private Vec2f windowRad;
	private Body camera;
	private Vec2f zeroPoint;

	public boolean showContacts = false;
	public boolean showAABBs = false;

	public Renderer(int width, int height, int[] frameBufferData, Physics physics) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.frameBufferData = frameBufferData;
		this.physics = physics;
		this.camera = null;
		this.windowRad = new Vec2f(WIDTH * 0.5f, HEIGHT * 0.5f);
		this.zeroPoint = new Vec2f();
	}

	public boolean testing = false;

	public void testingArea() {
		// TODO delete

		// Declarations
		Circle circleA = (Circle) physics.getBodies(2).getShape(0);
		Polygon poliB = (Polygon) physics.getBodies(1).getShape(0);

	}

	public void renderGame() {

		clear(0x000000);
		updateCamera();
		chooseRenderedBodies();
		drawBodies();
		displayAABBs();
		displayContacts();

		// TODO delete
		if (testing) {
			testingArea();
		}

	}

	private void updateCamera() {
		Vec2f min = new Vec2f(camera.getPos()).sub(windowRad);
		Vec2f max = new Vec2f(camera.getPos()).add(windowRad);
		window = new AABB(min, max);
		zeroPoint = min;
	}

	private void chooseRenderedBodies() {
		numRenderedShapes = 0;
		renderedShapes = new Shape[physics.getNumBodies() * Body.MAX_SHAPES];
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			for (int j = 0; j < body.getNumShapes(); j++) {
				Shape shape = body.getShape(j);
				if (shape.getAABB().overlaps(window)) {
					if (shape.isVisible()) {
						renderedShapes[numRenderedShapes++] = shape;
					}
				}
			}
		}
	}

	private void clear(int color) {
		// all black
		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			frameBufferData[i] = color;
		}
	}

	private void drawBodies() {
		// draw bodies
		for (int i = 0; i < numRenderedShapes; i++) {
			Shape shape = renderedShapes[i];
			if (ShapeType.PLANE.equals(shape.getType())) {
				Plane plane = (Plane) shape;
				// draws planes
				Vec2f startPoint = plane.getPoint();
				Vec2f perp = new Vec2f(plane.getNormal()).perpRight();
				Vec2f endPoint = new Vec2f(startPoint).addMult(perp, plane.getLen());
				drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), plane.getColor());

				// draws normal of planes
				Vec2f center = new Vec2f(startPoint).addMult(perp, plane.getLen() * 0.5f);
				drawNormal(center, plane.getNormal());
			}

			if (ShapeType.LINESEGMENT.equals(shape.getType())) {
				LineSegment lineSegment = (LineSegment) shape;
				Vec2f startPoint = new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosA());
				Vec2f endPoint = new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosB());
				drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(),
						lineSegment.getColor());
			}

			if (ShapeType.CIRCLE.equals(shape.getType())) {
				Circle circle = (Circle) shape;
				drawCircle(circle.getAbsPos().getX(), circle.getAbsPos().getY(), circle.getRad(), circle.getColor(),
						false);
			}

			if (ShapeType.BOX.equals(shape.getType())) {
				Box box = (Box) shape;
				drawRect(box, box.getColor(), false);
			}

			if (ShapeType.POLYGON.equals(shape.getType())) {
				Polygon pol = (Polygon) shape;
				drawPolygon(pol, pol.getColor());
			}

			if (ShapeType.POINT.equals(shape.getType())) {
				Point point = (Point) shape;
				drawPoint(point.getAbsPos(), 2, point.getColor());
			}

		}
	}

	private void displayAABBs() {
		// Displays AABBs
		if (showAABBs)

		{
			for (int i = 0; i < physics.getNumBodies(); i++) {
				Body body = physics.getBodies(i);
				for (int j = 0; j < body.getNumShapes(); j++) {
					Shape shape = body.getShape(j);
					drawRect(shape.getAABB(), 0x00FF00, false);
				}
			}
		}
	}

	private void displayContacts() {
		// Displays contacts
		if (showContacts) {
			for (int i = 0; i < physics.getNumShapePairs(); i++) {
				ShapePair shapePair = physics.getShapePairs(i);
				Contact contact = shapePair.getContact();
				Vec2f normal = contact.getNormal();
				Vec2f closestPointOnPlane = contact.getPoint();
				Vec2f closestPointOnBox = new Vec2f(closestPointOnPlane).addMult(normal, contact.getDistance());
				drawCircle(closestPointOnPlane.getX(), closestPointOnPlane.getY(), 4f, 0xff00ff, true);
				drawCircle(closestPointOnBox.getX(), closestPointOnBox.getY(), 4f, 0xffff00, true);
				drawNormal(closestPointOnPlane, normal);
			}
		}
	}

	private void drawPolygon(Polygon pol, int color) {
		Vec2f absPos = pol.getAbsPos();
		for (int i = 0; i < pol.getNumPoints(); i++) {
			int j = i == pol.getNumPoints() - 1 ? 0 : i + 1;
			Vec2f pointA = new Vec2f(pol.getPoint(i)).add(absPos);
			Vec2f pointB = new Vec2f(pol.getPoint(j)).add(absPos);
			drawLine(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY(), color);
		}
	}

	private void drawPoint(float x, float y, int rad, int color) {
		drawRect(x - rad, y - rad, x + rad, y + rad, color, true);
	}

	private void drawPoint(Vec2f p, int rad, int color) {
		drawPoint(p.getX(), p.getY(), rad, color);
	}

	private float getCircleError(float x, float y, float r) {
		return x * x + y * y - r * r;
	}

	private void drawCircle(float cx, float cy, float r, int color, boolean filled) {
		float x = 0;
		float y = r;
		float f = getCircleError(1f, y - 0.5f, r);

		while (x <= y) {
			if (filled) {
				drawLine(cx + x, cy + y, cx - x, cy + y, color);
				drawLine(cx + x, cy - y, cx - x, cy - y, color);
				drawLine(cx + y, cy + x, cx - y, cy + x, color);
				drawLine(cx + y, cy - x, cx - y, cy - x, color);
			} else {
				setPixelSave(cx + x, cy + y, color);
				setPixelSave(cx - x, cy + y, color);
				setPixelSave(cx + x, cy - y, color);
				setPixelSave(cx - x, cy - y, color);
				setPixelSave(cx + y, cy + x, color);
				setPixelSave(cx - y, cy + x, color);
				setPixelSave(cx + y, cy - x, color);
				setPixelSave(cx - y, cy - x, color);
			}
			x++;
			if (f > 0) {
				y--;
			}
			f = getCircleError(x, y - 0.5f, r);
		}

	}

	private void drawRect(AABB aabb, int color, boolean filled) {
		drawRect(aabb.getMin().getX(), aabb.getMin().getY(), aabb.getMax().getX(), aabb.getMax().getY(), color, filled);
	}

	private void drawRect(Box box, int color, boolean filled) {
		drawRect(box.getMin().getX(), box.getMin().getY(), box.getMax().getX(), box.getMax().getY(), color, filled);
	}

	private void drawRect(int x0, int y0, int x1, int y1, int color, boolean filled) {
		// draws a rectangle.

		int maxX = Math.max(x0, x1);
		int minX = Math.min(x0, x1);
		int maxY = Math.max(y0, y1);
		int minY = Math.min(y0, y1);

		// cut parts that are not in window
		maxX = Math.min(WIDTH - 1, maxX);
		minX = Math.max(0, minX);
		maxY = Math.min(HEIGHT - 1, maxY);
		minY = Math.max(0, minY);

		// draw rect
		if (filled) {
			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					setPixel(x, y, color);
				}
			}
		} else {
			drawLine(x0, y0, x0, y1, color);
			drawLine(x0, y0, x1, y0, color);
			drawLine(x0, y1, x1, y1, color);
			drawLine(x1, y0, x1, y1, color);
		}
	}

	private void drawRect(float x0, float y0, float x1, float y1, int color, boolean filled) {
		drawRect(Scalar.round(x0), Scalar.round(y0), Scalar.round(x1), Scalar.round(y1), color, filled);
	}

	private void drawLine(float x0, float y0, float x1, float y1, int color) {
		drawLine(Scalar.round(x0), Scalar.round(y0), Scalar.round(x1), Scalar.round(y1), color);
	}

	private void drawLine(int x0, int y0, int x1, int y1, int color) {
		// draws a line.

		int dx = x0 - x1;
		int dy = y0 - y1;

		int signX = dx < 0 ? -1 : 1;
		int signY = dy < 0 ? -1 : 1;

		dx = Math.abs(dx);
		dy = Math.abs(dy);

		setPixelSave(x0, y0, color);
		setPixelSave(x1, y1, color);

		if (dx > dy) {
			int err = dx / 2;
			int y = 0;

			for (int x = 0; x < dx; x++) {
				err = err - dy;
				if (err < 0) {
					y++;
					err = err + dx;
				}
				setPixelSave(x1 + x * signX, y1 + y * signY, color);
			}
		} else {
			int err = dy / 2;
			int x = 0;

			for (int y = 0; y < dy; y++) {
				err = err - dx;
				if (err < 0) {
					x++;
					err = err + dy;
				}
				setPixelSave(x1 + x * signX, y1 + y * signY, color);
			}
		}
	}

	private void setPixel(int x, int y, int color) {
		x -= zeroPoint.getX();
		y -= zeroPoint.getY();
		int index = Scalar.getY(y, HEIGHT) * WIDTH + x;
		frameBufferData[index] = color;
	}

	private void setPixelSave(int x, int y, int color) {
		// TODO remove this later. To slow.
		x -= zeroPoint.getX();
		y -= zeroPoint.getY();
		if (!(x < 0 || y < 0 || x > WIDTH - 1 || y > HEIGHT - 1)) {
			int index = Scalar.getY(y, HEIGHT) * WIDTH + x;
			frameBufferData[index] = color;
		}
	}

	private void setPixelSave(float x, float y, int color) {
		// TODO remove this later. To slow.
		setPixelSave(Scalar.round(x), Scalar.round(y), color);
	}

	private void drawNormal(Vec2f pos, Vec2f normal) {
		int arrowRadLen = 15;
		int arrowRadWid = 15;
		int nLen = 40;
		Vec2f perp = new Vec2f(normal).perpRight();

		Vec2f arrowTip = new Vec2f(pos).addMult(normal, nLen);
		Vec2f leftArmPos = new Vec2f(arrowTip).addMult(perp, -arrowRadLen).addMult(normal, -arrowRadWid);
		Vec2f rightArmPos = new Vec2f(arrowTip).addMult(perp, arrowRadLen).addMult(normal, -arrowRadWid);

		drawLine(pos.getX(), pos.getY(), arrowTip.getX(), arrowTip.getY(), 0xFFFFFF);
		drawLine(arrowTip.getX(), arrowTip.getY(), leftArmPos.getX(), leftArmPos.getY(), 0xFFFFFF);
		drawLine(arrowTip.getX(), arrowTip.getY(), rightArmPos.getX(), rightArmPos.getY(), 0xFFFFFF);
	}

	public void setCamera(Body camera) {
		this.camera = camera;
	}
}
