package qg.fangrui.boot.exception.user;

/**
 * Created by FunriLy on 2017/4/6.
 * From small beginnings comes great things.
 */
public class InformationEmptyUser extends RuntimeException{
    public InformationEmptyUser(String message) {
        super(message);
    }

    public InformationEmptyUser(String message, Throwable cause) {
        super(message, cause);
    }
}

