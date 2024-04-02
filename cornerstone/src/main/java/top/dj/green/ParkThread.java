package top.dj.green;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: DengJia
 * @Date: 2024-04-01 22:48
 * @Description: 打断处于park状态的线程
 */
@Slf4j
public class ParkThread {
    public static void main(String[] args) {
        park();
    }

    static void park() {
        Thread thread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            log.info("{}线程开始运行...", currentThread.getName());
            log.info("park");
            LockSupport.park();
            log.info("unpark");
            // log.info("{}中断状态::{}", currentThread.getName(), currentThread.isInterrupted());

            /* interrupted()，判断当前线程是否被打断，并且清除打断标记。 */
            log.info("{}中断状态::{}", currentThread.getName(), Thread.interrupted());

            LockSupport.park();
            log.info("unpark");
        }, "Thread01");
        thread.start();

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            log.error("主线程中断异常::", e);
        }
        thread.interrupt();
    }
}
