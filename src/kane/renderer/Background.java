package kane.renderer;

import kane.math.Vec2f;

public class Background{
    public SpriteController spriteController;
    private int offsetX;

    public Background(String filepath){
        spriteController = new SpriteController(new Sprite(filepath));
        spriteController.sprite.addState(SpriteState.STATIC, new int[]{0});
        spriteController.setCurrentSpriteState(SpriteState.STATIC);
        float scale = (float) ResolutionSpecification.RES_SPECS.GAME_HEIGHT / spriteController.sprite.FRAME_HEIGHT;
        spriteController.scale = new Vec2f(scale, scale).mult(0.5f);

        this.offsetX = 0;
    }

    public void setOffsetX(int offsetX){
        offsetX = -(offsetX % spriteController.sprite.FRAME_WIDTH);
        this.offsetX = offsetX;
    }

    public int getOffsetX(){
        return offsetX;
    }
}
