package flight;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestObject {
	/*
	 * Test���� ���ڱ���ĳһ���������private���ԣ� ������Щ����Ϊ���飬 �޷�ֱ�Ӳ鿴����
	 */
	public static void print(BookingInfo m) {

		Class<?> c;
		try {
			c = Class.forName(BookingInfo.class.getName());
			Method[] method = c.getDeclaredMethods();
			for (Method s : method) {
				if (s.getName().indexOf("get") != -1) {

					try {
						System.out.println(s.invoke(m));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void print(Passenger m) {

		Class<?> c;
		try {
			c = Class.forName(Passenger.class.getName());
			Method[] method = c.getDeclaredMethods();
			for (Method s : method) {
				if (s.getName().indexOf("get") != -1) {

					try {
						System.out.println(s.invoke(m));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void print(Order m) {

		Class<?> c;
		try {
			c = Class.forName(Order.class.getName());
			Method[] method = c.getDeclaredMethods();
			for (Method s : method) {
				if (s.getName().indexOf("get") != -1) {

					try {
						if (s.invoke(m).getClass().isArray()) {
							System.out.println(s.invoke(m).toString());
						} else {
							System.out.println(s.invoke(m));
						}
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void print(Flight m) {

		Class<?> c;
		try {
			c = Class.forName(Flight.class.getName());
			Method[] method = c.getDeclaredMethods();
			for (Method s : method) {
				if (s.getName().indexOf("get") != -1) {

					try {

						System.out.println(s.invoke(m));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void print(DateTime m) {

		Class<?> c;
		try {
			c = Class.forName(DateTime.class.getName());
			Method[] method = c.getDeclaredMethods();
			for (Method s : method) {
				if (s.getName().indexOf("get") != -1) {

					try {
						System.out.println(s.invoke(m));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void print(Admin m) {

		Class<?> c;
		try {
			c = Class.forName(Admin.class.getName());
			Method[] method = c.getDeclaredMethods();
			for (Method s : method) {
				if (s.getName().indexOf("get") != -1) {

					try {
						System.out.println(s.invoke(m));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
