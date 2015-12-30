import database.PersistentDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by dibriel on 26-12-15.
 */

//Warning!
//Always check if database exists

public class ObjectRetriever {

    private PersistentDatabase database;
    private ClassProyector inspector;
    private ObjectBuilder builder;
    public ObjectRetriever(){
        this.database = ObjectPersistency.getDatabase();
        inspector = new ClassProyector();
        builder = new ObjectBuilder();
    }

    public PersistentObject find(PersistentObject anExampleObject, int id){
        //finds object by id
        return find(anExampleObject.getClass(), anExampleObject.tableName(), id);
    }

    private PersistentObject find(Class aClass, String table, int id){
        PersistentObject object = null;
        if (database != null) {
            HashMap<String, String> objectState = database.find(table, inspector.proyect(aClass), id);
            object = objectState == null ? null : builder.build(aClass, objectState);
        } else {
            System.out.println("Database is not operative.");
        }
        return object;
    }

    public Collection<PersistentObject> where(PersistentObject anExampleObject, String selection, String[] sArgs){
        return where(anExampleObject.getClass(), anExampleObject.tableName(), selection, sArgs);
    }

    private Collection<PersistentObject> where(Class aClass, String table, String selection, String[] sArgs){
        ArrayList<PersistentObject> result = null;
        if (database != null){
            Collection<HashMap<String, String>> states;
            states = database.where(table, inspector.proyect(aClass), selection, sArgs);
            result = new ArrayList<>(states.size());
            for(HashMap<String, String> state : states){
                result.add(builder.build(aClass, state));
            }
        } else {
            System.out.println("Database is not oparative.");
        }
        return result;
    }

}
