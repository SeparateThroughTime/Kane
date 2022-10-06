package kane.genericGame.hud;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import kane.genericGame.Item;
import kane.genericGame.PassiveAttributes;
import kane.genericGame.item.NONE;
import kane.genericGame.item.SWORD;
import kane.math.Vec2f;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.renderer.ResolutionSpecification;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

public class Inventory {
	public static final int NUM_SLOTS = 8;

	private Material mInterface = new Material(1, 0);
	private ResolutionSpecification resSpecs;
	private ArrayList<Item> items;
	private Shape mainShape;
	private Shape[] slotShapes;

	public Inventory(Shape mainShape, Shape[] slotShapes, ResolutionSpecification resSpecs) {
		this.mainShape = mainShape;
		this.slotShapes = slotShapes;
		this.resSpecs = resSpecs;
		items = new ArrayList<Item>();

		createInventory();
		createItems();
	}

	private void createItems() {
		items.add(new NONE());
		items.add(new SWORD());
	}

	private void createInventory() {
		mainShape.setVisible(false);
		mainShape.setCollision(false);
		mainShape.addPassiveAttribute(PassiveAttributes.INVENTORY);

		File file = new File("sprites\\interface\\Inventory.png");
		Sprite sprite = new Sprite(file, 224, 128);
		sprite.addState(SpriteState.STATIC, new int[] { 0 });
		SpriteController[] spriteControllers = new SpriteController[1];
		spriteControllers[0] = new SpriteController(sprite);
		spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
		spriteControllers[0]
				.setSpritePosOffset(new Vec2f(-resSpecs.gameWidth / 4 - 24, -resSpecs.GAME_HEIGHT / 4 + 24));
		mainShape.setSpriteControllers(spriteControllers);

		// Slots
		for (Shape shape : slotShapes) {
			shape.setVisible(false);
			shape.setCollision(false);
			shape.addPassiveAttribute(PassiveAttributes.INVENTORY);
		}

	}

	public void changeResolution() {
		// At the moment nothing tbd.
	}

	public Item getItem(String name) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getName() == name) {
				return items.get(i);
			}
		}
		return null;
	}

	/**
	 * This returns the active item shown in the inventory with the specific index.
	 * This does not return the item with the static index!
	 * 
	 * @param index
	 * @return
	 */
	public Item getItem(int index) {
		int counter = 0;
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if (item.getAmount() > 0) {
				if (counter == index) {
					return item;
				}
				counter++;
			}
		}
		return null;
	}

	private void showItems() {
		ArrayList<Item> activeItems = new ArrayList<Item>();
		for (Item item : items) {
			if (item.getAmount() > 0) {
				activeItems.add(item);
			}
		}

		for (int i = 0; i < slotShapes.length; i++) {
			try {
				Item item = activeItems.get(i - 1);
				slotShapes[i].setSpriteControllers(item.getItemSpriteControllers());
				slotShapes[i].setVisible(true);
			} catch (Exception e) {
				slotShapes[i].setVisible(false);
			}
		}
	}
	
	public Shape getSlot(int i) {
		return slotShapes[i];
	}
	
	public void setVisible(boolean visible) {
		mainShape.setVisible(visible);
		
//		for (int i = 0; i < inventory.getNumShapes(); i++) {
//			Shape shape = inventory.getShape(i);
//			if (shape.hasPassiveAtrribute(PassiveAttributes.INVENTORY)) {
//				shape.setVisible(showInventory);
//			}
//		}
		
		if (visible) {
			showItems();
		}
	}
}
