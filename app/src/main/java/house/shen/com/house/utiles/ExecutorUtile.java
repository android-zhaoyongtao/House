package house.shen.com.house.utiles;

import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用线程池执行 工具类
 * Created by zhaoyongtao on 2017/11/23.
 */
public class ExecutorUtile {
    private static final int COREPOOLSIZE = 0;//corePoolSize
    private static final int MAXIMUMPOOLSIZE = Integer.MAX_VALUE;//maximumPoolSize
    private static final long KEEPALIVETIME = 120;//keeplalivetime
    private static final ThreadFactory THREADFACTORY = new UXinRentThreadFactory();//keeplalivetime 毫秒单位

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(COREPOOLSIZE, MAXIMUMPOOLSIZE, KEEPALIVETIME, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), THREADFACTORY, new ThreadPoolExecutor.CallerRunsPolicy());
//    private Future<?> future;

    /**
     * 子线程执行
     *
     * @param runnable
     */
    public static void runInSubThred(@NonNull Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {//UI线程
            threadPoolExecutor.execute(runnable);
        } else {
            runnable.run();
        }

    }
//    public void submitForSource(Runnable runnable) {
//        future = threadPoolExecutor.submit(runnable);
//    }
//
//    void cancel(boolean mayInterruptIfRunning) {
//        future.cancel(mayInterruptIfRunning);
//    }

    /**
     * The UXinRentThred thread factory
     */
    static class UXinRentThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        UXinRentThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "HaHaHa~Thredpool-" + poolNumber.getAndIncrement() + "-thread-";//设置线程前缀名称 UXinRentThredpool
            LogUtils.d("pool-", namePrefix);
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


}
