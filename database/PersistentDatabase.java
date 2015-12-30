package database;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by dibriel on 26-12-15.
 */
public interface PersistentDatabase {
    int insert(String table, HashMap<String, Object> values);
    int update(String table, HashMap<String, Object> values, int columnID);
    HashMap<String, String> find(String table, String[] proyection, int columnID);
    Collection<HashMap<String, String>> where(String table, String[] proyection, String selection, String[] sArgs);
}