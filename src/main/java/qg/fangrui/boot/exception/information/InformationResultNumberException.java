package qg.fangrui.boot.exception.information;

/**
 * 系统通知处理数只异常
 * Created by FunriLy on 2017/4/16.
 * From small beginnings comes great things.
 */
public class InformationResultNumberException extends RuntimeException {

    public InformationResultNumberException(String message){
        super(message);
    }

    public InformationResultNumberException(String message, Throwable cause){
        super(message, cause);
    }
}
