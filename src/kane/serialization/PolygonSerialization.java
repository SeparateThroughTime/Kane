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
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("shapeType", "POLYGON");
        jsonObject.add("points", context.serialize(src.pointsAlign));
        jsonObject.addProperty("angle", src.getAngle());

        ShapeSerialization shapeSerializer = new ShapeSerialization();
        shapeSerializer.serialize(jsonObject, src, typeOfSrc, context);
        return jsonObject;
    }

    @Override
    public Polygon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        throw new UnsupportedOperationException("Tried to deserialize a Polygon without body.");
    }

    public Shape deserialize(Vec2f relPos, Body body, Color color, Material material, int renderLayer, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();

        JsonArray pointArray = jsonObject.get("points").getAsJsonArray();
        Vec2f[] points = new Vec2f[pointArray.size()];
        Vec2fSerialization vec2fDeserializer = new Vec2fSerialization();
        for (int i = 0; i < pointArray.size(); i++) {
            points[i] = vec2fDeserializer.deserialize(pointArray.get(i), Vec2f.class, context);
        }

        float angle = jsonObject.get("angle").getAsFloat();

        Polygon polygon = new Polygon(relPos.x, relPos.y, body, color, points, material, renderLayer);
        polygon.rotate(angle);
        return polygon;
    }
}
