package kane.genericGame.hud;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import kane.genericGame.Item;
import kane.genericGame.PassiveAttributes;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.shapes.Point;
import kane.renderer.ResolutionSpecification;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

public class HudBar extends Body {
	private Material mInterface = new Material(1, 0);
	private ResolutionSpecification resSpecs;
	private BufferedImage img;
	private static final int HUD_WIDTH = 100;
	private static final int HUD_HEIGHT = 10;

	public HudBar(ResolutionSpecification resSpecs, BufferedImage img, int pos) {
		super(HUD_HEIGHT + HUD_WIDTH / 2, resSpecs.GAME_HEIGHT - (int) (HUD_HEIGHT * (pos + 1) * 1.5));
		this.img = img;
		this.resSpecs = resSpecs;
		createHudBar();
	}

	private void createHudBar() {
		addShape(new Point(0, 0, this, Color.BLUE, mInterface, 3));
		getShape(0).setCollision(false);
		getShape(0).addPassiveAttribute(PassiveAttributes.HUD);
		setReactToGravity(false);
		Sprite sprite = new Sprite(img, HUD_WIDTH, HUD_HEIGHT);
		sprite.addState(SpriteState.STATIC, new int[] { 0 });
		SpriteController[] spriteControllers = new SpriteController[1];
		spriteControllers[0] = new SpriteController(sprite);
		spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
		spriteControllers[0].setSpritePosOffset(new Vec2f(-HUD_WIDTH / 2, -HUD_HEIGHT / 2));
		getShape(0).setSpriteControllers(spriteControllers);
	}
	
	public void refreshHudBar(float percentage) {
		int newWidth = (int) (HUD_WIDTH * percentage);
		BufferedImage refreshedImg = img.getSubimage(0, 0, newWidth, HUD_HEIGHT);
		Sprite sprite = new Sprite(refreshedImg, newWidth, HUD_HEIGHT);
		sprite.addState(SpriteState.STATIC, new int[] { 0 });
		SpriteController[] spriteControllers = new SpriteController[1];
		spriteControllers[0] = new SpriteController(sprite);
		spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
		spriteControllers[0].setSpritePosOffset(new Vec2f(-HUD_WIDTH / 2, -HUD_HEIGHT / 2));
		getShape(0).setSpriteControllers(spriteControllers);
		
	}
}
