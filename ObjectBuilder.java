import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by dibriel on 28-12-15.
 */
public class ObjectBuilder {

    public PersistentObject build(Class aClass, HashMap<String, String> state){
        PersistentObject object;
        try{
            object = (PersistentObject)aClass.newInstance();
            for(Field field : aClass.getDeclaredFields()){
                if (state.containsKey(field.getName()))
                    setField(field, object, state.get(field.getName()));
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Make sure all attributes of "+aClass.getName()+" can be constructed from a single String.");
            object = null;
        }

        return object;
    }

    private void setField(Field field, PersistentObject object, String value) throws Exception{
        field.setAccessible(true);
        Class fieldClass = field.getType();
        System.out.println("Setting field: " + field.getName() + " of class " + field.getType().getName());
        if (fieldClass.getName().equals("int")){
            field.set(object, Integer.parseInt(value));
        } else {
            field.set(object, fieldClass.getConstructor(String.class).newInstance(value));
        }
    }
}
