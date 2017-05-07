package qg.fangrui.boot.proxy;

import javax.servlet.Servlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by FunriLy on 2017/4/19.
 * From small beginnings comes great things.
 */
public class TestServlet {

//    @Bean
//    public Servlet baiduProxyServlet(){
//        return new HTTPSProxyServlet();
//    }
//
//    @Bean
//    public ServletRegistrationBean proxyServletRegistration(){
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(baiduProxyServlet(), "/*");
//        Map<String, String> params = ImmutableMap.of(
//                "targetUri", "https://www.baidu.com",
//                "log", "true");
//        registrationBean.setInitParameters(params);
//        return registrationBean;
//    }

}
