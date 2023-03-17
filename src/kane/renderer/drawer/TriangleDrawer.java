package kane.renderer.drawer;

import java.awt.Color;

import kane.math.Vec2f;
import kane.physics.Physics;
import kane.physics.Shape;
import kane.physics.ShapeType;
import kane.physics.shapes.Box;
import kane.physics.shapes.Polygon;
import kane.renderer.Camera;
import kane.renderer.Drawer;
import kane.renderer.Renderer;

public class TriangleDrawer extends Drawer{

	public TriangleDrawer(Physics physics, Renderer renderer) {
		super(3, physics, renderer);
		// TODO Auto-generated constructor stub
	}
	
	public void drawBodies() {
		countCurrentVertices = 0;
		countCurrentElements = 0;
		// draw bodies
		for (int layer = 1; layer < Shape.MAX_RENDER_LAYER; layer++) {
			for (int i = 0; i < numRenderedShapes; i++) {
				Shape shape = renderedShapes[i];
				if (shape.RENDER_LAYER == layer) {
					// TODO drawSprite
//					if (shape.hasSprite()) {
//						SpriteController[] spriteControllers = shape.getSpriteControllers();
//						for (SpriteController spriteController : spriteControllers) {
//							float scale = spriteController.getScale();
//							if (!g.pause) {
//								spriteController.step();
//							}
//							BufferedImage frame = spriteController.getFrame();
//							Vec2f pos = shape.getAbsPos();
//							Vec2f spriteAbsPos = new Vec2f(pos).add(spriteController.getSpritePosOffset());
////							int posX = (int) spriteAbsPos.getX();
////							int posY = (int) spriteAbsPos.getY();
//							int posX = Scalar.round(spriteAbsPos.getX());
//							int posY = Scalar.round(spriteAbsPos.getY());
//							drawImage(frame, scale, posX, posY, g2d);
//						}

//					if (ShapeType.PLANE.equals(shape.getType())) {
//						Plane plane = (Plane) shape;
//						Vec2f startPoint = transformPosToVertex(plane.getPoint());
//						Vec2f perp = new Vec2f(plane.getNormal()).perpRight();
//						// len won't be accurate because it's calculated as if the plane is
//						// horizontally. Not gonna change it because it's irrelevant for later game.
//						float len = (plane.getLen() - resSpecs.halfWidth) / resSpecs.halfWidth;
//						Vec2f endPoint = transformPosToVertex(new Vec2f(plane.getPoint()).addMult(perp, len));
//						drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(),
//								plane.getColor());
//
//						// draws normal of planes
//						Vec2f center = transformPosToVertex(new Vec2f(startPoint).addMult(perp, len * 0.5f));
//						drawNormal(center, plane.getNormal());
//					}

//					else if (ShapeType.LINESEGMENT.equals(shape.getType())) {
//						LineSegment lineSegment = (LineSegment) shape;
//						Vec2f startPoint = transformPosToVertex(
//								new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosA()));
//						Vec2f endPoint = transformPosToVertex(
//								new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosB()));
//						drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(),
//								lineSegment.getColor());
//					}

					// TODO drawCircle
//					else if (ShapeType.CIRCLE.equals(shape.getType())) {
//						Circle circle = (Circle) shape;
//						drawCircle((int) circle.getAbsPos().getX(), (int) circle.getAbsPos().getY(),
//								(int) circle.getRad(), circle.getColor(), g2d);
//					}

					if (ShapeType.BOX.equals(shape.getType())) {
						Box box = (Box) shape;
						Vec2f absPos = box.getAbsPos();
						Vec2f rad = box.getRad();
						Vec2f point1 = transformPosToVertex(new Vec2f(absPos).sub(rad));
						Vec2f point2 = transformPosToVertex(
								new Vec2f(absPos.getX() + rad.getX(), absPos.getY() - rad.getY()));
						Vec2f point3 = transformPosToVertex(new Vec2f(absPos).add(rad));
						Vec2f point4 = transformPosToVertex(
								new Vec2f(absPos.getX() - rad.getX(), absPos.getY() + rad.getY()));
						drawRect(point1, point2, point3, point4, box.getColor());
					}

					else if (ShapeType.POLYGON.equals(shape.getType())) {
						Polygon pol = (Polygon) shape;
						int numPoints = pol.getNumPoints();
						Vec2f[] points = new Vec2f[numPoints];
						Vec2f absPos = pol.getAbsPos();
						for (int p = 0; p < numPoints; p++) {
							Vec2f pointAbsPos = new Vec2f(pol.getPoint(p)).add(absPos);
							points[p] = transformPosToVertex(pointAbsPos);
						}
						Vec2f center = transformPosToVertex(absPos);
						drawPolygon(points, center, pol.getColor());
					}

//					else if (ShapeType.POINT.equals(shape.getType())) {
//						Point point = (Point) shape;
//						Vec2f absPos = transformPosToVertex(point.getAbsPos());
//						drawLine(absPos.getX(), absPos.getY(), absPos.getX(), absPos.getY(), point.getColor());
//					}
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
		vertices[verticeStartingIndex + 0] = point1.getX();
		vertices[verticeStartingIndex + 1] = point1.getY();
		vertices[verticeStartingIndex + 2] = 0f;

		vertices[verticeStartingIndex + 7] = point2.getX();
		vertices[verticeStartingIndex + 8] = point2.getY();
		vertices[verticeStartingIndex + 9] = 0f;

		vertices[verticeStartingIndex + 14] = point3.getX();
		vertices[verticeStartingIndex + 15] = point3.getY();
		vertices[verticeStartingIndex + 16] = 0f;

		vertices[verticeStartingIndex + 21] = point4.getX();
		vertices[verticeStartingIndex + 22] = point4.getY();
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
		vertices[verticeStartingIndex + 0] = center.getX();
		vertices[verticeStartingIndex + 1] = center.getY();
		vertices[verticeStartingIndex + 2] = 0f;

		vertices[verticeStartingIndex + 3] = red;
		vertices[verticeStartingIndex + 4] = green;
		vertices[verticeStartingIndex + 5] = blue;
		vertices[verticeStartingIndex + 6] = 1f;

		// Point0
		vertices[verticeStartingIndex + 7] = points[0].getX();
		vertices[verticeStartingIndex + 8] = points[0].getY();
		vertices[verticeStartingIndex + 9] = 0f;

		vertices[verticeStartingIndex + 10] = red;
		vertices[verticeStartingIndex + 11] = green;
		vertices[verticeStartingIndex + 12] = blue;
		vertices[verticeStartingIndex + 13] = 1f;

		for (int i = 1; i < points.length; i++) {
			Vec2f point = points[i];

			vertices[verticeStartingIndex + 14 + (i - 1) * VERTEX_SIZE] = point.getX();
			vertices[verticeStartingIndex + 15 + (i - 1) * VERTEX_SIZE] = point.getY();
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

}
