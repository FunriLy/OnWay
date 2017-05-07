package qg.fangrui.boot.exception.information;

/**
 * Created by FunriLy on 2017/4/18.
 * From small beginnings comes great things.
 */
public class InformationNotFoundException extends RuntimeException {

    public InformationNotFoundException(String message){
        super(message);
    }

    public InformationNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
