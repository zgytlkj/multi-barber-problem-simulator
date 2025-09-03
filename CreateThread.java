/**
 * 创建理发师、顾客线程:
 * 不在主线程中创建线程,因为创建线程中涉及sleep操作
 */
public class CreateThread implements Runnable{
    private int bnum;//理发师数量
    private int cnum;//顾客数量
    public CreateThread(int bnum,int cnum){
        this.bnum = bnum;
        this.cnum = cnum;
    }
    @Override
    public void run() {
        Conditions.worker = new Semaphore(bnum);
        Conditions.served = new int[bnum];
        //理发师线程
        for (int i = 0;i < bnum; i++){
            Thread barber = new Thread(new BarBer(i+1,"理发师"+(i+1)));
            barber.start();
            BarberFrame.infoArea.append("理发师"+(i+1)+"线程创建成功\n");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //顾客线程
        for (int i = 0; i < cnum; i++){
            //接收到停止信号
            if (BarBer.die == true){
                BarberFrame.infoArea.append("停止创建顾客线程\n");
                return;
            }
            Thread customer = new Thread(new Customer("顾客"+(i+1)));
            customer.start();
            BarberFrame.infoArea.append("顾客"+(i+1)+"线程创建成功\n");
            try {
                Thread.sleep((long) (6000*Math.random()));//模拟顾客随机到来
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
