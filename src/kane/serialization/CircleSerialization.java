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
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("shapeType", "CIRCLE");
        jsonObject.addProperty("rad", src.rad);

        ShapeSerialization shapeSerializer = new ShapeSerialization();
        shapeSerializer.serialize(jsonObject, src, typeOfSrc, context);
        return jsonObject;
    }

    @Override
    public Circle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Shape deserialize(Vec2f relPos, Body body, Color color, Material material, int renderLayer, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();
        float rad = jsonObject.get("rad").getAsFloat();
        return new Circle(rad, relPos.x, relPos.y, color, body, material, renderLayer);
    }
}
