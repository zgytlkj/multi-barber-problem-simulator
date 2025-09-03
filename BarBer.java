import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Time;

public class BarBer implements Runnable{
    private int id;
    private String name;
    private int randomtime;
    public static boolean die = false;

    public BarBer(int id, String name){
        this.id = id;
        this.name = name;
    }
    @Override
    public void run() {
        while (true){
            //System.out.println("====="+name);
            Conditions.worker.P();//自旋锁
            System.out.println("====="+name);
            Conditions.mutex2.P();
            if (Conditions.waiting == 0){
                Sleeping();
                System.out.println("无顾客,理发师:"+name+"睡觉中....");
                Conditions.working--;
                Conditions.mutex2.V();
                Conditions.worker.V();
                /**
                 * 主动结束:存在无法结束所有理发线程的概率(bug)
                 * 暂未找到解决方案
                 * 主动结束的前提是知道总共要服务多少人
                 * 已服务+离开 = 总顾客-工作中数
                 */
                if (Conditions.count + Conditions.leave == Conditions.CNUM - (Conditions.BNUM - Conditions.worker.value)){
                    System.out.println(name+"主动结束");
                    return;
                }
                Conditions.customer.P();
            }else {
                Conditions.barbers.V();
                Conditions.waiting--;
                //模拟理发随机数
                randomtime = (int) (20000*Math.random());
                StartCut();

                System.out.println(name+"开始理发,已服务人数:"+Conditions.served[id-1]);
                System.out.println("等待理发的顾客数:"+Conditions.waiting);
                Conditions.mutex2.V();
                //理发

                //Cutting();
                Conditions.cutHair(randomtime);

                BarberFrame.infoArea.append(name+"为顾客理发结束\n");
                System.out.println("顾客理发结束");
                Conditions.count++;
                Conditions.served[id-1]++;//当前理发师服务人数增加
                Conditions.worker.V();
            }
            /**
             * 将等待区用户服务完成后,结束理发线程
             */
            if (die == true && Conditions.waiting == 0){
                Sleeping();
                BarberFrame.infoArea.append(name+"停止\n");
                return;
            }
            /**
             * 单理发师(废弃)
             */
//            if (Conditions.customer.value <= 0){
//                System.out.println("无顾客,理发师睡觉");
//            }
//            Conditions.customer.P();//尝试给顾客理发,无顾客就休息
//            Conditions.mutex.P();//进程互斥,有顾客就进入临界区,在理发椅上准备开始理发
//
//            Conditions.waiting--;//等待顾客数减少一,waiting为变量(非信号量)
//
//            Conditions.barbers.V();//理发师去为顾客理发,不会大于1
//            Conditions.mutex.V();//退出临界区
//
//            /**
//             * 执行理发操作
//             */
//            //获取顾客进程信息,链队中的第一个节点
//            ProcNode procNode = Conditions.customer.link.getFirst();
////            if (Conditions.waiting > 0){
////                System.out.println("理发师叫顾客");
////            }
//            System.out.println("理发师开始为:"+procNode.name+"理发");
//            Timer progressTimer =  new Timer(500,null);
//            progressTimer.addActionListener(new AbstractAction() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    if (progress != 90){
//                        progress += 5;
//                        MainFrame.progressBar.setValue(progress);
//                    }else {
//                        progressTimer.stop();
//                    }
//
//                }
//            });
//            progressTimer.start();
//            Conditions.cutHair();
//            System.out.println("理发完成....支付完成.....");
        }

    }

    /**
     * 理发师休息
     */
    public void Sleeping(){
        BarberFrame.cutter[id-1].setText(name+"休息中..");//理发师文本
        //理发师图像
        if ((id-1)%2 == 0){
            BarberFrame.cutter[id-1].setIcon(new ImageIcon("./src/static/barber_man.png"));
        }else {
            BarberFrame.cutter[id-1].setIcon(new ImageIcon("./src/static/barber_female.png"));
        }
        BarberFrame.cuttingPanel.validate();//刷新
        BarberFrame.infoArea.append(name+"因为无顾客,休息中\n");//日志区文本
    }

    /**
     * 理发师理发
     */
    public void StartCut(){
        ProcNode temp = Conditions.customer.link.getFirst();//获取第一个节点,节点获取后节点被删除
        int cusid = (int) temp.id;//cusid-即customer的id
        String cname = temp.name;//cusid-即customer的name

        /**
         * 进度条加载进程
         */
        Timer timer = new Timer((int)randomtime/20,null);//最大值100,每次加5(实际为6),100/5=20
        timer.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = BarberFrame.progressBar[id-1].getValue();
                if (temp < BarberFrame.progressBar[id-1].getMaximum()){
                    BarberFrame.progressBar[id-1].setValue(temp+6);//让进度条适当提前
                }else {
                    //测试:设置的值不会超过最大值
                    //System.out.println(BarberFrame.progressBar[id-1].getValue());
                    BarberFrame.progressBar[id-1].setValue(1);
                    timer.stop();//停止计时器
                }
            }
        });
        timer.start();

        BarberFrame.sofa[cusid-1].setText("沙发"+cusid+"空闲");
        BarberFrame.nodes.add(new ProcNode(cusid,"沙发"+cusid+"空闲"));
        if ((cusid-1)%2 == 0){
            BarberFrame.sofa[cusid-1].setIcon(new ImageIcon("./src/static/sofa_one.png"));
        }else{
            BarberFrame.sofa[cusid-1].setIcon(new ImageIcon("./src/static/sofa_two.png"));
        }
        BarberFrame.waitPanel.validate();



        BarberFrame.cutter[id-1].setText(name+"为"+cname+"理发");
        BarberFrame.infoArea.append(name+"为"+cname+"理发\n");
        BarberFrame.cutter[id-1].setIcon(new ImageIcon("./src/static/cutting.png"));
        BarberFrame.cuttingPanel.validate();
    }

//    public void Cutting(){
//
//        Timer timer = new Timer((int)randomtime/20,null);
//        timer.addActionListener(new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int temp = BarberFrame.progressBar[id-1].getValue();
//                if (temp < BarberFrame.progressBar[id-1].getMaximum()){
//                    BarberFrame.progressBar[id-1].setValue(temp+5);
//                }else {
//                    System.out.println(BarberFrame.progressBar[id-1].getValue());
//                    BarberFrame.progressBar[id-1].setValue(1);
//                    timer.stop();
//
//                }
//            }
//        });
//        timer.start();
//    }
}
