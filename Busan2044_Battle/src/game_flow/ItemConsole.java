package game_flow;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kr.co.green.BusanUtil;

//import java.awt.Font;
//import java.awt.Image;
//import java.awt.Toolkit;
//import java.net.URL;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import javax.swing.ImageIcon;
//import javax.swing.JLabel;
//import javax.swing.ToolTipManager;
//
//import kr.co.green.BusanUtil;
//
////여기에는 dao에서의 기본 메소드 이용해서 만드는 복잡한 로직을 짜는거!

public class ItemConsole{
	private ItemDao dao;
	private UserInfo user;
	
	public ItemConsole(ItemDao dao, UserInfo user) {
		this.dao = dao;
		this.user = user;
	}

	public void getItem(int item_id) {
		try (Connection conn = BusanUtil.getConnection()) {
			int amount;
			if ((amount = dao.getItemHp(conn, item_id)) != 0) { // 아이템의 hp가 0이 아니면 체력회복아이템
				if (user.getHp() < user.FULL_HP) {
					int newHp = user.getHp() + amount;
					if (newHp > user.FULL_HP) {
						newHp = user.FULL_HP;
					}
					user.setHp(newHp);
				} else {
					inputInven(item_id);
				}
			} else if ((amount = dao.getItemMental(conn, item_id)) != 0) { // 아이템의 Mental이 0이 아니면 멘탈회복아이템
				if (user.getMental() < user.FULL_MENTAL) {
					int newMental = user.getMental() + amount;
					if (newMental > user.FULL_MENTAL) {
						newMental = user.FULL_MENTAL;
					}
					user.setMental(newMental);
				} else {
					inputInven(item_id);
				}
			} else { // 위의 두개가 이나면 잡템
				inputInven(item_id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 중복아이템을 주으면 해당 인벤토리 인덱스의 count를 ++하는 걸로 바꾸기~
		private void inputInven(int item_id) {
			int index = checkDuple(item_id);
			if (index != -1) { // 중복이면
				user.getInventory().get(index).setItem_count(user.getInventory().get(index).getItem_count() + 1);
			} else { // 중복이 아니면
				try (Connection conn = BusanUtil.getConnection()) {
					user.getInventory().add(dao.setItemInfo(conn, item_id));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		// 중복인지 체크
		// 중복이면 인벤토리에서의 index반환, 중복 아니라면 -1반환
		private int checkDuple(int item_id) {
			List<Item> inventory = user.getInventory();
			for (int i = 0; i < inventory.size(); i++) {
				if (inventory.get(i).getItem_id() == item_id) {
					return i;
				}
			}
			return -1;
		}
		
		//내구도 깍이는 로직
		   public boolean wpnUseDown(int item_id) { //true면 파괴 
		      boolean destroy = false;
		      List<Item> inventory = user.getInventory();
		      int index = -1;
		      for (int i = 0; i < inventory.size(); i++) {
		         if (inventory.get(i).getItem_id() == item_id) {
		            index = i;
		         }
		      }
		      int count = inventory.get(index).getItem_durability();
		      count--;
		      if (count == 0) {
		         inventory.remove(index);
		         destroy = true;
		      } else {
		         inventory.get(index).setItem_durability(count);
		      }
		      return destroy;
		   }
		   
}
