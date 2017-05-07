package qg.fangrui.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import qg.fangrui.boot.dao.ChatLogDao;
import qg.fangrui.boot.dao.RouteDao;
import qg.fangrui.boot.dao.UserDao;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.route.RouteNotExitException;
import qg.fangrui.boot.model.ChatLog;
import qg.fangrui.boot.model.ChatRoom;
import qg.fangrui.boot.model.Route;
import qg.fangrui.boot.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 聊天室控制器
 * Created by FunriLy on 2017/4/15.
 * From small beginnings comes great things.
 */
@Service
public class ChatLogService {

    //存储聊天室列表
    public static Map<Integer, List<Integer>> chatlogSocketSet = new HashMap<Integer, List<Integer>>();
    //存储聊天室信息
    public static Map<Integer, ChatRoom> chatRoomMap = new HashMap<Integer, ChatRoom>();

    private final ReentrantLock lock = new ReentrantLock();
    private final Random random = new Random();

    //模拟名字
    private static List<String> chatroomNameList = new ArrayList<String>();
    static {
        chatroomNameList.add("好好学习,天天向上");
        chatroomNameList.add("刚好遇见你");
        chatroomNameList.add("动物世界");
        chatroomNameList.add("封刀不为峥嵘");
        chatroomNameList.add("My Demons");
        chatroomNameList.add("不愿回头");
    }

    @Autowired
    private UserDao userDao;

    @Autowired
    private RouteDao routeDao;

    @Autowired
    private ChatLogDao chatLogDao;

    /**
     * 用户创建聊天室
     * @param userid
     * @param routeId
     * @return
     */
    public RequestResult<Integer> creatLogSocket(int userid, int routeId){
        Route route = routeDao.getRouteById(routeId);
        if(route == null)
            throw new RouteNotExitException("路线不存在");
        //获得终点 x y 坐标，地点名，类型
        double x = route.getmEndX();
        double y = route.getmEndY();
        int type = route.getSelectedRouteType();
        String destination = route.getmEndName();

        Random random = new Random();
        //获得有效的编号
        int key = random.nextInt(100000)+1;
        while (null != chatlogSocketSet.get(key)){
            key = random.nextInt(100000)+1;
        }
        List<Integer> logList = new ArrayList<Integer>();
        //将用户id放到集合中
        logList.add(userid);
        chatlogSocketSet.put(key, logList);

        //创建聊天室类
        String chatroomName = chatroomNameList.get(random.nextInt(6));
        ChatRoom chatRoom = new ChatRoom(key, routeId, chatroomName, x, y, destination, type);
        chatRoomMap.put(key, chatRoom);

        return new RequestResult<Integer>(StatEnum.CHAT_LOG_CREATE_SUCCESS, key);
    }

    /**
     * 用户加入某个聊天室
     * @param key 聊天室标志
     * @param userid 用户id
     * @return
     */
    public RequestResult<?> addLogSocket(int key, int userid){
        //找不到相应的聊天室
        if (null == chatlogSocketSet.get(key))
            return new RequestResult<Object>(StatEnum.CHAT_LOG_ISNOT_EXIT, null);

        if (chatlogSocketSet.get(key).contains(userid))
            return new RequestResult<Object>(StatEnum.CHAT_LOG_HAS_EXIT, null);
        chatlogSocketSet.get(key).add(userid);
        //自增
        chatRoomMap.get(key).increase();
        return new RequestResult<Object>(StatEnum.CHAT_LOG_ADD_SUCCESS, null);
    }

    /**
     * 用户退出某个聊天室
     * @param key 聊天室标志
     * @param userid 用户id
     * @return
     */
    public RequestResult<?> deleteLogSocket(int key, int userid){
        //找不到相应的聊天室
        if (null == chatlogSocketSet.get(key))
            return new RequestResult<Object>(StatEnum.CHAT_LOG_ISNOT_EXIT, null);
        int size = chatlogSocketSet.get(key).size();
        //删除聊天室
        if (size <= 0) {
            chatlogSocketSet.remove(key);
            chatRoomMap.remove(key);
            return new RequestResult<Object>(StatEnum.CHAT_LOG_DELETE_SUCCESS, null);
        }

        /**
         * 重入锁锁定资源
         */
        lock.lock();
        for (int i=0; i<size; i++){
            if (userid == chatlogSocketSet.get(key).get(i)){
                chatlogSocketSet.get(key).remove(i);
                //自减
                chatRoomMap.get(key).decrease();
                //判断是否需要回收聊天室
                if (chatlogSocketSet.get(key).size() <= 0){
                    chatlogSocketSet.remove(key);
                    chatRoomMap.remove(key);
                }
                break;
            }
        }
        //确保还锁
        lock.unlock();
        return new RequestResult<Object>(StatEnum.CHAT_LOG_DELETE_SUCCESS, null);
    }

    /**
     * 用户获取当前聊天室的所有成员信息
     * @param key 聊天室标志
     * @return
     */
    public RequestResult<List<User>> getLogSocketList(int key){
        List<User> userList = new ArrayList<User>();
        //找不到相应的聊天室
        if (null == chatlogSocketSet.get(key))
            return new RequestResult<List<User>>(StatEnum.CHAT_LOG_ISNOT_EXIT, null);

        List<Integer> useridList = chatlogSocketSet.get(key);
        for (int userid : useridList){
            User user = userDao.findByUuid(userid);
            user.setPassword("");
            userList.add(user);
        }
        return new RequestResult<List<User>>(StatEnum.CAHT_LOG_ALL_USER, userList);
    }

    /**
     * 获得聊天室信息
     * @param key
     * @return
     */
    public RequestResult<ChatRoom> getChatRoom(int key){
        //找不到相应的聊天室
        if (null == chatlogSocketSet.get(key))
            return new RequestResult<ChatRoom>(StatEnum.CHAT_LOG_ISNOT_EXIT, null);
        ChatRoom chatRoom = chatRoomMap.get(key);
        return new RequestResult<ChatRoom>(StatEnum.CHAT_LOG_ROOM_INFO, chatRoom);
    }

    /**
     * 用户插入一条聊天记录
     * @param chatLog
     */
    public void insertChatLog(ChatLog chatLog){
        chatLogDao.insertChatLog(chatLog);
    }

    public RequestResult<List<ChatLog>> getChatLogList(int userid){
        List<ChatLog> chatLogList = chatLogDao.getChatLog(userid);
        chatLogDao.deleteChatLog(userid);
        return new RequestResult<List<ChatLog>>(StatEnum.CHAT_LOG_List, chatLogList);
    }
}
