package statistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import kr.co.green.BusanUtil;

// 개인 업적은 path 테이블에 붙여놨는데
// 통계 테이블은 중요한 선택지만 넣어두고 죽거나 엔딩나면 DB에 더해지도록 만들자!
// 둘 다 꺼내 쓸 수 있도록...
// 업적은 개인 배열로 꺼낼 수 있게 01(칭호'??')/03(칭호'??')

// TODO 무슨무슨 업적 있는지 한글로 꺼내는 메소드,


// TODO 개인 업적 테이블도 생성해줘야함!!!!!!!!!!!!!!
// 업적 id만 치면 무슨 업적인지 나오는 메소드...
public class AchievementsTest { // 업적 달성 // login_info에 숫자로 입력하고 꺼낼 때 업적 String 변환 메소드	
	// String을 String[]로 바꿔서 반환하는 메소드
	public String[] strSlashArr(String str) {
		String[] StingArr = str.split("/");
		
		return StingArr;
	}
	
	// 나눈 String[] 배열에서 원하는 값이 있는지 검색하는 메소드
	public boolean searchArr(String[] strArr, int num) {
		boolean search = false;
		
		for (int i = 0; i < strArr.length; i++) {
			if (Integer.valueOf(strArr[i]) == num) {
				search = true;
			}
		}
		
		return search;
	}
	
	public String[] saveArr(String save) { // savehere에 저장된 path를 ,만 빼고 [ ] 다 삭제해서 배열로 뱉어내는 메소드
		save = save.replace(" ", "").replace("[", "").replace("]", "");
		String[] saveArr = save.split(",");
		
		return saveArr;
	}
	
	// 가져온 value 스트링을 set으로 변환하고 int값 더해서 다시 arraylist로 정렬해서 String으로 뱉어내는 메소드 작성
	public String arraysValue(String value, int plus) {
		String[] valueArr = strSlashArr(value);
		Set<Integer> valueSet = new HashSet<>();
		
		for (int i = 0; i < valueArr.length; i++) {
			valueSet.add(Integer.valueOf(valueArr[i]));
		}
		
		valueSet.add(plus);
		List<Integer> valueList = new ArrayList<>(valueSet);
		Arrays.asList(valueList);
		
		value = "";
		
		for (int i = 0; i < valueSet.size(); i++) {
			if(i > 0) {
				value = value.concat("/" + valueList.get(i));
			} else {
				value = value.concat("" + valueList.get(i));
			}
		}
		
		return value;
	}
	
	public void addAchv(String id) { // 회차 끝나고 player_info 확인 후 업적 있으면 업적 테이블에 더해주고 이번 회차에 업적 추가되었는지 배열 반환
		String[] addAchv = null;
		try (Connection conn = BusanUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Busan.player_info");
				ResultSet rs = pstmt.executeQuery();) {
			
			while(rs.next()) {
				if(id.equals(rs.getString("id"))) { // 차라리 리스트 정렬을 해놨으니 배열 String이 포함되는지 확인하자
					// 동료 다 모았을 때 업적 추가
					String attainment = rs.getString("yourAttainment");
					if(attainment.contains("7907/7908/7909/7910/7911")) {
						addPlayerInfo(id, "yourAchv", 4);
					}
					
					// 모든 적 다 봤을 때 업적 추가
					if(attainment.contains("10/11/12/13/21/22/24/31/32")) {
						addPlayerInfo(id, "yourAchv", 5);
					}
					
					// 엔딩 다 봤을 때 업적 추가
					if("1/2/3/4/5/6/7/8".equals(rs.getString("yourEndings"))) { // TODO 되는지 확인하기
						addPlayerInfo(id, "yourAchv", 6);
					}
					
					if(rs.getInt("yourZombiDeath") == 99) {
						addPlayerInfo(id, "yourAchv", 3);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String[] achvStr(String achv) { // 개인이 본 엔딩 모두 꺼내는 메소드
		String[] achvArr = strSlashArr(achv);
		try (Connection conn = BusanUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Busan.statistics_ending");
				ResultSet rs = pstmt.executeQuery();){
			
			while (rs.next()) {
				for (int i = 0; i < achvArr.length; i++) {
					if(Integer.valueOf(achvArr[i]) == rs.getInt("ending_id")) {
						achvArr[i] = rs.getString("ending_name");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return achvArr;
	}
	
	public double countCrossrode(String id) { // 선택지 넣어서 통계
		int crHeadCount = 1;
		int crHeadCountSum = 0;
		int num1 = 0;
		int num2 = 0;
		int have = 0;
		
		double statis = 0.0;
		
		boolean havePath1 = false;
		boolean havePath2 = false;
		boolean saveMe = false;
		boolean notPInfo = false;
		
		String yourSave1 = "";
		String yourSave2 = "";
		
		try (Connection conn = BusanUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Busan.savehere");
				PreparedStatement pstmt2 = conn.prepareStatement("SELECT * FROM Busan.statistics_crossrode");
				PreparedStatement pstmt3 = conn.prepareStatement("UPDATE Busan.statistics_crossrode SET headCount = ? WHERE num = ?");
				PreparedStatement pstmt4 = conn.prepareStatement("INSERT INTO statistics_crossrode(storyPath1, storyPath2, headCount) VALUES (?, ?, 1)");
				// 업적 달성 조건과 업적 업로드
				PreparedStatement pstmt5 = conn.prepareStatement("SELECT * FROM Busan.player_info");
				PreparedStatement pstmt6 = conn.prepareStatement("INSERT INTO player_info(id) VALUES (?)");
				// TODO 메소드 나누기
				ResultSet rs = pstmt.executeQuery();
				ResultSet rs2 = pstmt2.executeQuery();
				ResultSet rs3 = pstmt5.executeQuery();) {
			
			while (rs.next()) {
				if (id.equals(rs.getString("userId"))) {
					yourSave1 = rs.getString("storyPath1");
					yourSave2 = rs.getString("storyPath2");
				}
			}
			
			String[] saveArr1 = saveArr(yourSave1);
			String[] saveArr2 = saveArr(yourSave2);

			while (rs3.next()) {
				if(id.equals(rs3.getString("id"))) {
					notPInfo = true;
				}
			}
			
			if(!notPInfo) {
				pstmt6.setString(1, id);
				pstmt6.executeUpdate();
			} else {
				for (int i = 0; i < saveArr1.length; i++) {
					if (7 <= Integer.valueOf(saveArr1[i]) && Integer.valueOf(saveArr1[i]) <= 11) { // 업적 조건의 동료 획득 추가
						addPlayerInfo(id, "yourAttainment", 7900 + Integer.valueOf(saveArr1[i]));
					}
					if (Integer.valueOf(saveArr1[i]) == 12) { // 개죽음 업적 추가
						addPlayerInfo(id, "yourAchv", 1);
					}
				}
			}
			
			yourSave1 = "";
			yourSave2 = "";
			
			for(int i = 0; i < saveArr1.length; i++) {
				if(i > 0) {
					yourSave1 = yourSave1.concat("/" + saveArr1[i]);
				} else {
					yourSave1 = yourSave1.concat(saveArr1[i]);
				}
			}
			
			for (int i = 0; i < saveArr2.length; i++) {
				if(i > 0) {
					yourSave2 = yourSave2.concat("/" + saveArr2[i]);
				} else {
					yourSave2 = yourSave2.concat(saveArr2[i]);
				}
			}
			
			while (rs2.next()) { // 튜토리얼도 못 깬 업적 체크할 때  다음 선택지가 나오기 전에 전투에서 죽었는지의 조건도 넣어야함
				if(yourSave1.equals(rs2.getString("storyPath1"))){
					System.out.println(rs2.getString("storyPath1"));
					havePath1 = true;
					num1 = rs2.getInt("num");
					
					crHeadCount = rs2.getInt("headCount") + 1;
				}
				
				if(yourSave2.equals(rs2.getString("storyPath2"))) {
					System.out.println(rs2.getString("storyPath2"));
					havePath2 = true;
					num2 = rs2.getInt("num");
				}
				
				crHeadCountSum += rs2.getInt("headCount");
			}
			
			if((havePath1 && havePath2) && num1 == num2) {
				have = 1;
				saveMe = true;
			}
			
			crHeadCountSum++;
			
			if(saveMe) {
				pstmt3.setInt(1, crHeadCount);
				pstmt3.setInt(2, num1);
				
				pstmt3.executeUpdate();
			} else {
				crHeadCount = 1;
				
				pstmt4.setString(1, yourSave1);
				pstmt4.setString(2, yourSave2);
				
				pstmt4.executeUpdate(); // 필요하면 다회차 루트 어디로 갔는지 볼 수 있게
			}
			
			System.out.println(saveMe);
			
			System.out.println(yourSave1);
			System.out.println(yourSave2);
			
			System.out.println(crHeadCountSum);
			System.out.println(crHeadCount);
			
			statis = Math.floor((crHeadCount + (double)crHeadCount / crHeadCountSum) * 10000);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return statis;
	}
	
	public List<String> thisRoundParty(String id) { // 이번회차 동료 누구와 함께했는지
		String[] path1 = null;
		List<String> party = null;
		
		int count = 0;
		
		try (Connection conn = BusanUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Busan.savehere");
				ResultSet rs = pstmt.executeQuery();) {
			
			while (rs.next()) {
				if(id.equals(rs.getString("userId"))) {
					path1 = saveArr(rs.getString("storyPath1"));
					
					if(searchArr(path1, 7)) {
						party.add("장주먹 할아버지");
					}
					
					if(searchArr(path1, 8)) {
						party.add("장주먹 할아버지");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return party;
	}
	
	// 엔딩 시 자동 업로드가 아닌 직접 입력해야하는 조건, 엔딩, 승리횟수, 업적 추가할 때 쓰는 메소드
	// 개인 DB에 업적 추가해서 중복은 없애고(set) 순서대로 정렬하는(arraylist) 메소드
	public void addPlayerInfo(String id, String column, int plus) { //TODO 승리횟수 추가함!
		String columnQuery = "UPDATE Busan.player_info SET "+ column +" = ? WHERE id = ?";
		
		// (id, "yourZombiDeath", 죽은 횟수1) {	// 좀비에게 죽었을 때
		// (id, "yourAttainment", 10 -> 적의 id 번호); // 얘는 적 마주칠때
		// (id, "yourAch", 8) -> 멧돼지 긁었을 때  // 효자손 스킬 발동 직후
		
		String value = null;
		int victory = 0;
		
		try (Connection conn = BusanUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Busan.player_info");
				PreparedStatement pstmt2 = conn.prepareStatement(columnQuery);
				ResultSet rs = pstmt.executeQuery();) {
			
			boolean arrColumn = column.equals("yourAttainment") || column.equals("yourEndings") || column.equals("yourAchv");
			
			while (rs.next()) {
				if(id.equals(rs.getString("id"))) {
					if (arrColumn) {
						value = rs.getString(column);
						
						if(value.equals("") || value == null) { // 널값일 때 그냥 plus
							pstmt2.setString(1, String.valueOf(plus));
						} else {
							pstmt2.setString(1, arraysValue(value, plus));
						}
						
						pstmt2.setString(2, id);
						pstmt2.executeUpdate();
					} else if (column.equals("yourZombiDeath")) { // 전투 승리 카운트 메소드
						victory = rs.getInt(column);
						if(victory == 0) {
							pstmt2.setInt(1, plus);
						} else {
							pstmt2.setInt(1, victory + plus);
						}
						
						pstmt2.setString(2, id);
						pstmt2.executeUpdate();
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public String searchAchv(String AchvId) { // 업적 검색해서 String으로 반환하는 메소드
		String achv = "";
		
		try (Connection conn = BusanUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Busan.login_choice");
				ResultSet rs = pstmt.executeQuery();) {
			
			while (rs.next()) {
				if(1 == 1) {
					// 개발자는 코딩코딩 0
					// 튜토리얼도 못 깬 1
					// 좀비와의 첫 만남을 한 2
					// 좀비와 몇번 전투해서 많이 죽으면? -> 좀비의 동료가 된
					// 모든 동료를 모은 4
					// 모든 적을 마주친?? 
					// 유니버스에 통달한
					// npc에게 죽은 7
					// 어딘가의 효자
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return achv;
	}
	
	public static void main(String[] args) {
		AchievementsTest ac = new AchievementsTest();
//		double result = ac.countCrossrode("magic22x");
		
//		ac.addPlayerInfo("magic22x", "yourEndings", 5);
		ac.addAchv("magic22x");

//		System.out.println(result + "\n당신과 같은 선택을 한 사람은 " + (int)(result / 10000) 
//				+ "명이고 전체의 " + result % 10000 / 100 + "%가 이 선택을 했습니다.");
	}
}