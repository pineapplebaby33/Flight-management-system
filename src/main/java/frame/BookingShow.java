package frame;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import flight.BookingInfo;
import flight.DateTime;
import flight.DbSelect;

public class BookingShow {
    private int table_h = 330;
    private int table_w = 400;
    private JFrame frame;
    private JTable BookingInfo_Table;
    private JScrollPane scrollPane;
    private JTextField FlightName;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BookingShow window = new BookingShow();
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
    public BookingShow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("航班预订信息查询");
        frame.setBounds(100, 100, 594, 517);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        String[] columnNames = {"乘客姓名", "身份证号", "座位号", "预定时间", "订单状态", "航班名称"};
        BookingInfo[] bookingInfos = new DbSelect().BookingInfoSelect();
        String[][] booking_ob = new String[bookingInfos.length][6];
        for (int i = 0; i < bookingInfos.length; i++) {
            booking_ob[i][0] = bookingInfos[i].getPname();
            booking_ob[i][1] = bookingInfos[i].getPident();
            booking_ob[i][2] = Integer.toString(bookingInfos[i].getSeat());
            booking_ob[i][3] = DateTime.GetDateTimeStr(bookingInfos[i]
                    .getoCreateDate());
            booking_ob[i][4] = bookingInfos[i].getoStatus();
            booking_ob[i][5] = bookingInfos[i].getF().getFlightName();
            // System.out.println(flights[i].getFlightStatus());
        }
        BookingInfo_Table = new JTable(booking_ob, columnNames) {

            /**
             *
             */
            private static final long serialVersionUID = 6755177410256977124L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }// ��������༭
        };
        TableColumn column = null;
        int colunms = BookingInfo_Table.getColumnCount();
        for (int i = 0; i < colunms; i++) {
            column = BookingInfo_Table.getColumnModel().getColumn(i);
            /* ��ÿһ�е�Ĭ�Ͽ������Ϊ100 */
            column.setPreferredWidth(100);
        }

        BookingInfo_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        BookingInfo_Table.setSelectionBackground(Color.LIGHT_GRAY);
        BookingInfo_Table.setSelectionForeground(Color.yellow);
        BookingInfo_Table.setBounds(33, 34, table_w, table_h);

        JButton button = new JButton("返回");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                AdminFunction w = new AdminFunction();
                w.getAdminFrame().setVisible(true);
            }
        });
        button.setFont(new Font("宋体", Font.PLAIN, 18));
        button.setBounds(205, 413, 127, 39);
        frame.getContentPane().add(button);
        scrollPane = new JScrollPane();
        scrollPane.setBounds(21, 77, 540, 295);
        frame.getContentPane().add(scrollPane);
        scrollPane.setViewportView(BookingInfo_Table);

        FlightName = new JTextField();
        FlightName.setBounds(82, 46, 127, 21);
        frame.getContentPane().add(FlightName);
        FlightName.setColumns(10);

        JLabel label = new JLabel("航班号：");
        label.setBounds(32, 49, 54, 15);
        frame.getContentPane().add(label);

        JButton Search = new JButton("搜索航班");
        Search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] columnNames = {"乘客姓名", "身份证号", "座位号", "预定时间", "订单状态", "航班名称"};
                BookingInfo[] bookingInfos = new DbSelect().BookingInfoSelect();
                int len = bookingInfos.length;
                String FN = FlightName.getText();
                BookingInfo[] temp = new BookingInfo[len];
                int newlen = 0;
                for (int i = 0; i < bookingInfos.length; i++) {
                    if (bookingInfos[i].getF().getFlightName().equals(FN)) {
                        temp[i] = bookingInfos[i];
                        newlen++;
                    } else {
                        temp[i] = null;
                    }
                }
                if (newlen != 0) {
                    bookingInfos = null;
                    bookingInfos = new BookingInfo[newlen];
                    int t = 0;
                    for (int i = 0; i < len; i++) {
                        if(temp[i]!=null)
                        {
                            bookingInfos[t]=temp[i];
                            t++;
                        }

                    }
                    String[][] booking_ob = new String[bookingInfos.length][6];
                    for (int i = 0; i < bookingInfos.length; i++) {
                        booking_ob[i][0] = bookingInfos[i].getPname();
                        booking_ob[i][1] = bookingInfos[i].getPident();
                        booking_ob[i][2] = Integer.toString(bookingInfos[i]
                                .getSeat());
                        booking_ob[i][3] = DateTime
                                .GetDateTimeStr(bookingInfos[i]
                                        .getoCreateDate());
                        booking_ob[i][4] = bookingInfos[i].getoStatus();
                        booking_ob[i][5] = bookingInfos[i].getF()
                                .getFlightName();
                        // System.out.println(flights[i].getFlightStatus());
                    }
                    BookingInfo_Table = new JTable(booking_ob, columnNames) {

                        /**
                         *
                         */
                        private static final long serialVersionUID = 6755177410256977124L;

                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }// ��������༭
                    };
                    TableColumn column = null;
                    int colunms = BookingInfo_Table.getColumnCount();
                    for (int i = 0; i < colunms; i++) {
                        column = BookingInfo_Table.getColumnModel()
                                .getColumn(i);
                        /* ��ÿһ�е�Ĭ�Ͽ������Ϊ100 */
                        column.setPreferredWidth(100);
                    }

                    BookingInfo_Table
                            .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    BookingInfo_Table.setSelectionBackground(Color.LIGHT_GRAY);
                    BookingInfo_Table.setSelectionForeground(Color.yellow);
                    BookingInfo_Table.setBounds(33, 34, table_w, table_h);
                    scrollPane.setViewportView(BookingInfo_Table);
                } else {
                    AllDialog.Dialog(frame, "操作失败，请重试");
                }
            }
        });
        Search.setBounds(420, 28, 110, 39);
        frame.getContentPane().add(Search);
    }

    public JFrame getFrame() {
        return frame;
    }
}

