package kane.serialization;

import com.google.gson.*;
import kane.genericGame.ActiveAttributes;
import kane.genericGame.PassiveAttributes;
import kane.genericGame.hud.HudElement;
import kane.math.ArrayOperations;
import kane.math.Vec2f;
import kane.renderer.SpriteController;

import java.lang.reflect.Type;

public class HudElementSerialization implements JsonSerializer<HudElement>, JsonDeserializer<HudElement>{
    @Override
    public JsonElement serialize(HudElement src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("posInPercent", context.serialize(src.posInPercent));
        jsonObject.add("radInPercent", context.serialize(src.radInPercent));
        jsonObject.addProperty("stretchX", src.stretchX);
        jsonObject.addProperty("renderLayer", src.renderLayer);
        jsonObject.add("activeAttributes", context.serialize(ArrayOperations.remEmpty(src.activeAttributes)));
        jsonObject.add("passiveAttributes", context.serialize(ArrayOperations.remEmpty(src.passiveAttributes)));
        jsonObject.add("spriteControllers", context.serialize(src.getSpriteControllers()));
        return jsonObject;
    }

    @Override
    public HudElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        Vec2f posInPercent = context.deserialize(jsonObject.get("posInPercent"), Vec2f.class);
        Vec2f radInPercent = context.deserialize(jsonObject.get("radInPercent"), Vec2f.class);
        boolean stretchX = jsonObject.get("stretchX").getAsBoolean();
        int renderLayer = jsonObject.get("renderLayer").getAsInt();
        ActiveAttributes[] activeAttributes = context.deserialize(jsonObject.get("activeAttributes"), ActiveAttributes.class);
        PassiveAttributes[] passiveAttributes = context.deserialize(jsonObject.get("passiveAttributes"), PassiveAttributes.class);
        SpriteController[] spriteControllers = context.deserialize(jsonObject.get("spriteControllers"), SpriteController.class);

        HudElement hudElement = new HudElement(posInPercent, radInPercent, renderLayer, stretchX);

        for (ActiveAttributes attribute : activeAttributes){
            hudElement.addActiveAttribute(attribute);
        }
        for (PassiveAttributes attribute : passiveAttributes){
            hudElement.addPassiveAttribute(attribute);
        }
        if (spriteControllers != null){
            hudElement.setSpriteControllers(spriteControllers);
        }

        return hudElement;
    }
}
