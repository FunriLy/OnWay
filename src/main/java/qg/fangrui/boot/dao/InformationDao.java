package qg.fangrui.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import qg.fangrui.boot.model.Information;

import java.util.List;

/**
 * 系统通知实体操作接口
 * Created by FunriLy on 2017/4/15.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface InformationDao {

    /**
     * 插入系统通知
     * @param info 系统通知
     * @return
     */
    int insertInformation(@Param("info")Information info);

    /**
     * 获取用户发送或者收到的系统通知
     * @param userid 用户id
     * @param start 开始条数
     * @param size 条数
     * @return
     */
    List<Information> getallInformation(@Param("userid") int userid, @Param("start") int start, @Param("size") int size);

    /**
     * 删除特定的系统通知
     * @param infoId 通知id
     * @return
     */
    int deleteInformation(@Param("infoId") int infoId);

    /**
     * 更新接收者系统通知
     * @param receiveId 接收者id
     * @param infoId 通知id
     * @param flag 处理结果
     * @param deleFlag
     * @return
     */
    int updateInformationReceive(@Param("receiveId") int receiveId,@Param("infoId") int infoId, @Param("flag") int flag, @Param("deleFlag") int deleFlag);

    /**
     * 更新发送者系统通知
     * @param sendId 发送者id
     * @param infoId 通知id
     * @param flag 处理结果
     * @param deleFlag
     * @return
     */
    int updateInformationSend(@Param("sendId") int sendId, @Param("infoId") int infoId, @Param("r_flag") int flag, @Param("deleFlag") int deleFlag);


    /**
     * 通过消息id获得某条系统通知
     * @param infoId 通知id
     * @return 系统通知
     */
    Information getInfoByInfoId(@Param("infoId") int infoId);

    /**
     * 若有多条通知，且两者为好友关系
     * 全部更新为同意
     * @param infoId 通知id
     * @return
     */
    int updateRelationInfoFlag(@Param("infoId") int infoId);
}
