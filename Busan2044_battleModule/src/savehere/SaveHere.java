package savehere;

public class SaveHere {
	private int num;
	private String userId;
	private String storyPath1, storyPath2, dayNight, date, userStat, party, item;
	
	public SaveHere(String userId, String storyPath, String storyPath2, String dayNight, String date,
			String userStat, String party, String item) {
		super();
		
		this.userId = userId;
		this.storyPath1 = storyPath;
		this.storyPath2 = storyPath2;
		this.dayNight = dayNight;
		this.date = date;
		this.userStat = userStat;
		this.party = party;
		this.item = item;
	}

	public SaveHere(String userId, String stroyPath) {
		super();
		this.userId = userId;
		this.storyPath1 = stroyPath;
	}
	
	

	public SaveHere(String userId, String stroyPath, String storyPath2) {
		super();
		this.userId = userId;
		this.storyPath1 = stroyPath;
		this.storyPath2 = storyPath2;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStoryPath1() {
		return storyPath1;
	}

	public void setStoryPath1(String stroyPath) {
		this.storyPath1 = stroyPath;
	}


	

	public String getStoryPath2() {
		return storyPath2;
	}

	public void setStoryPath2(String storyPath2) {
		this.storyPath2 = storyPath2;
	}

	public String getDayNight() {
		return dayNight;
	}

	public void setDayNight(String dayNight) {
		this.dayNight = dayNight;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserStat() {
		return userStat;
	}

	public void setUserStat(String userStat) {
		this.userStat = userStat;
	}

	public String getParty() {
		return party;
	}

	public void setParty(String party) {
		this.party = party;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "SaveHere [num=" + num + ", userId=" + userId + ", stroyPath=" + storyPath1 + ", storyPath2=" + storyPath2
				+ ", dayNight=" + dayNight + ", date=" + date + ", userStat=" + userStat + ", party=" + party
				+ ", item=" + item + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SaveHere))
			return false;
		SaveHere other = (SaveHere) obj;
		if (storyPath2 == null) {
			if (other.storyPath2 != null)
				return false;
		} else if (!storyPath2.equals(other.storyPath2))
			return false;
		if (storyPath1 == null) {
			if (other.storyPath1 != null)
				return false;
		} else if (!storyPath1.equals(other.storyPath1))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
		
	
	
}
