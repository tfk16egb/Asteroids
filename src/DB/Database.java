package DB;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Database {

    private JSONArray existingObjects;

    private String name;
    private static Database databaseSingleton;
    private String filepath;

    private Database(String filepath) {
        this.existingObjects = new JSONArray();
        this.filepath = filepath;
    }
    public void registerPlayer(String name){
        this.name = name;
    }

    public static Database getInstance() {
        if (databaseSingleton == null) {
            databaseSingleton = new Database("scoreboard.json");
        }
        return databaseSingleton;
    }

    private void reloadDatabase() {
        // Read existing JSON objects from the file
        try (FileReader reader = new FileReader(filepath)) {
            existingObjects = (JSONArray) new JSONParser().parse(reader);
            existingObjects.sort((o1, o2) -> {
                int points1 = Integer.parseInt(((JSONObject) o1).get("Points").toString());
                int points2 = Integer.parseInt(((JSONObject) o2).get("Points").toString());
                return Integer.compare(points2, points1);
            });
        } catch (FileNotFoundException e) {
            // Ignore file not found errors, we will create the file later
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void save(String name, int points) {
        reloadDatabase();

        // Create a new JSON object for the current game score
        JSONObject newObject = new JSONObject();
        newObject.put("Points", points);
        newObject.put("Name", name);


        // Add the new JSON object to the existing array
        existingObjects.add(newObject);

        // Write the updated JSON array to the file
        try (FileWriter file = new FileWriter(filepath)) {
            file.write(existingObjects.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public List<JSONObject> getScoreboard() {
        reloadDatabase();
        JSONArray scoreboard = new JSONArray();
        for(int i = 1; i <= 10; i++){
            scoreboard.add(existingObjects.get(i));
        }
        return scoreboard;
    }


    public String getRegisteredPlayer() {
        return this.name;
    }
}
