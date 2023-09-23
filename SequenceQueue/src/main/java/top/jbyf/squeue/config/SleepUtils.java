package top.jbyf.squeue.config;

public class SleepUtils {
    public static void sleep(int second){
        try {
            Thread.sleep(second*1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
