package kane.genericGame.hud;

import kane.genericGame.GuiElement;
import kane.genericGame.PassiveAttributes;
import kane.math.Vec2f;
import kane.physics.Shape;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

import java.util.ArrayList;

public class HudBar{
    public static int MAX_PLAYER_HEALTH = 10;

    public static final int HUD_WIDTH = 100;
    public static final int HUD_HEIGHT = 10;
    public static final float HUD_WIDTH_PERCENT = 12.5f;
    public static final float HUD_HEIGHT_PERCENT = 1.666667f;

    private static ArrayList<HudBar> hudBars = new ArrayList<>();

    public static HudBar get(int index){
        return hudBars.get(index);
    }

    public static int size(){
        return hudBars.size();
    }

    private final GuiElement hudShape;

    public HudBar(String filepath){
        int hudPos = hudBars.size();
        hudShape = new GuiElement(new Vec2f(-50 + HudBar.HUD_HEIGHT_PERCENT + HudBar.HUD_WIDTH_PERCENT / 2,
                50 - HudBar.HUD_HEIGHT_PERCENT * (hudPos + 1) * 1.5f), new Vec2f(HudBar.HUD_WIDTH_PERCENT / 2,
                HudBar.HUD_HEIGHT_PERCENT / 2), 3, false);

        hudBars.add(this);

        hudShape.collision = false;
        hudShape.addPassiveAttribute(PassiveAttributes.HUD);

        Sprite sprite = new Sprite(filepath, HUD_WIDTH, HUD_HEIGHT);
        sprite.addState(SpriteState.STATIC, new int[]{0});
        SpriteController[] spriteControllers = new SpriteController[1];
        spriteControllers[0] = new SpriteController(sprite);
        spriteControllers[0].setCurrentSpriteState(SpriteState.STATIC);
        spriteControllers[0].spritePosOffset = new Vec2f((float) -HUD_WIDTH / 2, (float) -HUD_HEIGHT / 2);
        hudShape.setSpriteControllers(spriteControllers);
    }

    public void refresh(float percentage){
        SpriteController spriteController = hudShape.getSpriteControllers()[0];
        Vec2f[] texCoords = spriteController.getFrameTexCoords();
        texCoords[1].x = percentage;
        texCoords[2].x = percentage;
        spriteController.scale.x = percentage;
    }
}
