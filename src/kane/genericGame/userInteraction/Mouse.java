package kane.genericGame.userInteraction;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import kane.math.Scalar;
import kane.math.Vec2i;
import kane.renderer.Camera;
import kane.renderer.ResolutionSpecification;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The Mouse is managing all actions, when keys on the hw-mouse are pressed.
 */
public class Mouse {

	private final int NUMBUTTONS = 16;
	private boolean click[] = new boolean[NUMBUTTONS];
	private ResolutionSpecification resSpecs;

	protected Vec2i mousePos = new Vec2i();
	protected boolean[] mouseState = new boolean[NUMBUTTONS];
	private MouseInterface mouseInt;
	private Camera camera;

	/**
	 * 
	 * @param resSpecs -Used ResolutionSpecification
	 * @param mouseInt -Specify the used MouseInterface
	 */
	public Mouse(ResolutionSpecification resSpecs, MouseInterface mouseInt, long window) {
		this.resSpecs = resSpecs;
		this.mouseInt = mouseInt;
		glfwSetCursorPosCallback(window, mousePositionCallback);
		glfwSetMouseButtonCallback(window, mouseButtonCallback);
	}

	/**
	 * This needs to run every frame. It updates the pushed Buttons on the Mouse.
	 */
	public void update() {
		for (int i = 0; i < mouseState.length; i++) {
			if(mouseState[i]) {
				chooseActionPressed[i].choose();
				if(click[i]) {
					click[i] = false;
					chooseActionClick[i].choose();
				}
			}
		}
	}
	
	protected GLFWCursorPosCallbackI mousePositionCallback = new GLFWCursorPosCallbackI() {
		
		@Override
		public void invoke(long window, double xpos, double ypos) {
			mousePos.set((int)xpos, Scalar.getY((int)ypos, resSpecs.height));
			if (camera != null) {
				mousePos.add(camera.zeroPoint.toVec2i());
			}
		}
	};
	
	protected GLFWMouseButtonCallbackI mouseButtonCallback = new GLFWMouseButtonCallbackI() {
		
		@Override
		public void invoke(long window, int button, int action, int mods) {
			if (action == GLFW_PRESS) {
				mouseState[button] = true;
				click[button] = true;
			}
			
			else if (action == GLFW_RELEASE) {
				chooseActionReleased[button].choose();
				mouseState[button] = false;
			}
		}
	};
	
	public void addCamera(Camera camera) {
		this.camera = camera;
	}
	
	/**
	 * Returns the current Position of the mouse
	 * @return -Vec2i
	 */
	public Vec2i getMousePos() {
		return mousePos;
	}

	/**
	 * Sets the MouseInterface to a new
	 * @param mouseInt -new MouseInterface
	 */
	public void setMouseInt(MouseInterface mouseInt) {
		this.mouseInt = mouseInt;
	}

	// To choose wich method is used in for-loop
	private interface ChooseAction {
		/**
		 * This is a dummy for deciding, which actual method is used in update.
		 * Though java is not able to manage an array with methods, this will do the same.
		 */
		public void choose();
	}

	//@formatter:off
	private ChooseAction[] chooseActionClick = new ChooseAction[] {
			new ChooseAction() { public void choose() {mouseInt.leftMouseClick();}},
			new ChooseAction() { public void choose() {mouseInt.rightMouseClick();}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}}
		};
	private ChooseAction[] chooseActionPressed = new ChooseAction[] {
			new ChooseAction() { public void choose() {mouseInt.leftMousePressed();}},
			new ChooseAction() { public void choose() {mouseInt.rightMousePressed();}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}}
	};
	private ChooseAction[] chooseActionReleased = new ChooseAction[] {
			new ChooseAction() { public void choose() {mouseInt.leftMouseReleased();}},
			new ChooseAction() { public void choose() {mouseInt.rightMouseReleased();}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}}
	};
	//@formatter:on

}
