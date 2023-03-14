package kane.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.opengl.GL20;

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
			vertexShader = new String(Files.readAllBytes(Paths.get(filepath + ".vertex")));
			fragmentShader = new String(Files.readAllBytes(Paths.get(filepath + ".fragment")));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: Could not open file for Shader: " + filepath);
		}
	}
	
	public void compile() {
		loadVertexShader();
		loadFragmentShader();
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

	private void loadFragmentShader() {
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
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
