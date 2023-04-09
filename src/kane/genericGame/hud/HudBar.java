package kane.genericGame.hud;

import java.awt.image.BufferedImage;

import kane.genericGame.PassiveAttributes;
import kane.math.Vec2f;
import kane.physics.Shape;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

public class HudBar {
	public static int MAX_PLAYER_HEALTH = 10;

	public static final int HUD_WIDTH = 100;
	public static final int HUD_HEIGHT = 10;
	private Shape hudShape;

	public HudBar(String filepath, Shape hudShape) {
		this.hudShape = hudShape;
		
		hudShape.collision = false;
		hudShape.addPassiveAttribute(PassiveAttributes.HUD);
		Sprite sprite = new Sprite(filepath, HUD_WIDTH, HUD_HEIGHT);
		sprite.addState(SpriteState.STATIC, new int[] { 0 });
		SpriteController[] spriteControllers = new SpriteController[1];
		spriteControllers[0] = new SpriteController(sprite);
		spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
		spriteControllers[0].spritePosOffset = new Vec2f(-HUD_WIDTH / 2, -HUD_HEIGHT / 2);
		hudShape.setSpriteControllers(spriteControllers);
	}

	public void refresh(float percentage) {
		SpriteController spriteController = hudShape.getSpriteControllers()[0];
		Vec2f[] texCoords = spriteController.getFrameTexCoords();
		texCoords[1].x = percentage;
		texCoords[2].x = percentage;
		spriteController.scale.x = percentage;
	}
}
