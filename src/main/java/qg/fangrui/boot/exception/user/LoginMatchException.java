package qg.fangrui.boot.exception.user;

/**
 * 空用户对象
 * Created by FunriLy on 2017/4/6.
 * From small beginnings comes great things.
 */
public class LoginMatchException extends RuntimeException {

    public LoginMatchException(String message) {
        super(message);
    }

    public LoginMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
