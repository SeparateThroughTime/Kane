package kane.serialization;

import com.google.gson.*;
import kane.renderer.Sprite;
import kane.renderer.SpriteState;

import java.lang.reflect.Type;

public class SpriteSerialization implements JsonSerializer<Sprite>, JsonDeserializer<Sprite>{
    @Override
    public JsonElement serialize(Sprite src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("frameWidth", src.FRAME_WIDTH);
        jsonObject.addProperty("frameHeight", src.FRAME_HEIGHT);
        jsonObject.addProperty("filepath", src.filepath);

        for(SpriteState spriteState : SpriteState.values()){
            if(!src.stateIsAssigned(spriteState)){
                continue;
            }
            jsonObject.add(spriteState.name() + "-SpriteState", context.serialize(src.states.get(spriteState)));
        }
        return jsonObject;
    }

    @Override
    public Sprite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        int frameWidth = jsonObject.get("frameWidth").getAsInt();
        int frameHeight = jsonObject.get("frameHeight").getAsInt();
        String filepath = jsonObject.get("filepath").getAsString();

        Sprite sprite = new Sprite(filepath, frameWidth, frameHeight);

        for(SpriteState spriteState : SpriteState.values()){
            JsonArray frameNumberArray = null;
            try{
                frameNumberArray = jsonObject.get(spriteState.name() + "-SpriteState").getAsJsonArray();
            } catch (Exception e){
                continue;
            }
            if(frameNumberArray == null){
                continue;
            }

            int frameCount = frameNumberArray.size();
            int[] frameNumbers = new int[frameCount];
            for(int i = 0; i < frameCount; i++){
                frameNumbers[i] = frameNumberArray.get(i).getAsInt();
            }
            sprite.addState(spriteState, frameNumbers);
        }

        return sprite;
    }
}
