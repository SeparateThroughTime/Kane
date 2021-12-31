package kane.genericGame;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import kane.genericGame.item.NONE;
import kane.genericGame.item.SWORD;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;
import kane.renderer.ResolutionSpecification;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

public class Inventory extends Body {

	private Material mInterface = new Material(1, 0);
	private ResolutionSpecification resSpecs;
	private ArrayList<Item> items;

	public Inventory(ResolutionSpecification resSpecs) {
		super(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);
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
		addShape(new Point(0, 0, this, Color.BLUE, mInterface, 3));
		getShape(0).setVisible(false);
		getShape(0).setCollision(false);
		getShape(0).addPassiveAttribute(PassiveAttributes.INVENTORY);
		setReactToGravity(false);
		File file = new File("sprites\\interface\\inventory.png");
		Sprite sprite = new Sprite(file, 14, 8);
		sprite.addState(SpriteState.STATIC, new int[] { 0 });
		SpriteController[] spriteControllers = new SpriteController[1];
		spriteControllers[0] = new SpriteController(sprite);
		spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
		spriteControllers[0].setSpritePosOffset(new Vec2f(-224, -128));
		getShape(0).setSpriteControllers(spriteControllers);

		// Slots
		addShape(new Box(-144, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		getShape(1).setVisible(false);
		getShape(1).setCollision(false);
		getShape(1).addPassiveAttribute(PassiveAttributes.INVENTORY);

		addShape(new Box(-48, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		getShape(2).setVisible(false);
		getShape(2).setCollision(false);
		getShape(2).addPassiveAttribute(PassiveAttributes.INVENTORY);

		addShape(new Box(48, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		getShape(3).setVisible(false);
		getShape(3).setCollision(false);
		getShape(3).addPassiveAttribute(PassiveAttributes.INVENTORY);

		addShape(new Box(144, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		getShape(4).setVisible(false);
		getShape(4).setCollision(false);
		getShape(4).addPassiveAttribute(PassiveAttributes.INVENTORY);

		addShape(new Box(-144, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		getShape(5).setVisible(false);
		getShape(5).setCollision(false);
		getShape(5).addPassiveAttribute(PassiveAttributes.INVENTORY);

		addShape(new Box(-48, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		getShape(6).setVisible(false);
		getShape(6).setCollision(false);
		getShape(6).addPassiveAttribute(PassiveAttributes.INVENTORY);

		addShape(new Box(48, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		getShape(7).setVisible(false);
		getShape(7).setCollision(false);
		getShape(7).addPassiveAttribute(PassiveAttributes.INVENTORY);

		addShape(new Box(144, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
		getShape(8).setVisible(false);
		getShape(8).setCollision(false);
		getShape(8).addPassiveAttribute(PassiveAttributes.INVENTORY);

	}

	public void changeResolution() {
		pos.set(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);
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
	 * @param index
	 * @return
	 */
	public Item getItem(int index) {
		int counter = 0;
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if(item.getAmount() > 0) {
				if(counter == index) {
					return item;
				}
				counter++;
			}
		}
		return null;
	}

	public void showItems() {
		ArrayList<Item> activeItems = new ArrayList<Item>();
		for (Item item : items) {
			if(item.getAmount() > 0) {
				activeItems.add(item);
			}
		}
		
		for (int i = 1; i < numShapes; i++) {
			try {
				Item item = activeItems.get(i-1);
				getShape(i).setSpriteControllers(item.getItemSpriteControllers());
				getShape(i).setVisible(true);
			} catch (Exception e) {
				getShape(i).setVisible(false);
			}
		}
	}
}
