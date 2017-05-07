package qg.fangrui.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import qg.fangrui.boot.model.Relation;

import java.util.List;

/**
 * Created by FunriLy on 2017/4/8.
 * From small beginnings comes great things.
 */
@Mapper
@Repository("relationdao")
public interface RelationDao {

    /**
     * 插入一个好友关系
     * @param minUserId 数值小的用户id
     * @param maxUserId 数值大的用户id
     * @return
     */
    int insertRelation(@Param("minUserId") int minUserId, @Param("maxUserId") int maxUserId);

    /**
     * 根据关系id删除好友关系
     * @param id 关系id
     * @return
     */
    int deleteRelation(@Param("id") int id);

    /**
     * 通过用户id来查找其好友
     * @param userid 用户id
     * @return
     */
    List<Relation> getListByUserId(@Param("userid") int userid);

    /**
     * 判断两个用户之间是否存在好友关系
     * @param min 数值小的用户id
     * @param max 数值大的用户id
     * @return
     */
    Relation isExitRelation(@Param("min") int minUserid, @Param("max") int maxUserId);

    /**
     * 通过好友关系id获得关系
     * @param id 好友关系
     * @return
     */
    Relation findByRelationId(@Param("id") int id);
}
