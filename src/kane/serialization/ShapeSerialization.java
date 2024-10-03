package kane.serialization;

import com.google.gson.*;
import kane.genericGame.ActiveAttributes;
import kane.genericGame.PassiveAttributes;
import kane.math.Vec2f;
import kane.physics.Material;
import kane.physics.Shape;
import kane.renderer.SpriteController;
import kane.physics.shapes.*;

import java.awt.*;
import java.lang.reflect.Type;

public class ShapeSerialization implements JsonSerializer<Shape>, JsonDeserializer<Shape>{
    @Override
    public JsonElement serialize(Shape src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("renderLayer", src.renderLayer);
        jsonObject.add("relPos", context.serialize(src.relPos));
        jsonObject.add("color", context.serialize(src.color));
        jsonObject.addProperty("collision", src.collision);
        jsonObject.addProperty("visible", src.visible);
        jsonObject.add("material", context.serialize(src.material));
        jsonObject.add("activeAttributes", context.serialize(src.activeAttributes));
        jsonObject.add("passiveAttributes", context.serialize(src.passiveAttributes));
        jsonObject.add("spriteControllers", context.serialize(src.getSpriteControllers()));
        return jsonObject;
    }

    @Override
    public Shape deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        int renderLayer = jsonObject.get("renderLayer").getAsInt();
        Vec2f relPos = context.deserialize(jsonObject.get("relPos"), Vec2f.class);
        Color color = context.deserialize(jsonObject.get("color"), Color.class);
        boolean collision = jsonObject.get("collision").getAsBoolean();
        boolean visible = jsonObject.get("visible").getAsBoolean();
        Material material = context.deserialize(jsonObject.get("material"), Material.class);
        ActiveAttributes[] activeAttributes = context.deserialize(jsonObject.get("activeAttributes"), ActiveAttributes[].class);
        PassiveAttributes[] passiveAttributes = context.deserialize(jsonObject.get("passiveAttributes"), PassiveAttributes[].class);
        SpriteController[] spriteControllers = context.deserialize(jsonObject.get("spriteControllers"), SpriteController[].class);

        if (typeOfT.equals(Box.class)){

        }

    }
}
