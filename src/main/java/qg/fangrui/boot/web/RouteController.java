package qg.fangrui.boot.web;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.route.RouteNotExitException;
import qg.fangrui.boot.model.ChatRoom;
import qg.fangrui.boot.model.Route;
import qg.fangrui.boot.model.User;
import qg.fangrui.boot.service.RouteService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by FunriLy on 2017/5/2.
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/route")
public class RouteController {

    private final static Logger logger = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private RouteService routeService;

    /**
     * 获取路线列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public RequestResult<List<Route>> getAllRoute(HttpServletRequest request){
        try {
            User user = (User)  request.getSession().getAttribute("user");
            RequestResult<List<Route>> result = routeService.getAllRoute(user.getUserid());
            return result;
        } catch (Exception e) {
            logger.warn("获取用户所有路线发生未知错误", e);
            return new RequestResult<List<Route>>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 保存路线
     * @param map
     * @param request
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public RequestResult<?> saveRoute(@RequestBody MultiValueMap<String, String> map, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            Gson gson = new Gson();
            String routeJson = map.get("route").get(0);
            System.out.println(routeJson);
            Route route = gson.fromJson(routeJson, Route.class);
            route.setUserid(user.getUserid());
            RequestResult<?> result = routeService.saveRoute(route);
            return result;
        } catch (Exception e){
            logger.warn("保存路线出错：未知错误", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 获得匹配路线
     * @param routeId
     * @param request
     * @return
     */
    @RequestMapping("/path/{routeId}")
    @ResponseBody
    public RequestResult<ChatRoom> getPath(@PathVariable("routeId") int routeId, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<ChatRoom> result = routeService.getPath(routeId, user.getSuitability());
            return result;
        } catch (RouteNotExitException e){
            logger.warn("匹配路线出错：路线不存在 ");
            return new RequestResult<ChatRoom>(StatEnum.ROUTE_NOT_EXIE, null);
        } catch (Exception e){
            logger.warn("获取匹配路线出错：未知错误", e);
            return new RequestResult<ChatRoom>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 获得聊天室信息
     * @param roomId
     * @return
     */
    @RequestMapping("/{roomId}")
    @ResponseBody
    public RequestResult<ChatRoom> getChatRoom(@PathVariable("roomId") int roomId){
        try {
            return routeService.getChatRoom(roomId);
        } catch (Exception e){
            logger.warn("获取聊天室出错：未知错误", e);
            return new RequestResult<ChatRoom>(StatEnum.DEFAULT_WRONG, null);
        }
    }
}
