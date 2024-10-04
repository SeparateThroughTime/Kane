package kane.renderer;

import kane.exceptions.LoadTextureException;
import kane.math.Vec2f;

import java.util.HashMap;
import java.util.Map;

import static kane.genericGame.ResourceManager.RESOURCE_MANAGER;

public class Sprite{

    public static final float SCALE = 2f;

    public final int FRAME_WIDTH;
    public final int FRAME_HEIGHT;
    public int PIXEL_PER_FRAME;
    public Texture texture;
    private Vec2f[][] texCoords;
    public final String filepath;

    public final Map<SpriteState, int[]> states;

    public Sprite(String filepath, int frameWidth, int frameHeight){
        this.FRAME_HEIGHT = frameHeight;
        this.FRAME_WIDTH = frameWidth;
        this.states = new HashMap<>();
        this.filepath = filepath;

        try{
            texture = RESOURCE_MANAGER.getTexture(filepath);
        } catch (LoadTextureException e){
            e.printStackTrace();
        }
        init();
    }

    public Sprite(String filepath){
        this.states = new HashMap<>();
        this.filepath = filepath;

        try{
            texture = RESOURCE_MANAGER.getTexture(filepath);
        } catch (LoadTextureException e){
            e.printStackTrace();
        }

        this.FRAME_HEIGHT = texture.HEIGHT;
        this.FRAME_WIDTH = texture.WIDTH;
        init();
    }

    private void init(){
        int texWidth = texture.WIDTH;
        int texHeight = texture.HEIGHT;
        this.PIXEL_PER_FRAME = FRAME_WIDTH * FRAME_HEIGHT;
        int frameCount = (texWidth * texHeight) / PIXEL_PER_FRAME;
        int frameCountX = texWidth / FRAME_WIDTH;
        texCoords = new Vec2f[frameCount][4];

        for (int i = 0; i < frameCount; i++){
            int xCount = i % frameCountX;
            int xPixel = xCount * FRAME_WIDTH;
            int yCount = i / frameCountX;
            int yPixel = yCount * FRAME_HEIGHT;

            float topY = (yPixel + FRAME_HEIGHT) / (float) texHeight;
            float rightX = (xPixel + FRAME_WIDTH) / (float) texWidth;
            float leftX = xPixel / (float) texWidth;
            float bottomY = yPixel / (float) texHeight;

            Vec2f[] frameTexCoords = {new Vec2f(leftX, bottomY), new Vec2f(rightX, bottomY), new Vec2f(rightX, topY),
                    new Vec2f(leftX, topY)};
            texCoords[i] = frameTexCoords;
        }
    }

    public void addState(SpriteState state, int[] frameNumbers){
        states.put(state, frameNumbers);
    }

    public boolean stateIsAssigned(SpriteState state){
        return states.containsKey(state);
    }

    public Vec2f[] getTexCoords(SpriteState spriteState, int spriteStateFrameNo){
        if (texture == null){
            return new Vec2f[]{new Vec2f(0, 1), new Vec2f(0, 0), new Vec2f(1, 0), new Vec2f(1, 1)};
        }

        if (!stateIsAssigned(spriteState)){
            return new Vec2f[]{new Vec2f(0, 1), new Vec2f(0, 0), new Vec2f(1, 0), new Vec2f(1, 1)};
        }

        int frameNo = states.get(spriteState)[spriteStateFrameNo];
        return texCoords[frameNo];
    }

    public int getFrameCount(SpriteState state){
        if (!stateIsAssigned(state)){
            return 0;
        }

        return states.get(state).length;
    }
}
