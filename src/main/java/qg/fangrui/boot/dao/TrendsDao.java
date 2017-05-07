package qg.fangrui.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import qg.fangrui.boot.model.Trends;

import java.util.Date;
import java.util.List;

/**
 * Created by FunriLy on 2017/4/25.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface TrendsDao {

    /**
     * 插入一条朋友圈
     * @param trends 朋友圈信息
     * @return
     */
    int insertTrends(@Param("trends") Trends trends);

    /**
     * 获得某一条朋友圈
     * @param id 朋友圈id
     * @return
     */
    Trends getTrendsById(@Param("id") String id);

    /**
     * 按条数获得用户的聊天
     * @param userid 用户id
     * @param start 开始标记
     * @param end 结束标记
     * @return
     */
    List<Trends> getTrendsByPage(@Param("userid") int userid, @Param("start") int start, @Param("end") int end);

    /**
     * 删除某一条记录
     * @param id 记录id
     * @return
     */
    int deleteTrends(@Param("id") String id);

    /**
     * 获得用户某段时间内的朋友圈
     * @param userid 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<Trends> getTrendsBySendTime(@Param("userid") int userid, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 获取一堆用户的朋友圈
     * @param list 用户列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<Trends> getTrendsByUseridList(@Param("list") List<Integer> list, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
