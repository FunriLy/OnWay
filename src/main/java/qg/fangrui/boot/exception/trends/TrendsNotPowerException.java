package qg.fangrui.boot.exception.trends;

/**
 * Created by FunriLy on 2017/4/29.
 * From small beginnings comes great things.
 */
public class TrendsNotPowerException extends RuntimeException {

    public TrendsNotPowerException(String message){
        super(message);
    }

    public TrendsNotPowerException(String message, Throwable cause){
        super(message, cause);
    }

}
