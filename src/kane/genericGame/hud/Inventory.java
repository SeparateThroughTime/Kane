package kane.genericGame.hud;

import kane.genericGame.Item;
import kane.genericGame.PassiveAttributes;
import kane.genericGame.item.NONE;
import kane.genericGame.item.SWORD;
import kane.math.Vec2f;
import kane.physics.Shape;
import kane.renderer.ResolutionSpecification;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

import java.util.ArrayList;

public class Inventory{

    public static Inventory INVENTORY;

    public static final int NUM_SLOTS = 8;

    private final ResolutionSpecification resSpecs;
    private final ArrayList<Item> items;
    private final Shape mainShape;
    private final Shape[] slotShapes;

    private Inventory(Shape mainShape, Shape[] slotShapes, ResolutionSpecification resSpecs){
        this.mainShape = mainShape;
        this.slotShapes = slotShapes;
        this.resSpecs = resSpecs;
        items = new ArrayList<>();

        createInventory();
        createItems();
    }

    public static void initializeInventory(Shape mainShape, Shape[] slotShapes, ResolutionSpecification resSpecs){
        if (INVENTORY == null){
            INVENTORY = new Inventory(mainShape, slotShapes, resSpecs);
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
                new Vec2f((float) -resSpecs.gameWidth / 4 - 24, (float) -resSpecs.GAME_HEIGHT / 4 + 24);
        mainShape.setSpriteControllers(spriteControllers);

        // Slots
        for (Shape shape : slotShapes){
            shape.visible = false;
            shape.collision = false;
            shape.addPassiveAttribute(PassiveAttributes.INVENTORY);
        }

    }

    public void changeResolution(){
        // At the moment nothing tbd.
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
            if (shape.hasPassiveAttribute(PassiveAttributes.INVENTORY)){
                shape.visible = visible;
            }
        }

        if (visible){
            showItems();
        }
    }

    public boolean isVisible(){
        return mainShape.visible;
    }
}
