package qg.fangrui.boot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qg.fangrui.boot.dao.InformationDao;
import qg.fangrui.boot.dao.RelationDao;
import qg.fangrui.boot.dao.UserDao;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.information.InformationNotFoundException;
import qg.fangrui.boot.exception.information.InformationPageException;
import qg.fangrui.boot.exception.information.InformationResultNumberException;
import qg.fangrui.boot.exception.relation.RelationIsExitException;
import qg.fangrui.boot.exception.user.UserException;
import qg.fangrui.boot.model.Information;
import qg.fangrui.boot.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FunriLy on 2017/4/16.
 * From small beginnings comes great things.
 */
@Service
public class InformationService {

    private static final Logger logger = LoggerFactory.getLogger(InformationService.class);

    @Autowired
    private InformationDao informationDao;

    @Autowired
    private RelationDao relationDao;

    @Autowired
    private UserDao userDao;

    /**
     * 插入好友申请系统通知
     * @param information 通知实体
     * @return
     */
    public RequestResult<?> insertInformation(Information information){
        int minId, maxId;
        if (information.getSendId() > information.getReceiveId()){
            minId = information.getReceiveId();
            maxId = information.getSendId();
        } else {
            maxId = information.getReceiveId();
            minId = information.getSendId();
        }
        if (null != relationDao.isExitRelation(minId, maxId)){
            throw new RelationIsExitException("申请双方已经存在好友关系");
        }

        User receiver = userDao.findByUuid(information.getReceiveId());
        if (null == receiver)
            throw new UserException("该用户不存在");

        information.setContent("用户"+information.getSendId()+"向用户"+information.getReceiveId()+"发送好友申请");
        information.setFlag(0);
        information.setDeleFlag(0);
        int result = informationDao.insertInformation(information);
        if (result == 0)
            return new RequestResult<Object>(StatEnum.INFO_INSERT_FAULT, null);
        return new RequestResult<Object>(StatEnum.INFO_INSERT_SUCCESS, null);
    }

    /**
     * 获得某个页面的10条记录
     * @param userid 用户id
     * @param page 页码
     * @param userName 用户名
     * @return
     */
    public RequestResult<List<Information>> getInfoList(int userid, int page, String userName){

        //判断page参数
        if (page<0){
            throw new InformationPageException("页面值非法，小于0");
        }
        int start = page * 10;
        int end = start + 10;
        List<Information> infoList = informationDao.getallInformation(userid, start, 10);
        List<Information> list = new ArrayList<Information>();

        for (Information info : infoList){
            //当前用户是发送者
            if (userid == info.getSendId() && 1 != info.getDeleFlag()){
                //若系统通知为其他通知
                if (4 == info.getFlag()){
                    list.add(info);
                } else {
                    try {
                        //修改字符
                        Information information = replaceInformation(userid, userName, info);
                        list.add(information);
                    } catch (UserException e){
                        logger.debug("获得系统通知错误", e);
                        continue;
                    }
                }

            } else if (userid == info.getReceiveId() && 2 != info.getDeleFlag()){
                //当前用户是接收者
                if (4 == info.getDeleFlag()){
                    list.add(info);
                } else {
                    try {
                        Information information = replaceInformation(userid, userName, info);
                        list.add(information);
                    } catch (UserException e){
                        logger.debug("获得系统通知错误", e);
                        continue;
                    }
                }
            }
        }

        return new RequestResult<List<Information>>(StatEnum.INFO_LIST, list);
    }

    /**
     * 处理用户系统通知(主要是好友申请)
     * 1-同意好友申请；2-忽略好友申请；
     * 3-拒绝好友申请；4-删除通知；
     * @param userid 用户id
     * @param infoId 通知id
     * @return
     */
    public RequestResult<?> updateInformation(int userid, int infoId, int result){
        if (result <= 0 || result >= 5){
            throw new InformationResultNumberException("处理数据之非法，数值应该位于1-4之内");
        }

        //获得系统通知
        Information info = informationDao.getInfoByInfoId(infoId);
        if (info == null){
            throw new InformationNotFoundException("没有找到相应的系统通知");
        }

        //类型 若1代表发送者，若2代表接收者
        int type = 1;
        if(userid != info.getSendId())
            type =2;

        //多条通知处理情况
        if(result != 4){
            int minId = info.getSendId() > info.getReceiveId() ? info.getReceiveId() : info.getSendId();
            int maxId = info.getSendId() > info.getReceiveId() ? info.getSendId() : info.getReceiveId();
            //如果两者已经是好友关系，进行再处理
            if (null != relationDao.isExitRelation(minId, maxId)){
                informationDao.updateRelationInfoFlag(infoId);
                throw new RelationIsExitException("申请双方已经存在好友关系");
            }
        }

        if (type == 1){
            //若申请服务的是发送者
            //删除操作
            if (result ==4) {
                if (0 > deleteInformation(userid, infoId, 1, info.getDeleFlag()))
                    return new RequestResult<Object>(StatEnum.INFO_DELETE_FAULT);
                else
                    return new RequestResult<Object>(StatEnum.INFO_DELETE_SUCCESS);
            }
            //TODO:也许有其他操作
        } else {
            //若申请服务的是接收者
            if (result == 4){
                //删除操作
                if (0 > deleteInformation(userid, infoId, 2, info.getDeleFlag()))
                    return new RequestResult<Object>(StatEnum.INFO_DELETE_FAULT);
                else
                    return new RequestResult<Object>(StatEnum.INFO_DELETE_SUCCESS);
            } else if (result == 3){
                //拒绝好友申请
                informationDao.insertInformation(new Information(0, info.getSendId(), "用户"+info.getReceiveId()+"拒绝你的好友申请", 4, 1));
            } else if (result == 2){
                //忽略好友申请
                //TODO:不做处理
            } else {
                //同意好友申请
                informationDao.insertInformation(new Information(0, info.getSendId(), "用户"+info.getReceiveId()+"同意你的好友生申请", 4, 1));
                //插入好友关系
                if(userid > info.getSendId()){
                    relationDao.insertRelation(info.getSendId(), userid);
                } else if (userid < info.getSendId()){
                    relationDao.insertRelation(userid, info.getSendId());
                } else {
                    throw  new InformationNotFoundException("发送者id与接收者id一样");
                }
            }
            informationDao.updateInformationReceive(userid, infoId, result, 0);
        }

        return new RequestResult<Object>(StatEnum.INFO_UPDATE_SUCCESS);
    }

    /**
     * 删除某条系统通知
     * 若标志为0，更新为发送(接收)者已删除，为1(2)
     * 若标志为1，且删除者为接收者则删除该条记录
     * 若标志为2，且删除者为发送者则删除该条记录
     * @param userid 用户id
     * @param infoId 通知id
     * @param type 用户类型
     * @param result 通知的删除记录标志
     * @return
     */
    private int deleteInformation(int userid, int infoId, int type, int result){
        if (result == 1 && type == 2){
            //发送者已经删除，接收者删除
            informationDao.deleteInformation(infoId);
        } else if (result == 2 && type ==1){
            //接收者已经删除，发送者删除
            informationDao.deleteInformation(infoId);
        } else if (result == 0 && type == 1){
            informationDao.updateInformationSend(userid, infoId, 0, 1);
        } else if (result == 0 && type == 2){
            informationDao.updateInformationReceive(userid, infoId, 0, 2);
        } else {
            return -1;
        }
        return 1;
    }

    /**
     * 将好友申请中的用户id替换为用户名
     * @param userid
     * @param username
     * @param info
     * @return
     */
    private Information replaceInformation(int userid, String username, Information info){
        if (userid == info.getSendId()){
            User receiver = userDao.findByUuid(info.getReceiveId());
            //不存在该用户
            if (null == receiver)
                throw new UserException("不存在的用户");

            String content = info.getContent();
            content = content.replace("用户"+String.valueOf(userid), "您");
            content = content.replace(String.valueOf(info.getReceiveId()), receiver.getName());
            info.setContent(content);

        } else {
            User sender = userDao.findByUuid(info.getSendId());
            if (null == sender)
                throw new UserException("不存在的用户");

            String content = info.getContent();
            content.replace("用户"+String.valueOf(userid), "您");
            content.replace(String.valueOf(info.getSendId()), sender.getName());
            info.setContent(content);
        }
        return info;
    }
}
