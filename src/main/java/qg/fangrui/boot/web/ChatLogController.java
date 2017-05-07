package qg.fangrui.boot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.route.RouteNotExitException;
import qg.fangrui.boot.model.ChatLog;
import qg.fangrui.boot.model.User;
import qg.fangrui.boot.service.ChatLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 聊天室控制器
 * Created by FunriLy on 2017/4/15.
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/chat")
public class ChatLogController {

    private static final Logger logger = LoggerFactory.getLogger(ChatLogController.class);

    @Autowired
    private ChatLogService chatLogService;

    /**
     * 获得用户未读消息
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public RequestResult<List<ChatLog>> getChatLogList(HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<List<ChatLog>> result = chatLogService.getChatLogList(user.getUserid());
            return result;
        } catch (Exception e){
            logger.warn("获取用户未读消息记录出错：未知错误", e);
            return new RequestResult<List<ChatLog>>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 用户创建聊天室
     * @param request
     * @return
     */
    @RequestMapping("/create/{routeId}")
    @ResponseBody
    public RequestResult<Integer> createChatLog(@PathVariable int routeId,  HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<Integer> result = chatLogService.creatLogSocket(user.getUserid(), routeId);
            return result;
        } catch (RouteNotExitException e){
            logger.warn("创建聊天室失败:找不到路线");
            return new RequestResult<Integer>(StatEnum.ROUTE_NOT_EXIE, 0);
        } catch (Exception e){
            logger.warn("创建聊天室出错：未知错误", e);
            return new RequestResult<Integer>(StatEnum.DEFAULT_WRONG, 0);
        }
    }

    /**
     * 用户获得聊天室成员列表
     * @param key 聊天室标志
     * @return
     */
    @RequestMapping("/list/{key}")
    @ResponseBody
    public RequestResult<List<User>> getChatLogList(@PathVariable("key") int key){
        try {
            RequestResult<List<User>> result = chatLogService.getLogSocketList(key);
            return result;
        } catch (Exception e){
            logger.warn("获取聊天室成员列表出错：未知错误", e);
            return new RequestResult<List<User>>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 用户退出聊天室
     * @param key 聊天室标志
     * @param request
     * @return
     */
    @RequestMapping("/quit/{key}")
    @ResponseBody
    public RequestResult<?> deleteChatLog(@PathVariable("key") int key, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<?> result = chatLogService.deleteLogSocket(key, user.getUserid());
            return result;
        } catch (Exception e){
            logger.warn("退出聊天室出错：未知错误", e);
            return new  RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 用户加入聊天室
     * @param key 聊天室标志
     * @param request
     * @return
     */
    @RequestMapping("/add/{key}")
    @ResponseBody
    public RequestResult<?> addChatLog(@PathVariable("key") int key, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<?> result = chatLogService.addLogSocket(key, user.getUserid());
            return result;
        } catch (Exception e){
            logger.warn("加入聊天室出错：未知错误", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }
}
