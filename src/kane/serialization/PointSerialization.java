package kane.serialization;

import com.google.gson.*;
import kane.physics.Body;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;

import java.lang.reflect.Type;

public class PointSerialization implements JsonSerializer<Point>, JsonDeserializer<Point>{
    @Override
    public JsonElement serialize(Point src, Type typeOfSrc, JsonSerializationContext context){
        return null;
    }

    @Override
    public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public Point deserialize(Body body, JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{

    }
}
