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
import qg.fangrui.boot.exception.information.InformationNotFoundException;
import qg.fangrui.boot.exception.information.InformationPageException;
import qg.fangrui.boot.exception.information.InformationResultNumberException;
import qg.fangrui.boot.exception.relation.RelationIsExitException;
import qg.fangrui.boot.exception.user.UserException;
import qg.fangrui.boot.model.Information;
import qg.fangrui.boot.model.User;
import qg.fangrui.boot.service.InformationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by FunriLy on 2017/4/21.
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/info")
public class InformationController {

    private static final Logger logger = LoggerFactory.getLogger(InformationController.class);

    @Autowired
    private InformationService informationService;

    /**
     * 获得消息列表
     * @param page 页面值
     * @param request
     * @return
     */
    @RequestMapping("/list/{page}")
    @ResponseBody
    public RequestResult<List<Information>> getInfoList(@PathVariable("page") int page, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<List<Information>> result = informationService.getInfoList(user.getUserid(), page, user.getName());
            return result;
        } catch (InformationPageException e){
            logger.warn("获得消息列表出错：非法Page参数 ");
            return new RequestResult<List<Information>>(StatEnum.INFO_PARAM_ERROR, null);
        } catch (Exception e){
            logger.warn("获取消息列表出错：未知错误 ", e);
            return new RequestResult<List<Information>>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 用户发送好友申请
     * @param receive
     * @param request
     * @return
     */
    @RequestMapping("/add/{receive}")
    @ResponseBody
    public RequestResult<?> addInformation(@PathVariable("receive") int receive, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            Information info = new Information();
            info.setSendId(user.getUserid());
            info.setReceiveId(receive);
            RequestResult<?> result = informationService.insertInformation(info);
            return result;
        } catch (RelationIsExitException e){
            logger.warn("发送好友申请出错：双方已经存在好友关系 ");
            return new RequestResult<Object>(StatEnum.RELATION_IS_EXIT, null);
        } catch (UserException e){
            logger.warn("发送好友申请出错：不存在的用户 ");
            return new RequestResult<Object>(StatEnum.LOGIN_NOT_EXIT_USER, null);
        } catch (Exception e){
            logger.warn("发送好友申请出错：未知错误 ", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 处理系统通知
     * 1-同意好友申请；2-忽略好友申请；
     * 3-拒绝好友申请；4-删除通知；
     * @param infoId 通知id
     * @param handle 处理结果
     * @param request
     * @return
     */
    @RequestMapping("/handle/{infoId}/{handle}")
    @ResponseBody
    public RequestResult<?> handleInformation(@PathVariable("infoId") int infoId, @PathVariable("handle") int handle, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<?> result = informationService.updateInformation(user.getUserid(), infoId, handle);
            return result;
        } catch (InformationResultNumberException e){
            logger.warn("处理消息出错：非法参数");
            return new RequestResult<Object>(StatEnum.INFO_PARAM_ERROR, null);
        } catch (InformationNotFoundException e){
            logger.warn("处理消息出错：未找到消息");
            return new RequestResult<Object>(StatEnum.INFO_NO_EXIT_ERROR, null);
        } catch (RelationIsExitException e){
            logger.warn("处理消息出错：已经存在好友关系");
            return new RequestResult<Object>(StatEnum.RELATION_IS_EXIT, null);
        } catch (Exception e){
            logger.warn("处理消息出错：未知错误", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }
}
