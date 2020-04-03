package kane.genericGame.userInteraction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import kane.genericGame.Renderer;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Physics;
import kane.physics.Shape;
import kane.physics.ShapeType;

public class Keyboard implements KeyListener{
	
	private final int NUM_BUTTONS = 128;
	protected boolean[] keyState = new boolean[NUM_BUTTONS];
	protected boolean[] click = new boolean[NUM_BUTTONS];
	protected boolean[] clickAgain = new boolean[NUM_BUTTONS];
	
	private final float DELTATIME;
	private final Renderer renderer;
	private final Physics physics;
	private KeyboardInterface keyInt;
	
	public Keyboard(float deltaTime, Renderer renderer, Physics physics, KeyboardInterface keyInt) {
		generateChooseAction();
		this.DELTATIME = deltaTime;
		this.physics = physics;
		this.renderer = renderer;
		this.keyInt = keyInt;
		
		for (int i = 0; i < NUM_BUTTONS; i++) {
			clickAgain[i] = true;
		}
	}

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
		
		
		// To choose wich method is used in update
		private interface ChooseAction {
			public void choose();
		}

		//@formatter:off
		//left up right down 37 38 39 40          113 114
		private ChooseAction[] chooseActionClick = new ChooseAction[NUM_BUTTONS];
		private ChooseAction[] chooseActionPressed = new ChooseAction[NUM_BUTTONS];
		private ChooseAction[] chooseActionReleased = new ChooseAction[NUM_BUTTONS];
		private void generateChooseAction() {
			for (int i = 0; i < NUM_BUTTONS; i++) {
				chooseActionClick[i] = new ChooseAction() { public void choose() {}};
				chooseActionPressed[i] = new ChooseAction() { public void choose() {}};
				chooseActionReleased[i] = new ChooseAction() { public void choose() {}};
			}
			chooseActionPressed[37] = new ChooseAction() { public void choose() {keyInt.leftArrowPressed();}};
			chooseActionReleased[37] = new ChooseAction() { public void choose() {keyInt.leftArrowReleased();}};
			
			chooseActionPressed[38] = new ChooseAction() { public void choose() {keyInt.upArrowPressed();}};
			chooseActionReleased[38] = new ChooseAction() { public void choose() {keyInt.upArrowReleased();}};
			
			chooseActionPressed[39] = new ChooseAction() { public void choose() {keyInt.rightArrowPressed();}};
			chooseActionReleased[39] = new ChooseAction() { public void choose() {keyInt.rightArrowReleased();}};
			
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
		}
		//@formatter:on
}
