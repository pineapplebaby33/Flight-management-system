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

public class CancelOrder {

    private JFrame frame;
    private JPasswordField passwordField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CancelOrder window = new CancelOrder();
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
    public CancelOrder() {
        initialize();
    }

    public JFrame getFrame() {
        return frame;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("取消订单");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);


        //取消订单提示
        JLabel label = new JLabel(
                "你确认要取消订单吗？");
        label.setFont(new Font("宋体", Font.PLAIN, 18));
        label.setForeground(Color.RED);
        label.setBounds(112, 38, 208, 63);
        frame.getContentPane().add(label);

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
        button.setBounds(48, 183, 93, 47);
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
                //查询订单
                Order o = new DbSelect().OrderSelect(Login.OrderId,Research.isDomestic);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
                boolean x = new DbUpdate().OrderUpdate(Login.OrderId, o.getPassengerId().getId(), o.getSeat(),
                                                        o.getFlightId().getId(),
                                                        o.getCreateDate().format(formatter), "CANCEL",Research.isDomestic) //更新订单表，状态列为CANCEL
                        && Passenger.UnsubscribeFlight(o.getPassengerId().getId(),o.getFlightId().getId(),pwd,Research.isDomestic);//成功取消航班
                DbDelete d = new DbDelete();
                boolean x2 = d.deletePackageOrder(Login.PassengerId,Login.packagestatus,Login.OrderId);
                if (x) {
                    if(x2){
                        System.out.println("已删除当前套餐下的订单,订单id： "+Login.OrderId);
                    }
                    else{
                        System.out.println("取消订单不是当前套餐订单,订单id： "+Login.OrderId);
                    }
                    frame.setVisible(false);
                    PassengerOrder wMyOrder = new PassengerOrder();
                    wMyOrder.getFrame().setVisible(true);
                    AllDialog.Dialog(wMyOrder.getFrame(), "操作成功");

                } else {
                    AllDialog.Dialog(frame, "操作失败，请重试");
                }
            }
        });
        button_1.setBounds(283, 183, 93, 47);
        frame.getContentPane().add(button_1);

        passwordField = new JPasswordField();
        passwordField.setBounds(176, 122, 144, 29);
        frame.getContentPane().add(passwordField);

        JLabel label_1 = new JLabel("输入密码");
        label_1.setBounds(112, 133, 54, 15);
        frame.getContentPane().add(label_1);
    }
}
