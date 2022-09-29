package battle.algo;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.border.*;

import battle.model.BattleOverDao;
import battle.model.Enemy;
import battle.model.Enemy_Dao;
import battle.model.Skill;
import battle.model.SkillImpl;
import de.*;
import game_flow.ItemConsole;
import game_flow.ItemDao;
import game_flow.UserInfo;
import gogo0801.*;
import kr.co.green.BusanUtil;
import statistics.AchievementsTest;

class TSPanel extends JPanel {
	TSPanel() {
		this.setOpaque(false);
	}
}

class BattleTextArea extends JTextArea {
	BattleTextArea() {
		// 배경색
		this.setBackground(Design.LSkyblue);

		// Border, padding
		EmptyBorder padding = new EmptyBorder(20, 20, 20, 20);
		LineBorder borderLine = new LineBorder(Design.SBlack, 2);
		CompoundBorder comBorder = new CompoundBorder(borderLine, padding);
		this.setBorder(comBorder);

		// 폰트
		this.setFont(Design.MSFont);

		// 텍스트 편집가능여부
		this.setEditable(false);
	}
}

public class Battle_App extends JPanel {
	UserInfo useruser = new UserInfo();
	JPanel pnl = new JPanel();
	JPanel enemyBox = new TSPanel();
	JPanel middleBox = new TSPanel();
	JPanel userBox = new TSPanel();
	JPanel footerBox = new TSPanel();
	JButton endBtn = new ColorButton("전투 종료");

	boolean isGetAway = false;
	boolean isHappy = false;
	boolean isDead = false;
	int phaseCountNum;

	//////////////////////////////////////////
	// img
	Toolkit kit = Toolkit.getDefaultToolkit();
	URL we = Battle_App.class.getClassLoader().getResource("Enemy/we.png");
	URL heart_empty = Battle_App.class.getClassLoader().getResource("Enemy/heart_empty.png");
	URL heart_full = Battle_App.class.getClassLoader().getResource("Enemy/heart_full.png");
	URL middle_L = Battle_App.class.getClassLoader().getResource("Enemy/middle_L.png");
	URL middle_R = Battle_App.class.getClassLoader().getResource("Enemy/middle_R.png");

	ImageIcon weImg = new ImageIcon(kit.getImage(we));
	ImageIcon heart_emptyImg = new ImageIcon(kit.getImage(heart_empty));
	ImageIcon heart_fullImg = new ImageIcon(kit.getImage(heart_full));
	ImageIcon middle_LImg = new ImageIcon(kit.getImage(middle_L));
	ImageIcon middle_RImg = new ImageIcon(kit.getImage(middle_R));
	///////////////////////////////////////////

	List<Skill> useSkillList = new ArrayList<>();
	Map<Integer, JButton> useSkillBtnList = new HashMap<>();

	Map<String, Integer> getBattleEndScript(Enemy enemy, UserInfo user) {
		String result;
		Integer a = 0;
		Random random = new Random();
		BattleOverDao bod = new BattleOverDao();
		Map<String, Integer> map = new HashMap<>();

		if (isGetAway) {
			result = bod.getRunAwayResult(enemy.getId());

			// 상대 죽이기 // win
		} else if (isDead) {
			result = bod.getWinResult(enemy.getId());
			if (enemy.getDropItem() != null) {
				int indx = random.nextInt(enemy.getDropItem().size());
				if (enemy.getDropItem().size() > 0) {

					ItemDao id = new ItemDao();
					String itemName = "";
					Connection conn = null;

					try {
						conn = BusanUtil.getConnection();
						itemName = itemName
								.concat(id.getItemName(conn, Integer.valueOf(enemy.getDropItem().get(indx))));
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						BusanUtil.closeConn(conn);
					}

					result = result.concat("\n떨어트린 아이템: " + itemName);
					a = Integer.valueOf(enemy.getDropItem().get(indx));
				} else {
					result = result.concat("\n로직실수 여기지롱~~~~~~~");
				}
			} else {
				result = result.concat("\n적의 시체를 뒤졌지만 쓸만한 것은 얻을 수 없었다.");
			}

			// 상대 협상 굿엔딩 //
		} else if (isHappy || enemy.getIniVar() == 3) {
			result = bod.getFriendResult(enemy.getId());
			if (enemy.getDropItem() != null) {
				int indx = random.nextInt(enemy.getDropItem().size());
				if (enemy.getDropItem().size() > 0) {

					ItemDao id = new ItemDao();
					String itemName = "";
					Connection conn = null;

					try {
						conn = BusanUtil.getConnection();
						itemName = itemName
								.concat(id.getItemName(conn, Integer.valueOf(enemy.getDropItem().get(indx))));
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						BusanUtil.closeConn(conn);
					}

					result = result.concat("\n떨어트린 아이템: " + itemName);

					a = Integer.valueOf(enemy.getDropItem().get(indx));

				} else {
					result = result.concat("\n로직실수 여기지롱~~~~~~~");
				}
			} else {
				result = result.concat("\n적이 떠난 자리에는 먼지만 날렸다.");
			}
			// 우리가 죽는 엔딩
		} else if (enemy.getIniVar() <= 0 || user.getHp() <= 0) {
			result = bod.getLoseResult(enemy.getId());

			// 아무것도 없는 엔딩
		} else {
			result = bod.getNomalResult(enemy.getId());
		}

		map.put(result, a);

		return map;
	}

	public void setUseSkillBtnList(List<Skill> useSkillList) {
		useSkillBtnList = new HashMap<>();
		Battle_Algo ba = new Battle_Algo();

		for (int i = 0; i < useSkillList.size(); i++) {
			Skill skill = useSkillList.get(i);
			JButton btn = new SkillBtn(skill.getName() + "                ");
			btn.setToolTipText("<html><p>" + "명중률: " + ba.getSkillAim(skill) + "<br>공격력: " + ba.getSkillPOW(skill)
					+ "<br>필요 아이템: " + ba.getSkillNeed(skill) + "</html></p>");
			ToolTipManager m = ToolTipManager.sharedInstance(); // 툴팁 여는 시간 조정 위해 객체 생성
			m.setInitialDelay(0); // 초기 툴팁 출력 지연시간 0초 설정
			useSkillBtnList.put(skill.getId(), btn);
		}

	}

	public void addBtnToPnl(Map<Integer, JButton> btnList, JPanel pnl) {
		for (Integer key : btnList.keySet()) {
			pnl.add(btnList.get(key));
		}
	}

	public JButton getEndBtn() {
		return endBtn;
	}

	public UserInfo getBtEndUserStatus() {
		return useruser;
	}

	public JPanel getPnl() {
		return pnl;
	}

	public void setPnl(Enemy enemy, UserInfo user, String id) {
		String userID = id;
		// { // 좀비에게 죽었을 때
		useruser = user;
		pnl.setBackground(Design.LWhite);
		Battle_Algo ba = new Battle_Algo();
		SkillImpl si = new SkillImpl();
//		AchievementsTest at = new AchievementsTest();
		int enemyFullLife = enemy.getLife();

		phaseCountNum = 1;

//		at.addPlayerInfo(userID, "yourAttainment", enemy.getId());

		BoxLayout pnlLayout = new BoxLayout(pnl, BoxLayout.Y_AXIS);
		pnl.setLayout(pnlLayout);

		JPanel enemyBox = new TSPanel();
		JPanel middleBox = new TSPanel();
		JPanel userBox = new TSPanel();
		JPanel footerBox = new TSPanel();

		pnl.add(enemyBox);
		pnl.add(middleBox);
		pnl.add(userBox);
		pnl.add(footerBox);

		/////////////////// enemyBox
		Toolkit kit = Toolkit.getDefaultToolkit();
		String idURL = String.valueOf(enemy.getId());
		URL eee = Battle_App.class.getClassLoader().getResource("Enemy/" + idURL + ".png");
		ImageIcon enemyImg = new ImageIcon(kit.getImage(eee));

		JLabel enemyImgLB = new JLabel(enemyImg);
		enemyBox.add(enemyImgLB);

		enemyImgLB.setToolTipText("<html><p>" + "몬스터 이름: " + enemy.getName() + "<br>몬스터의 상태: "
				+ ba.getEnemyIniVar(enemy) + "<br>" + ba.getEnemyPOW(enemy) + "</p></html>");
		ToolTipManager m = ToolTipManager.sharedInstance(); // 툴팁 여는 시간 조정 위해 객체 생성
		m.setInitialDelay(0); // 초기 툴팁 출력 지연시간 0초 설정

		Map<Integer, JLabel> enemyLifeImg = new HashMap<>();
		for (int i = 0; i < enemyFullLife; i++) {
			JLabel a = new JLabel(heart_fullImg);
			enemyLifeImg.put(i, a);
			enemyBox.add(a);
		}

		////////////////////// middleBox
		JLabel phaseCount = new JLabel(phaseCountNum + "턴    ");
		JLabel enemyStatus = new JLabel(ba.enemyStatus(enemy));

		enemyStatus.setToolTipText("몬스터의 적개심. 공격을 할 수록 높아집니다. 전투가 끝날 때 일정 이상 높으면 바로 죽어요. 주의!");
		ToolTipManager m2 = ToolTipManager.sharedInstance(); // 툴팁 여는 시간 조정 위해 객체 생성
		m2.setInitialDelay(0); // 초기 툴팁 출력 지연시간 0초 설정

		phaseCount.setIcon(middle_LImg);
		phaseCount.setFont(Design.MSFont);
		phaseCount.setForeground(Design.SBlack);
		phaseCount.setHorizontalTextPosition(JLabel.CENTER);

		enemyStatus.setIcon(middle_RImg);
		enemyStatus.setFont(Design.MSFont);
		enemyStatus.setForeground(Design.SBlack);
		enemyStatus.setHorizontalTextPosition(JLabel.CENTER);

		middleBox.add(phaseCount);
		middleBox.add(enemyStatus);

		////////////////////// userBox
		Map<Integer, JLabel> userLifeImg = new HashMap<>();
		for (int i = 0; i < useruser.getHp(); i++) {
			JLabel a = new JLabel(heart_fullImg);
			userLifeImg.put(i, a);
			userBox.add(a);
		}
		for (int i = useruser.getHp(); i < UserInfo.FULL_HP; i++) {
			JLabel a = new JLabel(heart_emptyImg);
			userLifeImg.put(i, a);
			userBox.add(a);
		}

		JLabel userImg = new JLabel(weImg);
		userBox.add(userImg);

		///////////////////// footerBox

		// 행동 선택 / 버튼 패널 (A
		JPanel chooseAct = new TSPanel();
		JButton attackBtn = new ColorButton("공격", Design.SRed, Color.white);
		JButton actionBtn = new ColorButton("행동", Design.SBlue, Color.white);
		JButton runAwayBtn = new ColorButton("도망", Design.SGreen, Color.white);
		attackBtn.setPreferredSize(new Dimension(200, 200));
		actionBtn.setPreferredSize(new Dimension(200, 200));
		runAwayBtn.setPreferredSize(new Dimension(200, 200));

		chooseAct.add(attackBtn);
		chooseAct.add(actionBtn);
		chooseAct.add(runAwayBtn);

		// 스킬 선택 / 스킬 패널(B
		JPanel chooseSkill = new TSPanel();

		JPanel skillBox = new TSPanel();
		BoxLayout skillBoxLayout = new BoxLayout(skillBox, BoxLayout.Y_AXIS);
		skillBox.setLayout(skillBoxLayout);

		JButton backBtn = new ColorButton("뒤로가기");
		chooseSkill.add(skillBox);
		chooseSkill.add(backBtn);

		// 결과 출력 / 스킬 결과 패널 (C
		JPanel skillResult = new TSPanel();
		BoxLayout skillResultLayout = new BoxLayout(skillResult, BoxLayout.Y_AXIS);
		skillResult.setLayout(skillResultLayout);

//		JLabel focusUser = new JLabel("::우리의 행동::");
		JTextArea printUserResult = new BattleTextArea();
		JButton nextBtnC = new ColorButton("다음");

//		skillResult.add(focusUser);
		skillResult.add(printUserResult);
		skillResult.add(nextBtnC);

		// 결과 출력 / 적 행동 결과 패널 (D
		JPanel enemyResult = new TSPanel();
		BoxLayout enemyResultLayout = new BoxLayout(enemyResult, BoxLayout.Y_AXIS);
		enemyResult.setLayout(enemyResultLayout);

//		JLabel focusEnemy = new JLabel("::적의 행동::");
		JTextArea printEnemyResult = new BattleTextArea();
		JButton nextBtnD = new ColorButton("다음");

//		enemyResult.add(focusEnemy);
		enemyResult.add(printEnemyResult);
		enemyResult.add(nextBtnD);

		// 결과 출력 / 전투 결과 패널 (E
		JPanel battleResult = new TSPanel();
		BoxLayout battleResultLayout = new BoxLayout(battleResult, BoxLayout.Y_AXIS);
		JTextArea battleRPrint = new BattleTextArea();
		battleResult.setLayout(battleResultLayout);
		battleResult.add(battleRPrint);
		battleResult.add(endBtn);

		// footer cardLayout
		CardLayout card = new CardLayout();
		footerBox.setLayout(card);
		footerBox.add(chooseAct, "A");
		footerBox.add(chooseSkill, "B");
		footerBox.add(skillResult, "C");
		footerBox.add(enemyResult, "D");
		footerBox.add(battleResult, "E");
		card.show(footerBox, "A");

		////////////////////// ActionListener

		/////////////////// 스킬 동작 버튼
		ActionListener skillact = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton useSkillBtn = (JButton) e.getSource();
				int skID = 0;
				Skill thisSkill = null;
				for (Integer key : useSkillBtnList.keySet()) {
					if (useSkillBtnList.get(key).equals(useSkillBtn)) {
						skID = key;
						break;
					}
				}
				for (Skill skil : useSkillList) {
					if (skil.getId() == skID) {
						thisSkill = skil;
						break;
					}
				}
				// 버튼에 따른 내 스킬 객체 찾는 로직

				// 스킬 실행
				System.out.println(thisSkill.getName());
				int[] result = ba.letsUseSkill(enemy, thisSkill);
				ItemDao id = new ItemDao();
				ItemConsole ic = new ItemConsole(id, useruser);

				boolean a = ic.wpnUseDown(Integer.valueOf(thisSkill.getNeedItem()));
				if (a) {
					JOptionPane.showMessageDialog(null,
							"내구도가 다 되어 아이템이 파괴됩니다.\n파괴되는 아이템: " + ba.getSkillNeed(thisSkill), "내구도 알림",
							JOptionPane.INFORMATION_MESSAGE);
				}

//				if (thisSkill.getId()==5001 && result[3]==1) {
//					at.addPlayerInfo(userID, "yourAch", 8);
//				}
				// 결과창 보여주기
				printUserResult
						.setText("<우리의 행동>\n" + thisSkill.getName() + "\n" + thisSkill.getSkillScript(result[3]));

				enemy.setIniVar(enemy.getIniVar() + result[0]);
				enemy.setLife(enemy.getLife() + result[1]);
				useruser.setHp(useruser.getHp() + result[2]);

				if (enemy.getLife() < 1) {
					enemy.setLife(0);
				}
				if (useruser.getHp() < 1) {
					useruser.setHp(0);
				}

				for (int i = 0; i < enemy.getLife(); i++) {
					enemyLifeImg.get(i).setIcon(heart_fullImg);
				}
				int ii = enemyFullLife;
				for (int i = enemy.getLife(); i < ii; i++) {
					enemyLifeImg.get(i).setIcon(heart_emptyImg);
				}
				for (int i = 0; i < useruser.getHp(); i++) {
					userLifeImg.get(i).setIcon(heart_fullImg);
				}
				for (int i = useruser.getHp(); i < UserInfo.FULL_HP; i++) {
					userLifeImg.get(i).setIcon(heart_emptyImg);
				}
				// 넘기기
				card.next(footerBox);
			}
		};

		ActionListener chooseActAct = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton eBtn = (JButton) e.getSource();
				useSkillList = new ArrayList<>();
				skillBox.removeAll();

				if (eBtn.getText().equals("공격")) {
					useSkillList = si.getAttckSkillList(enemy.getId(), useruser.getInventory());
					useSkillList.addAll(si.getHeistSkillList(enemy.getId(), useruser.getInventory()));
					setUseSkillBtnList(useSkillList);
					addBtnToPnl(useSkillBtnList, skillBox);

					for (Integer key : useSkillBtnList.keySet()) {
						useSkillBtnList.get(key).addActionListener(skillact);
					}
					card.next(footerBox);

				} else if (eBtn.getText().equals("행동")) {
					useSkillList = si.getActionSkillList(enemy.getId(), useruser.getInventory());
					setUseSkillBtnList(useSkillList);
					addBtnToPnl(useSkillBtnList, skillBox);

					for (Integer key : useSkillBtnList.keySet()) {
						useSkillBtnList.get(key).addActionListener(skillact);
					}
					card.next(footerBox);
				} else if (eBtn.getText().equals("도망")) {
					isGetAway = ba.runAway(phaseCountNum, enemy);
					if (isGetAway) {
						printUserResult.setText(
								"도망치기를 시도했다.\n도주확률: " + ba.calcGetAway(phaseCountNum, 0, enemy) + "\n. . .\n도망에 성공했다!");
					} else {
						printUserResult.setText(
								"도망치기를 시도했다.\n도주확률: " + ba.calcGetAway(phaseCountNum, 0, enemy) + "\n. . .\n도망에 실패했다!");
					}
					card.show(footerBox, "C");
				}
			}
		};

		attackBtn.addActionListener(chooseActAct);
		actionBtn.addActionListener(chooseActAct);
		runAwayBtn.addActionListener(chooseActAct);

		ActionListener getBackPanelAct = new ActionListener() { // 뒤로가기 버튼
			@Override
			public void actionPerformed(ActionEvent e) {
				card.previous(footerBox);
			}
		};

		backBtn.addActionListener(getBackPanelAct);

		// C 다음으로 넘어가는 버튼::
		ActionListener nextBtnCact = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/// 전투 조기종료인지 확인::
				if (enemy.getLife() == 0) {
					isDead = true;
				}
				if (enemy.getIniVar() >= 4) {
					isHappy = true;
				}
				// 전투 지속인지 종료인지 판별해 알맞은 화면으로 넘김
				if (isDead || isHappy || isGetAway) {
					Map<String, Integer> map = getBattleEndScript(enemy, useruser);

					String result = "";
					Integer item = null;

					for (Entry<String, Integer> entry : map.entrySet()) {
						result = result.concat(entry.getKey());
						item = entry.getValue();
					}

					if (item != null) {
						ItemDao id = new ItemDao();
						ItemConsole ic = new ItemConsole(id, useruser);
						ic.getItem(item);
					}

					////////////// 화면 구현
					battleRPrint.setText(result);
					enemyStatus.setText("");
					if (user.getHp() <= 0 && enemy.getId() / 10 == 1 || enemy.getId() / 10 == 2) {
//						at.addPlayerInfo("masic22x", "yourZombiDeath", 1);
					}
					card.show(footerBox, "E");
				} else {
					// 전투 지속일 때만 적 행동 로직 진행
					int d = 0;
					String s = null;
					Map<Integer, String> map = ba.enemyAction(enemy);
					for (Entry<Integer, String> entry : map.entrySet()) {
						d = entry.getKey();
						s = entry.getValue();
					}
					System.out.println(s);
					useruser.setHp(useruser.getHp() - d);

					// 적 행동 화면 반영 - footerBox
					printEnemyResult.setText("<적의 행동>\n" + s);

					// 적행동 화면 반영 - user::

					if (useruser.getHp() < 1) {
						useruser.setHp(0);
					}

					for (int i = 0; i < useruser.getHp(); i++) {
						userLifeImg.get(i).setIcon(heart_fullImg);
					}
					for (int i = useruser.getHp(); i < UserInfo.FULL_HP; i++) {
						userLifeImg.get(i).setIcon(heart_emptyImg);
					}
					card.show(footerBox, "D");
				}
			}
		};

		// D 다음으로 넘어가는 버튼::
		ActionListener nextBtnDact = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Map<String, Integer> map = getBattleEndScript(enemy, useruser);

				String result = "";
				Integer item = null;

				for (Entry<String, Integer> entry : map.entrySet()) {
					result = result.concat(entry.getKey());
					item = entry.getValue();
				}

				if (item != null) {
					ItemDao id = new ItemDao();
					ItemConsole ic = new ItemConsole(id, useruser);
					ic.getItem(item);
				}

				////////////// 화면 구현
				enemyStatus.setText("");
				battleRPrint.setText(result);

				///////////// 턴수 확인해서 A로 갈지 E로 갈지 정하는 로직
				if (useruser.getHp() <= 0 || phaseCountNum >= 3) {
					if (user.getHp() <= 0 && enemy.getId() / 10 == 1 || enemy.getId() / 10 == 2) {
//						at.addPlayerInfo(userID, "yourZombiDeath", 1);
					}
					card.show(footerBox, "E");
				} else {
					phaseCountNum++;
					phaseCount.setText(phaseCountNum + "턴    ");
					enemyStatus.setText(ba.enemyStatus(enemy));
					card.show(footerBox, "A");
				}
			}
		};
		nextBtnC.addActionListener(nextBtnCact);
		nextBtnD.addActionListener(nextBtnDact);
	}

	public static void main(String[] args) {
		Battle_App ba = new Battle_App();
		Battle_Algo baa = new Battle_Algo();
		JFrame f = new JFrame();
		Enemy_Dao ed = new Enemy_Dao();
		JPanel pnl;

		Enemy enemy = ed.selectRandomEnemy(1);
		UserInfo user = baa.setUserData();
		ba.setPnl(enemy, user, "123");
		pnl = ba.getPnl();
		f.add(pnl);
		f.setSize(800, 900);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
