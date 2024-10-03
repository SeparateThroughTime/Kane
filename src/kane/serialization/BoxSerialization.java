package kane.serialization;

import com.google.gson.*;
import kane.physics.Body;
import kane.physics.shapes.Box;

import java.lang.reflect.Type;

public class BoxSerialization implements JsonSerializer<Box>, JsonDeserializer<Box>{
    @Override
    public JsonElement serialize(Box src, Type typeOfSrc, JsonSerializationContext context){
        return null;
    }

    @Override
    public Box deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Box deserialize(Body body, JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{

    }
}
