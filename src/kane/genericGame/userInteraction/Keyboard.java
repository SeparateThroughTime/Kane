package kane.genericGame.userInteraction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The Keyboard is managing all actions, when keys on the hw-keyboard are
 * pressed.
 */
public class Keyboard{

	private final int NUM_BUTTONS = 128;
	protected boolean[] keyState = new boolean[NUM_BUTTONS];
	protected boolean[] click = new boolean[NUM_BUTTONS];
	protected boolean[] clickAgain = new boolean[NUM_BUTTONS];

	private KeyboardInterface keyInt;

	/**
	 * 
	 * @param keyInt -Specifies the used KeyboardInterface.
	 */
	public Keyboard(KeyboardInterface keyInt, long window) {
		generateChooseAction();
		this.keyInt = keyInt;

		for (int i = 0; i < NUM_BUTTONS; i++) {
			clickAgain[i] = true;
		}
		
		glfwSetKeyCallback(window, keyCallback );
	}

	//@formatter:off
	public boolean isKeyPressed(Keys key) {
		switch (key) {
		case LEFT: return keyState[GLFW_KEY_LEFT];
		case UP: return keyState[GLFW_KEY_UP];
		case RIGHT: return keyState[GLFW_KEY_RIGHT];
		case DOWN: return keyState[GLFW_KEY_DOWN];
		case F1: return keyState[GLFW_KEY_F1];
		case F2: return keyState[GLFW_KEY_F2];
		case F3: return keyState[GLFW_KEY_F3];
		case F4: return keyState[GLFW_KEY_F4];
		case F5: return keyState[GLFW_KEY_F5];
		case F6: return keyState[GLFW_KEY_F6];
		case F7: return keyState[GLFW_KEY_F7];
		case F8: return keyState[GLFW_KEY_F8];
		case F9: return keyState[GLFW_KEY_F9];
		case F10: return keyState[GLFW_KEY_F10];
		case F11: return keyState[GLFW_KEY_F11];
		case F12: return keyState[GLFW_KEY_F12];
		case SHIFT: return keyState[GLFW_KEY_LEFT_SHIFT];
		case C: return keyState[GLFW_KEY_C];
		case SPACE: return keyState[GLFW_KEY_SPACE];
		case ESC: return keyState[GLFW_KEY_ESCAPE];
		case I: return keyState[GLFW_KEY_I];

		default: return false;
		}
	}
	
	protected GLFWKeyCallbackI keyCallback = new GLFWKeyCallbackI() {
		
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (action == GLFW_PRESS) {
				keyState[key] = true;
				click[key] = true;
			}
			
			if (action == GLFW_RELEASE) {
				keyState[key] = false;
				chooseActionReleased[key].choose();
				clickAgain[key] = true;
			}
		}
	};
	
	//@formatter:on

	/**
	 * This needs to run every frame. It updates the pushed Buttons on the Keyboard.
	 */
	public void update() {
		for (int i = 0; i < keyState.length; i++) {
			if (keyState[i]) {
				chooseActionPressed[i].choose();
				if (click[i] && clickAgain[i]) {
					click[i] = false;
					clickAgain[i] = false;
					chooseActionClick[i].choose();
				}
			}
		}
	}

	/**
	 * Set the KeyboardInterface
	 * 
	 * @param keyInt -New KeyboardInterface
	 */
	public void setKeyInt(KeyboardInterface keyInt) {
		this.keyInt = keyInt;
	}

	// To choose which method is used in update
	private interface ChooseAction {
		/**
		 * This is a dummy for deciding, which actual method is used in update. Though
		 * java is not able to manage an array with methods, this will do the same.
		 */
		public void choose();
	}

	//@formatter:off
		private ChooseAction[] chooseActionClick = new ChooseAction[NUM_BUTTONS];
		private ChooseAction[] chooseActionPressed = new ChooseAction[NUM_BUTTONS];
		private ChooseAction[] chooseActionReleased = new ChooseAction[NUM_BUTTONS];
		private void generateChooseAction() {
			for (int i = 0; i < NUM_BUTTONS; i++) {
				chooseActionClick[i] = new ChooseAction() { public void choose() {}};
				chooseActionPressed[i] = new ChooseAction() { public void choose() {}};
				chooseActionReleased[i] = new ChooseAction() { public void choose() {}};
			}
			chooseActionClick[GLFW_KEY_LEFT] = new ChooseAction() { public void choose() {keyInt.leftArrowClick();}};
			chooseActionPressed[GLFW_KEY_LEFT] = new ChooseAction() { public void choose() {keyInt.leftArrowPressed();}};
			chooseActionReleased[GLFW_KEY_LEFT] = new ChooseAction() { public void choose() {keyInt.leftArrowReleased();}};
			
			chooseActionClick[GLFW_KEY_UP] = new ChooseAction() { public void choose() {keyInt.upArrowClick();}};
			chooseActionPressed[GLFW_KEY_UP] = new ChooseAction() { public void choose() {keyInt.upArrowPressed();}};
			chooseActionReleased[GLFW_KEY_UP] = new ChooseAction() { public void choose() {keyInt.upArrowReleased();}};
			
			chooseActionClick[GLFW_KEY_RIGHT] = new ChooseAction() { public void choose() {keyInt.rightArrowClick();}};
			chooseActionPressed[GLFW_KEY_RIGHT] = new ChooseAction() { public void choose() {keyInt.rightArrowPressed();}};
			chooseActionReleased[GLFW_KEY_RIGHT] = new ChooseAction() { public void choose() {keyInt.rightArrowReleased();}};
			
			chooseActionClick[GLFW_KEY_DOWN] = new ChooseAction() { public void choose() {keyInt.downArrowClick();}};
			chooseActionPressed[GLFW_KEY_DOWN] = new ChooseAction() { public void choose() {keyInt.downArrowPressed();}};
			chooseActionReleased[GLFW_KEY_DOWN] = new ChooseAction() { public void choose() {keyInt.downArrowReleased();}};
			
			chooseActionClick[GLFW_KEY_F1] = new ChooseAction() { public void choose() {keyInt.f1Click();}};
			chooseActionClick[GLFW_KEY_F2] = new ChooseAction() { public void choose() {keyInt.f2Click();}};
			chooseActionClick[GLFW_KEY_F3] = new ChooseAction() { public void choose() {keyInt.f3Click();}};
			chooseActionClick[GLFW_KEY_F4] = new ChooseAction() { public void choose() {keyInt.f4Click();}};
			chooseActionClick[GLFW_KEY_F5] = new ChooseAction() { public void choose() {keyInt.f5Click();}};
			chooseActionClick[GLFW_KEY_F6] = new ChooseAction() { public void choose() {keyInt.f6Click();}};
			chooseActionClick[GLFW_KEY_F7] = new ChooseAction() { public void choose() {keyInt.f7Click();}};
			chooseActionClick[GLFW_KEY_F8] = new ChooseAction() { public void choose() {keyInt.f8Click();}};
			chooseActionClick[GLFW_KEY_F9] = new ChooseAction() { public void choose() {keyInt.f9Click();}};
			chooseActionClick[GLFW_KEY_F10] = new ChooseAction() { public void choose() {keyInt.f10Click();}};
			chooseActionClick[GLFW_KEY_F11] = new ChooseAction() { public void choose() {keyInt.f11Click();}};
			chooseActionClick[GLFW_KEY_F12] = new ChooseAction() { public void choose() {keyInt.f12Click();}};
			
			chooseActionPressed[GLFW_KEY_LEFT_SHIFT] = new ChooseAction() { public void choose() {keyInt.shiftPressed();}};
			chooseActionReleased[GLFW_KEY_LEFT_SHIFT] = new ChooseAction() { public void choose() {keyInt.shiftReleased();}};
			chooseActionClick[GLFW_KEY_LEFT_SHIFT] = new ChooseAction() { public void choose() {keyInt.shiftClick();}};
			
			chooseActionPressed[GLFW_KEY_C] = new ChooseAction() { public void choose() {keyInt.cPressed();}};
			chooseActionReleased[GLFW_KEY_C] = new ChooseAction() { public void choose() {keyInt.cReleased();}};
			chooseActionClick[GLFW_KEY_C] = new ChooseAction() { public void choose() {keyInt.cClick();}};
			
			chooseActionPressed[GLFW_KEY_SPACE] = new ChooseAction() { public void choose() {keyInt.spacePressed();}};
			chooseActionReleased[GLFW_KEY_SPACE] = new ChooseAction() { public void choose() {keyInt.spaceReleased();}};
			chooseActionClick[GLFW_KEY_SPACE] = new ChooseAction() { public void choose() {keyInt.spaceClick();}};
			
			chooseActionClick[GLFW_KEY_ESCAPE] = new ChooseAction() { public void choose() {keyInt.escClick();}};
			
			chooseActionPressed[GLFW_KEY_I] = new ChooseAction() { public void choose() {keyInt.iPressed();}};
			chooseActionReleased[GLFW_KEY_I] = new ChooseAction() { public void choose() {keyInt.iReleased();}};
			chooseActionClick[GLFW_KEY_I] = new ChooseAction() { public void choose() {keyInt.iClick();}};
		}
		//@formatter:on
}
