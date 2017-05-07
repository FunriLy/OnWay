package qg.fangrui.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qg.fangrui.boot.dao.RelationDao;
import qg.fangrui.boot.dao.TrendsDao;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.trends.TrendsNotExitException;
import qg.fangrui.boot.exception.trends.TrendsNotPowerException;
import qg.fangrui.boot.model.Relation;
import qg.fangrui.boot.model.Trends;
import qg.fangrui.boot.util.CommonDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by FunriLy on 2017/4/25.
 * From small beginnings comes great things.
 */
@Service
public class TrendsService {

    @Autowired
    private TrendsDao trendsDao;

    @Autowired
    private RelationDao relationDao;

    /**
     * 插入一条朋友圈数据
     * @param trends
     * @return
     */
    public RequestResult<?> insertTrends(Trends trends){
        int result = trendsDao.insertTrends(trends);
        if (result == 0)
            return new RequestResult<Object>(StatEnum.TRENDS_ADD_FAULT, null);
        return new RequestResult<Object>(StatEnum.TRENDS_ADD_SUCCESS, trends.getId());
    }

    /**
     * 获得朋友圈一天记录
     * @param userid
     * @param endTime
     * @return
     */
    public RequestResult<List<Trends>> getTrendsList(int userid, long endTime){
        //获得一天内的时间
        long startTime = endTime - 24*60*60*1000;
        List<Relation> relationList = relationDao.getListByUserId(userid);
        List<Integer> userList = new ArrayList<Integer>();
        //先加入用户本身
        userList.add(userid);
        //获得好友与用户的集合
        for (Relation relation : relationList){
            if (relation.getMinUserId() != userid)
                userList.add(relation.getMinUserId());
            else
                userList.add(relation.getMaxUserId());
        }
        String list = "";
        for (int i=0; i<userList.size()-1; i++){
            list = list +  userList.get(i) + ",";
        }
        list = list + userList.get(userList.size()-1);

        Date startDate = CommonDate.changeLongtimeToDate(startTime);
        Date endDate = CommonDate.changeLongtimeToDate(endTime);
        List<Trends> trendses = trendsDao.getTrendsByUseridList(userList, startDate, endDate);
        return new RequestResult<List<Trends>>(StatEnum.TRENDS_List, trendses);
    }

    /**
     * 用户删除一条朋友圈
     * @param userid
     * @param trendsId
     * @return
     */
    public RequestResult<?> deleteTrends(int userid, String trendsId){
        Trends trends = trendsDao.getTrendsById(trendsId);
        if (null == trends){
            throw new TrendsNotExitException("找不到该条朋友圈");
        }
        if (userid != trends.getUserid()){
            throw new TrendsNotPowerException("用户没有相应的权限处理该条朋友圈");
        }
        trendsDao.deleteTrends(trendsId);
        return new RequestResult<Object>(StatEnum.TRENDS_DELETE_SUCCESS, null);
    }

    /**
     * 获得用户一天内的朋友圈
     * @param userid
     * @param endTime
     * @return
     */
    public RequestResult<List<Trends>> getMyTrendsList(int userid, long endTime){
        long startTime = endTime - 24*60*60*1000;

        Date startDate = CommonDate.changeLongtimeToDate(startTime);
        Date endDate = CommonDate.changeLongtimeToDate(endTime);

        List<Trends> myTrendses = trendsDao.getTrendsBySendTime(userid, startDate, endDate);
        return new RequestResult<List<Trends>>(StatEnum.TRENDS_MY_LIST, myTrendses);
    }
}
