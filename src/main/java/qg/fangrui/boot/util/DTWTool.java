package qg.fangrui.boot.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DTW 动态时间规整算法来匹配路线
 * Created by FunriLy on 2017/4/26.
 * From small beginnings comes great things.
 */
public class DTWTool {

    private double X[], Y[];
    private int len_x, len_y;
    private double Dist[][];
    private double DTW[][];

    private HashMap<String, Integer> checkNode = new HashMap<String, Integer>();

    private ArrayList<String> path = new ArrayList<String>();

    public  void setData(double x[], double y[]) {
        this.X = x;
        this.Y = y;
        this.len_x = x.length;
        this.len_y = y.length;
        this.Dist = new double[this.len_x][this.len_y];
        this.DTW = new double[this.len_x+1][this.len_y + 1];
    }

    public  double runDTW() {
        this.adjacencyMatrix();
        this.DTW();
        this.getPath();
        return getPathSum();
    }


    /**
     * 将路线的匹配度抽取出来
     * @return
     */
    private double getPathSum(){
        double result = 0.0;
        for (String onePath : this.path){
            String[] strPoint = onePath.split("_");
            int x = Integer.valueOf(strPoint[0]);
            int y = Integer.valueOf(strPoint[1]);
            result += this.DTW[x][y];
        }
        return result;
    }

    /**
     * DTW核心算法
     */
    private  void DTW() {
        this.initDTW();
        int x = 1, y = 1;
        this.DTW[0][0] = 2 * this.Dist[0][0];
        for (int i = 1; i < this.len_x; i++) {
            this.DTW[i][0] = this.DTW[i - 1][0] + this.Dist[i][0];
        }

        for (int i = 1; i < this.len_y; i++) {
            this.DTW[0][i] = this.DTW[0][i - 1] + this.Dist[0][i];
        }

        while (y < this.len_y) {
            x = 1;
            while (x < this.len_x) {
                this.DTW[x][y] = this.getMinDTW(x, y);
                x++;
            }
            y++;
        }
    }

    /**
     * 输出DTW矩阵
     */
    private  void printDTW() {
        int x = 0, y = this.len_y;
        while (y >= 0) {
            x = 0;
            while (x < this.len_x) {
                System.out.print(this.DTW[x][y] + " ");
                x++;
            }
            System.out.println("");
            y--;
        }
    }

    /**
     * 输出Dist矩阵
     */
    private  void printDist() {
        int x = 0, y = this.len_y - 1;
        while (y >= 0) {
            x = 0;
            while (x < this.len_x) {
                System.out.print(this.Dist[x][y] + " ");
                x++;
            }
            System.out.println("");
            y--;
        }
    }

    /**
     * 获得优先路线
     */
    private  void getPath() {
        int x = this.len_x - 1, y = this.len_y - 1;
        this.path.add(x + "_" + y);
        //System.out.println(this.DTW[x][y]);
        double space = 3;
        int px = 0, py = 0;
        boolean hasEle = false;
        while (x > 0) {
            hasEle = false;
            space = Double.MAX_VALUE;
            if (this.checkNode.containsKey((x - 1) + "_" + y)
                    && space > this.DTW[x - 1][y]) {
                px = x - 1;
                py = y;
                space = this.DTW[x - 1][y];
            }
            if (this.checkNode.containsKey((x - 1) + "_" + (y - 1))
                    && (space > this.DTW[x - 1][y - 1])) {
                px = x - 1;
                py = y - 1;
                space = this.DTW[x - 1][y - 1];
            }
            if (this.checkNode.containsKey(x + "_" + (y - 1))
                    && (space > this.DTW[x][y - 1])) {
                px = x;
                py = y - 1;
                space = this.DTW[x - 1][y - 1];
            }
            if (x == px && y == py) {
                break;
            }
            x = px;
            y = py;
            //System.out.println(this.DTW[x][y]);
            this.path.add(px + "_" + py);
        }
    }

    /**
     * DTW初始化
     */
    private  void initDTW() {
        this.DTW[0][0] = 0;
        int x = this.len_x, y = this.len_y;
        while (x >= 0) {
            y = this.len_y;
            while (y >= 0) {
                this.DTW[0][0] = 0;
                y--;
            }
            x--;
        }
    }

    /**
     * 获得矩阵前进路线
     * @param x
     * @param y
     * @return
     */
    private double getMinDTW(int x, int y) {
        double rs = Double.MAX_VALUE;
        int pos_x = 0, pos_y = 0;
        if (rs > this.DTW[x - 1][y] + this.Dist[x][y]) {
            rs = this.DTW[x - 1][y] + this.Dist[x][y];
            pos_x = x - 1;
            pos_y = y;
        }
        if (rs > this.DTW[x][y - 1] + this.Dist[x][y]) {
            rs = this.DTW[x][y - 1] + this.Dist[x][y];
            pos_x = x;
            pos_y = y - 1;
        }
        if (rs > this.DTW[x - 1][y - 1] + this.Dist[x][y] * 2) {
            rs = this.DTW[x - 1][y - 1] + this.Dist[x][y] * 2;
            pos_x = x - 1;
            pos_y = y - 1;
        }
        this.checkNode.put(pos_x + "_" + pos_y, 1);
        return rs;
    }

    /**
     * 矩阵计算
     */
    private  void adjacencyMatrix() {
        int x = this.len_x - 1, y;
        while (x >= 0) {
            y = this.len_y - 1;
            while (y >= 0) {
                this.Dist[x][y] = (double) Math.abs(this.X[x] - this.Y[y]);
                y--;
            }
            x--;
        }
    }

//    public static  void main(String[] args) {
//        double Y[] = { 3.2, 8.5, 2.1, 5.6};
//        double X[] = {  1.0, 2.4, 5.2, 12.0 };
//
//        DTWTool t = new DTWTool();
//        t.setData(X, Y);
//        t.runDTW();
//        System.out.println();
//        System.out.println(t.checkNode);
//        System.out.println(t.path);
//
//    }
}