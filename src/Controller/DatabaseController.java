package Controller;

import DB.Database;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class DatabaseController {

    private Database db;

    public DatabaseController() {
        this.db = Database.getInstance();
    }
    public void registerPlayer(String name){
        this.db.registerPlayer(name);
    }

    public void save(String name, int points){
        db.save(name, points);
    }

    public List<JSONObject> getAll(){

        return db.getScoreboard();
    }

    public String getRegisteredPlayer() {
        return this.db.getRegisteredPlayer();
    }
}
