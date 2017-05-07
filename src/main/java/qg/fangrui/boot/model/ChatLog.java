package qg.fangrui.boot.model;

import qg.fangrui.boot.util.CommonDate;

import java.util.Date;

/**
 * 聊天记录实体类
 * Created by FunriLy on 2017/4/8.
 * From small beginnings comes great things.
 */
public class ChatLog {



    private int id;
    private int sendId;         //发送者
    private int receiveId;      //接收者
    private String content;     //交流内容
    private int flag;           //是否已读，0为未读，1为已读，3为群聊信息
    private long sendTime;

    public ChatLog(){
        this.sendTime = System.currentTimeMillis();
    }

    public ChatLog(int sendId, int receiveId, String content){
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.content = content;
        this.flag = 0;
        this.sendTime = System.currentTimeMillis();
    }

    public ChatLog(int sendId, int receiveId, String content, int flag){
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.content = content;
        this.flag = flag;
        this.sendTime = System.currentTimeMillis();
    }

    // get && set

    public int getId() {
        return id;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    // toString

    @Override
    public String toString() {
        return "ChatLog{" +
                "id=" + id +
                ", sendId=" + sendId +
                ", receiveId=" + receiveId +
                ", content='" + content + '\'' +
                ", flag=" + flag +
                ", sendTime=" + sendTime +
                '}';
    }
}
