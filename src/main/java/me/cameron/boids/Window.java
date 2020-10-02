package me.cameron.boids;

import javax.swing.JFrame;

public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Screen screen;
	
	public Window(){
		screen = new Screen();
		add(screen);
		pack();
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public Screen getScreen() {
		return screen;
	}

}
