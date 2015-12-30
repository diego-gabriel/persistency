
import database.PersistentDatabase;
import exception.CantAccessFieldException;

/**
 * Created by dibriel on 27-12-15.
 */
public class ObjectKeeper {
    private PersistentDatabase database;
    public ObjectKeeper(){
        this.database = ObjectPersistency.getDatabase();
    }

    public int save(PersistentObject aObject){
        int saved = -1;
        if (database != null){
            try {
                saved = database.insert(aObject.tableName(), aObject.fieldsMap());
            } catch (CantAccessFieldException e){
                System.out.println(e.getMessage());
                saved = -1;
            }
        } else {
            System.out.println("Database is not operative");
        }
        return saved;
    }

    public int update(PersistentObject aObject){
        int updatedCount = -1;
        if (database != null){
            try{
                String[] params =  {String.valueOf(aObject.getID())};
                updatedCount = database.update(aObject.tableName(), aObject.fieldsMap(), aObject.getID());
            } catch (CantAccessFieldException e){
                System.out.println(e.getMessage());
                updatedCount = -1;
            }
        } else {
            System.out.println("Database is not operative.");
        }
        return updatedCount;
    }
}
