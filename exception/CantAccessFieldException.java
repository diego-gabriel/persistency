package exception;
/**
 * Created by dibriel on 27-12-15.
 */
public class CantAccessFieldException extends Exception {
    private String message;
    public CantAccessFieldException(String fieldName, String className){
        message = "Can't access field '"+fieldName+"' on class: " + className;
    }
    public String getMessage(){
        return message;
    }
}
