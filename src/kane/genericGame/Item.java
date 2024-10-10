package kane.genericGame;

import kane.exceptions.LoadJsonException;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

import static kane.genericGame.JsonManager.JSON_MANAGER;

public abstract class Item{
    int amount;
    String name;
    SpriteController[] itemSpriteControllers;
    SpriteController[] playerSpriteControllers;

    public Item(String name, String itemSpriteControllerFilepath, String playerSpriteControllerFilepath){
        this.name = name;
        try{
            this.itemSpriteControllers = JSON_MANAGER.loadSpriteControllers(itemSpriteControllerFilepath);
            this.playerSpriteControllers = JSON_MANAGER.loadSpriteControllers(playerSpriteControllerFilepath);
        } catch (LoadJsonException e){
            throw new RuntimeException(e);
        }

        this.itemSpriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);

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
