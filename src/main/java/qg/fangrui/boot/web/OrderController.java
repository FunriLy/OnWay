package qg.fangrui.boot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.model.Goods;
import qg.fangrui.boot.model.Order;
import qg.fangrui.boot.util.CommonDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单控制器
 * Created by FunriLy on 2017/4/12.
 * From small beginnings comes great things.
 */
@Controller
public class OrderController {

    public static List<Order> orderArray = new ArrayList<Order>();

    static {
        Goods goods1 = GoodsController.goods1;
        goods1.setNumber(2);
        Goods goods2 = GoodsController.goods2;
        Goods goods3 = GoodsController.goods3;
        Goods goods4 = GoodsController.goods4;



        //第一张订单
        Order order1 = new Order(1, CommonDate.changeLongtimeToDate(Long.valueOf("1492668851304")),
                11111, 111);
        List<Goods> goodsList1 = new ArrayList<Goods>();
        goodsList1.add(goods1);
        goodsList1.add(goods2);
        order1.setGoodses(goodsList1);
        order1.setAllprice(goods1.getNumber() * goods1.getPrice() + goods2.getNumber() * goods2.getPrice());

        //第二张订单
        Order order2 = new Order(2, CommonDate.changeLongtimeToDate(Long.valueOf("1492668861304")),
                11111, 222);
        List<Goods> goodsList2 = new ArrayList<Goods>();
        goodsList2.add(goods4);
        order2.setGoodses(goodsList2);
        order2.setAllprice(goods4.getNumber() * goods4.getPrice());

        //第三张订单
        Order order3 = new Order(3, CommonDate.changeLongtimeToDate(Long.valueOf("1492668861304")),
                22222, 111);
        List<Goods> goodsList3 = new ArrayList<Goods>();
        goodsList3.add(goods3);
        order3.setGoodses(goodsList3);
        order3.setAllprice(goods3.getNumber() * goods3.getPrice());

        //将订单放到统一缓存中
        orderArray.add(order1);
        orderArray.add(order2);
        orderArray.add(order3);
    }

    /**
     * 获取订单列表
     * @return
     */
    @RequestMapping("/orderlists/{userid}")
    @ResponseBody
    public RequestResult<List<Order>> getOrderLists(@PathVariable("userid") int userid){
        List<Order> orderList = new ArrayList<Order>();
        for (Order order : orderArray){
            if (userid == order.getUserid())
                orderList.add(order);
        }
        return new RequestResult<List<Order>>(StatEnum.ORDER_LIST, orderList);
    }
}
