package kane.genericGame.userInteraction;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import kane.math.Scalar;
import kane.math.Vec2i;
import kane.physics.Physics;

public class Mouse implements MouseListener, MouseMotionListener{

	private final int NUMBUTTONS = 16;
	private boolean click[] = new boolean[NUMBUTTONS];
	private final int HEIGHT;

	protected Vec2i mousePos = new Vec2i();
	private final Physics physics;
	protected boolean[] mouseState = new boolean[NUMBUTTONS];
	private MouseInterface mouseInt;

	public Mouse(Physics physics, int height, MouseInterface mouseInt) {
		this.physics = physics;
		HEIGHT = height;
		this.mouseInt = mouseInt;
	}

	public void update() {
		// Mouse Interarction
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
	
	public Vec2i getMousePos() {
		return mousePos;
	}

	public void setMouseInt(MouseInterface mouseInt) {
		this.mouseInt = mouseInt;
	}

	// MouseMotionListener
	@Override
	public void mouseDragged(MouseEvent e) {
		mousePos.set(e.getX(), Scalar.getY(e.getY(), HEIGHT));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePos.set(e.getX(), Scalar.getY(e.getY(), HEIGHT));
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
