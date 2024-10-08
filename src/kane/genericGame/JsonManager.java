package kane.genericGame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import kane.exceptions.LoadJsonException;
import kane.exceptions.WriteJsonException;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.physics.shapes.Circle;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Plane;
import kane.physics.shapes.Polygon;
import kane.renderer.Sprite;
import kane.renderer.SpriteController;
import kane.serialization.*;
import kane.sound.SoundSource;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class JsonManager{
    public static JsonManager JSON_MANAGER;

    private Gson gson;

    private JsonManager(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Body.class, new BodySerialization());
        gsonBuilder.registerTypeAdapter(Box.class, new BoxSerialization());
        gsonBuilder.registerTypeAdapter(Circle.class, new CircleSerialization());
        gsonBuilder.registerTypeAdapter(Color.class, new ColorSerialization());
        gsonBuilder.registerTypeAdapter(LineSegment.class, new LineSegmentSerialization());
        gsonBuilder.registerTypeAdapter(Mob.class, new MobSerialization());
        gsonBuilder.registerTypeAdapter(Plane.class, new PlaneSerialization());
        gsonBuilder.registerTypeAdapter(kane.physics.shapes.Point.class, new PointSerialization());
        gsonBuilder.registerTypeAdapter(Polygon.class, new PolygonSerialization());
        gsonBuilder.registerTypeAdapter(Shape.class, new ShapeSerialization());
        gsonBuilder.registerTypeAdapter(SoundSource.class, new SoundSourceSerialization());
        gsonBuilder.registerTypeAdapter(SpriteController.class, new SpriteControllerSerialization());
        gsonBuilder.registerTypeAdapter(Sprite.class, new SpriteSerialization());
        gsonBuilder.registerTypeAdapter(Vec2f.class, new Vec2fSerialization());
        gson = gsonBuilder.setPrettyPrinting().create();
    }

    public static void initJsonManager(){
        if(JSON_MANAGER == null){
            JSON_MANAGER = new JsonManager();
        }
    }

    public void write(String filepath, Mob object) throws WriteJsonException{
        try{
            FileWriter writer = new FileWriter("jsonFile.json");
            gson.toJson(object, writer);
            writer.flush();
            writer.close();
        } catch (IOException e){
            throw new WriteJsonException(filepath, e);
        }
    }

    private Object load(String filepath, Type type) throws LoadJsonException{
        try{
            JsonReader reader = new JsonReader(new FileReader(filepath));
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e){
            throw new LoadJsonException(filepath, e);
        }
    }

    public Mob loadMob(String filepath) throws LoadJsonException{
        return (Mob) load(filepath, Mob.class);
    }

    public Body loadBody(String filepath) throws LoadJsonException{
        return (Body) load(filepath, Body.class);
    }
}
