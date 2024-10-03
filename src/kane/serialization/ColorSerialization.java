package kane.serialization;

import com.google.gson.*;

import java.awt.*;
import java.lang.reflect.Type;

public class ColorSerialization implements JsonSerializer<Color>, JsonDeserializer<Color>{

    @Override
    public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject json = new JsonObject();
        json.addProperty("red", src.getRed());
        json.addProperty("green", src.getGreen());
        json.addProperty("blue", src.getBlue());
        json.addProperty("alpha", src.getAlpha());
        return json;
    }

    @Override
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        int red = jsonObject.get("red").getAsInt();
        int green = jsonObject.get("green").getAsInt();
        int blue = jsonObject.get("blue").getAsInt();
        int alpha = jsonObject.get("alpha").getAsInt();
        return new Color(red, green, blue, alpha);
    }
}
