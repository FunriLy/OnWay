package qg.fangrui.boot.exception.relation;

/**
 * Created by FunriLy on 2017/4/18.
 * From small beginnings comes great things.
 */
public class RelationIllegalException extends RuntimeException {

    public RelationIllegalException(String message){
        super(message);
    }

    public RelationIllegalException(String message, Throwable cause){
        super(message, cause);
    }
}
