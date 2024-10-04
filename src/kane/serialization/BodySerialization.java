package kane.serialization;

import com.google.gson.*;
import kane.genericGame.Mob;
import kane.math.ArrayOperations;
import kane.physics.Body;
import kane.physics.Shape;
import kane.sound.SoundType;

import java.lang.reflect.Type;
import java.util.Arrays;

public class BodySerialization implements JsonSerializer<Body>, JsonDeserializer<Body>{
    @Override
    public JsonElement serialize(Body src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        return serialize(jsonObject, src, typeOfSrc, context);
    }

    public JsonElement serialize(JsonObject jsonObject, Body src, Type typeOfSrc, JsonSerializationContext context){
        jsonObject.add("shapes", context.serialize(ArrayOperations.remEmpty(src.shapes)));

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


        ShapeSerialization shapeSerializer = new ShapeSerialization();
        JsonArray shapeArray = jsonObject.get("shapes").getAsJsonArray();
        for (JsonElement shapeElement : shapeArray){
            Shape shape = shapeSerializer.deserialize(body, shapeElement, Shape.class, context);
            System.out.println(body);
            body.addShape(shape);
        }

        for(SoundType soundType : SoundType.values()){
            if(jsonObject.has(soundType.name() + "-Sound")){
                String filepath = jsonObject.get(soundType.name() + "-Sound").getAsString();
                body.addSoundSource(filepath, soundType);
            }
        }

        return body;
    }


}
