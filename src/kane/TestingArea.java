package kane;

import java.awt.Color;

import kane.genericGame.Game;
import kane.math.Scalar;
import kane.math.Vec2f;
import kane.math.Vec2i;
import kane.physics.AABB;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.shapes.Box;
import kane.physics.shapes.Circle;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Plane;
import kane.physics.shapes.Point;
import kane.physics.shapes.Polygon;

public class TestingArea extends Game {

	Material mStatic = new Material(0, 1f);
	Material mDynamic = new Material(1, 0.7f);

	public TestingArea(String title) {
		super(title);
	}

	public static void main(String[] args) {
		TestingArea game = new TestingArea("Kane");
		game.run();

	}

	private int activeScenario = 0;

	private void scene0() {
		// Circle - Plane
		physics.setGravity(new Vec2f(0, 0));

		Body body = new Body(0, 0);
		body.addShape(new Plane(new Vec2f(0, 1), 30, resSpecs.gameWidth - 1, body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(0, -1), -(resSpecs.GAME_HEIGHT - 1) + 30, -(resSpecs.gameWidth - 1), body,
				Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(1, 0), 30, -(resSpecs.GAME_HEIGHT - 1), body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(-1, 0), -(resSpecs.gameWidth - 1) + 30, resSpecs.GAME_HEIGHT - 1, body,
				Color.BLUE, mStatic, 2));
		physics.addBody(body);

		Vec2f points[];

		body = new Body(resSpecs.gameWidth / 2 + 100, resSpecs.GAME_HEIGHT / 2 - 100);
		points = new Vec2f[4];
		points[0] = new Vec2f(-20, -20);
		points[1] = new Vec2f(20, -20);
		points[2] = new Vec2f(20, 20);
		points[3] = new Vec2f(-20, 20);
		body.addShape(new Polygon(30, 0, body, Color.BLUE, points, mDynamic, 2));
//		body.addShape(new Polygon(-30, 0, body, 0x0000ff, 6, 10, mDynamic));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2 - 100);
		points = new Vec2f[5];
		points[0] = new Vec2f(-20, -20);
		points[1] = new Vec2f(20, -25);
		points[2] = new Vec2f(20, 20);
		points[3] = new Vec2f(-20, 40);
		points[4] = new Vec2f(-50, 0);
		body.addShape(new Polygon(30, 0, body, Color.BLUE, points, mDynamic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);
		points = new Vec2f[5];
		points[0] = new Vec2f(-20, -20);
		points[1] = new Vec2f(20, -25);
		points[2] = new Vec2f(20, 20);
		points[3] = new Vec2f(-20, 40);
		points[4] = new Vec2f(-50, 0);
		body.addShape(new Polygon(30, 0, body, Color.BLUE, points, mDynamic, 2));
//		body.addShape(new Polygon(-30, 0, body, 0x0000ff, 6, 10, mDynamic));
//		body.addShape(new Polygon(0, -20, body, 0x0000ff, 3, 7, mDynamic));
		physics.addBody(body);

	}

	private void scene1() {
		// Circle - Line
		physics.setGravity(new Vec2f(0, 0));

		Body body = new Body(0, 0);
		body.addShape(new Plane(new Vec2f(0, 1), 30, resSpecs.gameWidth - 1, body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(0, -1), -(resSpecs.GAME_HEIGHT - 1) + 30, -(resSpecs.gameWidth - 1), body,
				Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(1, 0), 30, -(resSpecs.GAME_HEIGHT - 1), body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(-1, 0), -(resSpecs.gameWidth - 1) + 30, resSpecs.GAME_HEIGHT - 1, body,
				Color.BLUE, mStatic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 + 100, resSpecs.GAME_HEIGHT / 2 + 100);
		body.addShape(new Point(0, 0, body, Color.WHITE, mDynamic, 2));
		physics.addBody(body);

		body = new Body(0, 0);
		body.addShape(new LineSegment(new Vec2f(30, 500), new Vec2f(700, 300), body, Color.GREEN, mStatic, 2));
		body.addShape(new LineSegment(new Vec2f(90, 100), new Vec2f(800, 250), body, Color.GREEN, mStatic, 2));
		physics.addBody(body);
	}

	private void scene2() {
		// Murmelbahn
		physics.setGravity(new Vec2f(0, -10f));

		Body body = new Body(0, 0);
		body.addShape(new Plane(new Vec2f(0, 1), 30, resSpecs.gameWidth - 1, body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(0, -1), -(resSpecs.GAME_HEIGHT - 1) + 30, -(resSpecs.gameWidth - 1), body,
				Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(1, 0), 30, -(resSpecs.GAME_HEIGHT - 1), body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(-1, 0), -(resSpecs.gameWidth - 1) + 30, resSpecs.GAME_HEIGHT - 1, body,
				Color.BLUE, mStatic, 2));
		physics.addBody(body);

		body = new Body(0, 0);
		body.addShape(new LineSegment(new Vec2f(30, 500), new Vec2f(700, 300), body, Color.GREEN, mStatic, 2));
		body.addShape(new LineSegment(new Vec2f(90, 100), new Vec2f(800, 250), body, Color.GREEN, mStatic, 2));
		physics.addBody(body);
	}

	private void scene3() {
		physics.setGravity(new Vec2f(0, 0f));

		Body body = new Body(0, 0);
		body.addShape(new Plane(new Vec2f(0, 1), 30, resSpecs.gameWidth - 1, body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(0, -1), -(resSpecs.GAME_HEIGHT - 1) + 30, -(resSpecs.gameWidth - 1), body,
				Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(1, 0), 30, -(resSpecs.GAME_HEIGHT - 1), body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(-1, 0), -(resSpecs.gameWidth - 1) + 30, resSpecs.GAME_HEIGHT - 1, body,
				Color.BLUE, mStatic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 + 100, resSpecs.GAME_HEIGHT / 2 + 100);
		body.addShape(new Box(0, 0, body, new Vec2f(50, 30), Color.GREEN, mDynamic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 - 100, resSpecs.GAME_HEIGHT / 2 - 100);
		body.addShape(new Box(0, 0, body, new Vec2f(30, 50), Color.RED, mDynamic, 2));
		physics.addBody(body);
	}

	private void scene4() {
		physics.setGravity(new Vec2f(0, 0f));

		Body body = new Body(0, 0);
		body.addShape(new Plane(new Vec2f(0, 1), 30, resSpecs.gameWidth - 1, body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(0, -1), -(resSpecs.GAME_HEIGHT - 1) + 30, -(resSpecs.gameWidth - 1), body,
				Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(1, 0), 30, -(resSpecs.GAME_HEIGHT - 1), body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(-1, 0), -(resSpecs.gameWidth - 1) + 30, resSpecs.GAME_HEIGHT - 1, body,
				Color.BLUE, mStatic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 - 100, resSpecs.GAME_HEIGHT / 2 - 100);
		body.addShape(new Circle(30, 0, 0, Color.RED, body, mDynamic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 + 100, resSpecs.GAME_HEIGHT / 2 + 100);
		body.addShape(new Box(0, 0, body, new Vec2f(50, 30), Color.GREEN, mDynamic, 2));
		physics.addBody(body);

	}

	private void scene5() {
		physics.setGravity(new Vec2f(0, 0));

		Body body = new Body(0, 0);
		body.addShape(new Plane(new Vec2f(0, 1), 30, resSpecs.gameWidth - 1, body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(0.75f, 0.75f), 400, 300, body, Color.BLUE, mStatic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 + 100, resSpecs.GAME_HEIGHT / 2 + 100);
		body.addShape(new Box(0, 0, body, new Vec2f(50, 30), Color.BLUE, mDynamic, 2));
		physics.addBody(body);
	}

	private void scene6() {
		physics.setGravity(new Vec2f(0, 0));

		Body body = new Body(0, 0);
		body.addShape(new Plane(new Vec2f(0, 1), 30, resSpecs.gameWidth - 1, body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(0, -1), -(resSpecs.GAME_HEIGHT - 1) + 30, -(resSpecs.gameWidth - 1), body,
				Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(1, 0), 30, -(resSpecs.GAME_HEIGHT - 1), body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(-1, 0), -(resSpecs.gameWidth - 1) + 30, resSpecs.GAME_HEIGHT - 1, body,
				Color.BLUE, mStatic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 + 100, resSpecs.GAME_HEIGHT / 2 + 100);
		body.addShape(new Box(0, 0, body, new Vec2f(20, 30), Color.GREEN, mDynamic, 2));
		physics.addBody(body);

		body = new Body(0, 0);
		body.addShape(new LineSegment(new Vec2f(150, 100), new Vec2f(120, 500), body, Color.GREEN, mStatic, 2));
		physics.addBody(body);
	}

	private void scene7() {
		physics.setGravity(new Vec2f(0, -10f));

		Body body = new Body(0, 0);
		body.addShape(new Plane(new Vec2f(0, 1), 30, resSpecs.gameWidth - 1, body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(0, -1), -(resSpecs.GAME_HEIGHT - 1) + 30, -(resSpecs.gameWidth - 1), body,
				Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(1, 0), 30, -(resSpecs.GAME_HEIGHT - 1), body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(-1, 0), -(resSpecs.gameWidth - 1) + 30, resSpecs.GAME_HEIGHT - 1, body,
				Color.BLUE, mStatic, 2));
		physics.addBody(body);
	}

	private void scene8() {
		physics.setGravity(new Vec2f(0, 0));

		Body body = new Body(0, 0);
		body.addShape(new Plane(new Vec2f(0, 1), 30, resSpecs.gameWidth - 1, body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(0, -1), -(resSpecs.GAME_HEIGHT - 1) + 30, -(resSpecs.gameWidth - 1), body,
				Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(1, 0), 30, -(resSpecs.GAME_HEIGHT - 1), body, Color.BLUE, mStatic, 2));
		body.addShape(new Plane(new Vec2f(-1, 0), -(resSpecs.gameWidth - 1) + 30, resSpecs.GAME_HEIGHT - 1, body,
				Color.BLUE, mStatic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 + 100, resSpecs.GAME_HEIGHT / 2 - 100);
		Vec2f[] points = new Vec2f[4];
		points[0] = new Vec2f(-20, -20);
		points[1] = new Vec2f(20, -20);
		points[2] = new Vec2f(20, 20);
		points[3] = new Vec2f(-20, 20);
		body.addShape(new Polygon(0, 0, body, Color.YELLOW, points, mDynamic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 + 100, resSpecs.GAME_HEIGHT / 2 - 200);
		points = new Vec2f[5];
		points[0] = new Vec2f(-20, -20);
		points[1] = new Vec2f(20, -25);
		points[2] = new Vec2f(20, 20);
		points[3] = new Vec2f(-20, 40);
		points[4] = new Vec2f(-50, 0);
		body.addShape(new Polygon(0, 0, body, Color.RED, points, mDynamic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 - 100, resSpecs.GAME_HEIGHT / 2 - 100);
		body.addShape(new Circle(30, 0, 0, Color.RED, body, mDynamic, 2));
		physics.addBody(body);

		body = new Body(resSpecs.gameWidth / 2 + 100, resSpecs.GAME_HEIGHT / 2 + 100);
		body.addShape(new Box(0, 0, body, new Vec2f(20, 30), Color.GREEN, mDynamic, 2));
		physics.addBody(body);

		body = new Body(0, 0);
		body.addShape(new LineSegment(new Vec2f(150, 100), new Vec2f(120, 500), body, Color.GREEN, mStatic, 2));
		physics.addBody(body);
	}

	int numScenarios = 9;

	@Override
	public void spaceClick() {
		activeScenario++;
		activeScenario = activeScenario >= numScenarios ? 0 : activeScenario;
		physics.clearBodies();

		if (activeScenario == 0) {
			scene0();
		}
		if (activeScenario == 1) {
			scene1();
		}
		if (activeScenario == 2) {
			scene2();
		}
		if (activeScenario == 3) {
			scene3();
		}
		if (activeScenario == 4) {
			scene4();
		}
		if (activeScenario == 5) {
			scene5();
		}
		if (activeScenario == 6) {
			scene6();
		}
		if (activeScenario == 7) {
			scene7();
		}
		if (activeScenario == 8) {
			scene8();
		}
	}

	@Override
	protected void initGame() {
//		Body camera = new Body(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);
//		camera.addShape(new Point(0, 0, camera, 0x000000, mStatic));
//		camera.getShape(0).setCollision(false);
//		camera.getShape(0).setVisible(false);
//		physics.addBody(camera);
		scene0();

	}

	private Vec2i dragStart = new Vec2i();
	private Body dragObj = null;
	private boolean dragging = false;

	@Override
	public void leftMousePressed() {
		if (!dragging) {
			dragObj = null;
			for (int i = 0; i < physics.getNumBodies(); i++) {
				Body body = physics.getBodies(i);
				if (body.getImpulseRate() > 0) {
					for (int j = 0; j < body.getNumShapes(); j++) {
						Shape shape = body.getShape(j);
						AABB aabb = shape.getAABB();
						if (Scalar.isPointInRect(mouseListener.getMousePos().getX(), mouseListener.getMousePos().getY(),
								aabb.getMin().getX(), aabb.getMin().getY(), aabb.getMax().getX(),
								aabb.getMax().getY())) {
							dragging = true;
							dragStart.set(mouseListener.getMousePos());
							dragObj = shape.getBody();
							break;

						}
					}
				}
			}
		} else

		{
			if (dragObj != null) {
				dragObj.getVel().zero();
				dragObj.getAcc().zero();
				int dx = mouseListener.getMousePos().getX() - dragStart.getX();
				int dy = mouseListener.getMousePos().getY() - dragStart.getY();
				dragObj.getPos().add(new Vec2f(dx, dy));
				dragStart.set(mouseListener.getMousePos());
			}
		}
	}

	@Override
	public void leftMouseReleased() {
		dragObj = null;
		dragging = false;
	}

	@Override
	public void leftMouseClick() {
		if (dragObj == null) {
			final int radius = 60;
			final int numX;
			final int numY;
			if (!switchAmount) {
				numX = 1;
				numY = 1;
			} else {
				numX = 9;
				numY = 9;
			}
			final int halfDimX = radius * numX;
			final int halfDimY = radius * numY;

			for (int x = 0; x < numX; x++) {
				for (int y = 0; y < numY; y++) {
					Body body = new Body(mouseListener.getMousePos().getX() - halfDimX + x * radius * 2,
							mouseListener.getMousePos().getY() - halfDimY + y * radius * 2);
					body.addShape(new Circle(radius, 0, 0, Color.RED, body, mDynamic, 2));
					physics.addBody(body);
				}
			}

		}

	}

	@Override
	public void rightMousePressed() {

	}

	@Override
	public void rightMouseReleased() {

	}

	@Override
	public void rightMouseClick() {
		if (dragObj == null) {
			final int radius = 10;
			final int numX;
			final int numY;
			if (!switchAmount) {
				numX = 1;
				numY = 1;
			} else {
				numX = 9;
				numY = 9;
			}
			final int halfDimX = radius * numX;
			final int halfDimY = radius * numY;

			for (int x = 0; x < numX; x++) {
				for (int y = 0; y < numY; y++) {
					Body body = new Body(mouseListener.getMousePos().getX() - halfDimX + x * radius * 2,
							mouseListener.getMousePos().getY() - halfDimY + y * radius * 2);
					body.addShape(new Box(0, 0, body, new Vec2f(radius, radius), Color.GREEN, mDynamic, 2));
					physics.addBody(body);
				}
			}

		}

	}

	@Override
	public void leftArrowPressed() {
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			if (body.getImpulseRate() > 0) {
				body.getAcc().sub(new Vec2f(10 / DELTATIME, 0));

			}
		}

	}

	@Override
	public void leftArrowReleased() {

	}

	@Override
	public void rightArrowPressed() {
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			if (body.getImpulseRate() > 0) {
				body.getAcc().add(new Vec2f(10 / DELTATIME, 0));

			}
		}

	}

	@Override
	public void rightArrowReleased() {

	}

	@Override
	public void upArrowPressed() {
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			if (body.getImpulseRate() > 0) {
				body.getAcc().add(new Vec2f(0, 10 / DELTATIME));

			}
		}

	}

	@Override
	public void upArrowReleased() {

	}

	@Override
	public void downArrowPressed() {
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			if (body.getImpulseRate() > 0) {
				body.getAcc().sub(new Vec2f(0, 10 / DELTATIME));

			}
		}

	}

	@Override
	public void downArrowReleased() {

	}

	private boolean switchAmount = false;

	@Override
	public void f1Click() {
		switchAmount = !switchAmount;

	}

	@Override
	// show Contacts
	public void f2Click() {
		renderer.showContacts = !renderer.showContacts;

	}

	@Override
	// show AABBs
	public void f3Click() {
		renderer.showAABBs = !renderer.showAABBs;

	}

	@Override
	public void f4Click() {
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			body.align();
		}
	}

	@Override
	public void f5Click() {

	}

	@Override
	public void f6Click() {

	}

	@Override
	public void f7Click() {

	}

	@Override
	public void f8Click() {

	}

	@Override
	public void f9Click() {
	}

	@Override
	public void f10Click() {
	}

	@Override
	public void f11Click() {
	}

	@Override
	public void f12Click() {
	}

	@Override
	public void spacePressed() {
	}

	@Override
	public void spaceReleased() {

	}

	@Override
	public void shiftPressed() {

	}

	@Override
	public void shiftReleased() {

	}

	@Override
	public void shiftClick() {
		activeScenario--;
		spaceClick();

	}

	@Override
	public void escClick() {

	}

	@Override
	public void cPressed() {
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			body.rotate(0.001f);
		}

	}

	@Override
	public void cReleased() {

	}

	@Override
	public void cClick() {

	}

	@Override
	public void penetration(ShapePair pair) {

	}

	@Override
	public void penetrated(ShapePair pair) {

	}

	@Override
	public void separated(ShapePair pair) {

	}

	@Override
	protected void mechanicsLoop() {
		Body camera = renderer.getCamera();
		camera.getPos().set(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);
		camera.getAcc().set(0, 0);
		camera.getVel().set(0, 0);
	}

	@Override
	public void iPressed() {

	}

	@Override
	public void iReleased() {

	}

	@Override
	public void iClick() {

	}

	@Override
	public void leftArrowClick() {

	}

	@Override
	public void rightArrowClick() {

	}

	@Override
	public void upArrowClick() {

	}

	@Override
	public void downArrowClick() {

	}

	@Override
	protected void postMechanicsLoops() {
		// TODO Auto-generated method stub
		
	}
}
