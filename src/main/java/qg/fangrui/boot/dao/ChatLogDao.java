package qg.fangrui.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import qg.fangrui.boot.model.ChatLog;

import java.util.List;

/**
 * Created by FunriLy on 2017/4/8.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface ChatLogDao {

    /**
     * 插入一条聊天记录
     * @param chatLog
     * @return
     */
    int insertChatLog(@Param("chatLog") ChatLog chatLog);

    /**
     * 获得用户未读的聊天记录
     * @param userid 用户id
     * @return
     */
    List<ChatLog> getChatLog(@Param("userid") int userid);

    /**
     * 删除用户已读的聊天记录
     * @param userid 用户id
     * @return
     */
    int deleteChatLog(@Param("userid") int userid);
}
