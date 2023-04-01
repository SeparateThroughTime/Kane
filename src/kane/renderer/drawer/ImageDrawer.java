package kane.renderer.drawer;

import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.Color;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Point;
import kane.renderer.Drawer;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;
import kane.renderer.Texture;

public class ImageDrawer extends Drawer {

	public static ImageDrawer IMAGE_DRAWER;
	private static final int MAX_IMAGES = 1000;
	private static final int MAX_TEXTURES = 16;

	private Texture[] textureSlots;
	private int[] textureSlotNumbers;
	private int countTextureSlots;
	private HashMap<Integer, Integer> texIDTexSlot;

	private ImageDrawer() {
		super(3, GL_TRIANGLES);

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
//		// draw bodies
//		for (int i = 0; i < numRenderedShapes; i++) {
//			Shape shape = renderedShapes[i];
//			SpriteController[] spriteControllers = shape.getSpriteControllers();
//			for (SpriteController spriteController : spriteControllers) {
//				Sprite sprite = spriteController.sprite;
//				float scale = spriteController.scale * Sprite.SCALE;
//				if (!GAME.pause) {
//					spriteController.step();
//				}
//				ByteBuffer frame = spriteController.getFrame();
//				Vec2f pos = shape.getAbsPos();
//				Vec2f spriteAbsPos = new Vec2f(pos).add(spriteController.spritePosOffset);
//				int textureId = spriteController.sprite.TEXTURE_ID;
//				Vec2f dimension = new Vec2f((float) sprite.FRAME_WIDTH / RES_SPECS.halfGameWidth,
//						(float) sprite.FRAME_HEIGHT / RES_SPECS.halfGameHeight).mult(scale * RENDERER.multiplicator);
//				drawImage(transformPosToVertex(spriteAbsPos), frame, dimension, textureId, shape.color);
//			}
//		}
		
		Body b = new Body(new Vec2f(0, 0));
		Point p = new Point(0, 0, b, Color.WHITE, new Material(0, 0), 0);
		File file = new File("sprites\\items\\sword.png");
		Sprite s = new Sprite(file, 16, 16);
		SpriteController sc = new SpriteController(s);
		p.setSpriteControllers(new SpriteController[] {sc});
		s.addState(SpriteState.STATIC, new int[] { 0 });
		sc.setCurrentSpriteState(SpriteState.STATIC);
		
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, sc.getFrame().ID);

		
		initVerticesAndElements();
		
		elementsToDraw = 4;
		int[] slots = new int[3];
		slots[0] = 0;
		slots[1] = 1;
		slots[2] = 2;
		RENDERER.uploadIntArrayToShader("uTextures", slots);
		
		drawImage(new Vec2f(-0.9f, 0), sc.getFrame().IMAGE, new Vec2f(0.2f, 0.2f), 1, Color.WHITE);

	}

	public void addImagesToDrawer() {
		initTextureSlots();
		for (int i = 0; i < numRenderedShapes; i++) {
			Shape shape = renderedShapes[i];
			SpriteController[] spriteControllers = shape.getSpriteControllers();
			for (SpriteController spriteController : spriteControllers) {
				Texture frame = spriteController.getFrame();
				int textureId = frame.ID;
				boolean textureAlreadyExists = false;
				for (int j = 0; j < countTextureSlots; j++) {
					if (textureSlots[j].equals(frame)) {
						textureAlreadyExists = true;
						break;
					}
				}
				if (!textureAlreadyExists) {
					textureSlots[countTextureSlots] = frame;
					texIDTexSlot.put(textureId, countTextureSlots);
					glActiveTexture(GL_TEXTURE0 + countTextureSlots);
					glBindTexture(GL_TEXTURE_2D, textureId);
					countTextureSlots++;
				}
			}
		}
//		glBindTexture(GL_TEXTURE_2D, 0);

		textureSlotNumbers = new int[MAX_TEXTURES];
		for (int i = 0; i < MAX_TEXTURES; i++) {
			textureSlotNumbers[i] = i;
		}
		RENDERER.uploadIntArrayToShader("uTextures", textureSlotNumbers);
	}

	public void drawImage(Vec2f pos, ByteBuffer frame, Vec2f dimension, int textureID, Color color) {

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
		vertices[verticeStartingIndex + 9] = textureID;

		// Bottom right
		vertices[verticeStartingIndex + 10] = pos.x + dimension.x;
		vertices[verticeStartingIndex + 11] = pos.y;
		vertices[verticeStartingIndex + 12] = 0f;

		vertices[verticeStartingIndex + 13] = red;
		vertices[verticeStartingIndex + 14] = green;
		vertices[verticeStartingIndex + 15] = blue;
		vertices[verticeStartingIndex + 16] = 1f;

		vertices[verticeStartingIndex + 17] = 1;
		vertices[verticeStartingIndex + 18] = 1;
		vertices[verticeStartingIndex + 19] = textureID;

		// Top right
		vertices[verticeStartingIndex + 20] = pos.x + dimension.x;
		vertices[verticeStartingIndex + 21] = pos.y + dimension.y;
		vertices[verticeStartingIndex + 22] = 0f;

		vertices[verticeStartingIndex + 23] = red;
		vertices[verticeStartingIndex + 24] = green;
		vertices[verticeStartingIndex + 25] = blue;
		vertices[verticeStartingIndex + 26] = 1f;

		vertices[verticeStartingIndex + 27] = 1;
		vertices[verticeStartingIndex + 28] = 0;
		vertices[verticeStartingIndex + 29] = textureID;

		// Top left
		vertices[verticeStartingIndex + 30] = pos.x;
		vertices[verticeStartingIndex + 31] = pos.y + dimension.y;
		vertices[verticeStartingIndex + 32] = 0f;

		vertices[verticeStartingIndex + 33] = red;
		vertices[verticeStartingIndex + 34] = green;
		vertices[verticeStartingIndex + 35] = blue;
		vertices[verticeStartingIndex + 36] = 1f;

		vertices[verticeStartingIndex + 37] = 0;
		vertices[verticeStartingIndex + 38] = 0;
		vertices[verticeStartingIndex + 39] = textureID;

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

	// TODO drawBackground
	protected void drawBackground() {
		if (RENDERER.background != null) {
			float width = transformBackgroundXToVertexX(RENDERER.background.WIDTH * RENDERER.multiplicator);
			float height = transformBackgroundYToVertexY(RENDERER.background.HEIGHT * RENDERER.multiplicator);
			int numBackgroundDrawings = RES_SPECS.gameWidth / (int) width + 2;
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

			for (int i = 1; i < numBackgroundDrawings + 1; i++) {
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

	@Override
	protected void initVerticesAndElements() {
		countCurrentVertices = 0;
		vertices = new float[VERTEX_SIZE * MAX_IMAGES];
		countCurrentElements = 0;
		elements = new int[ELEMENT_SIZE * MAX_IMAGES];
	}

	protected void initTextureSlots() {
		countTextureSlots = 0;
		textureSlots = new Texture[MAX_TEXTURES];
		texIDTexSlot = new HashMap<Integer, Integer>();
	}
}
