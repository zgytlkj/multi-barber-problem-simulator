import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 程序主界面
 */
public class BarberFrame extends JFrame{
    private JPanel panelTop;
    private JPanel panelCenter;
    private JPanel cutPanel;
    public static JPanel cuttingPanel;
    public static JPanel waitPanel;
    public static JPanel panelBottom;

    private JLabel addCutLabel;//添加理发师标签
    private JLabel addSofaLabel;
    private JTextField addCutField;//添加理发师线程数量
    private JTextField addSofaField;
    private JLabel addCusLabel;//添加顾客标签
    private JTextField addCusField;//添加顾客线程数量
    private JButton confirmButton;
    private JButton startButton;
    private JButton stopButton;

    public static JButton[] cutter;
    public static JProgressBar[] progressBar;
    public static JButton[] sofa;
    public static LinkProcNode nodes = new LinkProcNode();
    private JScrollPane scrollPane;
    public static JTextArea infoArea;
    //public static JProgressBar progressBar;


    public BarberFrame(){
        this.setLayout(new BorderLayout());
        addCutLabel = new JLabel("添加理发师");
        addCutField = new JTextField("",5);
        addSofaLabel = new JLabel("添加沙发");
        addSofaField = new JTextField("",5);
        addCusLabel = new JLabel("最大顾客数");
        addCusField = new JTextField("",5);
        confirmButton = new JButton("确认");
        startButton = new JButton("开始模拟");
        stopButton = new JButton("停止");
        stopButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BarberFrame.infoArea.append("停止中\n");
                BarBer.die = true;
            }
        });

        confirmButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    /**
                     * 一系列初始化操作
                     */
                    //若中途停止,先前的数据还在链表中,将链表置空
                    if (nodes.getFirst() != null){
                        System.out.println("链表重新生成");
                        nodes = new LinkProcNode();
                    }
                    Conditions.count = 0;
                    Conditions.leave = 0;
                    cuttingPanel.removeAll();
                    waitPanel.removeAll();
                    infoArea.setText("");
                    panelBottom.removeAll();
                    //使用repaint而不是validate
                    cuttingPanel.repaint();
                    waitPanel.repaint();
                    panelBottom.repaint();
                    int barberNump = Integer.parseInt(addCutField.getText());
                    int sofas = Integer.parseInt(addSofaField.getText());
                    int custs = Integer.parseInt(addCusField.getText());
                    if (barberNump < 4 && barberNump > 0 && sofas < 11 && sofas > 0 && custs < 21 && custs > 0){
                        infoArea.append("\n初始化设置成功:添加理发师:"+barberNump+",沙发:"+sofas+",最大顾客数:"+custs+"\n");
                        Conditions.BNUM = barberNump;
                        Conditions.chairs = sofas;
                        Conditions.CNUM = custs;
                        cutter = new JButton[barberNump];
                        progressBar = new JProgressBar[barberNump];
                        sofa = new JButton[sofas];
                        for (int i = 0; i < Conditions.BNUM; i++){
                            //面板
                            JPanel cutterPanel = new JPanel(new BorderLayout());
                            cutterPanel.setName("理发面板"+i);
                            cutterPanel.setSize(210,210);
                            //理发按钮
                            cutter[i] = new JButton();
                            cutter[i].setBorderPainted(false);
                            cutter[i].setName("barber"+(i+1));
                            cutter[i].setText("理发师"+(i+1));
                            System.out.println(cutter[i].getText());
                            cutter[i].setPreferredSize(new Dimension(200,200));
                            cutter[i].setVerticalTextPosition(SwingConstants.BOTTOM);
                            cutter[i].setHorizontalTextPosition(SwingConstants.CENTER);
                            if (i%2 == 0){
                                cutter[i].setIcon(new ImageIcon("./src/static/barber_man.png"));
                            }else {
                                cutter[i].setIcon(new ImageIcon("./src/static/barber_female.png"));
                            }
                            //理发进度
                            progressBar[i] = new JProgressBar(0,100);
                            progressBar[i].setValue(1);
                            cutterPanel.add(cutter[i],BorderLayout.CENTER);
                            cutterPanel.add(progressBar[i],BorderLayout.SOUTH);
                            cuttingPanel.add(cutterPanel);
                            cuttingPanel.validate();
                            //等待按钮(沙发按钮)
                        }
                        for (int i = 0; i < sofas; i++){

                            sofa[i] = new JButton("沙发"+(i+1)+"空闲");
                            nodes.add(new ProcNode(i+1,sofa[i].getText()));
                            sofa[i].setBorderPainted(false);
                            sofa[i].setName("sofa"+(i+1));
                            sofa[i].setPreferredSize(new Dimension(120,120));
                            sofa[i].setVerticalTextPosition(SwingConstants.BOTTOM);
                            sofa[i].setHorizontalTextPosition(SwingConstants.CENTER);
                            if (i%2 == 0){
                                sofa[i].setIcon(new ImageIcon("./src/static/sofa_one.png"));
                            }else {
                                sofa[i].setIcon(new ImageIcon("./src/static/sofa_two.png"));
                            }
                            waitPanel.add(sofa[i]);
                            waitPanel.validate();
                        }

                    }else{
                        JOptionPane.showMessageDialog(BarberFrame.this,"理发师数量1-3\n沙发数量1-10","消息提示",JOptionPane.INFORMATION_MESSAGE);
                    }

                }catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(BarberFrame.this,"请输入整数数字!","消息提示",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });



        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * 创建启动理发师和顾客的线程
                 * 不在该主线程启动
                 */
                BarBer.die = false;
                Thread create = new Thread(new CreateThread(Conditions.BNUM,Conditions.CNUM));
                create.start();
                infoArea.append("启动成功\n");
            }
        });


        panelTop = new JPanel();
        panelTop.setBorder(new TitledBorder("初始化区"));
        panelTop.add(addCutLabel);
        panelTop.add(addCutField);
        panelTop.add(addSofaLabel);
        panelTop.add(addSofaField);
        panelTop.add(addCusLabel);
        panelTop.add(addCusField);
        panelTop.add(confirmButton);
        panelTop.add(Box.createHorizontalStrut(20));//添加一个水平间距
        panelTop.add(startButton);
        panelTop.add(stopButton);


        infoArea = new JTextArea();
        infoArea.setColumns(15);
        infoArea.setLineWrap(true);
        scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(new TitledBorder("日志区"));
        //infoArea.setText("初始文本");

        //顶级理发区面板
        cutPanel = new JPanel(new BorderLayout());
        //次级理发区面板
        cuttingPanel = new JPanel();
        cuttingPanel.setBorder(new TitledBorder("理发区"));

        cutPanel.add(cuttingPanel,BorderLayout.CENTER);

        //顶级等待区面板
        waitPanel = new JPanel();
        waitPanel.setBorder(new TitledBorder("等待区"));

        //放置理发区和等待区的面板
        panelCenter = new JPanel(new GridLayout(2,0));
        panelCenter.add(cutPanel);
        panelCenter.add(waitPanel);

        //底部离开区
        panelBottom = new JPanel();
        panelBottom.setBorder(new TitledBorder("直接离开区"));
        //放置一个空白的等待位,占据高度
        JButton leave = new JButton();
        leave.setPreferredSize(new Dimension(40,50));
        leave.setBorderPainted(false);
        //panelBottom.add(leave);
        panelBottom.setPreferredSize(new Dimension(950,80));//设置之后无需放置占用的button
        this.add(panelCenter,BorderLayout.CENTER);
        this.add(panelTop,BorderLayout.NORTH);
        this.add(panelBottom,BorderLayout.SOUTH);
        this.add(scrollPane,BorderLayout.EAST);
    }


}
