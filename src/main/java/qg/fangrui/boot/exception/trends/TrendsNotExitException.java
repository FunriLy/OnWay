package qg.fangrui.boot.exception.trends;

/**
 * Created by FunriLy on 2017/4/29.
 * From small beginnings comes great things.
 */
public class TrendsNotExitException extends RuntimeException{

    public TrendsNotExitException(String message){
        super(message);
    }

    public TrendsNotExitException(String message, Throwable cause){
        super(message, cause);
    }
}
