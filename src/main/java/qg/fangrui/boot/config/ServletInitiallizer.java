package qg.fangrui.boot.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import qg.fangrui.boot.MyApplication;

/**
 * Created by FunriLy on 2017/4/22.
 * From small beginnings comes great things.
 */
public class ServletInitiallizer extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(MyApplication.class);
    }
}
