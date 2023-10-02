import processing.core.PApplet;
import processing.event.KeyEvent;

public class WelcomeState implements IWorld{

	@Override
	public PApplet draw(PApplet c) {
		c.background(214, 99, 82);
		c.textSize(24);
		c.textAlign(c.CENTER);
		c.text("Press space bar to start", 400, 50);
		c.textSize(20);
		c.text("Use keys 'a' and 'd' to move player", 400, 80);
		return c;
	}

	@Override
	public IWorld update() {
		return this;
	}

	@Override
	public IWorld keyPressed(KeyEvent key) {
		if (key.getKey() == ' ') {
			TrackWorld w = new TrackWorld(200,0);
			return w;
		}
		return this;
	}
}