package qg.fangrui.boot.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qg.fangrui.boot.dao.RelationDao;
import qg.fangrui.boot.dao.UserDao;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.relation.RelationIllegalException;
import qg.fangrui.boot.exception.user.UserException;
import qg.fangrui.boot.model.Relation;
import qg.fangrui.boot.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FunriLy on 2017/4/18.
 * From small beginnings comes great things.
 */
@Service
public class RelationService {

    @Autowired
    private RelationDao relationDao;

    @Autowired
    private UserDao userDao;

    /**
     * 添加好友关系
     * @param sendId
     * @param receiveId
     * @return
     */
    public RequestResult<?> addRelation(int sendId, int receiveId){
        if (sendId == 0 || receiveId == 0 || sendId == receiveId){
            throw new RelationIllegalException("非法id，发送者/接收者id为0或者相等");
        }
        User receiver = userDao.findByUuid(receiveId);
        if (null == receiver){
            throw  new UserException("不存在的用户");
        }

        int minId, maxId;

        //插入好友关系
        if (sendId > receiveId){
            minId = receiveId;
            maxId = sendId;
        } else {
            maxId = receiveId;
            minId = sendId;
        }

//        if (0 != relationDao.isExitRelation(minId, maxId))
//            return new RequestResult<Object>(StatEnum.RELATION_IS_EXIT);

        relationDao.insertRelation(minId, maxId);
        return new RequestResult<Object>(StatEnum.RELATION_ADD_SUCCESS);
    }

    /**
     * 获得用户的好友列表
     * @param userid 好友id
     * @return
     */
    public RequestResult<List<User>> getRelationList(int userid){
        List<Relation> relationList = relationDao.getListByUserId(userid);
        List<User> lists = new ArrayList<User>();
        if (null == relationList)
            return new RequestResult<List<User>>(StatEnum.RELATION_LIST, lists);

        int friendId;
        //便利集合获得好友列表
        for (Relation relation : relationList) {
            if (userid != relation.getMinUserId()){
                friendId = relation.getMinUserId();
            } else {
                friendId = relation.getMaxUserId();
            }
            User user = userDao.findByUuid(friendId);
            if (null == user)
                continue;
            user.setPassword("");
            lists.add(user);
        }
        return new RequestResult<List<User>>(StatEnum.RELATION_LIST, lists);
    }

    /**
     * 删除好友关系
     * @param userid
     * @param relationId
     * @return
     */
    public RequestResult<?> deleteRelation(int userid, int relationId){
        Relation relation = relationDao.findByRelationId(relationId);
        //不存在好友关系
        //关系中不存在该用户
        if ( (null == relation) || (userid != relation.getMinUserId() && userid != relation.getMaxUserId()) )
            return new RequestResult<Object>(StatEnum.RELATION_ISNOT_EXIT, null);

        relationDao.deleteRelation(relationId);
        return new RequestResult<Object>(StatEnum.RELATION_DELETE_SUCCESS, null);
    }

    public Relation isExitRelation(int minId, int maxId){
        return relationDao.isExitRelation(minId, maxId);
    }

}
