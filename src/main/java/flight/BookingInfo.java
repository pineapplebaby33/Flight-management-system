package flight;

public class BookingInfo {

	private int fid;
	private int pid;
	private Flight f;
	private Passenger p;
	private String pname;
	private String pident;
	private int oid;
	private int seat;
	private DateTime oCreateDate;
	private String oStatus;

	BookingInfo(int fid, int pid, String pname, String pident, int oid,
			int seat, DateTime oCreateDate, String oStatus) {
		this.fid = fid;
		this.pid = pid;
		this.pname = pname;
		this.pident = pident;
		this.oid = oid;
		this.seat = seat;
		this.oCreateDate = oCreateDate;
		this.oStatus = oStatus;
	}

	BookingInfo(Flight f, Passenger p, String pname, String pident, int oid,
			int seat, DateTime oCreateDate, String oStatus) {
		this.f = f;
		this.p = p;
		this.pname = pname;
		this.pident = pident;
		this.oid = oid;
		this.seat = seat;
		this.oCreateDate = oCreateDate;
		this.oStatus = oStatus;
	}

	public int getFid() {
		return fid;
	}

	public int getPid() {
		return pid;
	}

	public String getPname() {
		return pname;
	}

	public String getPident() {
		return pident;
	}

	public int getOid() {
		return oid;
	}

	public int getSeat() {
		return seat;
	}

	public DateTime getoCreateDate() {
		return oCreateDate;
	}

	public String getoStatus() {
		return oStatus;
	}

	public Flight getF() {
		return this.f;
	}

	public Passenger getP() {
		return this.p;
	}

}
