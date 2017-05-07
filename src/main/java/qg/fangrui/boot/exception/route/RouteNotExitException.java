package qg.fangrui.boot.exception.route;

/**
 * Created by FunriLy on 2017/5/3.
 * From small beginnings comes great things.
 */
public class RouteNotExitException extends RuntimeException {

    public RouteNotExitException(String message){
        super(message);
    }

    public RouteNotExitException(String message, Throwable cause){
        super(message, cause);
    }
}
