package battle.algo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

import battle.model.BattleOverDao;
import battle.model.Enemy;
import battle.model.Skill;
import battle.model.SkillImpl;
import game_flow.Item;
import game_flow.ItemDao;
import game_flow.UserInfo;
import statistics.AchievementsTest;

public class Battle_Algo {
	Random random = new Random();

	String getEnemyPOW(Enemy enemy) {
		if (enemy.getPower() <= 1) {
			return "어쩌면 상대할 수 있을지도 모른다.";
		} else {
			return "꽤 강할지도 모른다.";
		}
	}

	String getEnemyIniVar(Enemy enemy) {
		if (enemy.getIniVar() <= 1) {
			return "적대";
		} else {
			return "중립";
		}
	}

	String getSkillNeed(Skill skill) {
		if (skill.getNeedItem()==null) {
			return "없음";
		} else {
			ItemDao id = new ItemDao();
			Connection conn = null;
			try {
				conn = BusanUtil.getConnection();
				return id.getItemName(conn, Integer.valueOf(skill.getNeedItem()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return "error";
			} catch (SQLException e) {
				e.printStackTrace();
				return "error";
			} finally {
				if (conn!=null) {
					BusanUtil.closeConn(conn);
				}
			}
		}
	}

	String getSkillPOW(Skill skill) {
		if (skill.getPower() <= 1) {
			return "보통";
		} else {
			return "높음";
		}
	}

	String getSkillAim(Skill skill) {
		if (skill.getAim() == 0) {
			return "낮음";
		} else if (skill.getAim() <= 20) {
			return "보통";
		} else {
			return "높음";
		}
	}

	int calcHit(int skillAim) {
		return 50 + skillAim;
	}

	int calcGetAway(int turn, int skillAim, Enemy enemy) {
		return (20 + (turn) * 5 + skillAim + (enemy.getIniVar() - 1) * 12);
		// 25 - 30 - 35 인데 이제
	}

	String enemyStatus(Enemy enemy) {
		if (enemy.getIniVar() >= 3) {
			return enemy.getFriendship_3_Script();
		} else if (enemy.getIniVar() == 2) {
			return enemy.getFriendship_2_Script();
		} else if (enemy.getIniVar() == 1) {
			return enemy.getFriendship_1_Script();
		} else {
			return enemy.getFriendship_0_Script();
		}
	}

	boolean runAway(int turn, Enemy enemy) {
		System.out.println("<도망치기>");
		double getAwayDouble = calcGetAway(turn, 0, enemy);
		double dice = random.nextDouble() * 100;
		System.out.println("도주성공률: " + String.valueOf(getAwayDouble));
		System.out.println("다이스: " + String.valueOf(dice));
		if (getAwayDouble >= dice) {
			System.out.println("도주 성공");
			return true;
		} else {
			System.out.println("도주 실패");
			return false;
		}
	}

	Map<Integer, String> enemyAction(Enemy enemy) {
		int userDamage = 0;
		String script = null;
		int randomDice = (int) (random.nextDouble() * 100);
		Map<Integer, String> result = new HashMap<>();

		if (enemy.getIniVar() < 1) {
			if (randomDice > 96) {
				script = "크리티컬! " + enemy.getName() + "에게 세게 공격당했다.";
				System.out.println(script);
				userDamage = 2 + enemy.getPower();
			} else if (randomDice > 30) {
				script = enemy.getName() + "에게 공격당했다.";
				System.out.println(script);
				userDamage = 1 + enemy.getPower();

			} else {
				script = enemy.getName() + "의 공격을 피했다.";
				System.out.println(script);
			}

		} else if (enemy.getIniVar() == 1) {
			if (randomDice > 50) {
				script = enemy.getName() + "에게 공격당했다.";
				System.out.println(script);
				userDamage = 1 + enemy.getPower();
			} else {
				script = enemy.getName() + "의 공격을 피했다.";
				System.out.println(script);
			}

		} else {
			script = enemy.getName() + "는 상황을 보고 있다.";
			System.out.println(script);
		}
		result.put(userDamage, script);
		return result;
	}

	int[] letsUseSkill(Enemy enemy, Skill skill) {
		int[] result = new int[4];

		if (skill.getType() == Skill.SKILL_TYPE_ACTION) {
			result = actionSkill(skill);
		} else if (skill.getType() == Skill.SKILL_TYPE_HEIST) {
			result = heistSkill(enemy, skill);
		} else if (skill.getType() == Skill.SKILL_TYPE_HIT) {
			result = hitSkill(skill);
		} else if (skill.getType() == Skill.SKILL_TYPE_SPECIAL) {
			
			result[1] = 0;
			result[2] = 0;

			if (enemy.getId() == 22 || enemy.getId() == 23) {
				result[0] = 30;
				result[3] = 1;				
			} else {
				result[0] = 0;
				result[3] = 4;
			}
		}

		System.out.println("확인용:::");
		System.out.println("우호도 증감: " + result[0]);
		System.out.println("체력 증감: " + result[1]);
		System.out.println("내 체력 증감: " + result[2]);
		// 0 = iniVar
		// 1 = enemylife
		// 3 = userLife
		return result;
	}

	int[] hitSkill(Skill skill) {
		System.out.println("공격스킬 실행");
		int[] result = new int[4];
		int damage = 0;
		int warning = 0;
		int userDamage = 0;
		int resultScript = 0;

		int calcHit = calcHit(skill.getAim());
		int dice = (int) (random.nextDouble() * 100);
		System.out.println("다이스: " + dice);

		if (dice > 95) {
			System.out.println(skill.getFumbleScript());
			warning -= 1;
			userDamage -= 1;
			resultScript = 4;
		} else if (dice < 5) {
			System.out.println(skill.getCriticalScript());
			damage -= 2;
			damage -= skill.getPower();
			resultScript = 1;
		} else if (dice < calcHit) {
			System.out.println(skill.getSucessScript());
			damage -= 1;
			damage -= skill.getPower();
			resultScript = 2;
		} else {
			System.out.println(skill.getFailScript());
			resultScript = 3;
		}
		warning -= 1;

		result[0] = warning;
		result[1] = damage;
		result[2] = userDamage;
		result[3] = resultScript;
		return result;
	}

	int[] actionSkill(Skill skill) {
		System.out.println("액션스킬 실행");
		int[] result = new int[4];
		int damage = 0;
		int warning = 0;
		int userDamage = 0;
		int resultScript = 0;

		int calcHit = calcHit(skill.getAim());
		int dice = (int) (random.nextDouble() * 100);
		System.out.println("성공치: " + calcHit);
		System.out.println("다이스: " + dice);

		if (dice > 95) {
			System.out.println(skill.getFumbleScript());
			resultScript = 4;
			warning -= 1;
		} else if (dice < 5) {
			System.out.println(skill.getCriticalScript());
			resultScript = 1;
			warning += 2;
		} else if (dice < calcHit) {
			System.out.println(skill.getSucessScript());
			resultScript = 2;
			warning += 1;
		} else {
			System.out.println(skill.getFailScript());
			resultScript = 3;
		}
		result[0] = warning;
		result[1] = damage;
		result[2] = userDamage;
		result[3] = resultScript;
		return result;
	}

	int[] heistSkill(Enemy enemy, Skill skill) {
		System.out.println("기습스킬 실행");
		int[] result = new int[4];
		int damage = 0;
		int warning = 0;
		int userDamage = 0;
		int resultScript = 0;

		int calcHit = calcHit(skill.getAim());
		int dice = (int) (random.nextDouble() * 100);
		System.out.println("성공치: " + calcHit);
		System.out.println("다이스: " + dice);

		if (dice > 95) {
			System.out.println(skill.getFumbleScript());
			warning -= 1;
			userDamage -= 1;
			resultScript = 4;

		} else if (dice < 5) {
			System.out.println(skill.getCriticalScript());
			damage = -(enemy.getIniVar() + 30);
			damage -= skill.getPower();
			resultScript = 1;

		} else if (dice < calcHit) {
			System.out.println(skill.getSucessScript());
			damage = -(enemy.getIniVar() - 1);
			damage -= skill.getPower();
			resultScript = 2;
		} else {
			System.out.println(skill.getFailScript());
			resultScript = 3;
		}

		warning -= 2;

		result[0] = warning;
		result[1] = damage;
		result[2] = userDamage;
		result[3] = resultScript;
		return result;
	}

	public UserInfo setUserData() {
		UserInfo user = new UserInfo();
		user.setHp(5);
		Item item1 = new Item(1, 2, 1); // 몽둥이
		Item item2 = new Item(4, 2, 1); // 권총
		Item item3 = new Item(9, 1, 1); // 육포
		Item item4 = new Item(8, 1, 1); // 살골
		Item item5 = new Item(14, 1, 1); // 통조림
		Item item6 = new Item(5, 1, 1); // 효자손

		List<Item> inven = new ArrayList<>();
		inven.add(item1);
		inven.add(item2);
		inven.add(item3);
		inven.add(item4);
		inven.add(item5);
		inven.add(item6);

		user.setInventory(inven);

		return user;
	}

	// 임시데이터: 유저
	public boolean battleLogic(Enemy enemy, UserInfo user) {
		SkillImpl si = new SkillImpl();
		List<Skill> useSkill = si.readSkill();

		BattleOverDao bt = new BattleOverDao();
		boolean isGetAway = false;
		boolean isHappy = false;
		boolean isDead = false;

		// 조우 페이즈
		System.out.println("========================");
		System.out.println(enemy.getName() + "와 마주쳤다.");
		System.out.println("========================");

		// 배틀 페이즈
		for (int i = 0; i < 3; i++) {
			System.out.println("------------------------");
			System.out.println(String.valueOf(i + 1) + "번 페이즈");

			// 좀비의 상태 출력
			System.out.println(enemyStatus(enemy));

			System.out.println("우리 체력: " + user.getHp());
			System.out.println("적의 체력: " + enemy.getLife());
			System.out.println("어떤 행동을 할까?");

			System.out.println("1. 공격하기");
			System.out.println("2. 행동하기");
			System.out.println("3. 도망치기");
			System.out.print("입력: ");
			int input1 = new Scanner(System.in).nextInt();
			if (input1 == 1) {
				List<Skill> c = si.getAttckSkillList(enemy.getId(), user.getInventory());
				List<Skill> b = si.getHeistSkillList(enemy.getId(), user.getInventory());

				List<Skill> a = new ArrayList<>();
				a.addAll(b);
				a.addAll(c);

				for (Skill skill : a) {
					System.out.println(skill.getName());
				}

			} else if (input1 == 2) {
				List<Skill> a = si.getActionSkillList(enemy.getId(), user.getInventory());
				for (Skill skill : a) {
					System.out.println(skill.getName());
				}
			} else if (input1 == 3) {

			} else {
				System.out.println("잘못 선택했서 .... 다시하장");
				i--;
				continue;
			}

			for (int j = 0; j < useSkill.size(); j++) {
				System.out.print(String.valueOf(j + 1) + ". " + useSkill.get(j).getName());
				System.out.println("(성공률: " + calcHit(useSkill.get(j).getAim()) + ")");
			}
			System.out.println((useSkill.size() + 1) + ". 도망치기(확률: ??)");
			System.out.print("입력: ");
			int input = new Scanner(System.in).nextInt() - 1;

			// 스킬결과
			if (input > useSkill.size()) { // 잘못된 입력
				System.out.println("잘못했서 ... 처음으로 가자.");
				i--;
			} else if (input == useSkill.size()) { // 도망
				isGetAway = runAway(i, enemy);
				if (isGetAway)
					break;
			} else { // 스킬 실행
				Skill thisSkill = useSkill.get(input);
				int[] result = letsUseSkill(enemy, thisSkill);

				enemy.setIniVar(enemy.getIniVar() + result[0]);
				enemy.setLife(enemy.getLife() + result[1]);
				user.setHp(user.getHp() + result[2]);
			}

			System.out.println("== 확인용");
			System.out.println("우호도: " + enemy.getIniVar());
			System.out.println("적 체력: " + enemy.getLife());

			// 상대의 행동 출력
			System.out.println(enemy.getName() + "이 행동한다.");

			int d = 0;
			String s = null;
			Map<Integer, String> map = enemyAction(enemy);
			for (Entry<Integer, String> entry : map.entrySet()) {
				d = entry.getKey();
				s = entry.getValue();
			}
			System.out.println(s);
			user.setHp(user.getHp() - d);

			// 특수종료 확인
			if (enemy.getLife() <= 0) {
				isDead = true;
				break;
			}
			if (enemy.getIniVar() >= 4) {
				isHappy = true;
				break;
			}
			if (user.getHp() <= 0) {
				enemy.setIniVar(-20);
			}
		}
		// 결과 페이즈
		System.out.println("========================");
		System.out.println("결과 출력");

		// 도망치기 //
		if (isGetAway) {
			bt.getRunAwayResult(enemy.getId());

			// 상대 죽이기 // win
		} else if (isDead) {
			bt.getWinResult(enemy.getId());
			if (enemy.getDropItem() != null) {
				int indx = random.nextInt(enemy.getDropItem().size());
				if (enemy.getDropItem().size() > 0) {
					System.out.println("떨어트린 아이템: " + enemy.getDropItem().get(indx));
				}
			} else {
				System.out.println("아이템이 없는 몬스터였당.");
			}

			// 상대 협상 굿엔딩 //
		} else if (isHappy || enemy.getIniVar() == 3) {
			bt.getFriendResult(enemy.getId());
			int indx = random.nextInt(enemy.getDropItem().size());
			if (enemy.getDropItem().size() > 0) {
				System.out.println("떨어트린 아이템: " + enemy.getDropItem().get(indx));
			}

			// 우리가 죽는 엔딩
		} else if (enemy.getIniVar() <= 0) {
			bt.getLoseResult(enemy.getId());

			// 아무것도 없는 엔딩
		} else {
			bt.getNomalResult(enemy.getId());
		}
		System.out.println("남은 HP: " + user.getHp());

		// 이건 반복테스트할때 필요한거:: 지워도 됨
		System.out.println("전투 한번 더 하려면 1, 아니면 아무거나 입력");
		boolean result = false;
		String a = new Scanner(System.in).next();
		if (a.equals("1")) {
			result = true;
		}

		return result;
	}
}
