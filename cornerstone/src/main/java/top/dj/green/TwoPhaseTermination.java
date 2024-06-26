package top.dj.green;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: DengJia
 * @Date: 2024-04-01 20:10
 * @Description: 两阶段终止模式，可使用此模式来代替线程的stop()。
 */
@Slf4j
public class TwoPhaseTermination {

    public static void main(String[] args) {
        TwoPhaseTerminationMode two = new TwoPhaseTerminationMode();
        two.start();
        try {
            TimeUnit.MILLISECONDS.sleep(3500);
        } catch (Exception e) {
            log.error("主线程中断::", e);
        }
        two.stop();
        log.info("主线程后续操作");
    }
}

@Slf4j
class TwoPhaseTerminationMode {
    private Thread monitor;

    /**
     * 启动监控线程
     */
    void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    log.info("监控中断后操作");
                    break;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    log.info("监控进行中");
                } catch (InterruptedException e) {
                    log.error("监控中断::", e);
                    // 重新设置打断标记
                    current.interrupt();
                }
            }
        });
        monitor.start();
    }

    /**
     * 停止监线程
     */
    void stop() {
        monitor.interrupt();
    }
}
