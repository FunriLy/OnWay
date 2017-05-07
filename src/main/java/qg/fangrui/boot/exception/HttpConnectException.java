package qg.fangrui.boot.exception;

/**
 * Created by FunriLy on 2017/4/5.
 * From small beginnings comes great things.
 */
public class HttpConnectException extends RuntimeException {

    public HttpConnectException(String message){
        super(message);
    }

    public HttpConnectException(String message, Throwable cause){
        super(message, cause);
    }
}
