package kane.renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader {
	private static String SHADER_PATH = "shaders/";
	public static Shader DEFAULT = new Shader(SHADER_PATH + "default.glsl");
	
	
	private int shaderProgramID;
	protected int vertexID;
	protected int fragmentID;
	
	private String vertexShader;
	private String fragmentShader;
	private String filepath;
	
	private Shader(String filepath) {
		this.filepath = filepath;
		readFile();	
		
	}
	
	private void readFile() {
		try {
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			String[] split = source.split("(#type )( )([a-zA-Z]+)", shaderProgramID);
			vertexShader = split[1];
			fragmentShader = split[2];
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: Could not open file for Shader: " + filepath);
		}
	}
	
	public void compile() {
		loadVertexShader();
		loadFragmendShader();
		linkShaders();
	}
	
	private void loadVertexShader() {
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexShader);
		glCompileShader(vertexID);

		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: Vertex Shader compilation failed!");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert false : "";
		}
	}

	private void loadFragmendShader() {
		fragmentID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(fragmentID, fragmentShader);
		glCompileShader(fragmentID);

		int success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: Fragment Shader compilation failed!");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false : "";
		}
	}

	private void linkShaders() {
		shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexID);
		glAttachShader(shaderProgramID, fragmentID);
		glLinkProgram(shaderProgramID);

		int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: Linking Shaders failed!");
			System.out.println(glGetProgramInfoLog(shaderProgramID, len));
			assert false : "";
		}
	}
	
	public void use() {
		glUseProgram(shaderProgramID);
	}
	
	public void detach() {
		glUseProgram(0);
	}
}
