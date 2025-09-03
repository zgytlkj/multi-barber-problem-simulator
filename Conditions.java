/**
 * 全局环境需要用到的一些同步、互斥信号量及变量
 */
public class Conditions {
    public static Semaphore customer = new Semaphore(0);//初始顾客数量,阻塞理发师进程
    public static Semaphore barbers = new Semaphore(0);//理发师初始状态为睡着,阻塞顾客进程
    public static Semaphore mutex = new Semaphore(1);//理发师和顾客之间的互斥锁,椅子是理发师和顾客进程都可以访问的临界区
    public static int waiting = 0;//初始等待理发的顾客数量,等待中的顾客数量
    public static int chairs = -1;//等待区椅子数量(设置量)

    public static int working = 0;//统计理发师情况,正在工作的理发师数量,休息就变为负值

    public static int BNUM = -1;//指定理发师数量(设置量)
    public static int CNUM = -1;//指定顾客线程数量(设置量)

    public static int count = 0;//理发师服务的总顾客数量
    public static Semaphore worker;//理发师线程创建时初始化(多理发师)
    public static Semaphore mutex2 = new Semaphore(1);//理发师和理发师之间的互斥锁
    public static int leave = 0;//因为没有座位而直接离开的顾客数量
    public static int[] served;//创建线程时new,统计各个理发师服务的顾客数量

    /**
     * 模拟剪发,即使线程休眠一段时间
     * @param randomtime
     */
    public static void cutHair(int randomtime){//参数为随机等待时间
        try {
            Thread.sleep(randomtime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
