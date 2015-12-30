
import database.PersistentDatabase;

/**
 * Created by dibriel on 27-12-15.
 */
public class ObjectPersistency {
    private static PersistentDatabase database = null;

    public static boolean setUp(PersistentDatabase database){
        boolean success = database != null;
        ObjectPersistency.database = database;
        return success;
    }
    public static PersistentDatabase getDatabase(){
        return database;
    }
}
