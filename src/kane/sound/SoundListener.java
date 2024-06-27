package kane.sound;

import kane.math.Vec2f;

import static org.lwjgl.openal.AL10.*;

public class SoundListener {
    public SoundListener() {
        this(new Vec2f(0f, 0f));
    }

    public SoundListener(Vec2f pos) {
        alListener3f(AL_POSITION, pos.x, pos.y, 0f);
        alListener3f(AL_VELOCITY, 0f, 0f, 0f);
    }

    public void setVel(Vec2f vel) {
        alListener3f(AL_VELOCITY, vel.x, vel.y, 0f);
    }

    public void setPos(Vec2f pos) {
        alListener3f(AL_POSITION, pos.x, pos.y, 0f);
    }


}
