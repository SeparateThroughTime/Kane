package kane.genericGame.userInteraction;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import kane.math.Scalar;
import kane.math.Vec2i;
import kane.renderer.ResolutionSpecification;

/**
 * The Mouse is managing all actions, when keys on the hw-mouse are pressed.
 */
public class Mouse implements MouseListener, MouseMotionListener{

	private final int NUMBUTTONS = 16;
	private boolean click[] = new boolean[NUMBUTTONS];
	private ResolutionSpecification resSpecs;

	protected Vec2i mousePos = new Vec2i();
	protected boolean[] mouseState = new boolean[NUMBUTTONS];
	private MouseInterface mouseInt;

	/**
	 * 
	 * @param resSpecs -Used ResolutionSpecification
	 * @param mouseInt -Specify the used MouseInterface
	 */
	public Mouse(ResolutionSpecification resSpecs, MouseInterface mouseInt) {
		this.resSpecs = resSpecs;
		this.mouseInt = mouseInt;
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

	// MouseMotionListener
	@Override
	public void mouseDragged(MouseEvent e) {
		mousePos.set(e.getX(), Scalar.getY(e.getY(), resSpecs.height));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePos.set(e.getX(), Scalar.getY(e.getY(), resSpecs.height));
	}

	// MouseListener
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseState[e.getButton()] = true;
		click[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		chooseActionReleased[e.getButton()].choose();
		mouseState[e.getButton()] = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

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
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {mouseInt.leftMouseClick();}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {mouseInt.rightMouseClick();}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}}
		};
	private ChooseAction[] chooseActionPressed = new ChooseAction[] {
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {mouseInt.leftMousePressed();}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {mouseInt.rightMousePressed();}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}}
	};
	private ChooseAction[] chooseActionReleased = new ChooseAction[] {
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {mouseInt.leftMouseReleased();}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {mouseInt.rightMouseReleased();}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}},
			new ChooseAction() { public void choose() {}}
	};
	//@formatter:on

}
