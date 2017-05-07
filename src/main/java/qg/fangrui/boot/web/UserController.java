package qg.fangrui.boot.web;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.exception.user.*;
import qg.fangrui.boot.model.ChatLog;
import qg.fangrui.boot.model.User;
import qg.fangrui.boot.service.UserService;
import qg.fangrui.boot.util.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * Created by FunriLy on 2017/4/5.
 * From small beginnings comes great things.
 */
@Controller
@PropertySource("classpath:amap.properties")
@RequestMapping("user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${key}")
    private String key;

    @Value("${ip_url}")
    private String ip_url;

    @Autowired
    private UserService userService;

    /**
     * 测试项目是否启动正常
     * @return
     */
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        System.out.println("key = "+key);
        return "hello world!";
    }

    /**
     * 测试与高德地图对接
     * @return
     */
    @RequestMapping("/getip")
    @ResponseBody
    public String getIP(){
        Map<String, String> param = new HashMap<String, String>();
        param.put("key", key);
        param.put("ip", "114.247.50.2");
        param.put("output", "JSON");
        String message = "";
        try {
            //获取 get 方法的返回参数
            message = HttpUtils.sendGet(ip_url,param);
        } catch (URISyntaxException e) {
            logger.error("", e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message = "+message);
        //将json字符串转化为json对象
        JSONObject obj = JSONObject.fromObject(message);
        return message;
    }

    /**
     * 用户登录
     * @param request
     * @return
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public RequestResult<User> login(@RequestBody MultiValueMap<String, String> map, HttpServletRequest request){
        try {
            System.out.println(map);
            String s_userid = map.get("userid").get(0);
            int userid = Integer.valueOf(s_userid);
            String password = map.get("password").get(0);
            RequestResult<User> result = userService.login(userid, password);
            User user = result.getData();
            //将用户存到session中
            request.getSession().setAttribute("user", user);
            return result;
        } catch (LoginMatchException e){
            logger.warn("用户登录出错：空用户对象");
            return new RequestResult<User>(StatEnum.LOGIN_USER_MISMATCH, null);
        } catch (LoginNotExitUserException e){
            logger.warn("用户登录出错：不存在的用户 ");
            return new RequestResult<User>(StatEnum.LOGIN_NOT_EXIT_USER, null);
        } catch (NumberFormatException e){
            logger.warn("用户登录出错：用户名非法");
            return new RequestResult<User>(StatEnum.LOGIN_USER_MISMATCH, null);
        } catch (Exception e){
            logger.warn("用户登录出错：未知错误 ", e);
            return new RequestResult<User>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 修改密码
     * @param request
     * @return
     */
    @RequestMapping("/changepass")
    @ResponseBody
    public RequestResult<Integer> passwordChange(@RequestBody MultiValueMap<String, String> map, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            String oldPassword = map.get("oldPassword").get(0);
            String newPassword = map.get("newPassword").get(0);
            RequestResult<Integer> result = userService.passwordChange(user.getUserid(), oldPassword, newPassword);
            return result;
        } catch (PasswordEmptyUserException e){
            logger.warn("修改密码错误：空用户对象 ");
            return new RequestResult<Integer>(StatEnum.PASSWORD_EMPTY_USER, null);
        } catch (PasswordFormatterFaultException e){
            logger.warn("修改密码错误：修改密码格式错误 ");
            return new RequestResult<Integer>(StatEnum.PASSWORD_FAMMTER_FAULT, null);
        } catch (LoginMatchException e){
            logger.warn("修改密码错误：旧密码错误 ");
            return new RequestResult<Integer>(StatEnum.PASSWORD_MISMATCH, null);
        } catch (Exception e){
            logger.warn("修改密码错误：未知错误 ", e);
            return new RequestResult<Integer>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 修改个人信息
     * @param map
     * @param request
     * @return
     */
    @RequestMapping("/info/update")
    @ResponseBody
    public RequestResult<User> infoChange(@RequestBody MultiValueMap<String, String> map, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            User temp = new User();
            temp.setUserid(user.getUserid());
            temp.setIntroduction(map.get("introduction").get(0));
            temp.setName(map.get("name").get(0));
            temp.setSex(Integer.valueOf(map.get("sex").get(0)));
            RequestResult<User> result = userService.updateUser(temp, user.getSex());
            User r_user = result.getData();
            request.getSession().setAttribute("user", r_user);
            return result;
        } catch (InformationEmptyUser e){
            logger.warn("修改用户信息错误：空用户对象 ");
            return new RequestResult<User>(StatEnum.INFORMATION_EMPTY_USER, null);
        } catch (Exception e){
            logger.warn("修改用户信息错误：未知错误 ", e);
            return new RequestResult<User>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 修改用户的匹配度
     * @param map
     * @param request
     * @return
     */
    @RequestMapping("/suitability")
    @ResponseBody
    public RequestResult<?> suitabilityChange(@RequestBody MultiValueMap<String, String> map,HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            String str = map.get("suitability").get(0);
            double suitability = Double.valueOf(str);
            RequestResult<?> result = userService.updateSuitability(user.getUserid(), suitability);
            return result;
        } catch (Exception e){
            logger.warn("修改匹配度出错：未知错误", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }


    /**
     * 上传头像
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/send/picture")
    @ResponseBody
    public RequestResult<?> sendPicture(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (null != file){
                String file_name = file.getOriginalFilename();
                if (file_name.endsWith(".jpg") || file_name.endsWith(".JPG")){
                    if (file.isEmpty())
                        return new RequestResult<Object>(StatEnum.SUBMIT_FILE_FAULT, null);

                    logger.debug("用户上传头像！ "+user.getUserid());
                    //System.out.println(request.getServletContext().getRealPath("/picture"));
                    //将文件上传到"picture"文件夹
                    FileUtils.copyInputStreamToFile(file.getInputStream(),
                            new File(request.getServletContext().getRealPath("/picture"),
                            user.getUserid()+".jpg"));
                    return new RequestResult<Object>(StatEnum.SUBMIT_FILE_SUCCESS, null);
                } else {
                    //不是jpg或者JPG文件
                    logger.warn("上传头像：上传空的文件！ "+user.getUserid());
                    return new RequestResult<Object>(StatEnum.SUBMIT_WRONG_FILE, null);
                }
            }
            logger.warn("上传头像：用户上传空！ ");
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        } catch (Exception e){
            logger.warn("用户上传头像错误：未知错误 ", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    @RequestMapping("/get/{userid}")
    @ResponseBody
    public RequestResult<User> getUserById(@PathVariable("userid") int userid){
        try {
            RequestResult<User> result = userService.getUserById(userid);
            return result;
        }catch (Exception e){
            return new RequestResult<User>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    @RequestMapping("/test1")
    @ResponseBody
    public RequestResult<ChatLog> nextLogin1(HttpServletRequest request){
        RequestResult<User> result = userService.login(11111, "123456");
        User user = result.getData();
        //将用户存到session中
        request.getSession().setAttribute("user", user);
        ChatLog chatLog = new ChatLog(11111, 22222, "数据测试");
        return new RequestResult<ChatLog>(StatEnum.DEFAULT_WRONG, chatLog);
    }

    @RequestMapping("/test2")
    @ResponseBody
    public RequestResult<User> nextLogin2(HttpServletRequest request){
        RequestResult<User> result = userService.login(22222, "123456");
        User user = result.getData();
        //将用户存到session中
        request.getSession().setAttribute("user", user);
        return result;
    }

}
