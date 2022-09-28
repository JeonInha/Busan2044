package savehere;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kr.co.green.BusanUtil;


public class SaveHereDaoImpl implements SaveHereDao {
	
	private SaveHere resultMapping(ResultSet rs) throws SQLException {
		String userId = rs.getString("userId");
		String storyPath1 = rs.getString("storyPath1");
		String storyPath2 = rs.getString("storyPath2");
		String dayNight = rs.getString("dayNight");
		String date = rs.getString("date");
		String userStat = rs.getString("userStat");
		String party = rs.getString("party");
		String item = rs.getString("item");
		

		return new SaveHere(userId, storyPath1, storyPath2, dayNight, date, userStat, party, item);
	}

	
	@Override
	public List<SaveHere> read() throws SQLException {
	String query = "SELECT * FROM savehere";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<SaveHere> list = new ArrayList<>();
		try {
			conn = BusanUtil.getConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(resultMapping(rs));
			}
			
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
			BusanUtil.closeConn(conn);
		}
		return list;
	}

	@Override
	public SaveHere read(String userId) throws SQLException {
		String query = "SELECT * FROM savehere WHERE userId = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SaveHere user = null;
		
		try {
			conn = BusanUtil.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
			  user = resultMapping(rs);
			}
			return user;
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
			BusanUtil.closeConn(conn);
		}
	}
		
	
	@Override
	public String readEnd(String ending_Name) throws SQLException {
		String query = "SELECT ending_text FROM statistics_ending WHERE ending_Name LIKE ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = "";
		
		try {
			conn = BusanUtil.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, ending_Name + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
			  result +=	rs.getString("ending_text");
			}
			
		} finally {
			BusanUtil.closeRS(rs);
			BusanUtil.closeStmt(pstmt);
			BusanUtil.closeConn(conn);
		}
		return result;
	}


	public static void main(String[] args) {
		SaveHereDaoImpl s = new SaveHereDaoImpl();
		try {
			System.out.println(s.read("magic22x"));
			System.out.println(s.readEnd("nomal"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
