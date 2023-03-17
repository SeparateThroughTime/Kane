package kane.genericGame.userInteraction;

import static kane.renderer.Camera.CAMERA;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import kane.math.Scalar;
import kane.math.Vec2i;

/**
 * The Mouse is managing all actions, when keys on the hw-mouse are pressed.
 */
public class Mouse {

	public static Mouse MOUSE;

	public final int NUMBUTTONS = 16;
	public boolean click[] = new boolean[NUMBUTTONS];

	public Vec2i mousePos = new Vec2i();
	public boolean[] mouseState = new boolean[NUMBUTTONS];
	private MouseInterface mouseInt;

	/**
	 * 
	 * @param resSpecs -Used ResolutionSpecification
	 * @param mouseInt -Specify the used MouseInterface
	 */
	private Mouse(MouseInterface mouseInt, long window) {
		this.mouseInt = mouseInt;
		glfwSetCursorPosCallback(window, mousePositionCallback);
		glfwSetMouseButtonCallback(window, mouseButtonCallback);
	}

	public static void initializeMouse(MouseInterface mouseInt, long window) {
		if (MOUSE == null) {
			MOUSE = new Mouse(mouseInt, window);
		}
	}

	/**
	 * This needs to run every frame. It updates the pushed Buttons on the Mouse.
	 */
	public void update() {
		for (int i = 0; i < mouseState.length; i++) {
			if (mouseState[i]) {
				chooseActionPressed[i].choose();
				if (click[i]) {
					click[i] = false;
					chooseActionClick[i].choose();
				}
			}
		}
	}

	protected GLFWCursorPosCallbackI mousePositionCallback = new GLFWCursorPosCallbackI() {

		@Override
		public void invoke(long window, double xpos, double ypos) {
			mousePos.set((int) xpos, Scalar.getY((int) ypos, RES_SPECS.height));
			if (CAMERA != null) {
				mousePos.add(CAMERA.zeroPoint.toVec2i());
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

	// To choose wich method is used in for-loop
	private interface ChooseAction {
		/**
		 * This is a dummy for deciding, which actual method is used in update. Though
		 * java is not able to manage an array with methods, this will do the same.
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
