package Project;

import java.awt.*;

public class RGBcolor {

	int R;
	int G;
	int B;

	public RGBcolor() {
		R = 0;
		G = 0;
		B = 0;
	}

	public RGBcolor(int r, int g, int b) {
		R = r;
		G = g;
		B = b;
	}

	public RGBcolor(String r, String g, String b) {
		R = Integer.parseInt(r);
		G = Integer.parseInt(g);
		B = Integer.parseInt(b);
	}

	public int getR() {
		return R;
	}

	public int getG() {
		return G;
	}

	public int getB() {
		return B;
	}

	public Color getColor() {
		return new Color(R, G, B);
	}
}
