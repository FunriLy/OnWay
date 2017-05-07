package qg.fangrui.boot.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import qg.fangrui.boot.config.GetHttpSessionConfigurator;
import qg.fangrui.boot.dao.ChatLogDao;
import qg.fangrui.boot.dao.RelationDao;
import qg.fangrui.boot.model.ChatLog;
import qg.fangrui.boot.model.User;
import qg.fangrui.boot.service.ChatLogService;
import qg.fangrui.boot.service.RelationService;
import qg.fangrui.boot.util.CommonDate;
import qg.fangrui.boot.util.SpringUtil;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 聊天控制类
 * Created by FunriLy on 2017/4/15.
 * From small beginnings comes great things.
 */
//@Component
@ServerEndpoint(value = "/websocket", configurator = GetHttpSessionConfigurator.class)
public class WebSocket {

    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);

    //记录当前在线连接数
    private static int onlineCount = 0;

    //线程安全类，实现服务端与单一客户端通信
    //使用 Map 来存放，其中 key 为用户标识
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();

    private Session session;            //与某个客户端的连接会话
    private int userid;                 //用户id
    private HttpSession httpSession;    //request的session

    //用户id和websocket的session绑定的路由表
    private static Map<Integer, Session> routetab = new HashMap<Integer, Session>();

    /**
     * 连接建立成功后调用的方法
     * @param session 与客户端建立的会话连接
     * @param config
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        //获取当前用户的httpsession
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        try {
            //获取当前用户的id
            this.userid = ((User) httpSession.getAttribute("user")).getUserid();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("用户还未登录");
        }
        //绑定用户id与session会话
        routetab.put(userid, session);
        System.out.println("有新链接加入，当前人数为 "+getOnlineCount());
    }

    /**
     * 连接关闭后调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        routetab.remove(userid);        //移除路由器记录

        System.out.println("连接断开，当前人数为 "+getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param messgae 客户端消息
     */
    @OnMessage
    public void onMessage(String messgae) {
        System.out.println("收到来自客户端的信息："+messgae);
        Gson gson = new Gson();
        /**
         * 测试
         */
        //ChatLog chatLog = new ChatLog(11111, 22222, "数据测试");
        ChatLog chatLog = gson.fromJson(messgae, ChatLog.class);  //将前台JSON消息转化为java对象
        //chatLog.setSendId(this.userid);
        chatLog.setSendTime(System.currentTimeMillis());
        //发送聊天室信息
        if (chatLog.getFlag() == 3){
            int key = chatLog.getReceiveId();       //聊天室id
            //获取聊天室成员
            List<Integer> useridList = ChatLogService.chatlogSocketSet.get(key);
            if (useridList == null || useridList.size() == 0) {
                //找不到聊天室集合，自动发送错误信息
                ChatLog chatLog1 = new ChatLog();
                chatLog1.setSendId(0);
                chatLog1.setReceiveId(userid);
                chatLog1.setContent("发生未知错误！");
                //TODO:chatlog1 可能需要修改
                //为发送者发送消息提醒
                sendSpeciallyMessage(chatLog1, this.session);
                //TODO 可能这里需要做点什么
            }

            //获取聊天室中在线用户的session会话
            HashSet<WebSocket> temp_webSocket = new HashSet<WebSocket>();
            for(WebSocket socket : webSocketSet){
                if (useridList.contains(socket.userid)){
                    temp_webSocket.add(socket);
                }
            }
            //将信息广播出去
            broadcast(chatLog, temp_webSocket);
        } else {
            //与好友交流信息
            sendSecurityMessgae(chatLog, chatLog.getReceiveId(), this.session);
        }

    }

    /**
     * 发生错误时调用的方法
     * @param session 客户端会话
     * @param error 错误堆栈信息
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }


    /**
     * 对于整个聊天室进行广播
     * @param message 消息
     * @param webSockets 聊天室在线成员集合
     */
    public void broadcast(ChatLog message, HashSet<WebSocket> webSockets){
        for (WebSocket socket : webSockets){
            try {
                //若轮询到当前用户则跳过
                if (this.session == socket.session){
                    continue;
                }
                socket.session.getBasicRemote().sendObject(message);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("对某个聊天室广播失败", e);
                continue;
            }
        }
    }

    /**
     * 对于某个用户进行私聊广播
     * @param message 消息
     * @param userid 接收者id
     * @param sendSession 发送者类型
     */
    private void sendSecurityMessgae(ChatLog message, int userid, Session sendSession){

        RelationDao relationDao = SpringUtil.getBean(RelationDao.class);
        ChatLogDao chatLogDao = SpringUtil.getBean(ChatLogDao.class);

        int minId = message.getSendId() > message.getReceiveId() ? message.getReceiveId() : message.getSendId();
        int maxId = message.getSendId() > message.getReceiveId() ? message.getSendId() : message.getReceiveId();
        //检查发送者与接收者是否存在好友关系
        //System.out.println(relationDao.);
        if(null == relationDao.isExitRelation(minId, maxId)){
            ChatLog chatLog1 = new ChatLog();
            chatLog1.setSendId(0);
            chatLog1.setReceiveId(userid);
            chatLog1.setContent("您还没有添加对方为好友！");
            //为发送者发送消息提醒
            sendSpeciallyMessage(chatLog1, session);
            return;
        }

        Session r_session = routetab.get(userid);
        //当前接收者用户在线
        if(r_session != null){
            try {
                r_session.getBasicRemote().sendText(new Gson().toJson(message));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("对于某个在线用户私聊失败，当前用户id："+this.userid, e);
            }
        } else {
            //接收用户不在线
            //TODO : 存入数据库操作
            chatLogDao.insertChatLog(message);
        }
    }

    /**
     * 对特定用户发送信息
     * @param message 信息
     * @param r_session 接收者对象
     */
    public void sendSpeciallyMessage(ChatLog message, Session r_session){
        try {
            r_session.getBasicRemote().sendText(new Gson().toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("对特定用户发送WebSocket信息出错", e);
        }
    }

    /**
     * 线程安全调用
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public HttpSession getHttpSession(){
        return this.httpSession;
    }

    public static synchronized void notice(int userid){
        for (WebSocket socket : webSocketSet){
            //当前用户在线
            if (userid == socket.userid){
                /**
                 * 直接推送
                 */
                ChatLog chatLog = new ChatLog(0, userid, "你有待处理通知", 1);
                try {
                    socket.session.getBasicRemote().sendText(new Gson().toJson(chatLog));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
