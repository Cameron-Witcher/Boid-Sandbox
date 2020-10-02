package me.cameron.boids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Screen extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Timer timer;
	List<Boid> boids = new ArrayList<>();
	boolean pause = false;
	double cx = 250;
	double cy = 250;
	Random random = new Random();
	
	Boid selected = null;

	public boolean debug = false;
	
	public boolean directionLines = true;
	public boolean searchCircle = true;
	public boolean avoidCircle = true;
	public boolean connectionLines = true;
	public boolean centerOfMass = true;

	public Screen() {
		setBackground(Color.BLACK);
		timer = new Timer(25, this);
		timer.start();

		load();
		addKeyListener(new TAdapter());
		setFocusable(true);
		requestFocusInWindow();

	}

	public List<Boid> getBoids() {
		return boids;
	}

	private void load() {
		try {
			for (int i = 0; i != 250; i++) {
				boids.add(new Boid(random.nextInt(getWidth()), random.nextInt(getHeight())));
			}
		} catch (IllegalArgumentException ex) {
			for (int i = 0; i != 1; i++) {
				boids.add(new Boid(random.nextInt(500), random.nextInt(500)));
			}
		}
	}

	private void unload() {
		boids.clear();
	}

	private void reset() {
		unload();
		load();
	}

	private void pause() {
		pause = !pause;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	
	private void selectBoid(Boid boid) {
		for(Boid b : boids) {
			if(!b.equals(boid)) {
				b.selected = false;
			} else {
				b.selected = true;
				selected = b;
			}
		}
	}
	
	private void unselectBoid() {
		for(Boid b : boids) {
				b.selected = false;
		}
		selected = null;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		

		cx = 0;
		cy = 0;
		for (Boid boid : boids) {
			cx = cx + boid.x;
			cy = cy + boid.y;
		}
		cx = cx / boids.size();
		cy = cy / boids.size();
		for (Boid boid : boids) {
			boid.update();
			boid.draw(g);
		}

		g.setColor(Color.GREEN);
		if (Main.getWindow().getScreen().debug && centerOfMass)
			g.fillRect((int) cx - 5, (int) cy - 5, 10, 10);

		

		g.dispose();
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_R) {
				pause = true;
				unload();
				load();
				pause = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				pause();
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				Boid boid = boids.get(new Random().nextInt(boids.size()));
				selectBoid(boid);
			}
			if (e.getKeyCode() == KeyEvent.VK_U) {
				unselectBoid();
			}
			if (e.getKeyCode() == KeyEvent.VK_F3) {
				debug = !debug;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_1) {
				directionLines = !directionLines;
			}
			if (e.getKeyCode() == KeyEvent.VK_2) {
				searchCircle = !searchCircle;
			}
			if (e.getKeyCode() == KeyEvent.VK_3) {
				avoidCircle = !avoidCircle;
			}
			if (e.getKeyCode() == KeyEvent.VK_4) {
				connectionLines = !connectionLines;
			}
			if (e.getKeyCode() == KeyEvent.VK_5) {
				centerOfMass = !centerOfMass;
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				reset();
				load();
			}

		}

	}

}
