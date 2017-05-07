package qg.fangrui.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qg.fangrui.boot.dao.RouteDao;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.route.RouteNotExitException;
import qg.fangrui.boot.model.ChatRoom;
import qg.fangrui.boot.model.Route;
import qg.fangrui.boot.model.XYPoint;
import qg.fangrui.boot.util.DTWTool;

import java.util.List;

/**
 * Created by FunriLy on 2017/5/2.
 * From small beginnings comes great things.
 */
@Service
public class RouteService {

    //TODO:获得用户存储的路线后记得规整

    @Autowired
    private RouteDao routeDao;

    /**
     * 获得聊天室信息
     * @param roomId
     * @return
     */
    public RequestResult<ChatRoom> getChatRoom(int roomId){
        ChatRoom chatRoom = ChatLogService.chatRoomMap.get(roomId);
        if (null == chatRoom)
            return new RequestResult<ChatRoom>(StatEnum.CHAT_LOG_ISNOT_EXIT, null);
        return new RequestResult<ChatRoom>(StatEnum.CHAT_LOG_ROOM_INFO, chatRoom);
    }

    /**
     * 查找是否有匹配路线
     * @param routeId 路线id
     * @param suitability 匹配度
     * @return
     */
    public RequestResult<ChatRoom> getPath(int routeId, double suitability){
        int key = 0;
        Route route = routeDao.getRouteById(routeId);
        if (null == route)
            throw new RouteNotExitException("不存在这条路线");
        //对路线进行规整
        route.restorePath();
        //TODO
        double endX = route.getmEndPoint().getX();
        double endY = route.getmEndPoint().getY();
        //便利所有聊天室
        for (int roomkey : ChatLogService.chatRoomMap.keySet()){
            //获得整个聊天室的信息
            ChatRoom chatRoom = ChatLogService.chatRoomMap.get(roomkey);
            //类型匹配:如果聊天室的类型与用户路线类型不符合
            if (chatRoom.getType() != route.getSelectedRouteType()){
                continue;
            }
            //终点匹配
            if (isSuitability(endX, endY, chatRoom)){
                Route baseRoute = routeDao.getRouteById(chatRoom.getRouteId());
                //TODO:暂时不考虑路线删除问题
                if (null == baseRoute) {
                    //所处的基础路线有问题
                    //将该聊天室移除
                    ChatLogService.chatRoomMap.remove(roomkey);
                    ChatLogService.chatlogSocketSet.remove(roomkey);
                    //返回0，用户自行创建新路线
                    return new RequestResult<ChatRoom>(StatEnum.ROUTE_GET_PATH, null);
                }
                //对路线进行规整
                baseRoute.restorePath();
                //获得DTW值
                double result = DTWService(route, baseRoute);
                //计算匹配度
                double realSuitability = 0.0;
                if (result < 1){
                    realSuitability = 1-(result%1);
                }
                if (suitability < realSuitability*100){
                    //找到第一条匹配路线
                    key =  roomkey;
                    return new RequestResult<ChatRoom>(StatEnum.ROUTE_GET_PATH, chatRoom);
                }
            }
        }
        return new RequestResult<ChatRoom>(StatEnum.ROUTE_GET_PATH, null);
    }

    /**
     * 获得用户所有的路线
     * @param userid 用户id
     * @return
     */
    public RequestResult<List<Route>> getAllRoute(int userid){
        List<Route> routeList = routeDao.getAllRoute(userid);
        for (int i=0; i<routeList.size(); i++){
            //规整路线并清空
            routeList.get(i).restorePath();
            routeList.get(i).cleanPath();
        }
        return new RequestResult<List<Route>>(StatEnum.ROUTE_List, routeList);
    }

    /**
     * 用户查询某条路线的信息
     * @param id 路线id
     * @return
     */
    public RequestResult<Route> getRouteById(int id){
        Route route = routeDao.getRouteById(id);
        if (null == route)
            throw new RouteNotExitException("不存在这条路线");
        //规整路线并清空
        route.restorePath();
        route.cleanPath();
        return new RequestResult<Route>(StatEnum.ROUTE_GET_BY_ID, route);
    }

    /**
     * 存储一条路线，返回路线的编号
     * @param route
     * @return
     */
    public RequestResult<Integer> saveRoute(Route route){
        if (null == route)
            throw new RouteNotExitException("不存在这条路线");
        //路线规整
        route.changePath();
        routeDao.insertRoute(route);
        int routeId  = route.getId();
        if (routeId == 0)
            return new RequestResult<Integer>(StatEnum.ROUTE_INSERT_FAULT, 0);
        return new RequestResult<Integer>(StatEnum.ROUTE_INSERT_SUCCESS, routeId);
    }

    /**
     * 删除一条路线
     * @param routeId
     * @return
     */
    public RequestResult<?> deleteRoute(int routeId){
        if (null == routeDao.getRouteById(routeId))
            throw new RouteNotExitException("不存在这条路线");
        int result = routeDao.deleteRouteById(routeId);
        return new RequestResult<Object>(StatEnum.ROUTE_DELETE_SUCCESS, null);
    }

    /**
     * 所有路线
     * @return
     */
    public List<Route> getaLLRoute(){
        List<Route> list = routeDao.allRoute();
        for(int i=0; i<list.size(); i++){
            //规整路线并清空
            list.get(i).restorePath();
            list.get(i).cleanPath();
        }
        return list;
    }

    /**
     * 用户所有的路线
     * @param userid
     * @return
     */
    public List<Route> getMyRoute(int userid){
        List<Route> list = routeDao.getAllRoute(userid);
        for(int i=0; i<list.size(); i++){
            //规整路线并清空
            list.get(i).restorePath();
            list.get(i).cleanPath();
        }
        return list;
    }

    /**
     * 获得DTW值
     * @param realRoute
     * @param baseRoute
     * @return
     */
    public double DTWService(Route realRoute, Route baseRoute){
        /**
         * 将ArrayList转化为数组
         */
        List<XYPoint> realXYPoint = realRoute.getmPath();
        List<XYPoint> baseXYPoint = baseRoute.getmPath();
        double[] realX = new double[realXYPoint.size()];
        double[] realY = new double[realXYPoint.size()];
        for (int i=0; i<realXYPoint.size(); i++){
            realX[i] = realXYPoint.get(i).getX();
            realY[i] = realXYPoint.get(i).getY();
        }
        double[] baseX = new double[baseXYPoint.size()];
        double[] baseY = new double[baseXYPoint.size()];
        for (int i=0; i<baseXYPoint.size(); i++){
            baseX[i] = baseXYPoint.get(i).getX();
            baseY[i] = baseXYPoint.get(i).getY();
        }

        //计算匹配度
        DTWTool tool = new DTWTool();
        tool.setData(realX, baseX);
        double resultX = tool.runDTW();
        tool.setData(realY, baseY);
        double resultY = tool.runDTW();

        //取权
        double result = Math.sqrt(resultX*resultX + resultY*resultY);
        return result;
    }


    /**
     * 查询路线终点是否匹配
     * 控制终点的容错范围
     * @param x
     * @param y
     * @param chatRoom
     * @return
     */
    private boolean isSuitability(double x, double y, ChatRoom chatRoom){
        return (Subtraction(x, chatRoom.getX()) && Subtraction(y, chatRoom.getY()));
    }

    /**
     * 判断 |a-b| 是否小于 100
     * @param a
     * @param b
     * @return
     */
    private boolean Subtraction(double a, double b){
        if (Math.abs(a*1000000 - b*1000000) < 100)
            return true;
        return false;
    }


}
