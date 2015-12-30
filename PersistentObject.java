import exception.CantAccessFieldException;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by dibriel on 22-12-15.
 */
public abstract class PersistentObject {

    private int id;

    public PersistentObject(){
        this.id = -1;
    }

    public abstract String tableName();

    public boolean save(){
        boolean saved = false;
        ObjectKeeper keeper = new ObjectKeeper();

        if (id == -1){
            this.id = keeper.save(this);
            saved = this.id != -1;
        } else {
            saved = keeper.update(this) != -1;
        }

        if (saved){
            System.out.println("Object with id "+id+" saved successfully");
        }

        return saved;
    }

    public HashMap<String, Object> fieldsMap() throws CantAccessFieldException{
        HashMap<String, Object> fm = new HashMap<>();
        for (Field f : getClass().getDeclaredFields()){
            try{
                f.setAccessible(true);
                if (!f.getName().equals("id"))
                    fm.put(f.getName(), f.get(this));
            } catch (IllegalAccessException e){
                e.printStackTrace();
                throw new CantAccessFieldException(f.getName(),getClass().getName());
            }
        }
        return fm;
    }



    public int getID(){
        return id;
    }

}
