package aiwac.admin.com.healthrobot.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**     用于管理自己的线程，主要是利用Executor框架进行线程的创建，维护和重用
 * Created by luwang on 2017/11/27.
 */

public class ThreadPoolManager {

    //单例模式
    private final static ThreadPoolManager threadPoolManager = new ThreadPoolManager();

    //线程池对象
    private ExecutorService executorService;


    private ThreadPoolManager(){
        init();
    }

    //用于初始化线程缓冲池
    private void init(){
        //executorService = Executors.newFixedThreadPool(Constant.APLLICATION_THREADPOOL_SIZE); //适用于执行相对稳定的任务
        executorService = Executors.newCachedThreadPool();  // 适用于执行短期任务
    }

    public static ThreadPoolManager getThreadPoolManager(){
        return threadPoolManager;
    }

    public void submitTask(Runnable task){
        executorService.submit(task);
    }

    public Future submitTask(Callable task){
        return executorService.submit(task);
    }


}
