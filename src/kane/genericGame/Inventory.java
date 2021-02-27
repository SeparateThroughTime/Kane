package kane.genericGame;

import java.awt.Color;
import java.io.File;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.contacts.PassiveAttributes;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;
import kane.renderer.ResolutionSpecification;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

public class Inventory extends Body {

	private Material mInterface = new Material(1, 0);
	private ResolutionSpecification resSpecs;
	
	public Inventory(ResolutionSpecification resSpecs) {
		super(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);
		this.resSpecs = resSpecs;
		
		addShape(new Point(0, 0, this, Color.BLUE, mInterface, 1));
		getShape(0).setVisible(false);
		getShape(0).setCollision(false);
		getShape(0).addPassiveAttribute(PassiveAttributes.INVENTORY);
		setReactToGravity(false);
		File file = new File("sprites\\inventory.png");
		Sprite sprite = new Sprite(file, 7, 4);
		sprite.addState(SpriteState.Static, new int[] { 0 });
		SpriteController spriteController = new SpriteController(sprite);
		getShape(0).setSpriteController(spriteController);
		spriteController.setCurrentSpriteState(SpriteState.Static, true);
		spriteController.setSpritePosOffset(new Vec2f(-224, -128));
		
		//Slots
		addShape(new Box(-144, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 2));
		getShape(1).setVisible(false);
		getShape(1).setCollision(false);
		getShape(1).addPassiveAttribute(PassiveAttributes.INVENTORY);
		
		addShape(new Box(-48, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 2));
		getShape(2).setVisible(false);
		getShape(2).setCollision(false);
		getShape(2).addPassiveAttribute(PassiveAttributes.INVENTORY);
		
		addShape(new Box(48, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 2));
		getShape(3).setVisible(false);
		getShape(3).setCollision(false);
		getShape(3).addPassiveAttribute(PassiveAttributes.INVENTORY);
		
		addShape(new Box(144, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 2));
		getShape(4).setVisible(false);
		getShape(4).setCollision(false);
		getShape(4).addPassiveAttribute(PassiveAttributes.INVENTORY);
		
		addShape(new Box(-144, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 2));
		getShape(5).setVisible(false);
		getShape(5).setCollision(false);
		getShape(5).addPassiveAttribute(PassiveAttributes.INVENTORY);
		
		addShape(new Box(-48, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 2));
		getShape(6).setVisible(false);
		getShape(6).setCollision(false);
		getShape(6).addPassiveAttribute(PassiveAttributes.INVENTORY);
		
		addShape(new Box(48, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 2));
		getShape(7).setVisible(false);
		getShape(7).setCollision(false);
		getShape(7).addPassiveAttribute(PassiveAttributes.INVENTORY);
		
		addShape(new Box(144, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 2));
		getShape(8).setVisible(false);
		getShape(8).setCollision(false);
		getShape(8).addPassiveAttribute(PassiveAttributes.INVENTORY);
	}
	
	public void changeResolution() {
		pos.set(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);
	}

}
