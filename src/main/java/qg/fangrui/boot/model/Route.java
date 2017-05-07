package qg.fangrui.boot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FunriLy on 2017/5/2.
 * From small beginnings comes great things.
 */
public class Route {

    private int id;         //唯一标识符
    private int userid;     //用户id
    // 起点和终点
    private XYPoint mStartPoint;
    private XYPoint mEndPoint;
    // 起点和终点的名字
    private String mStartName;
    private String mEndName;
    // 路线的类型
    private int selectedRouteType;
    // 路线的点集合，包含起点与终点
    private List<XYPoint> mPath;

    /**
     * 额外添加部分，方便存入数据库
     */
    private String xPath = "";
    private String yPath = "";
    private double mStartX;
    private double mStartY;
    private double mEndX;
    private double mEndY;

    public Route(XYPoint mStartPoint, XYPoint mEndPoint, String mStartName, String mEndName, int selectedRouteType, List<XYPoint> mPath) {
        this.mStartPoint = mStartPoint;
        this.mEndPoint = mEndPoint;
        this.mStartName = mStartName;
        this.mEndName = mEndName;
        this.selectedRouteType = selectedRouteType;
        this.mPath = mPath;
        //初始化
        this.mStartX = mStartPoint.getX();
        this.mStartY = mStartPoint.getY();
        this.mEndX = mEndPoint.getX();
        this.mEndY = mEndPoint.getY();
    }

    public Route(){}

    public XYPoint getmStartPoint() {
        return mStartPoint;
    }

    public void setmStartPoint(XYPoint mStartPoint) {
        this.mStartPoint = mStartPoint;
    }

    public XYPoint getmEndPoint() {
        return mEndPoint;
    }

    public void setmEndPoint(XYPoint mEndPoint) {
        this.mEndPoint = mEndPoint;
    }

    public String getmStartName() {
        return mStartName;
    }

    public void setmStartName(String mStartName) {
        this.mStartName = mStartName;
    }
    public String getmEndName() {
        return mEndName;
    }

    public void setmEndName(String mEndName) {
        this.mEndName = mEndName;
    }

    public int getSelectedRouteType() {
        return selectedRouteType;
    }

    public void setSelectedRouteType(int selectedRouteType) {
        this.selectedRouteType = selectedRouteType;
    }

    public List<XYPoint> getmPath() {
        return mPath;
    }

    public void setmPath(List<XYPoint> mPath) {
        this.mPath = mPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    //获得四个点的坐标
    public double getmStartX() {
        return mStartX;
    }

    public double getmStartY() {
        return mStartY;
    }

    public double getmEndX() {
        return mEndX;
    }

    public double getmEndY() {
        return mEndY;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", userid=" + userid +
                ", mStartPoint=" + mStartPoint +
                ", mEndPoint=" + mEndPoint +
                ", mStartName='" + mStartName + '\'' +
                ", mEndName='" + mEndName + '\'' +
                ", selectedRouteType=" + selectedRouteType +
                ", mPath=" + mPath +
                ", xPath='" + xPath + '\'' +
                ", yPath='" + yPath + '\'' +
                '}';
    }

    /**
     * 将点集合转化为字符串
     * 方便存入数据库
     */
    public void changePath(){
        for (XYPoint point : this.getmPath()){
            xPath = xPath + point.getX() + "%";
            yPath = yPath + point.getY() + "%";
        }
        this.mStartX = this.mStartPoint.getX();
        this.mStartY = this.mStartPoint.getY();
        this.mEndX = this.mEndPoint.getX();
        this.mEndY = this.mEndPoint.getY();
    }

    /**
     * 将字符串转化为点集合
     */
    public void restorePath(){
        if (!xPath.equals("") && !yPath.equals("")){
            String[] str_xPath = xPath.split("%");
            String[] str_yPath = yPath.split("%");
            List<XYPoint> pointList = new ArrayList<XYPoint>();
            for (int i=0; i<str_xPath.length && i<str_yPath.length; i++){
                pointList.add(new XYPoint(Double.valueOf(str_xPath[i]), Double.valueOf(str_yPath[i])));
            }
            this.setmStartPoint(new XYPoint(this.mStartX, this.mStartY));
            this.setmEndPoint(new XYPoint(this.mEndX, this.mEndY));
            this.setmPath(pointList);
        }
    }

    public void cleanPath(){
        this.xPath = "";
        this.yPath = "";
    }
}

