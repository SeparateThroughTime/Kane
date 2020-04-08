package kane.genericGame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import kane.genericGame.renderer.Renderer;
import kane.genericGame.userInteraction.Keyboard;
import kane.genericGame.userInteraction.KeyboardInterface;
import kane.genericGame.userInteraction.Mouse;
import kane.genericGame.userInteraction.MouseInterface;
import kane.physics.ContactListener;
import kane.physics.Physics;

public abstract class Game implements WindowListener, KeyboardInterface, MouseInterface, ContactListener{

	private final String TITLE;
	protected final int WIDTH = 800;
	protected final int HEIGHT = 600;
	private JFrame frame;
	private Canvas canvas;
	private BufferedImage frameBuffer;
	private int[] frameBufferData;

	protected boolean[] keyState = new boolean[128];
	protected boolean[] mouseState = new boolean[16];
	protected Mouse mouseListener;
	protected Keyboard keyListener;
	
	final long TARGET_FPS = 60;
	final long NANO_SECOND = 1000000000;
	final long NANO_SECOND_FPS = NANO_SECOND / TARGET_FPS;
	protected final float DELTATIME = 1.0f / TARGET_FPS;
	

	protected Physics physics;
	protected Renderer renderer;

	protected abstract void initGame();
	protected abstract void mechanicsLoop();

	public Game(String title) {
		// init Window
		TITLE = title;
		
		frameBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		frameBufferData = ((DataBufferInt) frameBuffer.getRaster().getDataBuffer()).getData();
		
		physics = new Physics(HEIGHT, WIDTH, DELTATIME, this);
		renderer = new Renderer(WIDTH, HEIGHT, frameBufferData, physics);
		mouseListener = new Mouse(physics, HEIGHT, this);
		keyListener = new Keyboard(DELTATIME, renderer, physics, this);

		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIgnoreRepaint(true);
		frame.addWindowListener(this);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		canvas.setIgnoreRepaint(true);
		canvas.addKeyListener(keyListener);
		canvas.addMouseListener(mouseListener);
		canvas.addMouseMotionListener(mouseListener);
		frame.add(canvas);
		frame.pack();

		frame.setVisible(true);
		canvas.requestFocus();
	}

	protected void run() {
		// runs the game
		
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
				userInteraction(DELTATIME);
				mechanicsLoop();
				physics.step(DELTATIME);
				accumulatedTime -= DELTATIME;
			}

			renderer.renderGame();

			// Display FrameBuffer
			Graphics graphics = canvas.getGraphics();
			graphics.drawImage(frameBuffer, 0, 0, null);
			graphics.dispose();
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
