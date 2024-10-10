package kane.genericGame.hud;

import kane.Kane;
import kane.genericGame.Game;
import kane.math.Vec2f;
import kane.physics.Material;
import kane.physics.shapes.Box;
import kane.renderer.Camera;

import java.awt.*;

import static kane.renderer.ResolutionSpecification.RES_SPECS;

public class HudElement extends Box{
    public final Vec2f posInPercent;
    public final Vec2f radInPercent;
    public final boolean stretchX;

    public HudElement(Vec2f pos, Vec2f rad, int renderLayer, boolean stretchX){
        super(pos.x, pos.y, Camera.CAMERA, new Vec2f(rad), Color.CYAN, Material.INTERFACE, renderLayer);
        this.posInPercent = pos;
        this.radInPercent = rad;
        this.stretchX = stretchX;
        this.collision = false;

        this.relPos.set(transformToGameUnit(this.posInPercent));
        this.rad.set(transformToGameUnit(this.radInPercent));

        Kane.GAME.addHudElement(this);

    }

    private Vec2f transformToGameUnit(Vec2f percent){
        float y = (float) RES_SPECS.GAME_HEIGHT / 100 * percent.y;
        float x = stretchX ? (float) RES_SPECS.gameWidth / 100 * percent.x : (float) RES_SPECS.GAME_HEIGHT / 100 * percent.x;
        return new Vec2f(x, y);
    }

    private Vec2f transformToPercent(Vec2f gameUnit){
        float y = gameUnit.y / RES_SPECS.GAME_HEIGHT * 100;
        float x = stretchX ? gameUnit.x / RES_SPECS.gameWidth * 100 : gameUnit.x / RES_SPECS.GAME_HEIGHT * 100;
        return new Vec2f(x, y);
    }

    public void changeResolution(){
        this.relPos.set(transformToGameUnit(this.posInPercent));
        this.rad.set(transformToGameUnit(this.radInPercent));
    }

}
