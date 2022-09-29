package battle.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game_flow.Item;

public class SkillImpl {

	List<Skill> skillList = new ArrayList<>();

	private void MakeSkillList() {
		JSONParser parser = new JSONParser();
		Reader reader;

		try {
			reader = new FileReader("D:\\dlsgk\\workspace_7month\\Busan2044_Battle\\Sourse\\data\\skill.json");
			JSONArray jsonArr = (JSONArray) parser.parse(reader);
			System.out.println(jsonArr.size());

			// JSON Array는 jsonObj의 배열::

			for (int i = 0; i < jsonArr.size(); i++) {

				JSONObject a = (JSONObject) jsonArr.get(i);

				int id = ((Long) a.get("skill_id")).intValue();
				int type = ((Long) a.get("skill_type")).intValue();
				String name = (String) a.get("skill_name");
				int power = ((Long) a.get("skill_power")).intValue();
				int aim = ((Long) a.get("skill_aim")).intValue();
				String needItem = String.valueOf(a.get("skill_needItem"));

				String criticalScript = (String) a.get("skill_criticalScript");
				String sucessScript = (String) a.get("skill_sucessScript");
				String failScript = (String) a.get("skill_failScript");
				String fumbleScript = (String) a.get("skill_fumbleScript");

				skillList.add(new Skill(id, type, name, aim, power, needItem, criticalScript, sucessScript, failScript,
						fumbleScript));
			}

		} catch (FileNotFoundException e) {
			System.out.println("파일경로 잘못됨");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private Skill resultMapping(ResultSet rs) throws SQLException {
		int id = rs.getInt("Skill_id");
		int type = rs.getInt("Skill_type");
		String name = rs.getString("Skill_name");
		int power = rs.getInt("Skill_power");
		int aim = rs.getInt("Skill_aim");
		String needItem = rs.getString("Skill_needItem");

		String criticalScript = rs.getString("Skill_criticalScript");
		String sucessScript = rs.getString("Skill_sucessScript");
		String failScript = rs.getString("Skill_failScript");
		String fumbleScript = rs.getString("Skill_fumbleScript");

		return new Skill(id, type, name, aim, power, needItem, criticalScript, sucessScript, failScript, fumbleScript);
	}

	// 전체 스킬 읽어오기
	public List<Skill> readSkill() {
		return skillList;
	}

	public String stringItem(List<Item> list) {
		List<Integer> itemIDList = new ArrayList<>();
		for (Item i : list) {
			int a = i.getItem_id();
			itemIDList.add(a);
		}
		String itemInt = "";
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
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
	public List<Skill> getActionSkillList(int enemyId, List<Item> list) {
		List<Skill> attackSkill = new ArrayList<>();

		String itemInt = stringItem(list);

		String skill = "SELECT * FROM busan.skill WHERE (floor(skill_id / 100 % 10) = " + (int) (enemyId / 10)
				+ " or floor(skill_id / 100 % 10) = 0)" + " AND floor(skill_id / 1000) in (1, 4, 5)"
				+ " AND (skill_needItem is null or skill_needItem in ( " + itemInt + " ));";

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
	public List<Skill> getAttckSkillList(int enemyId, List<Item> item) { // 공격 스킬 꺼내는 메소드
		List<Skill> attackSkill = new ArrayList<>();

		String itemInt = stringItem(item);

		String skill = "SELECT * FROM busan.skill WHERE (floor(skill_id / 100 % 10) = " + (int) (enemyId / 10)
				+ " or floor(skill_id / 100 % 10) = 0)" + " AND floor(skill_id / 1000) = 2"
				+ " AND (skill_needItem is null or skill_needItem in ( " + itemInt + " ));";

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
	public List<Skill> getHeistSkillList(int enemyId, List<Item> list) { // 기습
		List<Skill> attackSkill = new ArrayList<>();

		String itemInt = stringItem(list);

		String skill = "SELECT * FROM busan.skill WHERE (floor(skill_id / 100 % 10) = " + (int) (enemyId / 10)
				+ " or floor(skill_id / 100 % 10) = 0)" + " AND floor(skill_id / 1000) = 3"
				+ " AND (skill_needItem is null or skill_needItem in ( " + itemInt + " ));";

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
