package qg.fangrui.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import qg.fangrui.boot.dao.UserDao;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.user.*;
import qg.fangrui.boot.model.User;

/**
 * 用户实体业务逻辑
 * Created by FunriLy on 2017/4/6.
 * From small beginnings comes great things.
 */
@Service
@Component
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 用户登录
     * @param uuid 用户uuid
     * @param password 用户密码
     * @return
     */
    public RequestResult<User> login(int uuid, String password){
        if (uuid == 0 || password == null){
            throw new LoginMatchException("空用户对象");
        }
        User realUser = userDao.findByUuid(uuid);
        if (realUser == null){
            //不存在的对象
            throw new LoginNotExitUserException("不存在的用户");
        } else if (!realUser.getPassword().equals(password)){
            //用户名或者密码错误
            //暂时不做密码加密
            //TODO
            throw new LoginMatchException("错误的用户名或密码");
        } else {
            //登录成功后将其密码设置为空字符串
            realUser.setPassword("");
            return new RequestResult<User>(StatEnum.LOGIN_SUCCESS,realUser);
        }
    }

    /**
     * 修改密码
     * @param uuid 用户uuid
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    public RequestResult<Integer> passwordChange(int uuid, String oldPassword, String newPassword){
        if (uuid == 0 || newPassword == null || oldPassword == null){
            throw new PasswordEmptyUserException("空用户对象");
        }
        else if(!newPassword.matches("\\w{6,15}")){
            throw new PasswordFormatterFaultException("修改密码格式错误");
        }
        else if (!oldPassword.equals(userDao.findByUuid(uuid).getPassword())){
            throw new LoginMatchException("密码错误");
        }
        else {
            User user = userDao.findByUuid(uuid);
            user.setPassword(newPassword);
            userDao.updateUser(user);
            return new RequestResult<Integer>(StatEnum.PASSWORD_CHANGE_SUCCESS, uuid);
        }
    }

    /**
     * 更新用户信息
     * @param user 用户新实体
     * @param oldSex 旧性别
     * @return
     */
    public RequestResult<User> updateUser(User user, int oldSex) {
        if (user == null) {
            throw new InformationEmptyUser("空用户对象");
        } else {
            //因为int默认为0，专门判断性别是否需要更改
            if (oldSex != user.getSex()){
                int sex = (oldSex == 0 ? 1 : 0);
                userDao.updateUserSex(user.getUserid(), sex);
            }
            //TODO:设计有点耦合
            //更新用户对象
            //置空密码

            userDao.updateUser(user);
            user = userDao.findByUuid(user.getUserid());
            user.setPassword("");
            return new RequestResult<User>(StatEnum.INFORMATION_CHANGE_SUCCESS, user);
        }
    }

    /**
     * 修改用户的匹配度
     * @param userid
     * @param suitability
     * @return
     */
    public RequestResult<?> updateSuitability(int userid, double suitability){
        int result = userDao.updateUserSuitability(userid, suitability);
        if (result < 1){
            return new RequestResult<Object>(StatEnum.INFORMATION_FORMMATTER_FAULT, null);
        }
        return new RequestResult<Object>(StatEnum.INFORMATION_CHANGE_SUCCESS, null);
    }

    /**
     *
     * @param id
     * @return
     */
    public RequestResult<User> getUserById(int id){
        User user = userDao.findByUuid(id);
        if (null == user) {
            return new RequestResult<User>(StatEnum.DEFAULT_WRONG, null);
        }
        else {
            user.setPassword("");
            return new RequestResult<User>(StatEnum.INFORMATION_GET, user);
        }
    }
}
