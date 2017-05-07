package qg.fangrui.boot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.model.User;
import qg.fangrui.boot.service.RelationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by FunriLy on 2017/4/21.
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/relation")
public class RelationController {

    private static final Logger logger = LoggerFactory.getLogger(RelationController.class);

    @Autowired
    private RelationService relationService;

    /**
     * 删除好友关系
     * @param relationId
     * @param request
     * @return
     */
    @RequestMapping("/delete/{relationId}")
    @ResponseBody
    public RequestResult<?> deleteRelation(@PathVariable("relationId") int relationId, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<?> result = relationService.deleteRelation(user.getUserid(), relationId);
            return result;
        } catch (Exception e){
            logger.warn("删除好友关系出错：未知错误 ", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    @RequestMapping("/list")
    @ResponseBody
    public RequestResult<List<User>> getAllRelation(HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<List<User>> result = relationService.getRelationList(user.getUserid());
            return result;
        } catch (Exception e){
            logger.warn("获得好友列表出错：未知错误 ", e);
            return new RequestResult<List<User>>(StatEnum.DEFAULT_WRONG, null);
        }
    }


}
