//消息对话框
package frame;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class AllDialog {
    static JFrame j = new JFrame();

    public static void Dialog(Frame f, String s) {
        JOptionPane.showMessageDialog(f, s, "提示", JOptionPane.PLAIN_MESSAGE);
    }
}
