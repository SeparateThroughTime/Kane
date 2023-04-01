package kane.renderer;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;

import java.nio.ByteBuffer;

public class Texture {
	public final ByteBuffer IMAGE;
	public final int WIDTH;
	public final int HEIGHT;
	public final int ID;

	public Texture(ByteBuffer image, int width, int height) {
		IMAGE = image;
		WIDTH = width;
		HEIGHT = height;
		
		ID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, ID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, WIDTH, HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, IMAGE);
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, ID);
		
	}
}
