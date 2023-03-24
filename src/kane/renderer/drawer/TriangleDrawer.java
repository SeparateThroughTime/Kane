package kane.renderer.drawer;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.awt.Color;

import kane.math.Vec2f;
import kane.physics.Shape;
import kane.physics.ShapeType;
import kane.physics.shapes.Box;
import kane.physics.shapes.Polygon;
import kane.renderer.Drawer;

public class TriangleDrawer extends Drawer {

	private static final int MAX_TRIANGLES = 300;
	public static TriangleDrawer TRIANGLE_DRAWER;
	public static ShapeType[] renderedShapeTypes = { ShapeType.BOX, ShapeType.POLYGON };

	private TriangleDrawer() {
		super(3, GL_TRIANGLES, TriangleDrawer.renderedShapeTypes);
		// TODO Auto-generated constructor stub
	}

	public static void initializateTriangleDrawer() {
		if (TRIANGLE_DRAWER == null) {
			TRIANGLE_DRAWER = new TriangleDrawer();
		}
	}

	public void drawBodies() {
		countCurrentVertices = 0;
		countCurrentElements = 0;
		// draw bodies
		for (int layer = 1; layer < Shape.MAX_RENDER_LAYER; layer++) {
			for (int i = 0; i < numRenderedShapes; i++) {
				Shape shape = renderedShapes[i];
				if (shape.RENDER_LAYER == layer) {
//					if(shape.hasSprite) {
//						// They seem to get in the renderedShapes array. dont know why.
//					}
//					

					// TODO drawCircle
//					else if (ShapeType.CIRCLE.equals(shape.getType())) {
//						Circle circle = (Circle) shape;
//						drawCircle((int) circle.getAbsPos().getX(), (int) circle.getAbsPos().getY(),
//								(int) circle.getRad(), circle.getColor(), g2d);
//					}

					if (ShapeType.BOX.equals(shape.type)) {
						Box box = (Box) shape;
						Vec2f absPos = box.getAbsPos();
						Vec2f rad = box.getRad();
						Vec2f point1 = transformPosToVertex(new Vec2f(absPos).sub(rad));
						Vec2f point2 = transformPosToVertex(new Vec2f(absPos.x + rad.x, absPos.y - rad.y));
						Vec2f point3 = transformPosToVertex(new Vec2f(absPos).add(rad));
						Vec2f point4 = transformPosToVertex(new Vec2f(absPos.x - rad.x, absPos.y + rad.y));
						drawRect(point1, point2, point3, point4, box.color);
					}

					else if (ShapeType.POLYGON.equals(shape.type)) {
						Polygon pol = (Polygon) shape;
						int numPoints = pol.getNumPoints();
						Vec2f[] points = new Vec2f[numPoints];
						Vec2f absPos = pol.getAbsPos();
						for (int p = 0; p < numPoints; p++) {
							Vec2f pointAbsPos = new Vec2f(pol.getPoint(p)).add(absPos);
							points[p] = transformPosToVertex(pointAbsPos);
						}
						Vec2f center = transformPosToVertex(absPos);
						drawPolygon(points, center, pol.color);
					}
				}
			}
		}
	}

	private void drawRect(Vec2f point1, Vec2f point2, Vec2f point3, Vec2f point4, Color color) {
		int verticeStartingIndex = countCurrentVertices * VERTEX_SIZE;
		int elementsStartingIndex = countCurrentElements * ELEMENT_SIZE;

		int rgb = color.getRGB();
		int blue = (rgb & 0x000000FF);
		int green = (rgb & 0x0000FF00) >> 8;
		int red = (rgb & 0x00FF0000) >> 16;

		// Positions
		vertices[verticeStartingIndex + 0] = point1.x;
		vertices[verticeStartingIndex + 1] = point1.y;
		vertices[verticeStartingIndex + 2] = 0f;

		vertices[verticeStartingIndex + 7] = point2.x;
		vertices[verticeStartingIndex + 8] = point2.y;
		vertices[verticeStartingIndex + 9] = 0f;

		vertices[verticeStartingIndex + 14] = point3.x;
		vertices[verticeStartingIndex + 15] = point3.y;
		vertices[verticeStartingIndex + 16] = 0f;

		vertices[verticeStartingIndex + 21] = point4.x;
		vertices[verticeStartingIndex + 22] = point4.y;
		vertices[verticeStartingIndex + 23] = 0f;

		// Colors
		vertices[verticeStartingIndex + 3] = red;
		vertices[verticeStartingIndex + 4] = green;
		vertices[verticeStartingIndex + 5] = blue;
		vertices[verticeStartingIndex + 6] = 1f;

		vertices[verticeStartingIndex + 10] = red;
		vertices[verticeStartingIndex + 11] = green;
		vertices[verticeStartingIndex + 12] = blue;
		vertices[verticeStartingIndex + 13] = 1f;

		vertices[verticeStartingIndex + 17] = red;
		vertices[verticeStartingIndex + 18] = green;
		vertices[verticeStartingIndex + 19] = blue;
		vertices[verticeStartingIndex + 20] = 1f;

		vertices[verticeStartingIndex + 24] = red;
		vertices[verticeStartingIndex + 25] = green;
		vertices[verticeStartingIndex + 26] = blue;
		vertices[verticeStartingIndex + 27] = 1f;

		// Elements
		elements[elementsStartingIndex + 0] = countCurrentVertices + 0;
		elements[elementsStartingIndex + 1] = countCurrentVertices + 1;
		elements[elementsStartingIndex + 2] = countCurrentVertices + 2;

		elements[elementsStartingIndex + 3] = countCurrentVertices + 0;
		elements[elementsStartingIndex + 4] = countCurrentVertices + 2;
		elements[elementsStartingIndex + 5] = countCurrentVertices + 3;

		countCurrentVertices += 4;
		countCurrentElements += 2;
	}

	private void drawPolygon(Vec2f[] points, Vec2f center, Color color) {
		int verticeStartingIndex = countCurrentVertices * VERTEX_SIZE;
		int elementsStartingIndex = countCurrentElements * ELEMENT_SIZE;

		int rgb = color.getRGB();
		int blue = (rgb & 0x000000FF);
		int green = (rgb & 0x0000FF00) >> 8;
		int red = (rgb & 0x00FF0000) >> 16;

		// Center
		vertices[verticeStartingIndex + 0] = center.x;
		vertices[verticeStartingIndex + 1] = center.y;
		vertices[verticeStartingIndex + 2] = 0f;

		vertices[verticeStartingIndex + 3] = red;
		vertices[verticeStartingIndex + 4] = green;
		vertices[verticeStartingIndex + 5] = blue;
		vertices[verticeStartingIndex + 6] = 1f;

		// Point0
		vertices[verticeStartingIndex + 7] = points[0].x;
		vertices[verticeStartingIndex + 8] = points[0].y;
		vertices[verticeStartingIndex + 9] = 0f;

		vertices[verticeStartingIndex + 10] = red;
		vertices[verticeStartingIndex + 11] = green;
		vertices[verticeStartingIndex + 12] = blue;
		vertices[verticeStartingIndex + 13] = 1f;

		for (int i = 1; i < points.length; i++) {
			Vec2f point = points[i];

			vertices[verticeStartingIndex + 14 + (i - 1) * VERTEX_SIZE] = point.x;
			vertices[verticeStartingIndex + 15 + (i - 1) * VERTEX_SIZE] = point.y;
			vertices[verticeStartingIndex + 16 + (i - 1) * VERTEX_SIZE] = 0f;

			vertices[verticeStartingIndex + 17 + (i - 1) * VERTEX_SIZE] = red;
			vertices[verticeStartingIndex + 18 + (i - 1) * VERTEX_SIZE] = green;
			vertices[verticeStartingIndex + 19 + (i - 1) * VERTEX_SIZE] = blue;
			vertices[verticeStartingIndex + 20 + (i - 1) * VERTEX_SIZE] = 1f;

			elements[elementsStartingIndex + 0 + (i - 1) * ELEMENT_SIZE] = countCurrentVertices;
			elements[elementsStartingIndex + 1 + (i - 1) * ELEMENT_SIZE] = countCurrentVertices + i;
			elements[elementsStartingIndex + 2 + (i - 1) * ELEMENT_SIZE] = countCurrentVertices + i + 1;
		}

		// last element
		elements[elementsStartingIndex + 0 + (points.length - 1) * ELEMENT_SIZE] = countCurrentVertices;
		elements[elementsStartingIndex + 1 + (points.length - 1) * ELEMENT_SIZE] = countCurrentVertices + points.length;
		elements[elementsStartingIndex + 2 + (points.length - 1) * ELEMENT_SIZE] = countCurrentVertices + 1;

		countCurrentVertices += points.length + 1;
		countCurrentElements += points.length;
	}

	@Override
	protected void initVerticesAndElements() {
		vertices = new float[VERTEX_SIZE * MAX_TRIANGLES];
		elements = new int[ELEMENT_SIZE * MAX_TRIANGLES];
	}

}
