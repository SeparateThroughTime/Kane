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
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("shapeType", "PLANE");
        jsonObject.add("normal", context.serialize(src.getNormal(), Vec2f.class));
        jsonObject.addProperty("distance", src.getDistance());
        jsonObject.addProperty("len", src.getLen());

        ShapeSerialization shapeSerializer = new ShapeSerialization();
        shapeSerializer.serialize(jsonObject, src, typeOfSrc, context);
        return jsonObject;
    }

    @Override
    public Plane deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Shape deserialize(Vec2f relPos, Body body, Color color, Material material, int renderLayer, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();
        Vec2f normal = context.deserialize(jsonObject.get("normal"), Vec2f.class);
        float distance = jsonObject.get("distance").getAsFloat();
        float len = jsonObject.get("len").getAsFloat();
        return new Plane(normal, distance, len, body, color, material, renderLayer);
    }
}
