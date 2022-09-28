package battle;
import java.sql.*;
import java.util.*;

import kr.co.green.*;

public class Enemy_Dao {

	int Enemy_id;
	String Enemy_name;
	int Enemy_life;
	int Enemy_power;
	List<String> Enemy_Drop_Item;
	int Enemy_IniVar;
	String Friendship_3_Script;
	String Friendship_2_Script;
	String Friendship_1_Script;
	String Friendship_0_Script;
	
	// 리절트맵핑
	private Enemy resultMapping(ResultSet rs) throws SQLException {

		Enemy_Drop_Item = new ArrayList<>();
		
		Enemy_id = rs.getInt("Enemy_id");
		Enemy_name = rs.getString("Enemy_name");
		Enemy_life = rs.getInt("Life");
		Enemy_power = rs.getInt("Enemy_power");
		String Item = rs.getString("Item");

		if (Item != null) {
			String[] ItemList = Item.split("/");
			for (String s : ItemList) {
				Enemy_Drop_Item.add(s);
			}
		} else {
			Enemy_Drop_Item = null;
		}
		Enemy_IniVar = rs.getInt("IniVar");
		Friendship_3_Script = rs.getString("Friendship_3_Script");
		Friendship_2_Script = rs.getString("Friendship_2_Script");
		Friendship_1_Script = rs.getString("Friendship_1_Script");
		Friendship_0_Script = rs.getString("Friendship_0_Script");

		return new Enemy(Enemy_id, Enemy_name, Enemy_life, Enemy_power, Enemy_Drop_Item, Enemy_IniVar,
				Friendship_3_Script, Friendship_2_Script, Friendship_1_Script, Friendship_0_Script);
	}

	// 모든 Enemy의 ID를 가져오는 메소드
	List<Integer> readAllEnemyId() {
		List<Integer> enemy = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String quary = "select Enemy_id from busan.enemystatus";

		try {
			conn = BusanUtil.getConnection();
			pstmt = conn.prepareStatement(quary);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("Enemy_id");
				enemy.add(id);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
			BusanUtil.closeConn(conn);
		}
		return enemy;
	}
	
	// 아이디 선택하면 에너미 객체 반환하는 메소드
	Enemy selectEnemyUseID(int id) {
		Enemy enemy = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String quary = "select * from busan.enemystatus where Enemy_id =" + id + ";";

		try {
			conn = BusanUtil.getConnection();
			pstmt = conn.prepareStatement(quary);
			rs = pstmt.executeQuery();
			rs.next();
			enemy = resultMapping(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
			BusanUtil.closeConn(conn);
		}
		return enemy;
	}
	
	// 낮밤 : int로 받는데, 2가 밤>>
	Enemy ifDayOrNight(int dn, Enemy enemy) {
		if (dn==2) {
			enemy.setPower(enemy.getPower()+1);
			if (enemy.iniVar>0) {
				enemy.setIniVar(enemy.getIniVar()-1);
			}
		}
		return enemy;
	}
	
	// 모든 에너미 객체 반환하는 베소드
	public HashMap<Integer, Enemy> getEnemys() {

		HashMap<Integer, Enemy> EnemyMap = new HashMap<>();

		try (Connection conn = BusanUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM busan.enemystatus");
				ResultSet rs = pstmt.executeQuery();) {
			while (rs.next()) {
				Enemy enemy = resultMapping(rs);
				EnemyMap.put(enemy.getId(), enemy);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return EnemyMap;
	}
	
	// 랜덤 아이디 반환 메소드
	int randomEnemyID() {
		List<Integer> idList = readAllEnemyId();
		Collections.shuffle(idList);
		return idList.get(0);
	}
	
	// 이거 완성메소드 // 
	public Enemy selectRandomEnemy(int dn) {
		int id = randomEnemyID();
		Enemy enemy = selectEnemyUseID(id);
		enemy = ifDayOrNight(dn, enemy);
		return enemy;
	}

}
