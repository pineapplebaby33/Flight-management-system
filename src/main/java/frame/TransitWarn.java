package frame;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

import flight.*;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import javax.swing.JPasswordField;
public class TransitWarn {
    private JFrame frame;
    private JPasswordField passwordField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TransitWarn window = new TransitWarn("a","b");
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public TransitWarn(String info,String flightname) {
        initialize(info,flightname);
    }

    public JFrame getFrame() {
        return frame;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(String info,String flightname) {
        frame = new JFrame();

        frame.setTitle("取消订单");
        frame.setBounds(100, 100, 850, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //取消订单提示
        JLabel label1 = new JLabel("温馨提示：当前航班属于中转航班");
        JLabel label3 = new JLabel("如果要退票，则要将两个航班一起操作");
        JLabel label2 = new JLabel(info);

        label1.setFont(new Font("宋体", Font.PLAIN, 18));
        label1.setForeground(Color.RED);
        label1.setBounds(60, 38, 500, 63);
        frame.getContentPane().add(label1);

        label2.setFont(new Font("宋体", Font.PLAIN, 15));
        label2.setForeground(Color.BLACK);
        label2.setBounds(60, 170, 750, 63);
        frame.getContentPane().add(label2);

        label3.setFont(new Font("宋体", Font.PLAIN, 18));
        label3.setForeground(Color.RED);
        label3.setBounds(60, 100, 500, 63);
        frame.getContentPane().add(label3);


        //返回按钮
        JButton button = new JButton("返回");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                PassengerOrder wMyOrder = new PassengerOrder();
                wMyOrder.getFrame().setVisible(true);
            }
        });
        button.setBounds(48, 280, 93, 47);
        frame.getContentPane().add(button);

        //取消按钮
        JButton button_1 = new JButton("确认取消");
        button_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                char[] pass=passwordField.getPassword();
                String pwd="";
                for (int i = 0; i < pass.length; i++) {
                    pwd += pass[i];
                }
                String anotherflightname = new DbSelect().findOtherFlightName(Login.PassengerId,flightname);
                int anotherfid = new DbSelect().findFlightIdByName(anotherflightname);
                int anotherOrderid = new DbSelect().queryOrderId(Login.PassengerId,anotherfid);

                //查询订单
                Order o1 = new DbSelect().OrderSelect(anotherOrderid,Research.isDomestic);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
                boolean x1 = new DbUpdate().OrderUpdate(anotherOrderid, o1.getPassengerId().getId(), o1.getSeat(),
                        o1.getFlightId().getId(),
                        o1.getCreateDate().format(formatter), "CANCEL",Research.isDomestic) //更新订单表，状态列为CANCEL
                        && Passenger.UnsubscribeFlight(o1.getPassengerId().getId(),o1.getFlightId().getId(),pwd,Research.isDomestic);//成功取消航班


                //查询订单
                Order o = new DbSelect().OrderSelect(Login.OrderId,Research.isDomestic);
                boolean x = new DbUpdate().OrderUpdate(Login.OrderId, o.getPassengerId().getId(), o.getSeat(),
                        o.getFlightId().getId(),
                        o.getCreateDate().format(formatter), "CANCEL",Research.isDomestic) //更新订单表，状态列为CANCEL
                        && Passenger.UnsubscribeFlight(o.getPassengerId().getId(),o.getFlightId().getId(),pwd,Research.isDomestic);//成功取消航班
                if (x&&x1) {
                    frame.setVisible(false);
                    PassengerOrder wMyOrder = new PassengerOrder();
                    wMyOrder.getFrame().setVisible(true);
                    AllDialog.Dialog(wMyOrder.getFrame(), "操作成功");
                } else {
                    AllDialog.Dialog(frame, "操作失败，请重试");
                }
            }
        });
        button_1.setBounds(283, 280, 93, 47);
        frame.getContentPane().add(button_1);

        passwordField = new JPasswordField();
        passwordField.setBounds(176, 230, 144, 29);
        frame.getContentPane().add(passwordField);

        JLabel label_1 = new JLabel("输入密码");
        label_1.setBounds(112, 230, 54, 15);
        frame.getContentPane().add(label_1);



    }
}
