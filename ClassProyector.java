import java.lang.reflect.Field;

/**
 * Created by dibriel on 28-12-15.
 */
public class ClassProyector {

    public String[] proyect(Class aClass){

        Field[] fields = aClass.getDeclaredFields();
        String[] proy = new String[fields.length];

        for(int i = 0; i < fields.length; i++){
            proy[i] = fields[i].getName();
        }

        return proy;
    }
}
