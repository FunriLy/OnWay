package qg.fangrui.boot.exception.relation;

/**
 * Created by FunriLy on 2017/4/18.
 * From small beginnings comes great things.
 */
public class RelationIsExitException extends RuntimeException {

    public RelationIsExitException(String message){
        super(message);
    }

    public RelationIsExitException(String message, Throwable cause){
        super(message, cause);
    }
}
