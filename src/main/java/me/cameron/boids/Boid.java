package me.cameron.boids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

public class Boid {

	Image image;
	double x;
	double y;

	long life = 0;

	double cx;
	double cy;

	double movementAngle = 45;

	double turnSpeed = 1;

	int searchRadius = 90;
	int maxsearchRadius = searchRadius;
	int avoidRadius = 20;
	double speed = 1;
	boolean selected = false;

	int size = 10;

	int splitCooldown = 0;

	public Boid(int x, int y) {
		this.x = x;
		this.y = y;
		speed = new Random().nextInt(4) + 1;
//		movementAngle = (((Math.atan2((y - Main.getWindow().getScreen().getHeight()), (x - Main.getWindow().getScreen().getWidth())))) * 180 / Math.PI) - 180;
		movementAngle = new Random().nextInt(360);
		cx = 0;
		cy = 0;

		image = ImageUtils.getImage("images/boid.png");

	}

	public void update() {

//		while (movementAngle > 360) {
//			movementAngle = movementAngle - 360;
//		}
//		while (movementAngle < 0) {
//			movementAngle = movementAngle + 360;
//		}

		life = life + 1;

		cx = Main.getWindow().getScreen().cx;
		cy = Main.getWindow().getScreen().cy;

//		if(life < 5) {
//			movementAngle = getAngleTo(cx, cy);
//		}

//		moveToCenter();
//		move();
//		if (true)
//		return;

		if (new Random().nextDouble() < 0.005) {

			splitCooldown = 50;

			movementAngle = movementAngle + new Random().nextInt(50) * (new Random().nextBoolean() ? 1 : -1);

		}

		if (splitCooldown > 0) {
			searchRadius = 0;
			splitCooldown = splitCooldown - 1;
			if (splitCooldown == 0) {
				searchRadius = maxsearchRadius;
			}
		} else {

			for (Boid boid : Main.getWindow().getScreen().getBoids()) {

				if (boid.x != x && boid.y != y) {
					if (distance(boid) < avoidRadius) {
						int t = 0;
						if (getAngleTo(boid.x, boid.y) > movementAngle)
							t = -1;
						else
							t = 1;

						if (!(Math.sqrt(Math.pow(boid.x - predictX(), 2) + Math.pow(boid.y - predictY(), 2)) > distance(
								boid)))
							movementAngle = movementAngle + ((turnSpeed * 5) * t);
					}
					if (distance(boid) < searchRadius) {
						if (boid.movementAngle > movementAngle) {
							movementAngle = movementAngle + turnSpeed;
						}
						if (boid.movementAngle < movementAngle) {
							movementAngle = movementAngle - turnSpeed;
						}
						if (boid.speed < speed) {
							speed = speed - 0.01;
						}
						if (boid.speed > speed) {
							speed = speed + 0.01;
						}
						if (speed > 5) {
							speed = 4;
						}
						if (new Random().nextBoolean())
							break;
					}
				}
			}
		}

		move();

	}

	public void draw(Graphics g) {

		Color color = g.getColor();
		g.setColor(Color.WHITE);
//		Polygon p = new Polygon(new int[] { (int) x - size / 2, (int) x + size / 2, (int) x },
//				new int[] { (int) y - size / 2, (int) y - size / 2, (int) y + size }, 3);

		g.drawImage(ImageUtils.rotate(image, movementAngle+90), (int) x - size / 2, (int) y - size / 2, size, size, null);

//		g.fillRect(((int) x - (size / 2)), ((int) y - (size / 2)), size, size);
		if (Main.getWindow().getScreen().debug && (Main.getWindow().getScreen().selected == null || selected == true)) {
			if (Main.getWindow().getScreen().avoidCircle) {
				g.setColor(Color.RED);
				g.drawOval((int) x - avoidRadius / 2, (int) y - avoidRadius / 2, avoidRadius, avoidRadius);
			}
			if (Main.getWindow().getScreen().searchCircle) {
				g.setColor(Color.GREEN);
				g.drawOval((int) x - searchRadius / 2, (int) y - searchRadius / 2, searchRadius, searchRadius);
			}
			g.setColor(Color.CYAN);
			if (Main.getWindow().getScreen().directionLines)
				g.drawLine((int) x, (int) y, ((int) x) + ((int) ((double) getMotX() * 5)),
						((int) y) + ((int) ((double) getMotY() * 5)));
			// Draw lines between nearby boids

			if (Main.getWindow().getScreen().connectionLines)
				for (Boid boid : Main.getWindow().getScreen().boids) {
					if (boid.x != x && boid.y != y) {
						if (distance(boid) < searchRadius) {
							if (distance(boid) < avoidRadius) {
								g.setColor(Color.RED);
							} else {
								g.setColor(Color.GREEN);

							}
							g.drawLine((int) x, (int) y, (int) boid.x, (int) boid.y);
						}
					}
				}

		}
		g.setColor(color);
	}

	private void move() {
		x = x + ((double) getMotX());
		y = y + ((double) getMotY());
		if (x > Main.getWindow().getWidth())
			x = 0;
		if (x < 0)
			x = Main.getWindow().getWidth();

		if (y > Main.getWindow().getHeight())
			y = 0;
		if (y < 0)
			y = Main.getWindow().getHeight();
	}

	private double getAngleTo(double x, double y) {
		return (((Math.atan2((this.y - y), (this.x - x)))) * 180 / Math.PI) - 180;
	}

	private void moveToCenter() {
		double c = getAngleTo(cx, cy);
		while (c > 360) {
			c = c - 360;
		}
		while (c < 0) {
			c = c + 360;
		}

//		movementAngle = c;

		if (movementAngle > c - 1 && movementAngle < c + 1) {
			movementAngle = c + 0.02;
		} else {
			if (movementAngle < c) {
				movementAngle = movementAngle - turnSpeed;
			} else {
				movementAngle = movementAngle + turnSpeed;
			}
		}
	}

	public void moveToCenter(double cx, double cy, Graphics g) {
		double c = (((Math.atan2((y - cy), (x - cx)))) * 180 / Math.PI) - 180;
		while (c > 360) {
			c = c - 360;
		}
		while (c < 0) {
			c = c + 360;
		}

		if (movementAngle > c - 0.5 && movementAngle < c + 0.5) {
			movementAngle = c + 0.02;
		} else {
			if (movementAngle < c) {
				movementAngle = movementAngle + turnSpeed;
			} else {
				movementAngle = movementAngle - turnSpeed;
			}
		}
	}

	private double getMotX() {
		return speed * Math.cos(Math.toRadians(movementAngle));
	}

	private double getMotY() {
		return speed * Math.sin(Math.toRadians(movementAngle));
	}

	private double predictX() {
		return x + getMotX();
	}

	private double predictY() {
		return y + getMotY();
	}

	public double distance(Boid boid) {
		return Math.sqrt(Math.pow(boid.x - x, 2) + Math.pow(boid.y - y, 2));
	}

	public double distance(double x, double y) {
		return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
	}

}
