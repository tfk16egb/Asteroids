package Test;

import DB.Database;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    Database database;
    String path;
    @BeforeEach
    void setUp() {
        path = "example.json";
        database = Database.getInstance();
    }

    @AfterEach
    void tearDown() {
        database = null;
    }

    @org.junit.jupiter.api.Test
    void getInstance() {
        assertNotNull(database, "Db should not be null");
    }

    @org.junit.jupiter.api.Test
    void setDatabasePath() {
        try {
            database.setDatabasePath(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(path, database.getDatabasePath());
    }

    @org.junit.jupiter.api.Test
    void save() {
        try {
            database.setDatabasePath(path);
            database.save("Emil", 10);
            database.save("Ahmad", 90);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, database.size());
    }

    @org.junit.jupiter.api.Test
    void printScoreboard() {
        try {
            database.setDatabasePath(path);
            database.save("Emil", 100);
            database.save("Yousif", 3);
            database.save("Ahmad", 20);
            database.printScoreboard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}