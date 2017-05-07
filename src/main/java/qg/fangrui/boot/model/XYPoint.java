package qg.fangrui.boot.model;

/**
 * Created by FunriLy on 2017/5/2.
 * From small beginnings comes great things.
 */
public class XYPoint {
    // longitude and latitude
    private double x;
    private double y;

    public XYPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
