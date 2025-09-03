/**
 * 信号量实现:
 * 包含P、V原子操作
 */
public class Semaphore {
    int value;//整型变量
    LinkProcNode link;//链表,cusotmer信号量需要使用链表,按次序记录等待的客户
    //初始化量
    public Semaphore(int value){
        this.value = value;
        link = new LinkProcNode();//在构造体中初始化链表
    }
    //P操作为原子操作,不允许中断
    public synchronized void P(){
        value--;
        if (value < 0){
            try {
                this.wait();//阻塞当前进程
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //V操作为原子操作,不允许中断
    public synchronized void V(){
        value++;
        if (value <= 0){
            this.notify();//随机唤醒阻塞队列中的一个进程
        }
    }
}
