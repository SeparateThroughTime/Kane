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

	public Texture getTexture(String filepath) {
		if (filepath == null || filepath == "") {
			return null;
		}
		
		if (!sprites.containsKey(filepath)) {
			Texture texture = new Texture(filepath);
			sprites.put(filepath, texture);
			return texture;
		} else {
			return sprites.get(filepath);
		}

	}
	
	public Shader getShader(String filepath) {
		if (!shaders.containsKey(filepath)) {
			Shader shader = new Shader(filepath);
			shaders.put(filepath, shader);
			return shader;
		} else {
			return shaders.get(filepath);
		}
	}
}
