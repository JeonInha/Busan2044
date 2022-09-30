# Busan2044
텍스트 기반 생존게임 "Busan2044"의 전투모듈 / JAVA SWING
***
### 1. 프로젝트 정보
  좀비아포칼립스를 배경으로 한 다회차 생존게임 제작 프로젝트<BUSAN 2044>의 일부인 전투 모듈입니다.
  
  - 사용기술
    - JAVA 8
    - MySQL

  - 특징
    - 턴제 RPG 구성을 베이스로 하여 총3턴의 전투를 진행합니다.
    - 플레이어의 공격이나 행동은 각 동작에 할당된 "명중률"을 토대로 동작합니다. 확률에 따라 공격이 빗나가거나 치명타가 발생하기도 합니다.
    - 플레이어가 할 수 있는 공격이나 행동은 인벤토리의 소지품과 적의 상태에 따라 그 종류가 달라집니다.
    - 공격 외 “행동”을 구현하여 적의 주의를 돌리는 등 전투를 회피하는 플레이스타일 또한 제공합니다.
    - 적이 플레이어를 적대하는 정도를 적개심으로 수치화하여, 플레이어가 적을 적대할수록 적은 플레이어를 더 적대합니다.
    - 전투의 결과로 적의 공격에 따른 플레이어의 체력 감소, 아이템 획득, 플레이어의 사망 등을 출력합니다.
 
- 제작기간
  - 2022.7.22. ~ 2022.8.2.
  
- 담당업무와 기여도
  - 전투 모듈 제작
  - 프로그램 전체 기여도: 30%
  - 전투모듈 기여도: 95%

***

### 2. 개발 중점 사항과 문제점
  - 행동의 명중률 구현
    - 수식을 통해 행동의 종류마다 명중률을 지정하는 수식을 작성하였고 Random클래스를 통해 난수를 생성, 그 난수값과 명중률을 비교하여 난수값이 명중률보다 같거나 낮을 경우 스킬이 성공한 것으로 설정.
    ```java
  // "도망치기" 행동의 성공확률: 기본 20%, 도망치기 스킬의 활성화 여부, 적의 상태, 턴 수 등에 따라 성공확률이 변동함
  int calcGetAway(int turn, int skillAim, Enemy enemy) {
		return (20 + (turn) * 5 + skillAim + (enemy.getIniVar() - 1) * 12);
	}

  // "도망치기" 행동을 실행하고 그 결과를 boolean값으로 반환
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
    ```
    
    - 카드 레이아웃 구성
      - SWING 기능 중 하나인 CardLayout을 이용해 A(스킬 분류 선택) - B(사용할 스킬 선택) - C(스킬 결과 출력) - D(적 행동 결과 출력) E(전투 결과), 5개의 화면을 순회하며 전투 턴을 진행하는 구성.
      - 사용자의 행동에 따라 출력값이 변화하여, 다음 화면으로 넘어가는 버튼에 다음 화면을 구현하는 기능을 할당함.
      - 결과적으로 view와 기능 구현 코드가 서로 섞여있는 모양. 구현의 한계를 느낌.

***

### 3. 프로그램 미리보기
![poster](./img/1.PNG)    
![poster](./img/2.PNG)    
![poster](./img/3.PNG)   
![poster](./img/4.PNG)   


***

### 4. LICENSE
- (C) 2022. JeonInha all rights reserved.
- 해당 프로그램에 사용된 디자인 요소들은 미리캔버스(https://www.miricanvas.com/) 를 통해 제작하였습니다.
