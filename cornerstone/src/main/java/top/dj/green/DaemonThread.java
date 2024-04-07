package top.dj.green;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: DengJia
 * @Date: 2024-04-03 20:50
 * @Description: 守护线程
 */
@Slf4j
public class DaemonThread {
    public static void main(String[] args) {
        start();
    }

    private static void start() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            log.info("分支线程结束");
        }, "Thread_01");
        thread.setDaemon(Boolean.TRUE);
        thread.start();
        log.info("主线程结束");
    }

}
