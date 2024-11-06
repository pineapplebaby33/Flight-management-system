package frame;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DateChooser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1379027901492443365L;
	private int width = 200;
	private int height = 220;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JTextField dateField = new JTextField();
	private DateChooserButton btnChoose = new DateChooserButton(">"); // ����ָ����������ͷ��unicode��
	private String parten;
	private Container owner;
	private int length = 120;

	public DateChooser(Container owner, int length) {
		this.owner = owner;
		this.parten = "yyyy-MM-dd";
		this.length = length;
		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public DateChooser(Container owner, String partten, int length) {
		this.owner = owner;
		this.parten = partten;
		this.length = length;
		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public DateChooser(Container owner, String partten) {
		this.owner = owner;
		this.parten = partten;
		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public DateChooser(Container owner) {
		this.owner = owner;
		this.parten = "yyyy-MM-dd";
		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void init() throws Exception {
		dateField.setToolTipText("点击右侧按钮选择日期");
		btnChoose.setToolTipText("点击此按钮选择日期");
		this.setLayout(gridBagLayout1);
		dateField.setEditable(false);
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateChooser.this.btnChoose_actionPerformed(e);
			}
		});
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(parten);
		this.setText(simpleDateFormat.format(date));
		this.add(dateField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), this.length, 0));
		this.add(btnChoose, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 0, 0));
	}

	public void setToolTipText(String text) {
		dateField.setToolTipText(text);
		btnChoose.setToolTipText(text);
	}

	public void btnChoose_actionPerformed(ActionEvent e) {
		Rectangle r = dateField.getBounds();
		Point pOnScreen = dateField.getLocationOnScreen();

		Point result = new Point(pOnScreen.x, pOnScreen.y + r.height);
		Point powner = owner.getLocation();
		int offsetX = (pOnScreen.x + width) - (powner.x + owner.getWidth());
		int offsetY = (pOnScreen.y + r.height + height)
				- (powner.y + owner.getHeight());

		if (offsetX > 0) {
			result.x -= offsetX;
		}

		if (offsetY > 0) {
			result.y -= height + r.height;
		}

		JDialog dateFrame = new JDialog();
		dateFrame.setModal(false);
		dateFrame.setUndecorated(true);
		dateFrame.setLocation(result);
		dateFrame.setSize(width, height);

		dateFrame.addWindowListener(new WindowAdapter() {
			// ������ķ�����ѡ����������������ѡ���������Ϊ�ǻ״̬���Զ��ͷ���Դ��
			public void windowDeactivated(WindowEvent e) {
				JDialog f = (JDialog) e.getSource();
				f.dispose();
			}
		});
		DatePanel datePanel = new DatePanel(dateFrame, parten);
		dateFrame.getContentPane().setLayout(new BorderLayout());
		dateFrame.getContentPane().add(datePanel);
		dateFrame.setVisible(true);
	}

	public String getText() {
		return this.dateField.getText();
	}

	public void setText(String text) {
		this.dateField.setText(text);
	}

	public JTextField getDateField() {
		return dateField;
	}

	class DatePanel extends JPanel implements MouseListener, ChangeListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8135631884575370072L;
		int startYear = 1970; // Ĭ�ϡ���С����ʾ���
		int lastYear = 2050; // Ĭ�ϡ������ʾ���

		Color backGroundColor = Color.gray; // ��ɫ
		// ���������ɫ----------------//
		Color palletTableColor = Color.white; // �������ɫ
		Color weekFontColor = Color.blue; // ��������ɫ
		Color dateFontColor = Color.black; // ��������ɫ
		Color weekendFontColor = Color.red; // ��ĩ����ɫ
		Color moveButtonColor = Color.BLUE; // ����ƶ���������ɫ
		Color todayBtnColor = Color.pink; // �����������ɫ
		// ��������ɫ------------------//
		Color controlLineColor = Color.pink; // ��������ɫ
		Color controlTextColor = Color.white; // ��������ǩ����ɫ

		JSpinner yearSpin;
		JSpinner monthSpin;
		JSpinner hourSpin;
		JButton[][] daysButton = new JButton[6][7];

		JDialog f;

		JPanel dayPanel = new JPanel(); // ����panel
		JPanel yearPanel = new JPanel();

		Calendar calendar = Calendar.getInstance();
		String pattern;

		public DatePanel(JDialog target, String pattern) {
			super();

			this.f = target;
			this.pattern = pattern;

			setLayout(new BorderLayout());
			setBorder(new LineBorder(backGroundColor, 2));
			setBackground(backGroundColor);
			initButton(); // ��ʼ���������ڵİ�ť��
			createYearAndMonthPanal(); //
			this.flushWeekAndDayPanal(calendar); // ֮ǰ�����ȱ�֤�������ڵİ�ť�Ѿ���ʼ����
			this.setLayout(new BorderLayout());
			this.add(yearPanel, BorderLayout.NORTH);
			this.add(dayPanel, BorderLayout.CENTER);
		}

		private void initButton() {
			int actionCommandId = 1;
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					JButton numberButton = new JButton();
					numberButton.setBorder(BorderFactory.createEmptyBorder());
					numberButton.setHorizontalAlignment(SwingConstants.CENTER);
					numberButton.setActionCommand(String
							.valueOf(actionCommandId));

					numberButton.addMouseListener(this);

					numberButton.setBackground(palletTableColor);
					numberButton.setForeground(dateFontColor);
					numberButton.setText(String.valueOf(actionCommandId));
					numberButton.setPreferredSize(new Dimension(25, 25));
					daysButton[i][j] = numberButton;
					actionCommandId++;
				}
			}
		}

		private Calendar getNowCalendar() {
			Calendar result = Calendar.getInstance();
			return result;
		}

		private Date getSelectDate() {
			return calendar.getTime();
		}

		private void createYearAndMonthPanal() {
			Calendar c = getNowCalendar();
			int currentYear = c.get(Calendar.YEAR);
			int currentMonth = c.get(Calendar.MONTH) + 1;
			int currentHour = c.get(Calendar.HOUR_OF_DAY);
			yearSpin = new JSpinner(new SpinnerNumberModel(
					currentYear, startYear, lastYear, 1));
			monthSpin = new JSpinner(new SpinnerNumberModel(
					currentMonth, 1, 12, 1));
			hourSpin = new JSpinner(new SpinnerNumberModel(
					currentHour, 0, 23, 1));

			yearPanel.setLayout(new FlowLayout());
			yearPanel.setBackground(controlLineColor);

			yearSpin.setPreferredSize(new Dimension(48, 20));
			yearSpin.setName("Year");
			yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
			yearSpin.addChangeListener(this);
			yearPanel.add(yearSpin);

			JLabel yearLabel = new JLabel("年");
			yearLabel.setForeground(controlTextColor);
			yearPanel.add(yearLabel);

			monthSpin.setPreferredSize(new Dimension(35, 20));
			monthSpin.setName("Month");
			monthSpin.addChangeListener(this);
			yearPanel.add(monthSpin);

			JLabel monthLabel = new JLabel("月");
			monthLabel.setForeground(controlTextColor);
			yearPanel.add(monthLabel);

			hourSpin.setPreferredSize(new Dimension(35, 20));
			hourSpin.setName("Hour");
			hourSpin.addChangeListener(this);
			yearPanel.add(hourSpin);

			JLabel hourLabel = new JLabel("日");
			hourLabel.setForeground(controlTextColor);
			yearPanel.add(hourLabel);
		}

		private void flushWeekAndDayPanal(Calendar c) {
			// c.set
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.setFirstDayOfWeek(0);
			int firstdayofWeek = c.get(Calendar.DAY_OF_WEEK);
			int lastdayofWeek = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			String colname[] = {"日", "一", "二", "三", "四", "五", "六"};
			int today = getNowCalendar().get(Calendar.DAY_OF_MONTH);
			// ���ù̶����壬������û����ı�Ӱ���������
			dayPanel.setFont(new Font("宋体", Font.PLAIN, 12));
			dayPanel.setLayout(new GridBagLayout());
			dayPanel.setBackground(Color.white);

			JLabel cell;

			for (int i = 0; i < 7; i++) {
				cell = new JLabel(colname[i]);
				cell.setHorizontalAlignment(JLabel.CENTER);
				cell.setPreferredSize(new Dimension(25, 25));
				if (i == 0 || i == 6) {
					cell.setForeground(weekendFontColor);
				} else {
					cell.setForeground(weekFontColor);
				}
				dayPanel.add(cell, new GridBagConstraints(i, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
			}

			int actionCommandId = 1;
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {

					JButton numberButton = daysButton[i][j];
					actionCommandId = Integer.parseInt(numberButton
							.getActionCommand());
					if (actionCommandId == today) {
						numberButton.setBackground(todayBtnColor);
					}
					if ((actionCommandId + firstdayofWeek - 2) % 7 == 6
							|| (actionCommandId + firstdayofWeek - 2) % 7 == 0) {
						numberButton.setForeground(weekendFontColor);
					} else {
						numberButton.setForeground(dateFontColor);
					}
					if (actionCommandId <= lastdayofWeek) {
						int y = 0;
						if ((firstdayofWeek - 1) <= (j + firstdayofWeek - 1) % 7) {
							y = i + 1;
						} else {
							y = i + 2;
						}
						dayPanel.add(numberButton, new GridBagConstraints((j
								+ firstdayofWeek - 1) % 7, y, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.NONE,
								new Insets(0, 0, 0, 0), 0, 0));
					}
				}
			}
		}

		private int getSelectedYear() {
			return ((Integer) yearSpin.getValue()).intValue();
		}

		private int getSelectedMonth() {
			return ((Integer) monthSpin.getValue()).intValue();
		}

		private int getSelectedHour() {
			return ((Integer) hourSpin.getValue()).intValue();
		}

		public void stateChanged(ChangeEvent e) {
			JSpinner source = (JSpinner) e.getSource();
			if (source.getName().equals("Hour")) {
				calendar.set(Calendar.HOUR_OF_DAY, getSelectedHour());
				return;
			}
			if (source.getName().equals("Year")) {

				calendar.set(Calendar.YEAR, getSelectedYear());
				dayPanel.removeAll();
				this.flushWeekAndDayPanal(calendar);
				dayPanel.revalidate();
				dayPanel.updateUI();
				return;
			}
			if (source.getName().equals("Month")) {
				calendar.set(Calendar.MONTH, getSelectedMonth() - 1);

				dayPanel.removeAll();
				this.flushWeekAndDayPanal(calendar);
				dayPanel.revalidate();
				dayPanel.updateUI();
				return;
			}
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
				JButton source = (JButton) e.getSource();

				String value = source.getText();
				int day = Integer.parseInt(value);
				calendar.set(Calendar.DAY_OF_MONTH, day);
				Date selectDate = this.getSelectDate();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						pattern);
				DateChooser.this.setText(simpleDateFormat.format(selectDate));

				// System.out.println(year + "��" + month + "��" + day + "��");
				f.dispose();
			}
		}

		public void mousePressed(MouseEvent e) {
			// ��ʵ�ֽӿ��еķ���������ɾ��
		}

		public void mouseReleased(MouseEvent e) {
			// ��ʵ�ֽӿ��еķ���������ɾ��
		}

		public void mouseEntered(MouseEvent e) {
			JButton jbutton = (JButton) e.getSource();
			jbutton.setBackground(moveButtonColor);

		}

		public void mouseExited(MouseEvent e) {
			JButton jbutton = (JButton) e.getSource();
			int comm = Integer.parseInt(jbutton.getActionCommand());
			int today = getNowCalendar().get(Calendar.DAY_OF_MONTH);
			if (comm == today) {
				jbutton.setBackground(todayBtnColor);
			} else {
				jbutton.setBackground(palletTableColor);
			}
		}
	}

	class DateChooserButton extends JButton {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3645878705217653628L;

		public DateChooserButton(String text) {
			super(text);
		}

		public Insets getInsets() {
			return new Insets(4, 2, 0, 2);
		}

	}
}