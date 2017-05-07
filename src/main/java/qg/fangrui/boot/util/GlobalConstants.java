package qg.fangrui.boot.util;

import java.util.Properties;

/**
 * 获得配置文件的基础类
 * Created by FunriLy on 2017/4/5.
 * From small beginnings comes great things.
 */
public class GlobalConstants {

    public static Properties interfaceUrlProperties;

    public static String getInterfaceUrl(String key){
        return (String) interfaceUrlProperties.get(key);
    }
}
