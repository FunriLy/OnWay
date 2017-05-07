package qg.fangrui.boot.task;

import qg.fangrui.boot.web.WebSocket;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by FunriLy on 2017/5/7.
 * From small beginnings comes great things.
 */
public class QueueTest {

    //公平的阻塞队列
    private static ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(100, true);

    public static void produce(int userid){
        try {
            queue.add(userid);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 消费线程
     */
    class Consumer extends Thread{
        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while(true){
                try {
                    //若吴将会阻塞
                    int userid = queue.take();
                    WebSocket.notice(userid);
                    System.out.println("从队列取走一个元素，队列剩余"+queue.size()+"个元素");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
