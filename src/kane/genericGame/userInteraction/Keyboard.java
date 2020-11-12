package kane.genericGame.userInteraction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The Keyboard is managing all actions, when keys on the hw-keyboard are pressed.
 */
public class Keyboard implements KeyListener{
	
	private final int NUM_BUTTONS = 128;
	protected boolean[] keyState = new boolean[NUM_BUTTONS];
	protected boolean[] click = new boolean[NUM_BUTTONS];
	protected boolean[] clickAgain = new boolean[NUM_BUTTONS];
	
	private KeyboardInterface keyInt;
	
	/**
	 * 
	 * @param keyInt -Specifies the used KeyboardInterface.
	 */
	public Keyboard(KeyboardInterface keyInt) {
		generateChooseAction();
		this.keyInt = keyInt;
		
		for (int i = 0; i < NUM_BUTTONS; i++) {
			clickAgain[i] = true;
		}
	}

	/**
	 * This needs to run every frame. It updates the pushed Buttons on the Keyboard.
	 */
	public void update() {
		for (int i = 0; i < keyState.length; i++) {
			if(keyState[i]) {
				chooseActionPressed[i].choose();
				if(click[i] && clickAgain[i]) {
					click[i] = false;
					clickAgain[i] = false;
					chooseActionClick[i].choose();
				}
			}
		}
	}
	
	/**
	 * Set the KeyboardInterface
	 * @param keyInt -New KeyboardInterface
	 */
	public void setKeyInt(KeyboardInterface keyInt) {
		this.keyInt = keyInt;
	}
	
	//KeyListerner
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			keyState[e.getKeyCode()] = true;
			click[e.getKeyCode()] = true;
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			keyState[e.getKeyCode()] = false;
			chooseActionReleased[e.getKeyCode()].choose();
			clickAgain[e.getKeyCode()] = true;

		}
		
		
		// To choose which method is used in update
		private interface ChooseAction {
			/**
			 * This is a dummy for deciding, which actual method is used in update.
			 * Though java is not able to manage an array with methods, this will do the same.
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
			chooseActionClick[37] = new ChooseAction() { public void choose() {keyInt.leftArrowClick();}};
			chooseActionPressed[37] = new ChooseAction() { public void choose() {keyInt.leftArrowPressed();}};
			chooseActionReleased[37] = new ChooseAction() { public void choose() {keyInt.leftArrowReleased();}};
			
			chooseActionClick[38] = new ChooseAction() { public void choose() {keyInt.upArrowClick();}};
			chooseActionPressed[38] = new ChooseAction() { public void choose() {keyInt.upArrowPressed();}};
			chooseActionReleased[38] = new ChooseAction() { public void choose() {keyInt.upArrowReleased();}};
			
			chooseActionClick[39] = new ChooseAction() { public void choose() {keyInt.rightArrowClick();}};
			chooseActionPressed[39] = new ChooseAction() { public void choose() {keyInt.rightArrowPressed();}};
			chooseActionReleased[39] = new ChooseAction() { public void choose() {keyInt.rightArrowReleased();}};
			
			chooseActionClick[40] = new ChooseAction() { public void choose() {keyInt.downArrowClick();}};
			chooseActionPressed[40] = new ChooseAction() { public void choose() {keyInt.downArrowPressed();}};
			chooseActionReleased[40] = new ChooseAction() { public void choose() {keyInt.downArrowReleased();}};
			
			chooseActionClick[112] = new ChooseAction() { public void choose() {keyInt.f1Click();}};
			chooseActionClick[113] = new ChooseAction() { public void choose() {keyInt.f2Click();}};
			chooseActionClick[114] = new ChooseAction() { public void choose() {keyInt.f3Click();}};
			chooseActionClick[115] = new ChooseAction() { public void choose() {keyInt.f4Click();}};
			chooseActionClick[116] = new ChooseAction() { public void choose() {keyInt.f5Click();}};
			chooseActionClick[117] = new ChooseAction() { public void choose() {keyInt.f6Click();}};
			chooseActionClick[118] = new ChooseAction() { public void choose() {keyInt.f7Click();}};
			chooseActionClick[119] = new ChooseAction() { public void choose() {keyInt.f8Click();}};
			chooseActionClick[120] = new ChooseAction() { public void choose() {keyInt.f9Click();}};
			chooseActionClick[121] = new ChooseAction() { public void choose() {keyInt.f10Click();}};
			chooseActionClick[122] = new ChooseAction() { public void choose() {keyInt.f11Click();}};
			chooseActionClick[123] = new ChooseAction() { public void choose() {keyInt.f12Click();}};
			
			chooseActionPressed[16] = new ChooseAction() { public void choose() {keyInt.shiftPressed();}};
			chooseActionReleased[16] = new ChooseAction() { public void choose() {keyInt.shiftReleased();}};
			chooseActionClick[16] = new ChooseAction() { public void choose() {keyInt.shiftClick();}};
			
			chooseActionPressed[67] = new ChooseAction() { public void choose() {keyInt.cPressed();}};
			chooseActionReleased[67] = new ChooseAction() { public void choose() {keyInt.cReleased();}};
			chooseActionClick[67] = new ChooseAction() { public void choose() {keyInt.cClick();}};
			
			chooseActionPressed[32] = new ChooseAction() { public void choose() {keyInt.spacePressed();}};
			chooseActionReleased[32] = new ChooseAction() { public void choose() {keyInt.spaceReleased();}};
			chooseActionClick[32] = new ChooseAction() { public void choose() {keyInt.spaceClick();}};
			
			chooseActionClick[27] = new ChooseAction() { public void choose() {keyInt.escClick();}};
			
			chooseActionPressed[73] = new ChooseAction() { public void choose() {keyInt.iPressed();}};
			chooseActionReleased[73] = new ChooseAction() { public void choose() {keyInt.iReleased();}};
			chooseActionClick[73] = new ChooseAction() { public void choose() {keyInt.iClick();}};
		}
		//@formatter:on
}
