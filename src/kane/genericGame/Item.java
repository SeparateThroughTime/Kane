package kane.genericGame;

public abstract class Item {
	int amount;
	String name;
	
	public abstract void attack();
	public abstract void react();
	public abstract void jump();
	public abstract void move();
}
