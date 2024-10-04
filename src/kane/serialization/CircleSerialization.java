package kane.serialization;

import com.google.gson.*;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.physics.shapes.Circle;

import java.awt.*;
import java.lang.reflect.Type;

public class CircleSerialization implements JsonSerializer<Circle>, JsonDeserializer<Circle>{
    @Override
    public JsonElement serialize(Circle src, Type typeOfSrc, JsonSerializationContext context){
        return null;
    }

    @Override
    public Circle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Shape deserialize(Vec2f relPos, Body body, Color color, Material material, int renderLayer, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        return null;
    }
}
