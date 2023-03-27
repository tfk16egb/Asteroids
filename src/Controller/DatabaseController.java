package Controller;

import DB.Database;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public class DatabaseController {

    private Database db;

    public DatabaseController() {
        this.db = Database.getInstance();
    }

    public void save(String name, int points){
        try {
            db.save(name, points);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setDatabasePath(String path){
        try {
            this.db.setDatabasePath(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void printScoreboard(){
        try {
            this.db.printScoreboard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public List getAll(){
        return db.getAll();
    }
}
