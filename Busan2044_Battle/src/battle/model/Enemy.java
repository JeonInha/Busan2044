package battle.model;
import java.util.List;

public class Enemy {
	int id;
	String name;
	int life;
	int power;
	List<String> dropItem;
	int iniVar;
	String friendship_3_Script;
	String friendship_2_Script;
	String friendship_1_Script;
	String friendship_0_Script;

	public Enemy(int enemy_id, String enemy_name, int enemy_life, int enemy_power, List<String> enemy_Drop_Item, int iniVar,
			String friendship_3_Script, String friendship_2_Script, String friendship_1_Script,
			String friendship_0_Script) {
		super();
		id = enemy_id;
		name = enemy_name;
		life = enemy_life;
		power = enemy_power;
		dropItem = enemy_Drop_Item;
		this.iniVar = iniVar;
		this.friendship_3_Script = friendship_3_Script;
		this.friendship_2_Script = friendship_2_Script;
		this.friendship_1_Script = friendship_1_Script;
		this.friendship_0_Script = friendship_0_Script;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public List<String> getDropItem() {
		return dropItem;
	}

	public void setDropItem(List<String> dropItem) {
		this.dropItem = dropItem;
	}

	public int getIniVar() {
		return iniVar;
	}

	public void setIniVar(int enemy_IniVar) {
		iniVar = enemy_IniVar;
	}

	public String getFriendship_3_Script() {
		return friendship_3_Script;
	}

	public void setFriendship_3_Script(String friendship_3_Script) {
		this.friendship_3_Script = friendship_3_Script;
	}

	public String getFriendship_2_Script() {
		return friendship_2_Script;
	}

	public void setFriendship_2_Script(String friendship_2_Script) {
		this.friendship_2_Script = friendship_2_Script;
	}

	public String getFriendship_1_Script() {
		return friendship_1_Script;
	}

	public void setFriendship_1_Script(String friendship_1_Script) {
		this.friendship_1_Script = friendship_1_Script;
	}

	public String getFriendship_0_Script() {
		return friendship_0_Script;
	}

	public void setFriendship_0_Script(String friendship_0_Script) {
		this.friendship_0_Script = friendship_0_Script;
	}
}