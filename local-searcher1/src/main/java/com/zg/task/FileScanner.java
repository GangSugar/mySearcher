package com.zg.task;

import com.zg.model.FileMeta;
import com.zg.service.FileService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 扫描器
 */

/**
 * 2.创建一个扫描任务(也就是线程)
 * 这个扫描任务的目标：
 *     (1):传入参数，是一个文件夹
 *     (2):扫描这个文件夹的所有孩子，
 *     (3):针对孩子中也是文件夹的，包装成一个新的任务提交给线程池
 *     (4):收集所有非文件夹的孩子们(也就是文件)
 *     (5):调用 fileService.service(。。。)方法， 进行入库操作(入库之前进行先对比然后在入库)
 */
class ScanJob implements Runnable {
    private File root;
    private ExecutorService threadPool;
    private FileService fileService;
    private AtomicInteger jobCount;
    private  CountDownLatch countDownLatch;
    public ScanJob(File root,ExecutorService threadPool,FileService fileService,AtomicInteger jobCount, CountDownLatch countDownLatch ){
        this.root = root;
        this.threadPool = threadPool;
        this.fileService = fileService;
        this.jobCount = jobCount;
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void run() {
        //开始扫描
        File[] children = root.listFiles();
        if (children != null){
            List<FileMeta> scanResultList = new ArrayList<>();
            for (File child : children){
                if (child.isDirectory()){//是目录，需要包装成为一个新的目录
                    jobCount.incrementAndGet();
                    ScanJob job = new ScanJob(child,threadPool,fileService,jobCount,countDownLatch);
                    threadPool.execute(job);//将任务提交给线程池
                }else if (child.isFile()){
                    scanResultList.add(new FileMeta(child));
                }
            }
            //扫描完毕之后就可以传递下去，进行和数据库进行比对去了
            fileService.service(root.getAbsolutePath(), scanResultList);
            if (jobCount.decrementAndGet() == 0){
                countDownLatch.countDown();//jobCount.decrementAndGet() == 0说明所有任务都完成了，就将线程countDown
            }
        }
    }
}

public class FileScanner {
    //1.搞一个线程池，放到线程池中去扫描
    private final ExecutorService threadPool = new ThreadPoolExecutor(
            4,4,0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10)
    );
    //4.创建一个CountDownLatch
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    //5.
    private AtomicInteger jobCount = new AtomicInteger();

    private final FileService fileService = new FileService();

    /**
     * 进行多少线程的改良(总体步骤)
     * 1.搞一个线程池，放到线程池中去扫描
     *  2.创建一个扫描任务(也就是线程)
     */
    public void scan(File root) throws InterruptedException {
        ScanJob job = new ScanJob(root,threadPool,fileService,jobCount,countDownLatch);
        jobCount.incrementAndGet();//6.
        threadPool.execute(job);//将任务提交给线程池

        //3.如果不加限制，提交任务之后，会运行到这里
        //但是扫描工作还没有完成
        //如何在这里等待，直到扫描工作完成
        countDownLatch.await();//7.在这里一直等，等什么时候把CountDownLatch(1)置为0就可以退出了
    }

    /*public void scan(File root) {
        scanDir(root);
    }

    private void scanDir(File root) {
        if (!root.isDirectory()) {
            return;
        }

        File[] children = root.listFiles();
        if (children == null) {
            return;
        }

        List<FileMeta> scanResultList = new ArrayList<>();
        for (File child : children) {
            scanDir(child);
            if (child.isFile()) {
                scanResultList.add(new FileMeta(child));
            }
        }

        fileService.service(root.getAbsolutePath(), scanResultList);
    }*/
}





