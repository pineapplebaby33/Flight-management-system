package frame;

import flight.DbInsert;
import flight.DbSelect;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Food {
    private JFrame frame;
    private JTextField nameText;
    private JTextField identityText;
    private JPasswordField passwordField;

    public JFrame getFrame() {
        return this.frame;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Food window = new Food();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Food() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setFont(new Font("宋体", Font.PLAIN, 12));
        frame.setTitle("预定餐食");
        frame.setBounds(100, 100, 634, 511);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // 温馨提示标签
        JLabel label1 = new JLabel("温馨提示：确定要预定餐食吗");
        label1.setFont(new Font("宋体", Font.PLAIN, 18));
        label1.setForeground(Color.RED);
        label1.setBounds(60, 38, 500, 63);
        frame.getContentPane().add(label1);

        // 添加复选框 - 中餐
        JCheckBox chineseFoodCheckBox = new JCheckBox("中餐");
        chineseFoodCheckBox.setFont(new Font("宋体", Font.PLAIN, 16));
        chineseFoodCheckBox.setBounds(100, 150, 100, 30);
        frame.getContentPane().add(chineseFoodCheckBox);

        // 添加复选框 - 西餐
        JCheckBox westernFoodCheckBox = new JCheckBox("西餐");
        westernFoodCheckBox.setFont(new Font("宋体", Font.PLAIN, 16));
        westernFoodCheckBox.setBounds(250, 150, 100, 30);
        frame.getContentPane().add(westernFoodCheckBox);

        // 添加提示信息
        JLabel hintLabel = new JLabel("提示：具体餐食根据实际情况而定，这里只提供餐食类型选择");
        hintLabel.setFont(new Font("宋体", Font.PLAIN, 12));
        hintLabel.setForeground(Color.BLACK);
        hintLabel.setBounds(60, 200, 500, 30);
        frame.getContentPane().add(hintLabel);

        // 添加返回按钮
        JButton Button = new JButton("返回");
        Button.setFont(new Font("宋体", Font.PLAIN, 16));
        Button.setBounds(50, 300, 150, 40);
        frame.getContentPane().add(Button);
        Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                frame.setVisible(false);
                PassengerOrder wMyOrder = new PassengerOrder();
                wMyOrder.getFrame().setVisible(true);
            }
        });

        // 添加提交按钮
        JButton submitButton = new JButton("提交预定");
        submitButton.setFont(new Font("宋体", Font.PLAIN, 16));
        submitButton.setBounds(250, 300, 150, 40);
        frame.getContentPane().add(submitButton);

        // 提交按钮事件监听器
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder orderDetails = new StringBuilder("您已选择的餐食：");
                boolean x=false;
                if(chineseFoodCheckBox.isSelected()&&westernFoodCheckBox.isSelected()){
                    AllDialog.Dialog(frame, "只能选择一种餐食");
                }else{
                    if (chineseFoodCheckBox.isSelected()) {
                        orderDetails.append(" 中餐");
                        Login.food =1;
                        int orderid = new DbSelect().queryOrderId(Login.PassengerId,Login.FlightId);
                        x = new DbInsert().FoodInsert(orderid,Login.food);
                    }
                    if (westernFoodCheckBox.isSelected()) {
                        orderDetails.append(" 西餐");
                        Login.food =2;
                        System.out.println("Login.PassengerId:"+Login.PassengerId+"Login.FlightId:"+Login.FlightId);
                        x = new DbInsert().FoodInsert(Login.OrderId,Login.food);
                    }
                    if (!chineseFoodCheckBox.isSelected() && !westernFoodCheckBox.isSelected()) {
                        orderDetails.append(" 无");
                        Login.food = 0;
                    }

                    if(x){
                        // 弹出信息框显示选择的餐食
                        JOptionPane.showMessageDialog(frame, orderDetails.toString(), "预定结果", JOptionPane.INFORMATION_MESSAGE);

                    }else{
                        AllDialog.Dialog(frame, "选择失败");
                    }

                    frame.setVisible(false);
                    PassengerOrder wMyOrder = new PassengerOrder();
                    wMyOrder.getFrame().setVisible(true);
                }

            }
        });
    }
}
