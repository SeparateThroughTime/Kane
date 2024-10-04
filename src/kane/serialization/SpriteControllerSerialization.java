package kane.serialization;

import com.google.gson.*;
import kane.math.Vec2f;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;

import java.lang.reflect.Type;

public class SpriteControllerSerialization implements JsonSerializer<SpriteController>,
        JsonDeserializer<SpriteController>{
    @Override
    public JsonElement serialize(SpriteController src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("spritePosOffset", context.serialize(src.spritePosOffset));
        jsonObject.add("scale", context.serialize(src.scale));
        jsonObject.add("sprite", context.serialize(src.sprite));
        return jsonObject;
    }

    @Override
    public SpriteController deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        Vec2f spritePosOffset = context.deserialize(jsonObject.get("spritePosOffset"), Vec2f.class);
        Vec2f scale = context.deserialize(jsonObject.get("scale"), Vec2f.class);
        Sprite sprite = context.deserialize(jsonObject.get("sprite"), Sprite.class);

        SpriteController spriteController = new SpriteController(sprite);
        spriteController.spritePosOffset = spritePosOffset;
        spriteController.scale = scale;
        return spriteController;
    }
}
