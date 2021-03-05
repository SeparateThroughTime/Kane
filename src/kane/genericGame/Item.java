package kane.genericGame;

import kane.renderer.SpriteController;

public abstract class Item {
	int amount;
	String name;
	SpriteController spriteController;
	
	public Item(String name, SpriteController spriteController) {
		this.name = name;
		this.spriteController = spriteController;
	}
	
	public abstract void attack();
	public abstract void react();
	public abstract void jump();
	public abstract void move();
	
	public void addAmount(int amount) {
		this.amount += amount;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public SpriteController getSpriteController() {
		return spriteController;
	}
	
	public String getName() {
		return name;
	}
}
