import javax.swing.*;
import java.awt.*;

public class Customer implements Runnable{
    String name;
    public Customer(String name){
        this.name = name;
    }
    @Override
    public void run() {
        Conditions.mutex.P();
        BarberFrame.infoArea.append(name+"进店\n");
        System.out.println(name+"进店");
        if (Conditions.waiting == Conditions.chairs){//等待人数和椅子数相等则人满,离开
            Conditions.leave++;//总直接离开人数增加一
            Conditions.mutex.V();
            //直接离开
            Leave();

            System.out.println("人满了,"+name+"直接离开,总直接离开人数:"+Conditions.leave);
        }else{//否则等待
            Conditions.waiting++;
            SitDown();
            System.out.println(name+"坐下等待理发,等待理发人数"+Conditions.waiting);
            if (Conditions.waiting == 1 && Conditions.working < 0){
                System.out.println(name+"唤醒理发师,准备理发");
                Conditions.working++;//被唤醒的理发师数量增加
                Conditions.customer.V();
            }
            Conditions.mutex.V();
            Conditions.barbers.P();
        }

//        if (Conditions.waiting < Conditions.chairs){//若有空的椅子就坐下等待
//            Conditions.waiting++;//等待顾客数加1
//            //进程插入链队
//            GoBarber();
//            System.out.println(name+"来了,并等待");
//            Conditions.customer.link.add(new ProcNode(Thread.currentThread().getId(),name));
//            Conditions.customer.V();//顾客数量增加一,通知唤醒理发师
//
//            Conditions.mutex.V();//退出临界区,退出后理发师、其他线程才能进入临界区
//
//            if (Conditions.barbers.value == 0){
//                System.out.println("唤醒");
//            }
//            Conditions.barbers.P();//顾客P这个信号量后若小于0则阻塞,代表理发师忙碌中
//            //添加到等待队列
//            /**
//             * 让理发师理发
//             */
//            //System.out.println("理发对象为:"+name);
//        }else {
//            System.out.println("人满了,"+name+"离开");
//            Conditions.mutex.V();//人满,离开
//        }
    }

//    public void GoBarber(){
//        JButton button = new JButton(name);
//        button.setPreferredSize(new Dimension(100,100));
//        MainFrame.waitPanel.add(button);
//        MainFrame.waitPanel.validate();
//    }
    public void SitDown(){
        BarberFrame.infoArea.append(name+"坐下等待理发,等待理发人数"+Conditions.waiting+"\n");
        int id = (int) BarberFrame.nodes.getFirst().id;//id为座椅的id,占用那个座椅
        Conditions.customer.link.add(new ProcNode(id,name));//参数一不用线程ID,将座椅和用户绑定
        //System.out.println("我的名字"+name);
        BarberFrame.sofa[id-1].setText(name+"等待中..");
        if ((id-1)%3 == 0){
            BarberFrame.sofa[id-1].setIcon(new ImageIcon("./src/static/waitting_one.png"));
        }else if ((id-1)%3 == 1){
            BarberFrame.sofa[id-1].setIcon(new ImageIcon("./src/static/waitting_two.png"));
        }else {
            BarberFrame.sofa[id-1].setIcon(new ImageIcon("./src/static/waitting_three.png"));
        }
        BarberFrame.waitPanel.validate();
    }

    /**
     * 顾客直接离开
     */
    public void Leave(){
        JButton leave = new JButton();
        leave.setPreferredSize(new Dimension(40,50));
        leave.setIcon(new ImageIcon("./src/static/leave.png"));
        leave.setBorderPainted(false);
        BarberFrame.panelBottom.add(leave);
        BarberFrame.panelBottom.validate();
        BarberFrame.infoArea.append("人满了,"+name+"直接离开,总直接离开人数:"+Conditions.leave+"\n");
    }
}
