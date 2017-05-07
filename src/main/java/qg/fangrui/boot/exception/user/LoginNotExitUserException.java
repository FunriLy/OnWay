package qg.fangrui.boot.exception.user;

/**
 * Created by FunriLy on 2017/4/6.
 * From small beginnings comes great things.
 */
public class LoginNotExitUserException extends RuntimeException {

    public LoginNotExitUserException(String message) {
        super(message);
    }

    public LoginNotExitUserException(String message, Throwable cause) {
        super(message, cause);
    }
}

