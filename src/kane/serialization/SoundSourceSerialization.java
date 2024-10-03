package kane.serialization;

import com.google.gson.*;
import kane.sound.SoundSource;

import java.lang.reflect.Type;

public class SoundSourceSerialization implements JsonSerializer<SoundSource>, JsonDeserializer<SoundSource>{
    @Override
    public JsonElement serialize(SoundSource src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bufferFilepath", src.getBuffer().filepath);
        return jsonObject;
    }

    @Override
    public SoundSource deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        throw new UnsupportedOperationException("SoundSources should only be created by a body.");
    }
}
