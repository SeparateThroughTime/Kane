package kane.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import kane.genericGame.Game;
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
public class Renderer extends JPanel {

	private ResolutionSpecification resSpecs;
	private float multiplicator;
	private final Physics physics;
	private final Game g;
	private Shape[] renderedShapes;
	private int numRenderedShapes;
	private Camera camera;
	private Background background;

	public boolean showContacts = false;
	public boolean showAABBs = false;

	public Renderer(ResolutionSpecification resSpecs, Physics physics, Game g) {
		this.resSpecs = resSpecs;
		this.physics = physics;
		this.g = g;
		this.camera = new Camera(resSpecs, g);
		this.physics.addBody(camera);
		this.multiplicator = 1f;
		setFocusable(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.BLACK);
		Graphics2D g2d = (Graphics2D) g;

		camera.update();
		drawBackground(g2d);
		chooseRenderedShapes();
		drawBodies(g2d);
		displayAABBs(g2d);
		displayContacts(g2d);
	}

	private void drawBackground(Graphics2D g2d) {
		if (background != null) {
			int width = (int) (background.getImg().getWidth() * multiplicator);
			int height = (int) (background.getImg().getHeight() * multiplicator);
			int x = background.getOffsetX();
			while (x < resSpecs.gameWidth) {
				g2d.drawImage(background.getImg(), x, 0, width, height, null);
				x += width;
			}
		}
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

	}

	public void renderGame() {
		repaint();

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
			if (!body.isRemoved()) {
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
	}

	/**
	 * draw all bodies, that are determined to be rendered.
	 * 
	 * @param cox: Camera Offset X
	 * @param coy: Camera Offset Y
	 */
	private void drawBodies(Graphics2D g2d) {
		// draw bodies
		for (int layer = 1; layer < Shape.MAX_RENDER_LAYER; layer++) {
			for (int i = 0; i < numRenderedShapes; i++) {
				Shape shape = renderedShapes[i];
				if (shape.RENDER_LAYER == layer) {
					if (shape.hasSprite()) {
						SpriteController[] spriteControllers = shape.getSpriteControllers();
						for (SpriteController spriteController : spriteControllers) {
							float scale = spriteController.getScale();
							if (!g.pause) {
								spriteController.step();
							}
							BufferedImage frame = spriteController.getFrame();
							Vec2f pos = shape.getAbsPos();
							Vec2f spriteAbsPos = new Vec2f(pos).add(spriteController.getSpritePosOffset());
							int posX = (int) spriteAbsPos.getX();
							int posY = (int) spriteAbsPos.getY();
							drawImage(frame, scale, posX, posY, g2d);
						}

					} else if (ShapeType.PLANE.equals(shape.getType())) {
						Plane plane = (Plane) shape;
						// draws planes
						Vec2f startPoint = plane.getPoint();
						Vec2f perp = new Vec2f(plane.getNormal()).perpRight();
						Vec2f endPoint = new Vec2f(startPoint).addMult(perp, plane.getLen());
						drawLine((int) startPoint.getX(), (int) startPoint.getY(), (int) endPoint.getX(),
								(int) endPoint.getY(), plane.getColor(), g2d);

						// draws normal of planes
						Vec2f center = new Vec2f(startPoint).addMult(perp, plane.getLen() * 0.5f);
						drawNormal(center, plane.getNormal(), g2d);
					}

					else if (ShapeType.LINESEGMENT.equals(shape.getType())) {
						LineSegment lineSegment = (LineSegment) shape;
						Vec2f startPoint = new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosA());
						Vec2f endPoint = new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosB());
						drawLine((int) startPoint.getX(), (int) startPoint.getY(), (int) endPoint.getX(),
								(int) endPoint.getY(), lineSegment.getColor(), g2d);
					}

					else if (ShapeType.CIRCLE.equals(shape.getType())) {
						Circle circle = (Circle) shape;
						drawCircle((int) circle.getAbsPos().getX(), (int) circle.getAbsPos().getY(),
								(int) circle.getRad(), circle.getColor(), g2d);
					}

					else if (ShapeType.BOX.equals(shape.getType())) {
						Box box = (Box) shape;
						drawRect((int) (box.getAbsPos().getX() - box.getRad().getX()),
								(int) (box.getAbsPos().getY() + box.getRad().getY()), (int) box.getRad().getX() * 2,
								(int) box.getRad().getY() * 2, box.getColor(), g2d);
					}

					else if (ShapeType.POLYGON.equals(shape.getType())) {
						Polygon pol = (Polygon) shape;
						int numPoints = pol.getNumPoints();
						int[] xPoints = new int[numPoints];
						int[] yPoints = new int[numPoints];
						Vec2f absPos = pol.getAbsPos();
						for (int p = 0; p < numPoints; p++) {
							Vec2f pointAbsPos = new Vec2f(pol.getPoint(p)).add(absPos);
							xPoints[p] = (int) pointAbsPos.getX();
							yPoints[p] = (int) pointAbsPos.getY();
						}
						drawPolygon(xPoints, yPoints, pol.getColor(), g2d);
					}

					else if (ShapeType.POINT.equals(shape.getType())) {
						Point point = (Point) shape;
						drawLine((int) point.getAbsPos().getX(), (int) point.getAbsPos().getY(),
								(int) point.getAbsPos().getX(), (int) point.getAbsPos().getY(), point.getColor(), g2d);
					}
				}
			}
		}
	}

	/**
	 * Display AABBs
	 */
	private void displayAABBs(Graphics2D g2d) {
		if (showAABBs) {
			for (int i = 0; i < physics.getNumBodies(); i++) {
				Body body = physics.getBodies(i);
				for (int j = 0; j < body.getNumShapes(); j++) {
					Shape shape = body.getShape(j);
					AABB aabb = shape.getAABB();
					Vec2f diameter = new Vec2f(aabb.getMax()).sub(aabb.getMin());
					drawRect((int) aabb.getMin().getX(), (int) aabb.getMax().getY(), (int) diameter.getX(),
							(int) diameter.getY(), Color.GREEN, g2d);
				}
			}
		}
	}

	/**
	 * Display contacts.
	 */
	private void displayContacts(Graphics2D g2d) {
		if (showContacts) {
			for (int i = 0; i < physics.getNumShapePairs(); i++) {
				ShapePair shapePair = physics.getShapePairs(i);
				Contact contact = shapePair.getContact();
				if (contact != null && shapePair.getShapeA().isVisible() == true
						&& shapePair.getShapeB().isVisible() == true) {
					Vec2f normal = contact.getNormal();
					Vec2f closestPointOnPlane = contact.getPoint();
					Vec2f closestPointOnBox = new Vec2f(closestPointOnPlane).addMult(normal, contact.getDistance());
					fillCircle((int) closestPointOnPlane.getX(), (int) closestPointOnPlane.getY(), 4, Color.PINK, g2d);
					fillCircle((int) closestPointOnBox.getX(), (int) closestPointOnBox.getY(), 4, Color.YELLOW, g2d);
				}
			}
		}
	}

	private void drawImage(BufferedImage img, float scale, int posX, int posY, Graphics2D g2d) {
		posX -= Scalar.round(camera.zeroPoint.getX());
		posY -= Scalar.round(camera.zeroPoint.getY());
		int width = (int) (img.getWidth() * multiplicator * Sprite.SCALE * scale);
		int height = (int) (img.getHeight() * multiplicator * Sprite.SCALE * scale);
		posY += img.getHeight() * Sprite.SCALE * scale;
		posX = (int) (posX * multiplicator);
		posY = (int) (posY * multiplicator);
		posY = Scalar.getY(posY, resSpecs.height);
		g2d.drawImage(img, posX, posY, width, height, null);
	}

	private void drawLine(int x1, int y1, int x2, int y2, Color color, Graphics2D g2d) {
		x1 -= Scalar.round(camera.zeroPoint.getX());
		x2 -= Scalar.round(camera.zeroPoint.getX());
		y1 -= Scalar.round(camera.zeroPoint.getY());
		y2 -= Scalar.round(camera.zeroPoint.getY());
		x1 = (int) (x1 * multiplicator);
		x2 = (int) (x2 * multiplicator);
		y1 = (int) (y1 * multiplicator);
		y2 = (int) (y2 * multiplicator);
		y1 = Scalar.getY(y1, resSpecs.height);
		y2 = Scalar.getY(y2, resSpecs.height);
		g2d.setColor(color);
		g2d.drawLine(x1, y1, x2, y2);
	}

	private void drawCircle(int x, int y, int rad, Color color, Graphics2D g2d) {
		x -= Scalar.round(camera.zeroPoint.getX());
		y -= Scalar.round(camera.zeroPoint.getY());
		x -= rad;
		y += rad;
		x = (int) (x * multiplicator);
		y = (int) (y * multiplicator);
		rad = (int) (rad * multiplicator);
		y = Scalar.getY(y, resSpecs.height);
		g2d.setColor(color);
		g2d.drawOval(x, y, rad * 2, rad * 2);
	}

	private void fillCircle(int x, int y, int rad, Color color, Graphics2D g2d) {
		x -= Scalar.round(camera.zeroPoint.getX());
		y -= Scalar.round(camera.zeroPoint.getY());
		x -= rad;
		y += rad;
		x = (int) (x * multiplicator);
		y = (int) (y * multiplicator);
		rad = (int) (rad * multiplicator);
		y = Scalar.getY(y, resSpecs.height);
		g2d.setColor(color);
		g2d.fillOval(x, y, rad * 2, rad * 2);
	}

	private void drawRect(int x, int y, int width, int height, Color color, Graphics2D g2d) {
		x -= Scalar.round(camera.zeroPoint.getX());
		y -= Scalar.round(camera.zeroPoint.getY());
		x = (int) (x * multiplicator);
		y = (int) (y * multiplicator);
		width = (int) (width * multiplicator);
		height = (int) (height * multiplicator);
		y = Scalar.getY(y, resSpecs.height);
		g2d.setColor(color);
		g2d.drawRect(x, y, width, height);
	}

	private void drawPolygon(int[] xs, int[] ys, Color color, Graphics2D g2d) {
		g2d.setColor(color);
		for (int i = 0; i < xs.length; i++) {
			xs[i] -= Scalar.round(camera.zeroPoint.getX());
			xs[i] = (int) (xs[i] * multiplicator);
		}
		for (int i = 0; i < ys.length; i++) {
			ys[i] -= Scalar.round(camera.zeroPoint.getY());
			ys[i] = (int) (ys[i] * multiplicator);
			ys[i] = Scalar.getY(ys[i], resSpecs.height);
		}
		g2d.drawPolygon(xs, ys, xs.length);
	}

	/**
	 * draw a normal.
	 * 
	 * @param pos
	 * @param normal
	 */
	private void drawNormal(Vec2f pos, Vec2f normal, Graphics2D g2d) {
		pos.mult(multiplicator);

		int arrowRadLen = 15;
		int arrowRadWid = 15;
		int nLen = 40;
		Vec2f perp = new Vec2f(normal).perpRight();

		Vec2f arrowTip = new Vec2f(pos).addMult(normal, nLen);
		Vec2f leftArmPos = new Vec2f(arrowTip).addMult(perp, -arrowRadLen).addMult(normal, -arrowRadWid);
		Vec2f rightArmPos = new Vec2f(arrowTip).addMult(perp, arrowRadLen).addMult(normal, -arrowRadWid);

		drawLine((int) pos.getX(), (int) pos.getY(), (int) arrowTip.getX(), (int) arrowTip.getY(), Color.WHITE, g2d);
		drawLine((int) arrowTip.getX(), (int) arrowTip.getY(), (int) leftArmPos.getX(), (int) leftArmPos.getY(),
				Color.WHITE, g2d);
		drawLine((int) arrowTip.getX(), (int) arrowTip.getY(), (int) rightArmPos.getX(), (int) rightArmPos.getY(),
				Color.WHITE, g2d);
	}

	/**
	 * Get camera.
	 * 
	 * @return
	 */
	public Camera getCamera() {
		return camera;
	}

	public void changeBackground(File file) {
		background = new Background(file, resSpecs.GAME_HEIGHT);
	}

	public Background getGameBackground() {
		return background;
	}
}
