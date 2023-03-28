package kane.renderer.drawer;

import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static org.lwjgl.opengl.GL11.GL_LINES;

import java.awt.Color;

import kane.math.Vec2f;
import kane.physics.Shape;
import kane.physics.ShapeType;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Plane;
import kane.physics.shapes.Point;
import kane.renderer.Drawer;

public class LineDrawer extends Drawer {

	private static final int MAX_LINES = 300;
	public static LineDrawer LINE_DRAWER;
	public static ShapeType[] renderedShapeTypes = { ShapeType.LINESEGMENT, ShapeType.PLANE, ShapeType.POINT };

	private LineDrawer() {
		super(2, GL_LINES, LineDrawer.renderedShapeTypes);
	}

	public static void initializeLineDrawer() {
		if (LINE_DRAWER == null) {
			LINE_DRAWER = new LineDrawer();
		}
	}

	public void drawBodies() {
		countCurrentVertices = 0;
		countCurrentElements = 0;

		for (int i = 0; i < numRenderedShapes; i++) {
			Shape shape = renderedShapes[i];
//					if(shape.hasSprite) {
//						// They seem to get in the renderedShapes array. dont know why.
//					}

			if (ShapeType.PLANE.equals(shape.type)) {
				Plane plane = (Plane) shape;
				Vec2f startPoint = transformPosToVertex(plane.getPoint());
				Vec2f perp = new Vec2f(plane.getNormal()).perpRight();
				// len won't be accurate because it's calculated as if the plane is
				// horizontally. Not gonna change it because it's irrelevant for later game.
				float len = (plane.getLen() - RES_SPECS.halfWidth) / RES_SPECS.halfWidth;
				Vec2f endPoint = transformPosToVertex(new Vec2f(plane.getPoint()).addMult(perp, len));
				drawLine(startPoint, endPoint, plane.color);

				// draws normal of planes
				Vec2f center = transformPosToVertex(new Vec2f(startPoint).addMult(perp, len * 0.5f));
				drawNormal(center, plane.getNormal());
			}

			else if (ShapeType.LINESEGMENT.equals(shape.type)) {
				LineSegment lineSegment = (LineSegment) shape;
				Vec2f startPoint = transformPosToVertex(
						new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosA()));
				Vec2f endPoint = transformPosToVertex(new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosB()));
				drawLine(startPoint, endPoint, lineSegment.color);
			}

			else if (ShapeType.POINT.equals(shape.type)) {
				Point point = (Point) shape;
				Vec2f absPos = transformPosToVertex(point.getAbsPos());
				drawLine(absPos, absPos, point.color);
			}
		}
	}

	public void drawLine(Vec2f startPoint, Vec2f endPoint, Color color) {
		int verticeStartingIndex = countCurrentVertices * VERTEX_SIZE;
		int elementsStartingIndex = countCurrentElements * ELEMENT_SIZE;

		int rgb = color.getRGB();
		int blue = (rgb & 0x000000FF);
		int green = (rgb & 0x0000FF00) >> 8;
		int red = (rgb & 0x00FF0000) >> 16;

		// Positions
		vertices[verticeStartingIndex + 0] = startPoint.x;
		vertices[verticeStartingIndex + 1] = startPoint.y;
		vertices[verticeStartingIndex + 2] = 0f;

		vertices[verticeStartingIndex + 10] = endPoint.x;
		vertices[verticeStartingIndex + 11] = endPoint.y;
		vertices[verticeStartingIndex + 12] = 0f;

		// Colors
		vertices[verticeStartingIndex + 3] = red;
		vertices[verticeStartingIndex + 4] = green;
		vertices[verticeStartingIndex + 5] = blue;
		vertices[verticeStartingIndex + 6] = 1f;

		vertices[verticeStartingIndex + 13] = red;
		vertices[verticeStartingIndex + 14] = green;
		vertices[verticeStartingIndex + 15] = blue;
		vertices[verticeStartingIndex + 16] = 1f;

		// Elements
		elements[elementsStartingIndex + 0] = countCurrentVertices + 0;
		elements[elementsStartingIndex + 1] = countCurrentVertices + 1;

		countCurrentVertices += 2;
		countCurrentElements += 1;
	}

	public void drawNormal(Vec2f pos, Vec2f normal) {

		float arrowRadLen = 0.025f;
		float arrowRadWid = 0.025f;
		int nLen = 40;
		Vec2f perp = new Vec2f(normal).perpRight();

		Vec2f arrowTip = new Vec2f(pos).addMult(normal, nLen);
		Vec2f leftArmPos = new Vec2f(arrowTip).addMult(perp, -arrowRadLen).addMult(normal, -arrowRadWid);
		Vec2f rightArmPos = new Vec2f(arrowTip).addMult(perp, arrowRadLen).addMult(normal, -arrowRadWid);

		drawLine(pos, arrowTip, Color.WHITE);
		drawLine(arrowTip, leftArmPos, Color.WHITE);
		drawLine(arrowTip, rightArmPos, Color.WHITE);
	}

	@Override
	protected void initVerticesAndElements() {
		vertices = new float[VERTEX_SIZE * MAX_LINES];
		elements = new int[ELEMENT_SIZE * MAX_LINES];
	}
}
