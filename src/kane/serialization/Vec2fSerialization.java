package kane.serialization;

import com.google.gson.*;
import kane.math.Vec2f;

import java.lang.reflect.Type;

public class Vec2fSerialization implements JsonSerializer<Vec2f>, JsonDeserializer<Vec2f>{
    @Override
    public JsonElement serialize(Vec2f src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", src.x);
        jsonObject.addProperty("y", src.y);
        return jsonObject;
    }

    @Override
    public Vec2f deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        return new Vec2f(jsonObject.get("x").getAsFloat(), jsonObject.get("y").getAsFloat());
    }
}
