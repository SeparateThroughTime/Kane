package kane.renderer.shaders;

public class DefaultShader {
	public static String vertexShader = "#version 330 core\r\n"
			+ "layout (location=1) in vec3 aPos;\r\n"
			+ "layout (location=2) in vec4 aColor;\r\n"
			+ "\r\n"
			+ "out vec4 fColor\r\n"
			+ "\r\n"
			+ "void main(){\r\n"
			+ "	fColor = aColor;\r\n"
			+ "	gl_Position = vec4(aPos, 1.0);\r\n"
			+ "}\r\n";
	
	public static String fragmentShader = "#version 330 core\r\n"
			+ "\r\n"
			+ "in vec4 fColor;\r\n"
			+ "\r\n"
			+ "out vec4 color;\r\n"
			+ "\r\n"
			+ "void main(){\r\n"
			+ "	color = fColor\r\n"
			+ "}\r\n";
}
