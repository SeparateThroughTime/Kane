package kane.serialization;

import com.google.gson.*;
import kane.physics.Body;
import kane.physics.shapes.Box;
import kane.physics.shapes.LineSegment;

import java.lang.reflect.Type;

public class LineSegmentSerialization implements JsonSerializer<LineSegment>, JsonDeserializer<LineSegment>{
    @Override
    public JsonElement serialize(LineSegment src, Type typeOfSrc, JsonSerializationContext context){
        return null;
    }

    @Override
    public LineSegment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        return null;
    }

    public LineSegment deserialize(Body body, JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{

    }
}
