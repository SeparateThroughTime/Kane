package kane.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import kane.math.Vec2f;

public class Shader {
	
	protected int shaderProgramID;
	protected int vertexID;
	protected int fragmentID;
	
	private String vertexSource;
	private String fragmentSource;
	private final String filepath;
	
	public Shader(String filepath) {
		this.filepath = filepath;
		readFile();	
		
	}
	
	private void readFile() {
		try {
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

			//Didnt work for some reason after switching IDE...
			//Just assume the order of shaders: First is vertex and second is fragment.
//			int index = source.indexOf("#type") + 6;
//			int eol = source.indexOf("\r\n", index);
//			String firstPattern = source.substring(index, eol).trim();
//
//			index = source.indexOf("#type", eol) + 6;
//			eol = source.indexOf("\r\n", index);
//			String secondPattern = source.substring(index, eol).trim();
//
//			if (firstPattern.equals("vertex")) {
//				vertexSource = splitString[1];
//			} else if (firstPattern.equals("fragment")) {
//				fragmentSource = splitString[1];
//			} else {
//				throw new IOException("Unexpected token " + firstPattern);
//			}
//
//			if (secondPattern.equals("vertex")) {
//				vertexSource = splitString[2];
//			} else if (secondPattern.equals("fragment")) {
//				fragmentSource = splitString[2];
//			} else {
//				throw new IOException("Unexpected token " + secondPattern);
//			}

			vertexSource = splitString[1];
			fragmentSource = splitString[2];
		} catch (IOException e) {
			e.printStackTrace();
			assert false : "Error: Could not ipen file for shader";
		}
	}
	
	public void compile() {
		loadVertexShader();
		loadFragmentShader();
		linkShaders();
	}
	
	private void loadVertexShader() {
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexSource);
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
		glShaderSource(fragmentID, fragmentSource);
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
	
//	public void uploadVec4f(String varName, Vector4f vec) {
//		int varLocation = glGetUniformLocation(shaderProgramID, varName);
//		use();
//		glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
//	}
//	
//	public void uploadVec3f(String varName, Vector3f vec) {
//		int varLocation = glGetUniformLocation(shaderProgramID, varName);
//		use();
//		glUniform3f(varLocation, vec.x, vec.y, vec.z);
//	}
	
	public void uploadVec2f(String varName, Vec2f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform2f(varLocation, vec.x, vec.y);
	}
	
	public void uploadFloat(String varName, Float val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1f(varLocation, val);
	}
	
	public void uploadInt(String varName, int val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, val);
	}
	
	public void uploadTexture(String varName, int slot) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, slot);
	}
	
	public void uploadIntArray(String varName, int[] array) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1iv(varLocation, array);
	}
}
