package kane.serialization;

import com.google.gson.*;
import kane.genericGame.AIs;
import kane.genericGame.Mob;
import kane.genericGame.MobDirection;
import kane.math.Vec2f;
import kane.physics.Body;

import java.lang.reflect.Type;

public class MobSerialization implements JsonSerializer<Mob>, JsonDeserializer<Mob>{
    @Override
    public JsonElement serialize(Mob src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("maxHealth", src.getMaxHealth());
        jsonObject.addProperty("damage", src.getDamage());
        jsonObject.add("direction", context.serialize(src.getDirection()));
        jsonObject.add("walkAcc", context.serialize(src.getWalkAcc()));
        jsonObject.addProperty("walkSpeed", src.getWalkSpeed());
        jsonObject.add("jumpAcc", context.serialize(src.getJumpAcc()));
        jsonObject.add("ai", context.serialize(src.getAi()));

        return jsonObject;
    }

    @Override
    public Mob deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        int maxHealth = jsonObject.get("maxHealth").getAsInt();
        int damage = jsonObject.get("damage").getAsInt();
        MobDirection direction = jsonObject.has("direction") ? context.deserialize(jsonObject.get("direction"), MobDirection.class) : null;
        Vec2f walkAcc = jsonObject.has("walkAcc") ? context.deserialize(jsonObject.get("walkAcc"), Vec2f.class) : null;
        int walkSpeed = jsonObject.get("walkSpeed").getAsInt();
        Vec2f jumpAcc = jsonObject.has("jumpAcc") ? context.deserialize(jsonObject.get("jumpAcc"), Vec2f.class) : null;
        AIs ai = jsonObject.has("ai") ? context.deserialize(jsonObject.get("ai"), AIs.class) : null;

        Mob mob = new Mob(0, 0, maxHealth, damage, direction);
        mob.setWalkAcc(walkAcc);
        mob.setWalkSpeed(walkSpeed);
        mob.setJumpAcc(jumpAcc);
        if (ai != null){
            mob.setAI(ai);
        }



        BodySerialization bodySerializer = new BodySerialization();
        bodySerializer.deserialize(mob, jsonObject, Body.class, context);
        mob.setDirection(direction);

        return mob;

    }
}
