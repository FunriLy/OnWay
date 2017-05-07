package qg.fangrui.boot.web;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qg.fangrui.boot.dao.RelationDao;
import qg.fangrui.boot.dao.RouteDao;
import qg.fangrui.boot.dao.UserDao;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.model.ChatRoom;
import qg.fangrui.boot.model.Route;
import qg.fangrui.boot.model.User;
import qg.fangrui.boot.service.ChatLogService;
import qg.fangrui.boot.service.RouteService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by FunriLy on 2017/5/5.
 * From small beginnings comes great things.
 */
@Controller
public class OtherController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RelationDao relationDao;

    private static final int maxCount = 10;

    /**
     * 获得用户的圈子
     */
    @RequestMapping("/allroom")
    @ResponseBody
    public RequestResult<List<ChatRoom>> getAllChatRoom(HttpServletRequest request){
        try{
            User user = (User) request.getSession().getAttribute("user");
            List<ChatRoom> list = new ArrayList<ChatRoom>();
            for (int roomkey : ChatLogService.chatRoomMap.keySet()){
                List<Integer> useridList = ChatLogService.chatlogSocketSet.get(roomkey);
                if (useridList.contains(user.getUserid())){
                    list.add(ChatLogService.chatRoomMap.get(roomkey));
                }
            }
            return new RequestResult<List<ChatRoom>>(StatEnum.ALL_QUANZI, list);
        } catch (Exception e){
            return new RequestResult<List<ChatRoom>>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 推荐好友
     */
    @RequestMapping("/recommend")
    @ResponseBody
    public RequestResult<List<User>> recommendFriend(HttpServletRequest request){
        try {
            //存储dtw值与
            Map<Double, Integer> dtwMap = new HashMap<Double, Integer>();
            //存储dtw值，已经排好序
            Set<Double> dtwSet = new LinkedHashSet<Double>();
            //存储用户
            List<User> userList = new ArrayList<User>();

            int minId = 0;
            int maxId = 0;

            User user = (User) request.getSession().getAttribute("user");
            int count=0; //当前推荐用户数量和最大推荐数量
            //获得匹配度
            List<Route> allRoute = routeService.getaLLRoute();
            List<Route> myAllRoute = routeService.getMyRoute(user.getUserid());
            //遍历所有数组 m*n 规模
            for (Route myRoute : myAllRoute){
                for (Route BaseRoute : allRoute){
                    //同个用户路线
                    if (user.getUserid() == BaseRoute.getUserid()) {
                        continue;
                    }
                    //类型不一样
                    if (myRoute.getSelectedRouteType() != BaseRoute.getSelectedRouteType()){
                        continue;
                    }
                    //终点误差
                    //TODO:终点误差应该较大，不然可能匹配不到路线
                    double dtwResult = routeService.DTWService(myRoute, BaseRoute);
                    //将结果存入数组
                    dtwSet.add(dtwResult);
                    dtwMap.put(dtwResult, BaseRoute.getUserid());
                }
            }

            List<Double> dtwList = new ArrayList<Double>(dtwSet);
            //迭代dtw值获得相应用户id且用户少于10
            for (int i=dtwSet.size()-1; i>=0 && count<maxCount; i--){
                double dtwResult = dtwList.get(i);
                //如果路线差值大于1
                if(dtwResult > 1){
                    continue;
                }
                //获得可能推荐的好友id
                int f_userid = dtwMap.get(dtwResult);
                if (f_userid > user.getUserid()){
                    maxId = f_userid;
                    minId = user.getUserid();
                } else {
                    maxId = user.getUserid();
                    minId = f_userid;
                }
                //剔除掉已经存在好友关系的用户
                if (null != relationDao.isExitRelation(minId, maxId)){
                    continue;
                }

                //获得真正要推荐的好友
                User f_user = userDao.findByUuid(f_userid);
                if (f_user != null){
                    userList.add(f_user);
                    count++;
                }
            }

            return new RequestResult<List<User>>(StatEnum.RECOMMEND_FRIEND, userList);
        } catch (Exception e){
            return new RequestResult<List<User>>(StatEnum.DEFAULT_WRONG, null);
        }
    }
}
