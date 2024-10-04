package kane.serialization;

import com.google.gson.*;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Polygon;

import java.awt.*;
import java.lang.reflect.Type;

public class PolygonSerialization implements JsonSerializer<Polygon>, JsonDeserializer<Polygon>{

    @Override
    public JsonElement serialize(Polygon src, Type typeOfSrc, JsonSerializationContext context){
        return null;
    }

    @Override
    public Polygon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Shape deserialize(Vec2f relPos, Body body, Color color, Material material, int renderLayer, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        return null;
    }
}
