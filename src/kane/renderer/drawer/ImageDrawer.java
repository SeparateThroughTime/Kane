package kane.renderer.drawer;

import static kane.Kane.GAME;
import static kane.renderer.Renderer.RENDERER;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL46.*;
import static kane.renderer.ResolutionSpecification.RES_SPECS;

import java.awt.Color;
import java.nio.ByteBuffer;

import kane.math.Vec2f;
import kane.physics.Shape;
import kane.renderer.Drawer;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;

public class ImageDrawer extends Drawer {

	public static ImageDrawer IMAGE_DRAWER;

	private ImageDrawer() {
		super(3, GL_TRIANGLES, 2);
		// Don't duplicate Texture if it goes over uv-borders
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		// Pixelate
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	}

	public static void initializeImageDrawer() {
		if (IMAGE_DRAWER == null) {
			IMAGE_DRAWER = new ImageDrawer();
		}
	}

	@Override
	public void drawBodies() {
		countCurrentVertices = 0;
		countCurrentElements = 0;
		// draw bodies
		for (int layer = 1; layer < Shape.MAX_RENDER_LAYER; layer++) {
			for (int i = 0; i < numRenderedShapes; i++) {
				Shape shape = renderedShapes[i];
				SpriteController[] spriteControllers = shape.getSpriteControllers();
				for (SpriteController spriteController : spriteControllers) {
					Sprite sprite = spriteController.sprite;
					float scale = spriteController.scale * Sprite.SCALE;
					if (!GAME.pause) {
						spriteController.step();
					}
					ByteBuffer frame = spriteController.getFrame();
					Vec2f pos = shape.getAbsPos();
					Vec2f spriteAbsPos = new Vec2f(pos).add(spriteController.spritePosOffset);
					int textureId = spriteController.sprite.TEXTURE_ID;
					Vec2f dimension = new Vec2f((float) sprite.FRAME_WIDTH / RES_SPECS.halfGameWidth,
							(float) sprite.FRAME_HEIGHT / RES_SPECS.halfGameHeight)
							.mult(scale * RENDERER.multiplicator);
					drawImage(transformPosToVertex(spriteAbsPos), frame, dimension, textureId, shape.color);
				}
			}
		}
	}

	public void drawImage(Vec2f pos, ByteBuffer frame, Vec2f dimension, int textureID, Color color) {
		// TODO: Delete and optimize
		countCurrentVertices = 0;
		countCurrentElements = 0;
		initVerticesAndElements();

		glBindTexture(GL_TEXTURE_2D, textureID);
		RENDERER.uploadVarToShader("texSampler", 0);
		glActiveTexture(GL_TEXTURE0);
//		glBindTexture(GL_TEXTURE_2D, textureID);

		int verticeStartingIndex = countCurrentVertices * VERTEX_SIZE;
		int elementsStartingIndex = countCurrentElements * ELEMENT_SIZE;

		int rgb = color.getRGB();
		int blue = (rgb & 0x000000FF);
		int green = (rgb & 0x0000FF00) >> 8;
		int red = (rgb & 0x00FF0000) >> 16;

		// Bottom left
		vertices[verticeStartingIndex + 0] = pos.x;
		vertices[verticeStartingIndex + 1] = pos.y;
		vertices[verticeStartingIndex + 2] = 0f;

		vertices[verticeStartingIndex + 3] = red;
		vertices[verticeStartingIndex + 4] = green;
		vertices[verticeStartingIndex + 5] = blue;
		vertices[verticeStartingIndex + 6] = 1f;

		vertices[verticeStartingIndex + 7] = 0;
		vertices[verticeStartingIndex + 8] = 1;

		// Bottom right
		vertices[verticeStartingIndex + 9] = pos.x + dimension.x;
		vertices[verticeStartingIndex + 10] = pos.y;
		vertices[verticeStartingIndex + 11] = 0f;

		vertices[verticeStartingIndex + 12] = red;
		vertices[verticeStartingIndex + 13] = green;
		vertices[verticeStartingIndex + 14] = blue;
		vertices[verticeStartingIndex + 15] = 1f;

		vertices[verticeStartingIndex + 16] = 1;
		vertices[verticeStartingIndex + 17] = 1;

		// Top right
		vertices[verticeStartingIndex + 18] = pos.x + dimension.x;
		vertices[verticeStartingIndex + 19] = pos.y + dimension.y;
		vertices[verticeStartingIndex + 20] = 0f;

		vertices[verticeStartingIndex + 21] = red;
		vertices[verticeStartingIndex + 22] = green;
		vertices[verticeStartingIndex + 23] = blue;
		vertices[verticeStartingIndex + 24] = 1f;

		vertices[verticeStartingIndex + 25] = 1;
		vertices[verticeStartingIndex + 26] = 0;

		// Top left
		vertices[verticeStartingIndex + 27] = pos.x;
		vertices[verticeStartingIndex + 28] = pos.y + dimension.y;
		vertices[verticeStartingIndex + 29] = 0f;

		vertices[verticeStartingIndex + 30] = red;
		vertices[verticeStartingIndex + 31] = green;
		vertices[verticeStartingIndex + 32] = blue;
		vertices[verticeStartingIndex + 33] = 1f;

		vertices[verticeStartingIndex + 34] = 0;
		vertices[verticeStartingIndex + 35] = 0;

		// Elements
		elements[elementsStartingIndex + 0] = countCurrentVertices + 0;
		elements[elementsStartingIndex + 1] = countCurrentVertices + 1;
		elements[elementsStartingIndex + 2] = countCurrentVertices + 2;

		elements[elementsStartingIndex + 3] = countCurrentVertices + 0;
		elements[elementsStartingIndex + 4] = countCurrentVertices + 2;
		elements[elementsStartingIndex + 5] = countCurrentVertices + 3;

		countCurrentVertices += 4;
		countCurrentElements += 2;

		// TODO: Delete and optimize
		displayFrame(RENDERER.shader);

		glBindTexture(GL_TEXTURE_2D, 0);

	}

	// TODO drawBackground
	protected void drawBackground() {
		if (RENDERER.background != null) {
			float width = transformBackgroundXToVertexX(RENDERER.background.WIDTH * RENDERER.multiplicator);
			float height = transformBackgroundYToVertexY(RENDERER.background.HEIGHT * RENDERER.multiplicator);
			int numBackgroundDrawings = RES_SPECS.gameWidth / (int)width + 2;
			int numVertices = numBackgroundDrawings * 2 + 2;
			int numElements = numBackgroundDrawings * 2;
			vertices = new float[numVertices * VERTEX_SIZE];
			elements = new int[numElements * ELEMENT_SIZE];
			float x = transformBackgroundXToVertexX(RENDERER.background.getOffsetX());
			float y = height;
			
			vertices[0] = x;
			vertices[1] = y;
			vertices[2] = 0f;
			
			vertices[3] = 255;
			vertices[4] = 0;
			vertices[5] = 0;
			vertices[6] = 1f;
			
			vertices[7] = 0;
			
			for(int i = 1; i < numBackgroundDrawings + 1; i++) {
				x = x + width;
				int e = i - 1;
				vertices[i * 7 + 0] = x;
				vertices[i * 7 + 1] = y;
				vertices[i * 7 + 2] = 0f;
				
				vertices[i * 7 + 3] = 255;
				vertices[i * 7 + 4] = 0;
				vertices[i * 7 + 5] = 0;
				vertices[i * 7 + 6] = 1f;
				
				elements[e * 6 + 0] = e * 7 + 0;
				elements[e * 6 + 1] = i * 7 + 0;
				elements[e * 6 + 0] = e * 7 + 1;
				
				elements[e * 6 + 0] = i * 7 + 0;
				elements[e * 6 + 1] = i * 7 + 1;
				elements[e * 6 + 0] = e * 7 + 1;
			}
			
			
			
			while (x < RES_SPECS.gameWidth) {
//					g2d.drawImage(background.getImg(), x, 0, width, height, null);
				x += width;
			}
		}
	}

	// Background must not be altered by camera Pos.
	protected float transformBackgroundXToVertexX(float x) {
		return (x / RES_SPECS.halfGameWidth) - 1;
	}

	protected float transformBackgroundYToVertexY(float y) {
		return (y / RES_SPECS.halfGameHeight) - 1;
	}
}
