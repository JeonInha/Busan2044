package battle.model;

public class Skill {
	int id;
	int type;
	String name;
	int aim;
	int power;
	String needItem;
	
	String criticalScript;
	String sucessScript;
	String failScript;
	String fumbleScript;
	
	public static final int SKILL_TYPE_ACTION = 1;
	public static final int SKILL_TYPE_HIT = 2;
	public static final int SKILL_TYPE_HEIST = 3;
	public static final int SKILL_TYPE_TRAP = 4;
	public static final int SKILL_TYPE_SPECIAL = 5;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAim() {
		return aim;
	}
	public void setAim(int aim) {
		this.aim = aim;
	}
	public String getNeedItem() {
		return needItem;
	}
	public void setNeedItem(String needItem) {
		this.needItem = needItem;
	}
	public String getCriticalScript() {
		return criticalScript;
	}
	public void setCriticalScript(String criticalScript) {
		this.criticalScript = criticalScript;
	}
	public String getSucessScript() {
		return sucessScript;
	}
	public void setSucessScript(String sucessScript) {
		this.sucessScript = sucessScript;
	}
	public String getFailScript() {
		return failScript;
	}
	public void setFailScript(String failScript) {
		this.failScript = failScript;
	}
	public String getFumbleScript() {
		return fumbleScript;
	}
	public void setFumbleScript(String fumbleScript) {
		this.fumbleScript = fumbleScript;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	
	public Skill(int id, int type, String name, int aim, int power, String needItem, String criticalScript, String sucessScript,
			String failScript, String fumbleScript) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.aim = aim;
		this.power = power;
		this.needItem = needItem;
		this.criticalScript = criticalScript;
		this.sucessScript = sucessScript;
		this.failScript = failScript;
		this.fumbleScript = fumbleScript;
	}
	
	public String getSkillScript(int i) {
		if (i==4) {
			return fumbleScript;
		} else if (i==1) {
			return criticalScript;
		} else if (i==2) {
			return sucessScript;
		} else {
			return failScript;
		}
	}
}
