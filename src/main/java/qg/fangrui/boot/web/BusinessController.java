package qg.fangrui.boot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qg.fangrui.boot.dto.RequestResult;
import qg.fangrui.boot.enums.StatEnum;
import qg.fangrui.boot.model.Business;
import qg.fangrui.boot.model.Goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 推荐商家控制器
 * Created by FunriLy on 2017/4/11.
 * From small beginnings comes great things.
 */
@Controller
public class BusinessController {

    private static Business busi1 = new Business(111, "肯德基(gogo新天地店)", "广州大学城GoGo新天地",
            "15626198765", 5, 30);
    private static Business busi2 = new Business(222, "萨莉亚(gogo南亭店)", "广州大学城南亭",
            "15626198765", 4.5, 80);
    private static Business busi3 = new Business(333, "M记(贝岗店)", "广州大学城贝岗",
            "15626198765", 4, 35);
    private static Business busi4 = new Business(444, "麦当劳(gogo新天地店)", "广州大学城GoGo新天地",
            "15626198765", 4, 20);
    private static Business busi5 = new Business(555, "必胜客(gogo新天地店)", "广州大学城GoGo新天地",
            "15626198765", 3.5, 40);

    public static List<Business> busiArray = new ArrayList<Business>();
    private static Random random = new Random();

    static {
        //静态的商家信息
        //加入arraylist
        busiArray.add(busi1);
        busiArray.add(busi2);
        busiArray.add(busi3);
        busiArray.add(busi4);
        busiArray.add(busi5);
    }

    /**
     * 获取推荐商家列表
     * @return
     */
    @RequestMapping("/busilists")
    @ResponseBody
    public RequestResult<List<Business>> getBusinessList(){
        List<Business> lists = new ArrayList<Business>();
        int num = random.nextInt(4);
        for (int i=0; i<num; i++){
            lists.add(busiArray.get(i));
        }
        return new RequestResult<List<Business>>(StatEnum.BUSINESS_LIST, lists);
    }

    /**
     * 获取商品列表
     * @param busiId 商家id
     * @return
     */
    @RequestMapping("/goodslists/{busiId}")
    @ResponseBody
    public RequestResult<List<Goods>> getBusiGoodsList(@PathVariable("busiId")int busiId){
        List<Goods> lists = new ArrayList<Goods>();
        for (Goods goods : GoodsController.goodsArray){
            if (goods.getBusiId() == busiId)
                lists.add(goods);
        }
        return new RequestResult<List<Goods>>(StatEnum.GOODS_LIST, lists);
    }
}
