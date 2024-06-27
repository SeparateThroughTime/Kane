package kane.genericGame;

import java.awt.image.BufferedImage;
import java.util.HashMap;


import kane.renderer.Shader;
import kane.renderer.Texture;
import kane.sound.SoundBuffer;

public class ResourceManager {
	
	public static ResourceManager RESOURCE_MANAGER = new ResourceManager();
	
	private ResourceManager() {
		sprites = new HashMap<>();
		shaders = new HashMap<>();
		backgrounds = new HashMap<>();
        soundBuffers = new HashMap<>();
	}
	
	private HashMap<String, Texture> sprites;
	private HashMap<String, Shader> shaders;
	private HashMap<String, BufferedImage> backgrounds;
    private HashMap<String, SoundBuffer> soundBuffers;

	public Texture getTexture(String filepath) {
        if (filepath == null || filepath.isEmpty()) {
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

	public Shader getShader(String vertexFilepath, String fragmentFilepath) {
		String key = vertexFilepath + fragmentFilepath;
		if (!shaders.containsKey(key)) {
			Shader shader = new Shader(vertexFilepath, fragmentFilepath);
			shaders.put(key, shader);
			return shader;
		} else {
			return shaders.get(key);
		}
	}

    public SoundBuffer getSoundBuffer(String soundFilepath) {
        if (soundFilepath == null || soundFilepath.isEmpty()) {
            return null;
        }

        if (!soundBuffers.containsKey(soundFilepath)) {
            SoundBuffer soundBuffer = new SoundBuffer(soundFilepath);
            soundBuffers.put(soundFilepath, soundBuffer);
            return soundBuffer;
        } else {
            return soundBuffers.get(soundFilepath);
        }
    }
}
