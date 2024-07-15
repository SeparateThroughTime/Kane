package kane.genericGame;

import kane.renderer.SpriteController;

public abstract class Item{
    int amount;
    String name;
    SpriteController[] itemSpriteControllers;
    SpriteController[] playerSpriteControllers;

    public Item(String name, SpriteController[] itemSpriteControllers, SpriteController[] playerSpriteControllers){
        this.name = name;
        this.itemSpriteControllers = itemSpriteControllers;
        this.playerSpriteControllers = playerSpriteControllers;
    }

    public abstract void attack();

    public abstract void react();

    public abstract void jump();

    public abstract void move();

    public void addAmount(int amount){
        this.amount += amount;
    }

    public int getAmount(){
        return amount;
    }

    public SpriteController[] getItemSpriteControllers(){
        return itemSpriteControllers;
    }

    public SpriteController[] getPlayerSpriteControllers(){
        return playerSpriteControllers;
    }

    public String getName(){
        return name;
    }
}
