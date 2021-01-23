package kane.renderer;

import javax.swing.plaf.synth.SynthOptionPaneUI;

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

/**
 * The Renderer renders the game.
 */
public class Renderer {
	
	private ResolutionSpecification resSpecs;
	private float multiplicator;
	private int[] frameBufferData;
	private final Physics physics;
	private Shape[] renderedShapes;
	private int numRenderedShapes;
	private Camera camera;

	public boolean showContacts = true;
	public boolean showAABBs = false;

	public Renderer(ResolutionSpecification resSpecs, int[] frameBufferData, Physics physics) {
		this.resSpecs = resSpecs;
		this.frameBufferData = frameBufferData;
		this.physics = physics;
		this.camera = new Camera(resSpecs);
		this.physics.addBody(camera);
		this.multiplicator = 1;
	}

	/**
	 * Needs to run after Resolution is changed.
	 */
	public void changeResolution() {
		camera.changeResolution();
		multiplicator = (float) resSpecs.height / resSpecs.GAME_HEIGHT;
	}

	public void testingArea() {
		// TODO delete

		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			Vec2f pos = new Vec2f(body.getPos());
			Vec2f coM = body.getCenterOfMass();
			pos.add(coM);
			drawPoint(pos, 3, 0x00ff00);
			for (int j = 0; j < body.getNumShapes(); j++) {
				Shape shape = body.getShape(j);
				Vec2f shapePos = new Vec2f(shape.getAbsPos());
				Vec2f shapecom = shape.getCenterOfMass();
				shapePos.add(shapecom);
				drawPoint(shapePos, 2, 0xff0000);
			}

		}

	}

	public void renderGame() {

		clear(0x000000);
		camera.update();
		chooseRenderedShapes();
		drawBodies();
		displayAABBs();
		displayContacts();

		// TODO delete
//		testingArea();

	}

	/**
	 * Determines which Shapes will be rendered.
	 */
	private void chooseRenderedShapes() {
		numRenderedShapes = 0;
		renderedShapes = new Shape[physics.getNumBodies() * Body.MAX_SHAPES];
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			for (int j = 0; j < body.getNumShapes(); j++) {
				Shape shape = body.getShape(j);
				if (shape.getAABB().overlaps(camera.getWindow())) {
					if (shape.isVisible()) {
						renderedShapes[numRenderedShapes++] = shape;
					}
				}
			}
		}
	}

	/**
	 * fill whole window with color.
	 * 
	 * @param color -0xrrggbb
	 */
	private void clear(int color) {
		for (int i = 0; i < resSpecs.width * resSpecs.height; i++) {
			frameBufferData[i] = color;
		}
	}

	/**
	 * draw all bodies, that are determined to be rendered.
	 */
	private void drawBodies() {
		// draw bodies
		for (int i = 0; i < numRenderedShapes; i++) {
			Shape shape = renderedShapes[i];
			if (shape.hasSprite()) {
				Sprite sprite = shape.getSprite();
				sprite.step();
				int[] frame = sprite.getFrame();
				Vec2f pos = shape.getAbsPos();
				int width = sprite.FRAME_WIDTH * sprite.PIXELS;
				Vec2f spriteAbsPos = new Vec2f(pos).add(shape.getSprite().getSpritePosOffset());
				drawSprite(frame, spriteAbsPos, width);
			} else if (ShapeType.PLANE.equals(shape.getType())) {
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

			else if (ShapeType.LINESEGMENT.equals(shape.getType())) {
				LineSegment lineSegment = (LineSegment) shape;
				Vec2f startPoint = new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosA());
				Vec2f endPoint = new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosB());
				drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(),
						lineSegment.getColor());
			}

			else if (ShapeType.CIRCLE.equals(shape.getType())) {
				Circle circle = (Circle) shape;
				drawCircle(circle.getAbsPos().getX(), circle.getAbsPos().getY(), circle.getRad(), circle.getColor(),
						false);
			}

			else if (ShapeType.BOX.equals(shape.getType())) {
				Box box = (Box) shape;
				drawRect(box, box.getColor(), false);
			}

			else if (ShapeType.POLYGON.equals(shape.getType())) {
				Polygon pol = (Polygon) shape;
				drawPolygon(pol, pol.getColor());
			}

			else if (ShapeType.POINT.equals(shape.getType())) {
				Point point = (Point) shape;
				drawPoint(point.getAbsPos(), 2, point.getColor());
			}

		}
	}

	/**
	 * Display AABBs
	 */
	private void displayAABBs() {
		if (showAABBs) {
			for (int i = 0; i < physics.getNumBodies(); i++) {
				Body body = physics.getBodies(i);
				for (int j = 0; j < body.getNumShapes(); j++) {
					Shape shape = body.getShape(j);
					drawRect(shape.getAABB(), 0x00FF00, false);
				}
			}
		}
	}

	/**
	 * Display contacts.
	 */
	private void displayContacts() {
		if (showContacts) {
			for (int i = 0; i < physics.getNumShapePairs(); i++) {
				ShapePair shapePair = physics.getShapePairs(i);
				Contact contact = shapePair.getContact();
				if (contact != null && shapePair.getShapeA().isVisible() == true
						&& shapePair.getShapeB().isVisible() == true) {
					Vec2f normal = contact.getNormal();
					Vec2f closestPointOnPlane = contact.getPoint();
					Vec2f closestPointOnBox = new Vec2f(closestPointOnPlane).addMult(normal, contact.getDistance());
					drawCircle(closestPointOnPlane.getX(), closestPointOnPlane.getY(), 4f, 0xff00ff, true);
					drawCircle(closestPointOnBox.getX(), closestPointOnBox.getY(), 4f, 0xffff00, true);
//					drawNormal(closestPointOnPlane, normal);
				}
			}
		}
	}

	/**
	 * draw sprite.
	 * 
	 * @param frame
	 * @param pos
	 */
	private void drawSprite(int[] frame, Vec2f pos, int width) {
		int frameLen = frame.length;
		for (int i = 0; i < frameLen; i++) {
			int posX = (int) pos.getX();
			int posY = (int) pos.getY();
			int pixel = frame[frameLen - (i + 1)];
			int pixelPosX = posX + (i % width);
			int pixelPosY = posY + (i / width);
			setPixelSave(pixelPosX, pixelPosY, pixel);
		}
	}

	/**
	 * Draw a polygon
	 * 
	 * @param pol
	 * @param color
	 */
	private void drawPolygon(Polygon pol, int color) {
		Vec2f absPos = pol.getAbsPos();
		for (int i = 0; i < pol.getNumPoints(); i++) {
			int j = i == pol.getNumPoints() - 1 ? 0 : i + 1;
			Vec2f pointA = new Vec2f(pol.getPoint(i)).add(absPos);
			Vec2f pointB = new Vec2f(pol.getPoint(j)).add(absPos);
			drawLine(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY(), color);
		}
	}

	/**
	 * Draw a point with x and y
	 * 
	 * @param x
	 * @param y
	 * @param rad
	 * @param color
	 */
	private void drawPoint(float x, float y, int rad, int color) {
		drawRect(x - rad, y - rad, x + rad, y + rad, color, true);
	}

	/**
	 * Draw a point with vector.
	 * 
	 * @param p
	 * @param rad
	 * @param color
	 */
	private void drawPoint(Vec2f p, int rad, int color) {
		drawPoint(p.getX(), p.getY(), rad, color);
	}

	/**
	 * Get Circle Error for correct drawing of circles.
	 * 
	 * @param x
	 * @param y
	 * @param r
	 * @return
	 */
	private float getCircleError(float x, float y, float r) {
		return x * x + y * y - r * r;
	}

	/**
	 * Draw a circle.
	 * 
	 * @param cx
	 * @param cy
	 * @param r
	 * @param color
	 * @param filled
	 */
	private void drawCircle(float cx, float cy, float r, int color, boolean filled) {
		float cxOriginal = cx;
		float cyOriginal = cy;

		cx *= multiplicator;
		cy *= multiplicator;
		r *= multiplicator;
		float x = 0;
		float y = r;
		float f = getCircleError(1f, y - 0.5f, r);

		while (x <= y) {
			if (filled) {
				drawLine(cxOriginal + x, cyOriginal + y, cxOriginal - x, cyOriginal + y, color);
				drawLine(cxOriginal + x, cyOriginal - y, cxOriginal - x, cyOriginal - y, color);
				drawLine(cxOriginal + y, cyOriginal + x, cxOriginal - y, cyOriginal + x, color);
				drawLine(cxOriginal + y, cyOriginal - x, cxOriginal - y, cyOriginal - x, color);
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

	/**
	 * Draw a Rectangle with AABB.
	 * 
	 * @param aabb
	 * @param color
	 * @param filled
	 */
	private void drawRect(AABB aabb, int color, boolean filled) {
		drawRect(aabb.getMin().getX(), aabb.getMin().getY(), aabb.getMax().getX(), aabb.getMax().getY(), color, filled);
	}

	/**
	 * Draw a Rectangle with a Box.
	 * 
	 * @param box
	 * @param color
	 * @param filled
	 */
	private void drawRect(Box box, int color, boolean filled) {
		drawRect(box.getMin().getX(), box.getMin().getY(), box.getMax().getX(), box.getMax().getY(), color, filled);
	}

	/**
	 * Draw a Rectangle with int points.
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param color
	 * @param filled
	 */
	private void drawRect(int x0, int y0, int x1, int y1, int color, boolean filled) {
		// draws a rectangle.
		float x0Original = x0;
		float y0Original = y0;
		float x1Original = x1;
		float y1Original = y1;

		x0 *= multiplicator;
		y0 *= multiplicator;
		x1 *= multiplicator;
		y1 *= multiplicator;

		int maxX = Math.max(x0, x1);
		int minX = Math.min(x0, x1);
		int maxY = Math.max(y0, y1);
		int minY = Math.min(y0, y1);

		// cut parts that are not in window
		maxX = Math.min(resSpecs.width - 1, maxX);
		minX = Math.max(0, minX);
		maxY = Math.min(resSpecs.height - 1, maxY);
		minY = Math.max(0, minY);

		// draw rect
		if (filled) {
			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					setPixel(x, y, color);
				}
			}
		} else {
			drawLine(x0Original, y0Original, x0Original, y1Original, color);
			drawLine(x0Original, y0Original, x1Original, y0Original, color);
			drawLine(x0Original, y1Original, x1Original, y1Original, color);
			drawLine(x1Original, y0Original, x1Original, y1Original, color);
		}
	}

	/**
	 * Draw a Rectangle with float points.
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param color
	 * @param filled
	 */
	private void drawRect(float x0, float y0, float x1, float y1, int color, boolean filled) {
		drawRect(Scalar.round(x0), Scalar.round(y0), Scalar.round(x1), Scalar.round(y1), color, filled);
	}

	/**
	 * Draw a line with floats.
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param color
	 */
	private void drawLine(float x0, float y0, float x1, float y1, int color) {
		drawLine(Scalar.round(x0), Scalar.round(y0), Scalar.round(x1), Scalar.round(y1), color);
	}

	/**
	 * Draw a Line with integers.
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param color
	 */
	private void drawLine(int x0, int y0, int x1, int y1, int color) {
		// draws a line.

		x0 *= multiplicator;
		y0 *= multiplicator;
		x1 *= multiplicator;
		y1 *= multiplicator;

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

	/**
	 * Set a pixel to a specific color.
	 * 
	 * @param x
	 * @param y
	 * @param color -0xrrggbb
	 */
	private void setPixel(int x, int y, int color) {
		x *= multiplicator;
		x -= camera.zeroPoint.getX();
		x *= multiplicator;
		x -= camera.zeroPoint.getY();
		int index = Scalar.getY(y, resSpecs.height) * resSpecs.width + x;
		frameBufferData[index] = color;
	}

	/**
	 * Set a pixel, after checking if pixel is inside of the displayed window. With
	 * ints
	 * 
	 * @param x
	 * @param y
	 * @param color
	 */
	private void setPixelSave(int x, int y, int color) {
		// TODO remove this later. To slow.
		x *= multiplicator;
		x -= camera.zeroPoint.getX();
		x *= multiplicator;
		x -= camera.zeroPoint.getY();
		if (!(x < 0 || y < 0 || x > resSpecs.width - 1 || y > resSpecs.height - 1)) {
			int index = Scalar.getY(y, resSpecs.height) * resSpecs.width + x;
			frameBufferData[index] = color;
		}
	}

	/**
	 * Set a pixel, after checking if pixel is inside of the displayed window. With
	 * floats
	 * 
	 * @param x
	 * @param y
	 * @param color
	 */
	private void setPixelSave(float x, float y, int color) {
		// TODO remove this later. To slow.
		setPixelSave(Scalar.round(x), Scalar.round(y), color);
	}

	/**
	 * draw a normal.
	 * 
	 * @param pos
	 * @param normal
	 */
	private void drawNormal(Vec2f pos, Vec2f normal) {
		pos.mult(multiplicator);

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

	/**
	 * Set the frameBufferData to a new one.
	 * 
	 * @param frameBufferData
	 */
	public void newFrameBufferData(int[] frameBufferData) {
		this.frameBufferData = frameBufferData;
	}

	/**
	 * Get camera.
	 * 
	 * @return
	 */
	public Camera getCamera() {
		return camera;
	}
}
