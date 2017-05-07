package qg.fangrui.boot.exception.information;

/**
 * 系统通知页面异常
 * Created by FunriLy on 2017/4/16.
 * From small beginnings comes great things.
 */
public class InformationPageException extends RuntimeException {

    public InformationPageException(String message){
        super(message);
    }

    public InformationPageException(String message, Throwable cause){
        super(message, cause);
    }
}
