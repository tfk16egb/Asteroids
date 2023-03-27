package DB;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Database {
    private FileReader fileReader;
    private FileWriter fileWriter;
    private JSONArray jsonArray;



    private static Database databaseSingleton;
    private String filepath;

    private Database(){
        jsonArray = new JSONArray();
    }

    public static Database getInstance(){
        if(databaseSingleton == null){
            databaseSingleton = new Database();
        }
        return databaseSingleton;
    }

    public void setDatabasePath(String path) throws IOException {
        this.filepath = path;
        fileReader = new FileReader(path);
        fileWriter = new FileWriter(path);
    }
    public String getDatabasePath(){
        return this.filepath;
    }
    public int size(){
        return jsonArray.size();
    }

    public void save(String name, int points) throws IOException {
        JSONObject newJSON = new JSONObject();
        newJSON.put("Name", name);
        newJSON.put("Points", points);
        jsonArray.add(newJSON);


        Collections.sort(jsonArray, new PointsComparator());
        fileWriter.write(jsonArray.toJSONString());
        fileWriter.flush();

    }
    public void printScoreboard() throws IOException, ParseException {
        //jsonArray = (JSONArray) new JSONParser().parse(fileReader);
        //System.out.println(jsonArray.toJSONString());
        jsonArray.forEach(System.out::println);
    }
    public List<JSONObject> getAll(){
        return this.jsonArray;
    }

}
