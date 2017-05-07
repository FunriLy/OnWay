package qg.fangrui.boot.other;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; import java.util.Map;
/**
 * 趋势线拟合度算法
 * ww
 */
public class Trend
{
//    /* 任意两点间直线的倾斜角算法 */
//    public static void main(String[] args) throws Exception {
//        //基准坐标系
//        List<Map<String,Double>> ls = new ArrayList<Map<String,Double>>();
//        Map<String,Double> m = new HashMap<String,Double>();
//        String strs = "";
//        m = new HashMap<String,Double>();
//        m.put("x", 1.0);m.put("y", 1.0);
//        ls.add(m);
//        m = new HashMap<String,Double>();
//        m.put("x", 2.0);m.put("y", 2.0);
//        ls.add(m);
//        m = new HashMap<String,Double>();
//        m.put("x", 3.0);m.put("y", 3.0);
//        ls.add(m);
//        m = new HashMap<String,Double>();
//        m.put("x", 4.0);m.put("y", 4.0);
//        ls.add(m);
//        m = new HashMap<String,Double>();
//        m.put("x", 5.0);m.put("y", 5.0);
//        ls.add(m);
//        m = new HashMap<String,Double>();
//        m.put("x", 6.0);m.put("y", 6.0);
//        ls.add(m);
//        m = new HashMap<String,Double>();
//        m.put("x", 7.0);m.put("y", 7.0);
//        ls.add(m);
//        m = new HashMap<String,Double>();
//        m.put("x", 8.0);m.put("y", 8.0);
//        ls.add(m);
//        m = new HashMap<String,Double>();
//        m.put("x", 9.0);m.put("y", 9.0);
//        ls.add(m);
//        m = new HashMap<String,Double>();
//        m.put("x", 10.0);m.put("y", 10.0);
//        ls.add(m);
//        //坐标系斜率归一化
//        List<Double> lvs = getLvs(ls);
//        if(lvs != null && lvs.size() >0){
//            //匹配坐标系
//            List<Map<String,Double>> lss = new ArrayList<Map<String,Double>>();
//            for(int i=0;i<10;i++){
//                m = new HashMap<String,Double>();
//                //m.put("x", (double)(i+1));
//                m.put("x", (double)GetIntMathNumber(1,9));
//                m.put("y",(double)GetIntMathNumber(1,9));
//                lss.add(m);
//            }
//            double guis = getGuis(lss,lvs);
//            System.out.println("两条坐标系曲线拟合（值越小越拟合）： "+guis);
//            }
//    }
//
//    /*
//     * 根据坐标系计算出相邻坐标的斜率集
//     */
//    public static List<Double> getLvs(List<Map<String,Double>> ls){
//        List<Double> lvs = new ArrayList<Double>();
//        //排序
//        if(ls != null && ls.size() >0){
//            System.out.println("基准坐标系，斜率集核算");
//            for(int i=0;i<ls.size();i++){
//                if(i >0 && i<ls.size()){
//                    lvs.add(getGuiLv(ls.get(i-1).get("x"),ls.get(i-1).get("y"),ls.get(i).get("x"),ls.get(i).get("y"),0));
//                    System.out.println(i+" | "+ls.get(i-1).get("y")+" | "+ls.get(i).get("y")+" |基准斜率 "+getGuiLv(ls.get(i-1).get("x"),ls.get(i-1).get("y"),ls.get(i).get("x"),ls.get(i).get("y"),0));
//                    }
//            }
//            System.out.println("");
//        }
//        return lvs;
//     }
//
//     /*
//      * 根据坐标系计算出相邻坐标的斜率集，相对于基准的权重值（聚合权重）
//      */
//     public static double getGuis(List<Map<String,Double>> ls,List<Double> lvs){
//         double guis = 0;
//         //排序
//         if(ls != null && ls.size() >0){
//             System.out.println("匹配坐标系，斜率集核算匹配");
//             for(int i=0;i<ls.size();i++){
//                 if(i >0 && i<ls.size()){
//                     guis += getGuiLv(ls.get(i-1).get("x"),ls.get(i-1).get("y"),ls.get(i).get("x"),ls.get(i).get("y"),lvs.get(i-1));
//                     System.out.println(i+" | "+ls.get(i-1).get("y")+" | "+ls.get(i).get("y")+" |基准斜率 "+lvs.get(i-1)+" |匹配归一权重 "+getGuiLv(ls.get(i-1).get("x"),ls.get(i-1).get("y"),ls.get(i).get("x"),ls.get(i).get("y"),lvs.get(i-1)));
//                 }
//             }
//             System.out.println("");
//         }
//         return guis;
//     }
//
//     /*
//      * 根据任意两点坐标斜率，比较基准，并进行离差归一化权重计算(可计算斜率，也可计算权重)
//      */
//     public static double getGuiLv(double x1,double y1,double x2,double y2,double atan){
//         double i = 0;
//         //任意两点间直线的倾斜角
//         double x = Math.atan2(x2-x1,y2-y1)*180/Math.PI;
//         if(atan == 0){
//             i = x;
//         } else {
//             //线性归一化函数转换（离差） 归一化：(x-min)/(max-min);
//             double y = x-atan;if(y <0){y = -y;}
//             // 离差权重
//             i = (y-0)/(x-0);
//         }
//         return i;
//     }
//
//     /*
//      * 从startNumber到endNumber的随机整数
//      */
//     public static int GetIntMathNumber(int startNumber,int endNumber){
//         return (int)Math.round(Math.random()*(endNumber-startNumber)+startNumber);
//     }
}
