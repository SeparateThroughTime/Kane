package kane.genericGame;

import java.awt.image.BufferedImage;
import java.util.HashMap;


import kane.renderer.Shader;
import kane.renderer.Texture;

public class ResourceManager {
	
	public static ResourceManager RESOURCE_MANAGER = new ResourceManager();
	
	private ResourceManager() {
		sprites = new HashMap<>();
		shaders = new HashMap<>();
		backgrounds = new HashMap<>();
	}
	
	private HashMap<String, Texture> sprites;
	private HashMap<String, Shader> shaders;
	private HashMap<String, BufferedImage> backgrounds;

	public Texture getTexture(String filepath, int frameWidth, int frameHeight) {
		if (!sprites.containsKey(filepath)) {
			Texture texture = new Texture(filepath);
			return texture;
		} else {
			return sprites.get(filepath);
		}

	}
}
