import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import processing.core.PApplet;
import processing.event.KeyEvent;
/**
 *
 */
public class TrackWorld implements IWorld{
	private int players; // Max players 9   
	private long trackTime;
	private String[] colors;
	private Runner[] runners;
	public static int screenSize;
	private Player me;



	public TrackWorld(int players) { 
		this.players = players;
		runners = new Runner[players]; 
		screenSize = 41 *(players+1);
		colors = new String[] {"blue","green","red","purple","yellow","orange","pink","cyan","magenta"};
		for (int i = 0; i < players; i++) {
			runners[i] = new Runner(25, colors[i], new Posn(25, (i + 1) * 40));
		}

		this.me = new Player(25, "white", new Posn(25, (players + 1) *40));
	}
	
	

	/**
	 * Runners displayed on the track
	 */
	public PApplet draw(PApplet c) {

		startLine(c);
		finishLine(c);
		timer(c);
		players(c);
		trackLines(c);
		return c;
	}
	/* Displays the start line on the screen 
	 * based on the amount of players
	 */
	void startLine(PApplet c) {
		c.background(214, 99, 82);
		c.line(25,0, 25, screenSize);		// start line
		c.stroke(255,255,255);
		c.fill(255,255,255);
		c.textSize(12);
		c.text("Start!", 40, 25);
	}
	/* Displays the finish line on the screen 
	 * based on the amount of players
	 */
	void finishLine(PApplet c) {
		c.line(775,0, 775, screenSize);	// finish line
		c.fill(255,255,255);
		c.textSize(12);
		c.text("Finish!", 760, 25);
	}
	/* Displays the time on the screen */
	void timer(PApplet c) {
		c.fill(255,255,255);
		int elapsedTimeInSeconds = c.millis();
		trackTime = elapsedTimeInSeconds;
		c.textSize(20);
		c.text("Time:" + elapsedTimeInSeconds / 1000, 380,15);
	}
	/* Displays the runners on the screen*/
	void drawRunner(PApplet c , Runner runner) {
		c.fill(255,0,0);
		c.circle((int)runner.getPosn().getX(),(int)runner.getPosn().getY(),runner.getSize());
	}
	/* Displays the player on the screen*/
	void drawPlayer(PApplet c, Player player) {
		c.fill(255,255,255);
		c.circle((int)player.getPosn().getX(),(int)player.getPosn().getY(),player.getSize());
	}
	/* Draws the player on the scene*/
	void players(PApplet c) {
		for (Runner runner: runners) {
			drawRunner(c,runner);
		}
		drawPlayer(c,me);
	}
	/* Displays the track lines on the scene*/
	void trackLines(PApplet c) {
		for (int i = 0; i <= players; i++) {
			int yPos = 60 + (i *40);
			c.line(0, yPos, 800, yPos);
		}
	}

	
	
	/**
	 * Saves finishing times to a text file
	 */

	public void saveTimes() {
		try {

			PrintWriter pw = new PrintWriter(new FileOutputStream(
							new File("output.txt"), true) ); /* append = true */

			me.writeToFile(pw);

			pw.close();
		} catch (IOException exp) {
			System.out.println("Problem finding finishTime:" + exp.getMessage() );
		}
	}



	/**
	 * Produces an updated world where the circle moves
	 * across the screen, if it hasn't hit the finish
	 * line yet. If it has it will display a finish screen
	 * with the runners times.
	 */

	public IWorld update() {
		for (int i = 0; i < players; i++) {
			double updatedX = runners[i].getPosn().getX() + runners[i].getSpeed();

			if(!runners[i].hasCrossedFinishLine()) {
				runners[i].getPosn().setX(updatedX);

				if(updatedX >= 780) {
					runners[i].setCrossedLine(true);
					runners[i].getPosn().setX(790);
					runners[i].crossFinishLine(trackTime);
				}
				if(me.getPosn().getX() >= 780) {
					me.setCrossedLine(true);
					me.getPosn().setX(790);
					me.crossFinishLine(trackTime);
				}
			}
		}

		boolean allRunnersFinished = true;
		for (int i = 0; i < players; i++) {
			if (!runners[i].hasCrossedFinishLine()) {
				allRunnersFinished = false;
				break;
			}

		}
		if(allRunnersFinished) {
			saveTimes();
			double[] times = new double[players + 1];
			for (int i = 0; i < players; i++) {
				times[i] = runners[i].getTime();
			}
		if(!me.hasCrossedFinishLine()) {
			me.crossFinishLine(trackTime);
		}
		times[players] = me.getTime();
		
			return new FinishState(times);
			
		}
		return this;	
	}

	
	
	/**
	 * moves player based on keys pressed
	 */
	public IWorld keyPressed(KeyEvent kev) {
		double movementSpeed = 12;
		double newX = me.getPosn().getX()+ movementSpeed;

		if (kev.getKey() == 'a') { // left key <
			me.getPosn().setX(newX);
		} else if (kev.getKey() == 'd') { // right key >
			me.getPosn().setX(newX);
		}
		return this;
	}
}
