package cn.chengzhiya.mhdfdatabase.runnable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MHDFDatabaseThread implements Runnable {
    private final Thread thread = new Thread(this, "MHDF-Database-Thread");
    private final BlockingQueue<Runnable> jobs = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * 运行线程
     */
    public void start() {
        if (!this.running.compareAndSet(false, true)) {
            return;
        }

        this.thread.start();
    }

    /**
     * 关闭线程
     */
    public void kill() {
        if (!this.running.compareAndSet(true, false)) {
            return;
        }

        this.thread.interrupt();
        this.jobs.clear();
    }

    /**
     * 执行任务
     *
     * @param task 任务实例
     */
    public void execute(Runnable task) {
        if (!this.running.get()) {
            throw new IllegalStateException("Thread is not running");
        }

        this.jobs.offer(task);
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                this.jobs.take().run();
            } catch (InterruptedException e) {
                if (!running.get()) {
                    break;
                }

                Thread.currentThread().interrupt();
            }
        }
    }
}
