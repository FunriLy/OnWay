package qg.fangrui.boot.web;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.trends.TrendsNotExitException;
import qg.fangrui.boot.exception.trends.TrendsNotPowerException;
import qg.fangrui.boot.model.Trends;
import qg.fangrui.boot.model.User;
import qg.fangrui.boot.service.TrendsService;
import qg.fangrui.boot.util.UUIDGenerator;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by FunriLy on 2017/4/29.
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/trends")
public class TrendsController {

    private static final Logger logger = LoggerFactory.getLogger(TrendsController.class);

    @Autowired
    private TrendsService trendsService;

    /**
     * 上传朋友圈
     * @param request
     * @return
     */
    @RequestMapping("/send")
    @ResponseBody
    public RequestResult<?> insertTrends(@RequestParam("message") String message, @RequestParam("file") MultipartFile[] files, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            //获得UUID编号
            String uuid = UUIDGenerator.getUUID();
            int photoCount = 0;
            //String message = map.get("message").get(0);

            //上传图片超过9张
            if (9 < files.length){
                return new RequestResult<Object>(StatEnum.TRENDS_PHOTO_TOO_MORE, null);
            }

            for (int i=0; i<files.length; i++){
                MultipartFile file = files[i];
                String file_name = file.getOriginalFilename();
                //如果文件是JPG图片
                if (file_name.endsWith(".jpg") || file_name.endsWith(".JPG")){
                    if (!file.isEmpty()){
                        FileUtils.copyInputStreamToFile(file.getInputStream(),
                                new File(request.getServletContext().getRealPath("/photo"), uuid+"_"+(photoCount+1)+".jpg"));
                        //文件存储成功，数量+1
                        photoCount++;
                    }
                } else {
                    //文件格式出错
                    return new RequestResult<Object>(StatEnum.TRENDS_ONLY_PHOTO, null);
                }
            }
            Trends trends = new Trends(uuid, user.getUserid(), message, photoCount);
            RequestResult<?> result = trendsService.insertTrends(trends);
            return result;
        } catch (IOException e) {
            logger.warn("用户上传图片出错");
            return new RequestResult<Object>(StatEnum.TRENDS_ADD_FAULT, null);
        } catch (Exception e){
            logger.warn("用户发表朋友圈发生错误：未知错误", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 删除朋友圈
     * @param trendsId
     * @param request
     * @return
     */
    @RequestMapping("/delete/{trendsId}")
    @ResponseBody
    public RequestResult<?> deleteTrends(@PathVariable String trendsId, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<?> result = trendsService.deleteTrends(user.getUserid(), trendsId);
            return result;
        } catch (TrendsNotExitException e){
            logger.warn("用户删除朋友圈出错：找不到该条朋友圈");
            return new RequestResult<Object>(StatEnum.TRENDS_DELETE_FAULT, null);
        } catch (TrendsNotPowerException e){
            logger.warn("用户删除朋友圈出错：没有相应的权限");
            return new RequestResult<Object>(StatEnum.TRENDS_DELETE_FAULT, null);
        } catch (Exception e){
            logger.warn("用户删除朋友圈发生错误：未知错误", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 获得某个用户一天的朋友圈
     * @param searchTime 计算时间
     * @param request
     * @return
     */
    @RequestMapping("/personal/{userid}/{searchTime}")
    @ResponseBody
    public RequestResult<List<Trends>> getMyTrendsList(@PathVariable("userid") int userid, @PathVariable("searchTime") long searchTime, HttpServletRequest request){
        try {
            RequestResult<List<Trends>> result = trendsService.getMyTrendsList(userid, searchTime);
            //System.out.println("ddd==="+result.getData().toArray().toString());
            return result;
        } catch (Exception e){
            logger.warn("获取自己朋友圈一天记录出错：未知错误", e);
            return new RequestResult<List<Trends>>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 获得朋友圈一天记录
     * @param searchTime
     * @param request
     * @return
     */
    @RequestMapping("/trendses/{searchTime}")
    @ResponseBody
    public RequestResult<List<Trends>> getTrendsesList(@PathVariable("searchTime") long searchTime, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            RequestResult<List<Trends>> result = trendsService.getTrendsList(user.getUserid(), searchTime);
            return result;
        } catch (Exception e){
            logger.warn("获得用户一天朋友圈记录出错：未知错误", e);
            return new RequestResult<List<Trends>>(StatEnum.DEFAULT_WRONG, null);
        }
    }
}
