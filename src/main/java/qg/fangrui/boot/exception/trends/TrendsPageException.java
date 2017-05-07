package qg.fangrui.boot.exception.trends;

/**
 * Created by FunriLy on 2017/4/29.
 * From small beginnings comes great things.
 */
public class TrendsPageException extends RuntimeException {

    public TrendsPageException(String message){
        super(message);
    }

    public TrendsPageException(Throwable cause, String message){
        super(message, cause);
    }
}
