package battle;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.green.BusanUtil;

public class SkillImpl {
	
	private Skill resultMapping(ResultSet rs) throws SQLException {
		int id = rs.getInt("Skill_id");
		int type = rs.getInt("Skill_type");
		String name = rs.getString("Skill_name");
		int power = rs.getInt("Skill_power");
		int aim = rs.getInt("Skill_aim");
		String needItem = rs.getString("Skill_needItem");
		
		String criticalScript = rs.getString("Skill_criticalScript");
		String sucessScript= rs.getString("Skill_sucessScript");
		String failScript= rs.getString("Skill_failScript");
		String fumbleScript= rs.getString("Skill_fumbleScript");
		
		return new Skill(id, type, name, aim, power, needItem, criticalScript, sucessScript, failScript, fumbleScript);
	}

	// 전체 스킬 읽어오기
	List<Skill> readSkill() {
		String query = "SELECT * FROM skill";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Skill> skill = new ArrayList<>();
		
		try {
			conn = BusanUtil.getConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				skill.add(resultMapping(rs));
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
			BusanUtil.closeConn(conn);
		}
		return skill;
	}
	
	public String stringItem(List<game_flow.Item> list) {
		List<Integer> itemIDList = new ArrayList<>();
		for (game_flow.Item i : list) {
			int a = i.getItem_id();
			itemIDList.add(a);
		}
		String itemInt = "";
		for (int i=0; i<list.size(); i++) {
			if (i==0) {
				itemInt = itemInt.concat(String.valueOf(itemIDList.get(i)));
			} else {
				itemInt = itemInt.concat(", ");
				itemInt = itemInt.concat(String.valueOf(itemIDList.get(i)));
			}
		}
		return itemInt;
	}
	
	//////
	// 액션 스킬
	   public List<Skill> getActionSkillList(int enemyId, List<game_flow.Item> list) {
	      List<Skill> attackSkill = new ArrayList<>();
	      
	      String itemInt = stringItem(list);
	      
	      String skill = "SELECT * FROM busan.skill WHERE (floor(skill_id / 100 % 10) = " + (int)(enemyId / 10)
	            + " or floor(skill_id / 100 % 10) = 0)"
	            + " AND floor(skill_id / 1000) in (1, 4, 5)" 
	            + " AND (skill_needItem is null or skill_needItem in ( " + itemInt
	            + " ));";
	      
	      try (Connection conn = BusanUtil.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(skill);
	            ResultSet rs = pstmt.executeQuery();) {
	         
	         while (rs.next()) {
	            attackSkill.add(resultMapping(rs));
	         }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }
	      
	      return attackSkill;
	   }
	   
	   // 어택
	   public List<Skill> getAttckSkillList(int enemyId, List<game_flow.Item> item) { // 공격 스킬 꺼내는 메소드
	      List<Skill> attackSkill = new ArrayList<>();
	      
	      String itemInt = stringItem(item);
	      
	      String skill = "SELECT * FROM busan.skill WHERE (floor(skill_id / 100 % 10) = " + (int)(enemyId / 10)
	            + " or floor(skill_id / 100 % 10) = 0)"
	            + " AND floor(skill_id / 1000) = 2" 
	            + " AND (skill_needItem is null or skill_needItem in ( " + itemInt
	            + " ));";
	      
	      try (Connection conn = BusanUtil.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(skill);
	            ResultSet rs = pstmt.executeQuery();) {
	         
	         while (rs.next()) {
	            attackSkill.add(resultMapping(rs));
	         }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }
	      
	      return attackSkill;
	   }
	   
	   // 기습
	   public List<Skill> getHeistSkillList(int enemyId, List<game_flow.Item> list) { // 기습
	      List<Skill> attackSkill = new ArrayList<>();
	      
	      String itemInt = stringItem(list);
	      
	      String skill = "SELECT * FROM busan.skill WHERE (floor(skill_id / 100 % 10) = " + (int)(enemyId / 10)
	            + " or floor(skill_id / 100 % 10) = 0)"
	            + " AND floor(skill_id / 1000) = 3"
	            + " AND (skill_needItem is null or skill_needItem in ( " + itemInt
	            + " ));";
	      
	      try (Connection conn = BusanUtil.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(skill);
	            ResultSet rs = pstmt.executeQuery();) {
	         
	         while (rs.next()) {
	            attackSkill.add(resultMapping(rs));
	         }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }
	      
	      return attackSkill;
	   }
}
