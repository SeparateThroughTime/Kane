package kane.serialization;

import com.google.gson.*;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.physics.shapes.LineSegment;

import java.awt.*;
import java.lang.reflect.Type;

public class LineSegmentSerialization implements JsonSerializer<LineSegment>, JsonDeserializer<LineSegment>{
    @Override
    public JsonElement serialize(LineSegment src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("shapeType", "LINESEGMENT");
        jsonObject.add("relPosA", context.serialize(src.getRelPosA(), Vec2f.class));
        jsonObject.add("relPosB", context.serialize(src.getRelPosB(), Vec2f.class));

        ShapeSerialization shapeSerializer = new ShapeSerialization();
        shapeSerializer.serialize(jsonObject, src, typeOfSrc, context);
        return jsonObject;
    }

    @Override
    public LineSegment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        throw new UnsupportedOperationException("Tried to deserialize a Box without body.");
    }

    public Shape deserialize(Vec2f relPos, Body body, Color color, Material material, int renderLayer, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();
        Vec2f relPosA = context.deserialize(jsonObject.get("relPosA"), Vec2f.class);
        Vec2f relPosB = context.deserialize(jsonObject.get("relPosB"), Vec2f.class);

        LineSegment lineSegment = new LineSegment(relPosA, relPosB, body, color, material, renderLayer);
        return lineSegment;
    }
}
