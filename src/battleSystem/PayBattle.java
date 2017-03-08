package battleSystem;

import java.util.Random;

import application.GameEngine;
import character.Boss;
import character.Character;
import character.Player;
import characterEnums.InventoryAction;
import itemSystem.Item;

public class PayBattle extends BossBattle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9056371708169981166L;

	public PayBattle(Player player, Boss boss) {
		super(player, boss);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void start() {
		Character[] turnList = createTurnList();

		boolean battleOngoing = true;
		boolean allEnemiesDead = false;
		do {
			for (int i = 0; i < turnList.length; i++) {
				// NOTE(andrew): initialize this to true, but it will be
				// validated before it is used
				allEnemiesDead = true;
				if (turnList[i] instanceof Player) {
					playerTakesTurn();
				} else if (turnList[i] instanceof Boss) {
					if (turnList[i].getHPProperty().get() > 0) {
						Random r = new Random();
						switch (r.nextInt(2)) {
						case 0:
							if (boss.getAbilities().size() != 0) {
								boss.ability(boss.getAbilities().get(r.nextInt(boss.getAbilities().size())), player);
								break;
							}
						case 1:
							player.takeDmg(boss.attack());
							break;
						default:
							break;
						}
					}

				} else {
					// NOTE(andrew): this branch runs if it's the enemies turn,
					// this should probably be changed, not totally sure,
					// because
					// this AI is linear, the enemy will always attack
					if (turnList[i].getHPProperty().get() > 0) {
						player.takeDmg(turnList[i].attack());
						// System.out.println("EnemyAttacked");
					}
				}
				// NOTE(andrew): check if the player is dead
				if (player.getHPProperty().get() <= 0) {
					battleOngoing = false;
				}
				// NOTE(andrew): this validates that the enemies are dead
				for (int j = 0; j < enemies.length; j++) {
					if (enemies[j].getHPProperty().get() > 0) {
						allEnemiesDead = false;
					}
				}

				if (allEnemiesDead) {
					battleOngoing = false;
					for (int j = 0; j < enemies.length; j++) {
						Item[] loot = enemies[j].getInventoryContents();
						player.modifyInventory(InventoryAction.GIVE, loot);
						player.giveCredits(enemies[j].getCreditDrop());
						creditsDropped += enemies[j].getCreditDrop();
					}

					break;
				}

			}
		} while (battleOngoing);
		// System.out.println(Thread.currentThread().getName());
		isCompleted = true;
		GameEngine.displayEndBattle(this, leveledUp);
	}
}
