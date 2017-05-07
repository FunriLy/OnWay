package qg.fangrui.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import qg.fangrui.boot.model.Route;

import java.util.List;

/**
 * Created by FunriLy on 2017/5/2.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface RouteDao {

    /**
     * 加入一条路线记录
     * @param route
     * @return
     */
    int insertRoute(@Param("route") Route route);

    /**
     * 获得用户所有路线
     * @param userid
     * @return
     */
    List<Route> getAllRoute(@Param("userid") int userid);

    /**
     * 通过id获得某条路线
     * @param id
     * @return
     */
    Route getRouteById(@Param("id") int id);

    /**
     *
     * @param mEndName
     * @param userid
     * @return
     */
    Route getRouteBymEndName(@Param("mEndName") String mEndName, @Param("userid") int userid);

    /**
     * 通过id删除某一条路线
     * @param id
     * @return
     */
    int deleteRouteById(@Param("id") int id);

    /**
     * 获得所有路线
     * @return
     */
    List<Route> allRoute();

    /**
     * 获得用户所有路线
     * @param userid
     * @return
     */
    List<Route> myAllRoute(@Param("userid") int userid);
}
