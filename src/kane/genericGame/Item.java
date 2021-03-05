package kane.genericGame;

import kane.renderer.SpriteController;

public abstract class Item {
	int amount;
	String name;
	SpriteController itemSpriteController;
	SpriteController playerSpriteController;
	
	public Item(String name, SpriteController itemSpriteController, SpriteController playerSpriteController) {
		this.name = name;
		this.itemSpriteController = itemSpriteController;
		this.playerSpriteController = playerSpriteController;
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
	
	public SpriteController getItemSpriteController() {
		return itemSpriteController;
	}
	
	public SpriteController getPlayerSpriteController() {
		return playerSpriteController;
	}
	
	public String getName() {
		return name;
	}
}
