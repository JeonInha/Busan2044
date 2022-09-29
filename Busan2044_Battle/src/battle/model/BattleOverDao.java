package battle.model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.green.BusanUtil;

public class BattleOverDao {

	public String getFriendResult(int Enemy_id) {
		return findFinal_Scripts(Enemy_id, "FriendScript");
	}

	public String getRunAwayResult(int Enemy_id) {
		return findFinal_Scripts(Enemy_id, "RunAwayScript");
	}

	public String getWinResult(int Enemy_id) {
		return findFinal_Scripts(Enemy_id, "WinScript");
	}
	
	public String getNomalResult(int Enemy_id) {
		return findFinal_Scripts(Enemy_id, "NomalScript");
	}
	
	public String getLoseResult(int Enemy_id) {
		return findFinal_Scripts(Enemy_id, "LoseScript");
	}
	
	public String findFinal_Scripts(int Enemy_id, String find) {
		String script = null;
		try (Connection conn = BusanUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Busan.battleover");
				ResultSet rs = pstmt.executeQuery();) {
			while (rs.next()) {
				int getEnemy_id = rs.getInt("Enemy_id");
				if (Enemy_id == getEnemy_id) {
					script = rs.getString(find);
				} else {
					continue;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return script;
	}
}
