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
	public static int MAX_PLAYER_HEALTH = 10;

	private Material mInterface = new Material(1, 0);
	private ResolutionSpecification resSpecs;
	private BufferedImage img;
	private static final int HUD_WIDTH = 100;
	private static final int HUD_HEIGHT = 10;
	private int hudPos;

	public HudBar(ResolutionSpecification resSpecs, BufferedImage img, int hudPos) {
		super(resSpecs.gameWidth / 2, resSpecs.GAME_HEIGHT / 2);
		this.img = img;
		this.resSpecs = resSpecs;
		this.hudPos = hudPos;
		createHudBar();
	}

	private void createHudBar() {
		addShape(new Point(-resSpecs.gameWidth / 2 + HUD_HEIGHT + HUD_WIDTH / 2,
				-resSpecs.GAME_HEIGHT / 2 + resSpecs.GAME_HEIGHT - (int) (HUD_HEIGHT * (hudPos + 1) * 1.5), this,
				Color.BLUE, mInterface, 3));
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
		BufferedImage refreshedImg = img.getSubimage(0, 0, newWidth > 0 ? newWidth : 1, HUD_HEIGHT);
		Sprite sprite = new Sprite(refreshedImg, newWidth > 0 ? newWidth : 1, HUD_HEIGHT);
		sprite.addState(SpriteState.STATIC, new int[] { 0 });
		SpriteController[] spriteControllers = new SpriteController[1];
		spriteControllers[0] = new SpriteController(sprite);
		spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
		spriteControllers[0].setSpritePosOffset(new Vec2f(-HUD_WIDTH / 2, -HUD_HEIGHT / 2));
		getShape(0).setSpriteControllers(spriteControllers);

	}
}
