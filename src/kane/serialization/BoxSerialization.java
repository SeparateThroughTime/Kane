package kane.serialization;

import com.google.gson.*;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;

import java.awt.*;
import java.lang.reflect.Type;

public class BoxSerialization implements JsonSerializer<Box>, JsonDeserializer<Box>{
    @Override
    public JsonElement serialize(Box src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("shapeType", "BOX");
        jsonObject.add("rad", context.serialize(src.rad));

        ShapeSerialization shapeSerializer = new ShapeSerialization();
        shapeSerializer.serialize(jsonObject, src, typeOfSrc, context);
        return jsonObject;
    }

    @Override
    public Box deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        throw new UnsupportedOperationException("Tried to deserialize a Box without body.");
    }


    public Box deserialize(Vec2f relPos, Body body, Color color, Material material, int renderLayer, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();
        Vec2f rad = context.deserialize(jsonObject.get("rad"), Vec2f.class);
        Box box = new Box(relPos.x, relPos.y, body, rad, color, material, renderLayer);
        return box;
    }
}
