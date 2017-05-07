package qg.fangrui.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import qg.fangrui.boot.model.User;

/**
 * 用户实体操作接口
 * Created by FunriLy on 2017/4/11.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface UserDao {

    /**
     * 更新除创建时间外的用户信息
     * @param user 用户对象
     * @return
     */
    int updateUser(@Param("user") User user);

    int updateUserSex(@Param("userid") int userid, @Param("sex") int sex);

    int updateUserSuitability(@Param("userid") int userid, @Param("suitability") double suitability);

    /**
     * 通过userid获得用户的全部信息
     * @param userid 用户userid
     * @return
     */
    User findByUuid(@Param("userid") int userid);
}
