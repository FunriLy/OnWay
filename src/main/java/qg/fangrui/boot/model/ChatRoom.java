package qg.fangrui.boot.model;

import qg.fangrui.boot.util.CommonDate;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 聊天室实体
 * Created by FunriLy on 2017/5/2.
 * From small beginnings comes great things.
 */
public class ChatRoom {

    private int id;             //房间id
    private int routeId;        //路线id
    private String roomName;    //房间名字
    private int roomCount;      //房间人数
    private String destination; //终点名
    private double x;           //终点 X
    private double y;           //终点 Y
    private Date createTime;    //创建时间
    private int type;           //类型
    private String description; //描述

    public ChatRoom(){
        roomCount = 1;
        this.createTime = CommonDate.changeLongtimeToDate(System.currentTimeMillis());
    }

    public ChatRoom(int id, int routeId, String roomName, double x, double y, String destination, int type){
        this.id = id;
        this.routeId = routeId;
        this.roomName = roomName;
        this.x = x;
        this.y = y;
        this.destination = destination;
        this.roomCount = 1;
        this.createTime = CommonDate.changeLongtimeToDate(System.currentTimeMillis());
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", routeId=" + routeId +
                ", roomName='" + roomName + '\'' +
                ", roomCount=" + roomCount +
                ", destination='" + destination + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", createTime=" + createTime +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * i++
     */
    public void increase(){
        synchronized (ChatRoom.class){
            this.roomCount++;
        }
    }

    /**
     * i--
     */
    public void decrease(){
        synchronized (ChatRoom.class){
            this.roomCount--;
        }
    }
}
