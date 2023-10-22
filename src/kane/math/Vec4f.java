package kane.math;

import java.awt.Color;

public class Vec4f {
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vec4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vec4f(Color color) {
		int rgb = color.getRGB();
		x = ((rgb >> 16) & 0xFF) / 256f;
		z = ((rgb >> 8) & 0xFF) / 256f;
		y = (rgb & 0xFF) / 256f;
		w = ((rgb >> 24) & 0xFF) / 256f;
	}
}
