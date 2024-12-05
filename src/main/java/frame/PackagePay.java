package frame;

import flight.*;
import listeners.PaymentCompleteListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import flight.Flight;
import flight.Order;
import flight.Passenger;
import listeners.PaymentCompleteListener;

public class PackagePay {

    private JFrame frame;
    private JPasswordField passwordField;
    private JButton back;
    //private boolean isDmestic;
    private PaymentCompleteListener paymentListener; // 添加支付完成监听器

    public void setPaymentCompleteListener(PaymentCompleteListener listener) {
        this.paymentListener = listener;
    }

    /**
     * Launch the application.
     */

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PackagePay window = new PackagePay("");
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public PackagePay(String getSelectedItem) {
        initialize(getSelectedItem);
    }

    private void initialize(String getSelectedItem) {
        frame = new JFrame();
        frame.setTitle("套餐订购支付界面");
        frame.setBounds(550, 100, 550, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //输出订购信息
        //判断订单能否生效
        DbSelect sa = new DbSelect();
        //返回当前状态
        //String packagestatus = sa.queryPackageStatus(33);
        //返回所选状态
        System.out.println("再次确认订购信息 " + getSelectedItem);
        JLabel show1 = new JLabel("请再次确认订购信息: "+getSelectedItem);
        show1.setBounds(10, 10, 220, 30);
        show1.setForeground(Color.red);
        frame.getContentPane().add(show1);


        JLabel label = new JLabel(
                "确认支付请输入密码");
        label.setBounds(10, 64, 129, 73);
        frame.getContentPane().add(label);

        //按钮_支付
        JButton sure = new JButton("确认支付");
        sure.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //获取密码
                char[] cpwd = passwordField.getPassword();
                String pwd = "";
                for (int i = 0; i < cpwd.length; i++) {
                    pwd += cpwd[i];
                }

                int x = PackageOrder.ReservePackageOrder(pwd,Login.PassengerId,0,0);//插入套餐订单
                System.out.println("在PackagePay里PackageOrder.ReservePackageOrder成功");
                if (x==1) {
                    frame.setVisible(false);
                    FlightRecommendation window = new FlightRecommendation();//返回个性推荐界面
                    window.getFrame().setVisible(true);
                    AllDialog.Dialog(window.getFrame(), "订购成功");
                } else if(x==2){
                    AllDialog.Dialog(frame, "订购失败，请检查密码");
                }

            }
        });
        sure.setBounds(41, 157, 93, 38);
        frame.getContentPane().add(sure);

        passwordField = new JPasswordField();
        passwordField.setBounds(181, 90, 129, 21);
        frame.getContentPane().add(passwordField);

        back = new JButton("返回");
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                FlightRecommendation window = new FlightRecommendation();
                window.getFrame().setVisible(true);

            }
        });
        back.setBounds(178, 157, 103, 38);
        frame.getContentPane().add(back);
    }



    public Window getFrame() {
        return frame;
    }


}
