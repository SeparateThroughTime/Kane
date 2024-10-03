package kane.serialization;

import com.google.gson.*;
import kane.physics.Body;
import kane.physics.shapes.Box;
import kane.physics.shapes.Plane;

import java.lang.reflect.Type;

public class PlaneSerialization implements JsonSerializer<Plane>, JsonDeserializer<Plane>{
    @Override
    public JsonElement serialize(Plane src, Type typeOfSrc, JsonSerializationContext context){
        return null;
    }

    @Override
    public Plane deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Plane deserialize(Body body, JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{

    }
}
