package qg.fangrui.boot.exception.user;

/**
 * Created by FunriLy on 2017/4/6.
 * From small beginnings comes great things.
 */
public class PasswordEmptyUserException extends RuntimeException {
    public PasswordEmptyUserException(String message) {
        super(message);
    }

    public PasswordEmptyUserException(String message, Throwable cause) {
        super(message, cause);
    }
}