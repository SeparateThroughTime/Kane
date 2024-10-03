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
        JsonObject json = new JsonObject();

        json.addProperty("maxHealth", src.getMaxHealth());
        json.addProperty("damage", src.getDamage());
        json.add("direction", context.serialize(src.getDirection()));
        json.add("walkAcc", context.serialize(src.getWalkAcc()));
        json.addProperty("walkSpeed", src.getWalkSpeed());
        json.add("jumpAcc", context.serialize(src.getJumpAcc()));
        json.add("ai", context.serialize(src.getAi()));

        BodySerialization bodySerializer = new BodySerialization();
        json.add("Body", bodySerializer.serialize((Body) src, Body.class, context));

        return json;
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
        Body body = context.deserialize(jsonObject.get("Body"), Body.class);

        Mob mob = new Mob(0, 0, maxHealth, damage, direction);
        mob.setWalkAcc(walkAcc);
        mob.setWalkSpeed(walkSpeed);
        mob.setJumpAcc(jumpAcc);
        mob.setAI(ai);

//        BodySerialization bodySerializer = new BodySerialization();
//        bodySerializer.deserialize(mob, jsonObject.get("Body"), Body.class, context);

        return mob;

    }
}
