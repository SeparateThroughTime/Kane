package kane.serialization;

import com.google.gson.*;
import kane.physics.Body;
import kane.physics.Shape;
import kane.sound.SoundSource;
import kane.sound.SoundType;

import java.lang.reflect.Type;
import java.util.HashMap;

public class BodySerialization implements JsonSerializer<Body>, JsonDeserializer<Body>{
    @Override
    public JsonElement serialize(Body src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("shapes", context.serialize(src.shapes));

        for(SoundType soundType : SoundType.values()){
            if(src.soundSources.containsKey(soundType)){
                jsonObject.add(soundType.name() + "-Sound", context.serialize(src.soundSources.get(soundType).getBuffer().filepath));
            }
        }

        return jsonObject;
    }

    @Override
    public Body deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        Body body = new Body(0, 0);
        return deserialize(body, json, typeOfT, context);
    }

    public Body deserialize(Body body, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();

        HashMap<SoundType, SoundSource> soundSources = context.deserialize(jsonObject.get("soundSources"), HashMap.class);
        for(SoundType soundType : SoundType.values()){
            if(jsonObject.has(soundType.name() + "-Sound")){
                String filepath = jsonObject.get(soundType.name() + "-Sound").getAsString();
                body.addSoundSource(filepath, soundType);
            }
        }
    }
}
