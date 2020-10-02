package me.cameron.boids.res;

import java.awt.Image;
import java.io.InputStream;

import javax.swing.ImageIcon;

public class Texture {
	
	public static Image loadTexture(String resource){
		
		try {
			return new ImageIcon(Texture.class.getResource("images/" + resource)).getImage();
		} catch (Exception e) {
			try {
				return new ImageIcon(Texture.class.getResource("images/unknown.png")).getImage();
			} catch (Exception e1) {
				return null;
			}
		}
	}
	
	public static InputStream loadFont(String resource){
		
		try {
			return Texture.class.getResourceAsStream("font/" + resource);
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
}