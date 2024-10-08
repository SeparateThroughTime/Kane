package kane.genericGame.hud;

import kane.genericGame.Item;
import kane.genericGame.PassiveAttributes;
import kane.genericGame.item.NONE;
import kane.genericGame.item.SWORD;
import kane.math.Vec2f;
import kane.physics.Shape;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

import java.util.ArrayList;

import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.Kane.GAME;

public class Inventory{

    public static Inventory INVENTORY;

    public static final int NUM_SLOTS = 8;

    private final ArrayList<Item> items;
    private final HudElement mainShape;
    private final HudElement[] slotShapes;

    private Inventory(){

        mainShape = new HudElement(new Vec2f(0, 0), new Vec2f(0, 0), 3, false);
        GAME.addGuiElement(mainShape);

        slotShapes = new HudElement[8];
        slotShapes[0] = new HudElement(new Vec2f(-24, 8), new Vec2f(5.33333f, 5.33333f), 4, false);
        slotShapes[1] = new HudElement(new Vec2f(-8, 8), new Vec2f(5.33333f, 5.33333f), 4, false);
        slotShapes[2] = new HudElement(new Vec2f(8, 8), new Vec2f(5.33333f, 5.33333f), 4, false);
        slotShapes[3] = new HudElement(new Vec2f(24, 8), new Vec2f(5.33333f, 5.33333f), 4, false);
        slotShapes[4] = new HudElement(new Vec2f(-24, -8), new Vec2f(5.33333f, 5.33333f), 4, false);
        slotShapes[5] = new HudElement(new Vec2f(-8, -8), new Vec2f(5.33333f, 5.33333f), 4, false);
        slotShapes[6] = new HudElement(new Vec2f(8, -8), new Vec2f(5.33333f, 5.33333f), 4, false);
        slotShapes[7] = new HudElement(new Vec2f(24, -8), new Vec2f(5.33333f, 5.33333f), 4, false);
        for (HudElement slotShape : slotShapes){
            GAME.addGuiElement(slotShape);
        }

        items = new ArrayList<>();

        createInventory();
        createItems();
    }

    public static void initializeInventory(){
        if (INVENTORY == null){
            INVENTORY = new Inventory();
        }
    }

    private void createItems(){
        items.add(new NONE());
        items.add(new SWORD());
    }

    private void createInventory(){
        mainShape.visible = false;
        mainShape.collision = false;
        mainShape.addPassiveAttribute(PassiveAttributes.INVENTORY);

        Sprite sprite = new Sprite("sprites\\interface\\Inventory.png", 224, 128);
        sprite.addState(SpriteState.STATIC, new int[]{0});
        SpriteController[] spriteControllers = new SpriteController[1];
        spriteControllers[0] = new SpriteController(sprite);
        spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
        spriteControllers[0].spritePosOffset =
                new Vec2f((float) -RES_SPECS.gameWidth / 4 - 24, (float) -RES_SPECS.GAME_HEIGHT / 4 + 24);
        mainShape.setSpriteControllers(spriteControllers);

        // Slots
        for (Shape shape : slotShapes){
            shape.visible = false;
            shape.collision = false;
            shape.addPassiveAttribute(PassiveAttributes.INVENTORY);
        }

    }

    public void changeResolution(){
        mainShape.getSpriteControllers()[0].spritePosOffset =
                new Vec2f((float) -RES_SPECS.gameWidth / 4 - 24, (float) -RES_SPECS.GAME_HEIGHT / 4 + 24);
    }

    public Item getItem(String name){
        for (Item item : items){
            if (item.getName().equals(name)){
                return item;
            }
        }
        return null;
    }

    public Item getItem(int index){
        int counter = 0;
        for (Item item : items){
            if (item.getAmount() > 0){
                if (counter == index){
                    return item;
                }
                counter++;
            }
        }
        return null;
    }

    private void showItems(){
        ArrayList<Item> activeItems = new ArrayList<>();
        for (Item item : items){
            if (item.getAmount() > 0){
                activeItems.add(item);
            }
        }

        for (int i = 0; i < slotShapes.length; i++){
            try{
                Item item = activeItems.get(i - 1);
                slotShapes[i].setSpriteControllers(item.getItemSpriteControllers());
                slotShapes[i].visible = true;
            } catch (Exception e){
                slotShapes[i].visible = false;
            }
        }
    }

    public Shape getSlot(int i){
        return slotShapes[i];
    }

    public void setVisible(boolean visible){
        mainShape.visible = visible;

        for (Shape shape : slotShapes){
            shape.visible = visible;
        }

        if (visible){
            showItems();
        }
    }

    public boolean isVisible(){
        return mainShape.visible;
    }
}
