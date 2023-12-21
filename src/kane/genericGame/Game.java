package kane.genericGame;

import static kane.genericGame.userInteraction.Keyboard.KEYBOARD;
import static kane.genericGame.userInteraction.Mouse.MOUSE;
import static kane.physics.Physics.PHYSICS;
import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import kane.genericGame.hud.HudBar;
import kane.genericGame.userInteraction.Keyboard;
import kane.genericGame.userInteraction.KeyboardInterface;
import kane.genericGame.userInteraction.Mouse;
import kane.genericGame.userInteraction.MouseInterface;
import kane.physics.Body;
import kane.physics.Physics;
import kane.renderer.Renderer;
import kane.renderer.Resolution;
import kane.renderer.ResolutionSpecification;

/**
 * Game is an abstract class which provides the main-construct of the
 * game-engine
 */
public abstract class Game implements WindowListener, KeyboardInterface, MouseInterface, ContactManagementInterface {

	public static final float BACKGROUND_SPEED = 0.5f;

	public int mapLen;
	public int mapHeight;
	public final String title;

	public boolean[] keyState = new boolean[128];
	public boolean[] mouseState = new boolean[16];

	public final static long TARGET_FPS = 60;
	public final static long NANO_SECOND = 1000000000;
	public final static long NANO_SECOND_FPS = NANO_SECOND / TARGET_FPS;
	public final static float DELTATIME = 1.0f / TARGET_FPS;
    public static long time = 0;

	public boolean pause;
	public boolean isRunning;

	protected GameEvent[] events = new GameEvent[5000];
	protected int numEvents = 0;

	public HudBar healthBar;
	public Mob player;

	private float sanity;

	protected abstract void initGame();

	protected abstract void mechanicsLoop();

	protected abstract void postMechanicsLoops();

	/**
	 * 
	 * @param title -The title of the game
	 */
	protected Game(String title) {
		// init Window
		this.title = title;
		ResolutionSpecification.initializeResSpecs(600, 800, 600, 800);
		Physics.initializatePhysics(this);
		Renderer.initializeRenderer(title);
		long window = RENDERER.window;
		Keyboard.initializeKeyboard(this, window);
		Mouse.initializeMouse(this, window);
	}

	/**
	 * change the resolution. This uses the enum Resolution.
	 * 
	 * @param res -new resolution
	 */
	public void changeResolution(Resolution res) {
		switch (res) {
		case SOL800x600:
			RES_SPECS.width = 800;
			RES_SPECS.height = 600;
			break;
		case SOL1024x768:
			RES_SPECS.width = 1024;
			RES_SPECS.height = 768;
			break;
		case SOL1152x864:
			RES_SPECS.width = 1152;
			RES_SPECS.height = 864;
			break;
		case SOL1280x960:
			RES_SPECS.width = 1280;
			RES_SPECS.height = 960;
			break;
		case SOL1280x768:
			RES_SPECS.width = 1280;
			RES_SPECS.height = 768;
			break;
		case SOL1280x1024:
			RES_SPECS.width = 1280;
			RES_SPECS.height = 1024;
			break;
		case SOL1280x800:
			RES_SPECS.width = 1280;
			RES_SPECS.height = 800;
			break;
		case SOL1680x1050:
			RES_SPECS.width = 1680;
			RES_SPECS.height = 1050;
			break;
		case SOL1176x664:
			RES_SPECS.width = 1176;
			RES_SPECS.height = 664;
			break;
		case SOL1280x720:
			RES_SPECS.width = 1280;
			RES_SPECS.height = 720;
			break;
		case SOL1360x768:
			RES_SPECS.width = 1360;
			RES_SPECS.height = 768;
			break;
		case SOL1366x768:
			RES_SPECS.width = 1366;
			RES_SPECS.height = 768;
			break;
		case SOL1600x900:
			RES_SPECS.width = 1600;
			RES_SPECS.height = 900;
			break;
		case SOL1768x992:
			RES_SPECS.width = 1768;
			RES_SPECS.height = 992;
			break;
		case SOL1920x1080:
			RES_SPECS.width = 1920;
			RES_SPECS.height = 1080;
			break;
		case SOL1600x1024:
			RES_SPECS.width = 1600;
			RES_SPECS.height = 1024;
			break;
		default:
			break;
		}
		RES_SPECS.gameWidth = (int) ((float) RES_SPECS.GAME_HEIGHT / RES_SPECS.height * RES_SPECS.width);

		RENDERER.changeResolution();
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

		while (!glfwWindowShouldClose(RENDERER.window)) {
			long frameStartTime = System.nanoTime();
			float frameTime = Math.min((frameStartTime - lastFrameTime) / (float) NANO_SECOND, 0.25f);
            time += frameStartTime - lastFrameTime;
			lastFrameTime = frameStartTime;

			// Update Game
			accumulatedTime += frameTime;
			while (accumulatedTime >= DELTATIME) {
				if (!pause) {
					PHYSICS.preStep();
				}
				userInteraction();
				if (!pause) {
					coreMechanicsLoop();
					mechanicsLoop();
					eventsLoop();
					PHYSICS.step();
					postMechanicsLoops();

				}
				accumulatedTime -= DELTATIME;
			}
			RENDERER.renderGame();
			numFrames++;

			// FPS Output
			if (System.nanoTime() - startFpsTime >= NANO_SECOND) {
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
		
		glfwFreeCallbacks(RENDERER.window);
		glfwDestroyWindow(RENDERER.window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	protected void coreMechanicsLoop() {
		// Mob Mechanics
		for (int i = 0; i < PHYSICS.numBodies; i++) {
			Body body = PHYSICS.bodies[i];
			if (body instanceof Mob) {
				Mob mob = (Mob) body;
				mob.invulnerabilityCooldown();
			}
		}
	}

	protected void eventsLoop() {
		for (int i = 0; i < numEvents; i++) {
			GameEvent event = events[i];
			if (event.getFrameCounter() == 0) {
				event.start();
			} else if (event.getFrameCounter() < event.EVENT_DURATION) {
				event.procedure();
			} else {
				event.end();
				remEvent(i);
				// Otherwise the next event wouldn't be executed but the next but one.
				i--;
			}
			event.countFrame();
		}
	}

	/**
	 * This updates the states of keyboard and mouse and runs the actions, depending
	 * on it.
	 * 
	 * @param DELTATIME
	 */
	protected void userInteraction() {
		glfwPollEvents();
		MOUSE.update();
		KEYBOARD.update();
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

	public void addEvent(GameEvent e) {
		events[numEvents++] = e;
	}

	private void remEvent(int id) {
		for (int i = id + 1; i < numEvents; i++) {
			events[i - 1] = events[i];
		}
		numEvents--;
	}

	public void setSanity(float sanity){
		this.sanity = sanity;
		RENDERER.updateSanity(sanity);
	}

	public float getSanity(){
		return sanity;
	}
}
