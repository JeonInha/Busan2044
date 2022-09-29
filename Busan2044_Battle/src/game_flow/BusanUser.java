package game_flow;

public class BusanUser {
	private int userNum;
	private String id;
	private String password;
	private String name;
	private String birthDay;
	private String phoneNum;
	
	public BusanUser(int userNum, String id, String password) {
		super();
		this.userNum = userNum;
		this.id = id;
		this.password = password;
	}

	public BusanUser(String id, String password) {
		super();
		this.id = id;
		this.password = password;
	}

	public BusanUser(String id, String password, String name, String birthDay, String phoneNum) {
		super();
		this.id = id;
		this.password = password;
		this.name = name;
		this.birthDay = birthDay;
		this.phoneNum = phoneNum;
	}

	public BusanUser(int userNum, String id, String password, String name, String birthDay, String phoneNum) {
		super();
		this.userNum = userNum;
		this.id = id;
		this.password = password;
		this.name = name;
		this.birthDay = birthDay;
		this.phoneNum = phoneNum;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof BusanUser))
			return false;
		BusanUser other = (BusanUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BusanUser [userNum=" + userNum + ", id=" + id + ", password=" + password + ", name=" + name
				+ ", birthDay=" + birthDay + ", phoneNum=" + phoneNum + "]";
	}

	

	
}
