package game_flow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import kr.co.green.BusanUtil;
//다오에서는 DB와 교류하는 매우 기본적인것만 넣어주세요 delete나 update, select, getter setter같은것들,,
public class ItemDao {
	public ItemDao() {}
	//item_code를 지정해주면 확률게임 진행 후 item_id 하나를 뱉음
	public int trade_npcItem(Connection conn, String item_code) throws SQLException {
		String query = "select * from item_table where item_code = ? and item_nodeal = 0;"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Integer>rare1 = new ArrayList<>();
		List<Integer>rare2 = new ArrayList<>();
		List<Integer>rare3 = new ArrayList<>();
		List<Integer>rare4 = new ArrayList<>();
		List<Integer>rare5 = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, item_code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int rare = rs.getInt("item_rare");
				switch (rare) {
				case 0 : break;
				case 1 : rare1.add(rs.getInt("item_id")); break;
				case 2 : rare2.add(rs.getInt("item_id")); break;
				case 3 : rare3.add(rs.getInt("item_id")); break;
				case 4 : rare4.add(rs.getInt("item_id")); break;
				case 5 : rare5.add(rs.getInt("item_id")); break;
				}
			}
			//100퍼 중에서, 1:40퍼, 2:25퍼, 3:17퍼, 4:10퍼, 5:8퍼
			Random ran = new Random();
			int random = ran.nextInt(100) + 1;
			int item_id = 0;
			if (random <= 40) {
				//rare1에서 랜덤뽑기
				item_id = rare1.get(ran.nextInt(rare1.size()));
			} else if (random <= 65) {
				item_id = rare2.get(ran.nextInt(rare2.size()));
			} else if (random <= 82) {
				item_id = rare3.get(ran.nextInt(rare3.size()));
			} else if (random <= 92) {
				item_id = rare4.get(ran.nextInt(rare4.size()));
			} else {
				item_id = rare5.get(ran.nextInt(rare5.size()));
			} 
			return item_id;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	
	//아이템 획득시 진행될 로직 (1회용)
	//item_id에 맞게 durability, count가져오기
		public Item setItemInfo(Connection conn, int item_id) throws SQLException {
			String query = "select * from item_table where item_id = ?"; 
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Item item = new Item();
			try {
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, item_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					item.setItem_id(item_id);
					item.setItem_durability(rs.getInt("item_durability"));
					item.setItem_count(rs.getInt("item_count"));
				}
				return item;
			} finally {
				BusanUtil.closeRS(rs);
				BusanUtil.closeStmt(pstmt);
			}
		}

	
	//id로 item정보 다 뜯어오기
	public String getItemCode(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("item_code");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	public String getItemName(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("item_name");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	public String getItemMemo(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("item_memo");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	public String getItemImg(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("item_img");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	public String getItemBm(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("item_breakmessage");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	public int getItemRare(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("item_rare");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	public int getItemHp(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("item_hp");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	public int getItemMental(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("item_mental");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}

	public boolean getItemNodeal(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean result = true;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getBoolean("item_nodeal");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	
	public int getItemDurability(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("item_durability");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}
	public int getItemCount(Connection conn, int item_id) throws SQLException {
		String query = "select * from item_table where item_id = ?"; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, item_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("item_count");
			}
			return result;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
		}
	}

	
	
	
//	public static void main(String[] args) {
//		ItemDao mm = new ItemDao();
//		Character user1 = new Character();
//		List<Integer>storeInven = new ArrayList<>();
//		Connection conn = null;
//		try {
//			conn = BusanUtil.getConnection();
//			mm.trade_npcItem(conn, "rcv");
//			mm.trade_npcItem(conn, "rcv");
//			mm.trade_npcItem(conn, "rcv");
//			mm.trade_npcItem(conn, "wpn");
//			mm.trade_npcItem(conn, "evt");
//			
//			user1.getInventory().add(mm.inputInventory(conn, 3));
//			user1.getInventory().add(mm.inputInventory(conn, 21));
//			System.out.println(user1.getInventory().toString());
//			
//		} catch (Exception e) {
//			System.out.println("에러");
//		} finally {
//			BusanUtil.closeConn(conn);
//		}
//		
//				
//	}
}
