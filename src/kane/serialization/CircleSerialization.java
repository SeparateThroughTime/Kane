package kane.serialization;

import com.google.gson.*;
import kane.physics.Body;
import kane.physics.shapes.Box;
import kane.physics.shapes.Circle;

import java.lang.reflect.Type;

public class CircleSerialization implements JsonSerializer<Circle>, JsonDeserializer<Circle>{
    @Override
    public JsonElement serialize(Circle src, Type typeOfSrc, JsonSerializationContext context){
        return null;
    }

    @Override
    public Circle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Circle deserialize(Body body, JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{

    }
}
