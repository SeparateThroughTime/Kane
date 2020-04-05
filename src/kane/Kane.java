/*TODO
	Rotation
	Friction stops after jump
	Moving Camera -> what will be rendered?
	Events (Contact Listener)
		World Events
		Items
		Inventory
	Moving Character
	Level Ends/ Player dies -> Next level/ Restart
	Resizable Window
	Sprites
	Sounds
	Object Editor
		Ermitteln des besten Mittelpunkts
	Level Editor
	Event Editor?
	Campain Editor
	StartMenu
	

*/
package kane;

import kane.genericGame.Game;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.shapes.Box;
import kane.physics.shapes.Plane;

public class Kane extends Game {

	public Kane(String title) {
		super(title);
	}

	public static void main(String[] args) {
		Kane game = new Kane("Kane");
		game.run();

	}

	Material mStatic = new Material(0, 1f);
	Material mDynamic = new Material(1, 0.7f);
	Body player;

	@Override
	protected void initGame() {

		Body body = new Body(0, 0, mStatic);
		body.addShape(new Plane(new Vec2f(0, 1), 30, WIDTH - 1, body, 0x0000FF));
		physics.addBody(body);

		body = new Body(0, 0, mStatic);
		body.addShape(new Plane(new Vec2f(0, -1), -(HEIGHT - 1) + 30, -(WIDTH - 1), body, 0x0000FF));
		physics.addBody(body);

		body = new Body(0, 0, mStatic);
		body.addShape(new Plane(new Vec2f(1, 0), 30, -(HEIGHT - 1), body, 0x0000FF));
		physics.addBody(body);

		body = new Body(0, 0, mStatic);
		body.addShape(new Plane(new Vec2f(-1, 0), -(WIDTH - 1) + 30, HEIGHT - 1, body, 0x0000FF));
		physics.addBody(body);

		player = new Body(100, 130, mDynamic);
		player.addShape(new Box(0, 0, player, new Vec2f(10, 20), 0x00ff00));
		physics.addBody(player);
	}

	@Override
	public void leftMousePressed() {

	}

	@Override
	public void leftMouseReleased() {
	}

	@Override
	public void leftMouseClick() {

	}

	@Override
	public void rightMousePressed() {
	}

	@Override
	public void rightMouseReleased() {

	}

	@Override
	public void rightMouseClick() {
	}

	@Override
	public void leftArrowPressed() {
		if (-player.getVel().getX() <= 300) {
			player.getAcc().sub(new Vec2f(40 / DELTATIME, 0));
		}

	}

	@Override
	public void leftArrowReleased() {

	}

	@Override
	public void rightArrowPressed() {
		if (player.getVel().getX() <= 300) {
			player.getAcc().add(new Vec2f(40 / DELTATIME, 0));
		}
		System.out.println(player.getVel());
	}

	@Override
	public void rightArrowReleased() {

	}

	@Override
	public void upArrowPressed() {

	}

	@Override
	public void upArrowReleased() {

	}

	@Override
	public void downArrowPressed() {

	}

	@Override
	public void downArrowReleased() {

	}

	@Override
	public void f1Click() {

	}

	@Override
	// show Contacts
	public void f2Click() {
		renderer.showContacts = !renderer.showContacts;

	}

	@Override
	// show AABBs
	public void f3Click() {
		renderer.showAABBs = !renderer.showAABBs;

	}

	@Override
	public void f4Click() {

	}

	@Override
	public void f5Click() {

	}

	@Override
	public void f6Click() {
	}

	@Override
	public void f7Click() {
	}

	@Override
	public void f8Click() {

	}

	@Override
	public void f9Click() {

	}

	@Override
	public void f10Click() {

	}

	@Override
	public void f11Click() {

	}

	@Override
	public void f12Click() {

	}

	@Override
	public void spacePressed() {

	}

	@Override
	public void spaceReleased() {

	}

	@Override
	public void spaceClick() {
		player.getAcc().add(new Vec2f(0, 200 / DELTATIME));
	}

	@Override
	public void shiftPressed() {
		
	}

	@Override
	public void shiftReleased() {

	}

	@Override
	public void shiftClick() {

	}

	@Override
	public void escClick() {

	}

	@Override
	public void cPressed() {

	}

	@Override
	public void cReleased() {

	}

	@Override
	public void cClick() {

	}
}
