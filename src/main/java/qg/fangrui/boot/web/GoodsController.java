package qg.fangrui.boot.web;

import org.springframework.stereotype.Controller;
import qg.fangrui.boot.model.Goods;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品控制器
 * Created by FunriLy on 2017/4/12.
 * From small beginnings comes great things.
 */
@Controller
public class GoodsController {

    public static final List<Goods> goodsArray = new ArrayList<Goods>();

    //TODO:由于没有使用复制对象clone()，导致商品数量会有所变化

        /**
         * 设置六种静态商品
         */
    public static final Goods goods1 = new Goods(111101, 111, "天天特价", "实惠套餐A", 88.88, 1);
    public static final Goods goods2 = new Goods(111102, 111, "天天特价", "实惠套餐B", 66, 1);
    public static final Goods goods3 = new Goods(111203, 111, "一分钱一分货", "汉堡+小吃+可可圣代", 20, 1);
    public static final Goods goods4 = new Goods(222101, 222, "披萨专区", "水果披萨", 23.33, 1);
    public static final Goods goods5 = new Goods(222202, 222, "意面专区", "海鲜意大利面", 16, 1);
    public static final Goods goods6 = new Goods(222303, 222, "酒水专区", "无限续杯", 7, 1);
    static {
        goodsArray.add(goods1);
        goodsArray.add(goods2);
        goodsArray.add(goods3);
        goodsArray.add(goods4);
        goodsArray.add(goods5);
        goodsArray.add(goods6);
    }
}
