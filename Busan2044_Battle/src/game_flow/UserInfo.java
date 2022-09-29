package game_flow;

import java.util.ArrayList;
import java.util.List;
 //추가할것 맘대로 넣으셈요
public class UserInfo {
	public final static int FULL_HP = 5; 
	public final static int FULL_MENTAL = 5; 
	private int hp = 5;
	private int mental = 5;
	private int bleed = 3;
	private int blue = 0;
	private List<Integer> party = new ArrayList<>();
	private int npc_likability;
	private List<Item> inventory = new ArrayList<>();
	//item_id에 따라서 durability, count를 개별로 관리할 수 있게 정보 처리 -> 저장필요한 정보 inventory: list<Item>
//	private map<진행상황, 선택사항>
	
	public void mentalRefresh(String updown, int num) {
		if (updown.equals("up")) {
			this.mental += num;
			if (mental > 10) {
				this.mental = 10;
			}
		} else {
			this.mental -= num;
			if (mental <= 0) {
				this.mental = 0;
			}
		}
	}
	
	
	public void HPRefresh(String updown, int num) {
		if (updown.equals("up")) {
			this.hp += num;
			if (hp > 5) {
				this.hp = 5;
			}
		} else {
			this.hp -= num;
			if (hp <= 0) {
				this.hp = 0;
			}
		}
	}
	
	public int getBleed() {
		return bleed;
	}

	public void setBleed(int bleed) {
		this.bleed = bleed;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getMental() {
		return mental;
	}
	public void setMental(int mental) {
		this.mental = mental;
	}
	public List<Integer> getParty() {
		return party;
	}
	public void setParty(List<Integer> party) {
		this.party = party;
	}
	public int getNpc_likability() {
		return npc_likability;
	}
	public void setNpc_likability(int npc_likability) {
		this.npc_likability = npc_likability;
	}
	public List<Item> getInventory() {
		return inventory;
	}
	public void setInventory(List<game_flow.Item> inven) {
		this.inventory = inven;
	}
}
 //사용예) SaveInfo user1 = new SaveInfo();
