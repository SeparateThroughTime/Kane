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

import java.awt.image.BufferedImage;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Shape;
import kane.renderer.Drawer;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;

public class ImageDrawer extends Drawer {

	public static ImageDrawer IMAGE_DRAWER;

	private ImageDrawer() {
		super(3, GL_TEXTURE_2D, 2);
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
					float scale = spriteController.scale;
					if (!GAME.pause) {
						spriteController.step();
					}
					BufferedImage frame = spriteController.getFrame();
					Vec2f pos = shape.getAbsPos();
					Vec2f spriteAbsPos = new Vec2f(pos).add(spriteController.getSpritePosOffset());
//						int posX = (int) spriteAbsPos.getX();
//						int posY = (int) spriteAbsPos.getY();
					int posX = Scalar.round(spriteAbsPos.getX());
					int posY = Scalar.round(spriteAbsPos.getY());
					drawImage(frame, scale, posX, posY, g2d);
				}
			}
		}
	}
	
	public void drawImage(Vec2f pos, Sprite sprite) {
		// TODO: do draw stuff. render each image single for now with following addition:
		glBindTexture(GL_TEXTURE_2D, sprite.TEXTURE_ID);
		RENDERER.uploadVarToShader("texSampler", 0);
		glActiveTexture(GL_TEXTURE0);
		sprite.bind();
		
		
		
		
		
		sprite.unbind();
	}
}
