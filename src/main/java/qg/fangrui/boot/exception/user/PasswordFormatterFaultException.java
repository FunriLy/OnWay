package qg.fangrui.boot.exception.user;

/**
 * Created by FunriLy on 2017/4/6.
 * From small beginnings comes great things.
 */
public class PasswordFormatterFaultException extends  RuntimeException{
    public PasswordFormatterFaultException(String message) {
        super(message);
    }

    public PasswordFormatterFaultException(String message, Throwable cause) {
        super(message, cause);
    }
}
