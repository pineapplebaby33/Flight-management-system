package flight;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTime {
	private int year = 0;
	private int month = 0;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;

	public DateTime(String time) {
		this.GetTime(this, time);
	}

	public static DateTime GetDateTimeOb(String time) {
		DateTime x = new DateTime(time);
		return x;
	}

	public static boolean GetSub(DateTime t1, DateTime t2) {
		int y, m, d, h, min, s, y1, m1, d1, h1, min1, s1;
		if (DateTime.CompareDate(t1, t2)) {
			y = t1.getYear();
			m = t1.getMonth();
			d = t1.getDay();
			h = t1.getHour();
			min = t1.getMinute();
			s = t1.getSecond();
			y1 = t2.getYear();
			m1 = t2.getMonth();
			d1 = t2.getDay();
			h1 = t2.getHour();
			min1 = t2.getMinute();
			s1 = t2.getSecond();
		} else {
			y = t2.getYear();
			m = t2.getMonth();
			d = t2.getDay();
			h = t2.getHour();
			min = t2.getMinute();
			s = t2.getSecond();
			y1 = t1.getYear();
			m1 = t1.getMonth();
			d1 = t1.getDay();
			h1 = t1.getHour();
			min1 = t1.getMinute();
			s1 = t1.getSecond();
		}

		boolean re = false;

		if (y == y1) {
			if (m == m1) {
				if (d == d1) {
					if (s < s1) {
						min--;
						s = s + 60 - s1;
					} else {
						s = s - s1;
					}
					if (min < min1) {
						h--;
						min = min + 60 - min1;
					} else {
						min = min - min1;
					}
					h = h - h1;
					if (h > 2) {
						// ���������2Сʱ����
						return true;
					}
					if (h == 2) {
						if (min > 0 || s > 0) {
							return true;
						}
					}
				} else {
					// ���պ���
					d--;
					if (s < s1) {
						min--;
						s = s + 60 - s1;
					} else {
						s = s - s1;
					}
					if (min < min1) {
						h--;
						min = min + 60 - min1;
					} else {
						min = min - min1;
					}
					h = h + 24 - h1;
					if (d > 0) {
						return true;
					}
					if (h > 2) {
						// ���������2Сʱ����
						return true;
					}
					return false;
				}
			} else {
				// ���º���
				m--;
				m = m - m1;
				d = d + DateTime.MonthDay(m) - d1;
				if (d < 0) {
					System.err.println("���º���������");
					return false;
				}
				if (s < s1) {
					min--;
					s = s + 60 - s1;
				} else {
					s = s - s1;
				}
				if (min < min1) {
					h--;
					min = min + 60 - min1;
				} else {
					min = min - min1;
				}
				if (h < h1) {
					h = h + 24 - h1;
					d--;
				} else {
					h = h - h1;
				}
				if (d > 0 || m > 0) {
					return true;
				}
				if (h > 2) {
					// ���������2Сʱ����
					return true;
				}
				return false;
			}
		} else {
			// ���꺽��
			y--;
			y = y - y1;
			m = m + 12 - m1;
			if (d < d1) {
				m--;
				d = d + DateTime.MonthDay(m) - d1;
			} else {
				d = d - d1;
			}
			if (s < s1) {
				min--;
				s = s + 60 - s1;
			} else {
				s = s - s1;
			}
			if (min < min1) {
				h--;
				min = min + 60 - min1;
			} else {
				min = min - min1;
			}
			if (h < h1) {
				h = h + 24 - h1;
				d--;
			} else {
				h = h - h1;
			}
			if (h > 2 || d > 0 || m > 0 || y > 0) {
				return true;
			}
		}
		return re;
	}

	/*
	 * �Ƚ�����ʱ���t�Ƿ����2Сʱ ��t-NowDate<=2 hour,�򷵻�true ��ʾӦ�ø��ĺ���״̬ ���򷵻�false
	 * ��ʾ����ʱ�������ʱ��������2Сʱ������Ҫ����
	 */
	public boolean UpdateStatus(DateTime t) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		try {
			long x1 = sdf.parse(GetDateTimeStr(t)).getTime()
					- sdf.parse(GetDateTimeStr(this)).getTime();
			if ((x1 / 1000 / 60) <= 120) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * ���ر������� ����:�·�
	 */
	private static int MonthDay(int i) {
		switch (i) {
		// �и�bug��2��29����ĺ��෢����
		case 0:
			return 31;
		case 1:
			return 31;
		case 2:
			return 28;
		case 3:
			return 31;
		case 5:
			return 31;
		case 7:
			return 31;
		case 8:
			return 31;
		case 10:
			return 31;
		case 12:
			return 31;
		default:
			return 30;
		}

	}

	/*
	 * ��DateTime�������ַ���
	 */
	public static String GetDateTimeStr(DateTime t) {
		String s = "";
		s += t.CheckLen(t.year) + "-" + t.CheckLen(t.month) + "-"
				+ t.CheckLen(t.day) + "-" + t.CheckLen(t.hour) + "-"
				+ t.CheckLen(t.minute) + "-" + t.CheckLen(t.second);
		return s;

	}

	private void GetTime(DateTime t, String time) {
		if (time.equals("") == false) {

			String[] _t;
			_t = time.split("-");
			// �˴���CheckLen��û���ã���Ϊint���ͻ��0����
			// д��ŷ���û���ã�����
			t.year = Integer.parseInt(CheckLen(_t[0]));
			t.month = Integer.parseInt(CheckLen(_t[1]));
			t.day = Integer.parseInt(CheckLen(_t[2]));
			t.hour = Integer.parseInt(CheckLen(_t[3]));
			t.minute = Integer.parseInt(CheckLen(_t[4]));
			t.second = Integer.parseInt(CheckLen(_t[5]));
		}
	}


	public static boolean CompareDate(DateTime t1, DateTime t2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		try {
			long x1 = sdf.parse(DateTime.GetDateTimeStr(t1)).getTime()
					- sdf.parse(DateTime.GetDateTimeStr(t2)).getTime();
			if (x1 > 0) {
				// t1>t2,t1��ʱ���Ϊ����
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private String CheckLen(String s) {
		if (s.length() < 2) {
			s = "0" + s;
		}
		return s;
	}

	private String CheckLen(int s) {
		String ss = Integer.toString(s);
		if (ss.length() < 2) {
			ss = "0" + ss;
		}
		return ss;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

}
