package qg.fangrui.boot.exception.user;

/**
 * Created by FunriLy on 2017/4/6.
 * From small beginnings comes great things.
 */
public class InformationFormatterFault extends RuntimeException{
    public InformationFormatterFault(String message) {
        super(message);
    }

    public InformationFormatterFault(String message, Throwable cause) {
        super(message, cause);
    }
}