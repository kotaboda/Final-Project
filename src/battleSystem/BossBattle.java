package battleSystem;

import character.Boss;
import character.Enemy;
import character.Player;

public class BossBattle extends Battle{
	private Boss boss;
	
	public BossBattle(Player player, Boss boss, Enemy...enemies) {
		super(player, enemies);
		this.setBoss(boss);
	}
	
	public Boss getBoss() {
		return boss;
	}

	public void setBoss(Boss boss) {
		this.boss = boss;
	}

	@Override
	public void start() {
		Character[] turnList = createTurnList();
	}
	
	private Character[] createTurnList() {
		return null;
	}
}
