package kane.serialization;

import com.google.gson.*;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.physics.shapes.Plane;

import java.awt.*;
import java.lang.reflect.Type;

public class PlaneSerialization implements JsonSerializer<Plane>, JsonDeserializer<Plane>{
    @Override
    public JsonElement serialize(Plane src, Type typeOfSrc, JsonSerializationContext context){
        return null;
    }

    @Override
    public Plane deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Shape deserialize(Vec2f relPos, Body body, Color color, Material material, int renderLayer, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        return null;
    }
}
