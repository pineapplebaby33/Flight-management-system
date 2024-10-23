package flight;

public class Admin {
	private int id = 0;
	private String username = "";
	private String pwd = "";

	public Admin(int Id, String user, String pwd) {
		this.id = Id;
		this.username = user;
		this.pwd = pwd;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPwd() {
		return pwd;
	}

	/**
	 * @name ��¼��֤����
	 * @param username
	 * @param pwd
	 * @return boolean
	 */
	public static boolean CheckAdmin(String username, String pwd) {
		DbSelect _s = new DbSelect();
		Admin _a = _s.AdminSelect(username);
		if (_a != null) {
			// System.out.println(_a.getPwd() + "   " + _a.getUsername());
			// System.out.println(Encode.MD5(pwd));
			if (Encode.MD5(pwd).equals(_a.getPwd())) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @name ע�����Ա�û�
	 * @param username
	 * @param pwd
	 * @param cid
	 *            ����Բ²�������Ȩ��������ʲô
	 * @return boolean
	 */
	public static boolean CreateAdmin(String username, String pwd, String cid) {
		if (Encode.MD5(cid).equals("b5223cc5cda3647409cede336d76aed8")) {
			DbInsert _i = new DbInsert();
			if (_i.AdminInsert(username, pwd)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		/*
		 * ��¼��֤ if(Admin.CheckAdmin("admin", "admin")) {
		 * System.out.println("��¼�ɹ�"); }else{ System.out.println("�û��������ڻ��������");
		 * }
		 */
		/*
		 * ע�����Ա�˻�
		 * 
		 * if(Admin.CreateAdmin("����", "��������","����Բ²�������Ȩ��������ʲô")) {
		 * System.out.println("ע�����Ա�ɹ�"); } else { System.out.println("ע��ʧ��"); }
		 */
	}

}
