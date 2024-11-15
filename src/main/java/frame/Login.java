//创建登录界面，跳转到注册或用户/管理员信息界面

package frame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import flight.*;

import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Login {
    public static int FlightId = 0;
    static int AdminId = 0;
    public static int PassengerId = 0;
    public static String Pwd = "";
    static int OrderId = 0;
    private JFrame frame;
    private JTextField RealName;
    private JPasswordField passwordField;
    public static Map<Integer, List<String>> transferFlightsMap = new HashMap<>();


    public static void main(String[] args) {
        //将一个 Runnable 对象排队，以便在事件调度线程（EDT）上稍后执行。
        //这是为了确保所有的 Swing GUI 操作都在事件调度线程上完成，从而避免多线程问题。这样做可以保证 GUI 的线程安全性
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login window = new Login();
                    window.frame.setVisible(true);
                    printTransferRoutes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Login函数，用于调用实现初始化函数
    public Login() {
        initialize();
    }


    //initialize函数，用于
    //1.开启计时器，自动更新航班
    //2.显示主窗口(登录/注册)
    private void initialize() {
        //开启计时器，自动更新航班
        TimeThread updateBegin = new TimeThread();
        Thread t1 = new Thread(updateBegin);
        t1.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        System.out.println("航班状态更新完毕");

        //自动添加7天后的航班
        Generate gen = new Generate();
        gen.judgment();

        //主窗口-f
        frame = new JFrame();
        frame.setTitle("登录页面");
        frame.setBounds(100, 100, 607, 423);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //用户名-f
        JLabel label = new JLabel("用户名");
        label.setBounds(49, 117, 108, 29);
        frame.getContentPane().add(label);

        RealName = new JTextField();
        RealName.setBounds(194, 114, 273, 35);
        frame.getContentPane().add(RealName);
        RealName.setColumns(10);

        //密码-f
        JLabel label_1 = new JLabel("密码");
        label_1.setBounds(49, 200, 108, 29);
        frame.getContentPane().add(label_1);

        passwordField = new JPasswordField();
        passwordField.setBounds(194, 197, 273, 32);
        frame.getContentPane().add(passwordField);

        //注册-f
        JButton button = new JButton("注册");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //跳转到注册页面
                frame.setVisible(false);
                EnrollPassenger window = new EnrollPassenger();
                window.getFrame().setVisible(true);
            }
        });
        button.setBounds(106, 273, 153, 37);
        frame.getContentPane().add(button);

        //登录-f-j
        JButton button_1 = new JButton("登陆");
        button_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                //获取用户名和密码
                String user = RealName.getText();
                char[] pwd = passwordField.getPassword();
                String password = "";
                for (int i = 0; i < pwd.length; i++) {
                    password += pwd[i];
                }
                Login.Pwd = password;
                //判断用户权限
                boolean bo = Admin.CheckAdmin(user, password);
                if (user.equals("") || pwd.equals("") ) {
                    AllDialog.Dialog(frame, "请输入完整信息");
                }
                else if (bo) {
                    //跳转到管理员界面
                    frame.setVisible(false);
                    Login.AdminId = new DbSelect().AdminSelect(user).getId();
                    AdminFunction window = new AdminFunction();
                    window.getAdminFrame().setVisible(true);
                } else {
                    boolean p = Passenger.CheckPwd(user, password);
                    if (p) {
                        //跳转到乘客界面
                        Login.PassengerId = new DbSelect().PassengerSelect(user, password).getId();
                        frame.setVisible(false);
                        Research window = new Research();
                        window.getFrame().setVisible(true);

                    } else {
                        AllDialog.Dialog(frame, "用户名或密码错误");
                    }
                }

            }
        });
        button_1.setBounds(351, 273, 153, 37);
        frame.getContentPane().add(button_1);

    }

    public static void printTransferRoutes() {
        for (Map.Entry<Integer, List<String>> entry : Login.transferFlightsMap.entrySet()) {
            Integer flightId = entry.getKey();
            List<String> route = entry.getValue();
            System.out.println("Flight ID: " + flightId + " Route: " + route);
        }
    }


    //getFrame函数，用于返回登录页面
    public JFrame getFrame() {
        return frame;
    }

}
