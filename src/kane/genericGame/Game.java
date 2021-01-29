package kane.genericGame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import kane.genericGame.userInteraction.Keyboard;
import kane.genericGame.userInteraction.KeyboardInterface;
import kane.genericGame.userInteraction.Mouse;
import kane.genericGame.userInteraction.MouseInterface;
import kane.physics.ContactListener;
import kane.physics.Physics;
import kane.renderer.Renderer;
import kane.renderer.Resolution;
import kane.renderer.ResolutionSpecification;

/**
 * Game is an abstract class which provides the main-construct of the game-engine
 */
public abstract class Game implements WindowListener, KeyboardInterface, MouseInterface, ContactListener {

	protected ResolutionSpecification resSpecs;
	private final String TITLE;
	private JFrame frame;

	protected boolean[] keyState = new boolean[128];
	protected boolean[] mouseState = new boolean[16];
	protected Mouse mouseListener;
	protected Keyboard keyListener;

	final long TARGET_FPS = 60;
	final long NANO_SECOND = 1000000000;
	final long NANO_SECOND_FPS = NANO_SECOND / TARGET_FPS;
	protected final float DELTATIME = 1.0f / TARGET_FPS;

	protected boolean pause;

	protected Physics physics;
	protected Renderer renderer;

	protected abstract void initGame();

	protected abstract void mechanicsLoop();
	protected abstract void postMechanicsLoops();

	/**
	 * 
	 * @param title -The title of the game
	 */
	public Game(String title) {
		// init Window
		TITLE = title;
		
		resSpecs = new ResolutionSpecification(600, 800, 600, 800);

		physics = new Physics(DELTATIME, this);
		renderer = new Renderer(resSpecs, physics);
		mouseListener = new Mouse(resSpecs, this);
		keyListener = new Keyboard(this);

		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIgnoreRepaint(true);
		frame.addWindowListener(this);

		renderer.setPreferredSize(new Dimension(resSpecs.width, resSpecs.height));
		renderer.addKeyListener(keyListener);
		renderer.addMouseListener(mouseListener);
		renderer.addMouseMotionListener(mouseListener);
		renderer.setIgnoreRepaint(true);
		renderer.requestFocusInWindow();
		frame.add(renderer);
		frame.pack();

		frame.setVisible(true);
	}
	
	/**
	 * change the resolution. This uses the enum Resolution.
	 * @param res -new resolution
	 */
	protected void changeResolution(Resolution res) {
		switch (res) {
		case SOL800x600:
			resSpecs.width = 800;
			resSpecs.height = 600;
			break;
		case SOL1024x768:
			resSpecs.width = 1024;
			resSpecs.height = 768;
			break;
		case SOL1152x864:
			resSpecs.width = 1152;
			resSpecs.height = 864;
			break;
		case SOL1280x960:
			resSpecs.width = 1280;
			resSpecs.height = 960;
			break;
		case SOL1280x768:
			resSpecs.width = 1280;
			resSpecs.height = 768;
			break;
		case SOL1280x1024:
			resSpecs.width = 1280;
			resSpecs.height = 1024;
			break;
		case SOL1280x800:
			resSpecs.width = 1280;
			resSpecs.height = 800;
			break;
		case SOL1680x1050:
			resSpecs.width = 1680;
			resSpecs.height = 1050;
			break;
		case SOL1176x664:
			resSpecs.width = 1176;
			resSpecs.height = 664;
			break;
		case SOL1280x720:
			resSpecs.width = 1280;
			resSpecs.height = 720;
			break;
		case SOL1360x768:
			resSpecs.width = 1360;
			resSpecs.height = 768;
			break;
		case SOL1366x768:
			resSpecs.width = 1366;
			resSpecs.height = 768;
			break;
		case SOL1600x900:
			resSpecs.width = 1600;
			resSpecs.height = 900;
			break;
		case SOL1768x992:
			resSpecs.width = 1768;
			resSpecs.height = 992;
			break;
		case SOL1920x1080:
			resSpecs.width = 1920;
			resSpecs.height = 1080;
			break;
		case SOL1600x1024:
			resSpecs.width = 1600;
			resSpecs.height = 1024;
			break;
		default:
			break;
		}
		resSpecs.gameWidth = (int)((float)resSpecs.GAME_HEIGHT / resSpecs.height * resSpecs.width);
		
		renderer.setPreferredSize(new Dimension(resSpecs.width, resSpecs.height));
		frame.pack();
		
		renderer.changeResolution();
	}

	/**
	 * This is the main-loop for the game.
	 */
	protected void run() {
		long startFpsTime = System.nanoTime();
		long lastFrameTime = System.nanoTime();
		float accumulatedTime = 0.0f;
		int numFrames = 0;

		initGame();

		boolean isRunning = true;
		while (isRunning) {
			long frameStartTime = System.nanoTime();
			float frameTime = Math.min((frameStartTime - lastFrameTime) / (float) NANO_SECOND, 0.25f);
			lastFrameTime = frameStartTime;

			// Update Game
			accumulatedTime += frameTime;
			while (accumulatedTime >= DELTATIME) {
				if (!pause) {
					physics.preStep(DELTATIME);
				}
				userInteraction(DELTATIME);
				if (!pause) {
					mechanicsLoop();
					physics.step(DELTATIME);
					postMechanicsLoops();
					
				}
				accumulatedTime -= DELTATIME;
			}

			renderer.renderGame();
			numFrames++;

			// FPS Output
			if (System.nanoTime() - startFpsTime >= NANO_SECOND) {
				frame.setTitle(String.format("%s: Frames: %d, Bodie: %d", TITLE, numFrames, physics.getNumBodies()));
				startFpsTime = System.nanoTime();
				numFrames = 0;
			}

			// Reduction to 60 FPS
			long sleepDuration = NANO_SECOND_FPS - (System.nanoTime() - frameStartTime);
			if (sleepDuration > 0) {
				long sleepStart = System.nanoTime();
				while (System.nanoTime() - sleepStart < sleepDuration) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	/**
	 * This updates the states of keyboard and mouse and runs the actions, depending on it.
	 * @param DELTATIME
	 */
	private void userInteraction(float DELTATIME) {
		mouseListener.update();
		keyListener.update();
	}

//WindowListener
	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		for (int i = 0; i < keyState.length; i++) {
			keyState[i] = false;
		}

	}
}
