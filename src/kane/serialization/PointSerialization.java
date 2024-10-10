package kane.serialization;

import com.google.gson.*;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;

import java.awt.*;
import java.lang.reflect.Type;

public class PointSerialization implements JsonSerializer<Point>, JsonDeserializer<Point>{
    @Override
    public JsonElement serialize(Point src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("shapeType", "POINT");

        ShapeSerialization shapeSerializer = new ShapeSerialization();
        shapeSerializer.serialize(jsonObject, src, typeOfSrc, context);
        return jsonObject;
    }

    @Override
    public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Shape deserialize(Vec2f relPos, Body body, Color color, Material material, int renderLayer, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        return new Point(relPos.x, relPos.y, body, color, material, renderLayer);
    }
}
