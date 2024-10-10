package kane.serialization;

import com.google.gson.*;
import kane.genericGame.ActiveAttributes;
import kane.genericGame.PassiveAttributes;
import kane.genericGame.hud.HudElement;
import kane.math.ArrayOperations;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;
import kane.physics.shapes.Point;
import kane.physics.shapes.Polygon;
import kane.renderer.SpriteController;
import kane.physics.shapes.*;

import java.awt.*;
import java.lang.reflect.Type;

public class ShapeSerialization implements JsonSerializer<Shape>, JsonDeserializer<Shape>{
    @Override
    public JsonElement serialize(Shape src, Type typeOfSrc, JsonSerializationContext context){
        if (src.getClass().equals(HudElement.class)){
            HudElementSerialization hudElementSerializer = new HudElementSerialization();
            return hudElementSerializer.serialize((HudElement) src, typeOfSrc, context);
        }

        throw new UnsupportedOperationException("Tried to serialize abstract Shape as JSON");
    }

    public JsonElement serialize(JsonElement json, Shape src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();
        jsonObject.addProperty("renderLayer", src.renderLayer);
        jsonObject.add("relPos", context.serialize(src.relPos));
        jsonObject.add("color", context.serialize(src.color));
        jsonObject.addProperty("collision", src.collision);
        jsonObject.addProperty("visible", src.visible);
        jsonObject.add("material", context.serialize(src.material));
        jsonObject.add("activeAttributes", context.serialize(ArrayOperations.remEmpty(src.activeAttributes)));
        jsonObject.add("passiveAttributes", context.serialize(ArrayOperations.remEmpty(src.passiveAttributes)));
        jsonObject.add("spriteControllers", context.serialize(src.getSpriteControllers()));
        return jsonObject;
    }

    @Override
    public Shape deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        throw new UnsupportedOperationException("Tried to deserialize Shape without a body.");

    }

    public Shape deserialize(Body body, JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();

        int renderLayer = jsonObject.get("renderLayer").getAsInt();
        Vec2f relPos = context.deserialize(jsonObject.get("relPos"), Vec2f.class);
        Color color = context.deserialize(jsonObject.get("color"), Color.class);
        boolean collision = jsonObject.get("collision").getAsBoolean();
        boolean visible = jsonObject.get("visible").getAsBoolean();
        Material material = context.deserialize(jsonObject.get("material"), Material.class);
        ActiveAttributes[] activeAttributes = context.deserialize(jsonObject.get("activeAttributes"), ActiveAttributes[].class);
        PassiveAttributes[] passiveAttributes = context.deserialize(jsonObject.get("passiveAttributes"), PassiveAttributes[].class);
        SpriteController[] spriteControllers = context.deserialize(jsonObject.get("spriteControllers"), SpriteController[].class);

        ShapeType shapeType = context.deserialize(jsonObject.get("shapeType"), ShapeType.class);
        Shape shape;
        if (shapeType.equals(ShapeType.BOX)){
            BoxSerialization boxDeserializer = new BoxSerialization();
            shape = boxDeserializer.deserialize(relPos, body, color, material, renderLayer, json, typeOfT, context);
        } else if (shapeType.equals(ShapeType.POLYGON)){
            PolygonSerialization polygonDeserializer = new PolygonSerialization();
            shape = polygonDeserializer.deserialize(relPos, body, color, material, renderLayer, json, typeOfT, context);
        } else if (shapeType.equals(ShapeType.CIRCLE)){
            CircleSerialization circleDeserializer = new CircleSerialization();
            shape = circleDeserializer.deserialize(relPos, body, color, material, renderLayer, json, typeOfT, context);
        } else if (shapeType.equals(ShapeType.LINESEGMENT)){
            LineSegmentSerialization lineSegmentDeserializer = new LineSegmentSerialization();
            shape = lineSegmentDeserializer.deserialize(relPos, body, color, material, renderLayer, json, typeOfT, context);
        } else if (shapeType.equals(ShapeType.PLANE)){
            PlaneSerialization planeDeserializer = new PlaneSerialization();
            shape = planeDeserializer.deserialize(relPos, body, color, material, renderLayer, json, typeOfT, context);
        } else if (shapeType.equals(ShapeType.POINT)){
            PointSerialization pointDeserializer = new PointSerialization();
            shape = pointDeserializer.deserialize(relPos, body, color, material, renderLayer, json, typeOfT, context);
        } else{
            throw new UnsupportedOperationException("Unknown Shape type for deserialization.");
        }

        shape.collision = collision;
        shape.visible = visible;
        for (ActiveAttributes attribute : activeAttributes){
            shape.addActiveAttribute(attribute);
        }
        for (PassiveAttributes attribute : passiveAttributes){
            shape.addPassiveAttribute(attribute);
        }
        if (spriteControllers.length > 0){
            shape.setSpriteControllers(spriteControllers);
        }

        return shape;
    }
}
