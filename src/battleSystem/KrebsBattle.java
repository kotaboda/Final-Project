package battleSystem;

import java.util.Random;

import application.GameEngine;
import character.Boss;
import character.Character;
import character.Krebsinator;
import character.PickOnYou;
import character.Player;
import enums.Character.InventoryAction;
import interfaces.ability.Ability;
import interfaces.ability.AttackAbility;
import itemSystem.Item;

public class KrebsBattle extends BossBattle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3108289910004524673L;

	public KrebsBattle(Player player, Boss boss) {
		super(player, boss, new Krebsinator());
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
						Boss boss = (Boss) turnList[i];
						Random r = new Random();
						switch (r.nextInt(2)) {
						case 0:
							if (boss.getAbilities().size() != 0) {
								Ability bossAbility;
								if ((bossAbility = boss.getAbilities()
										.get(r.nextInt(boss.getAbilities().size()))) instanceof AttackAbility) {
									boss.ability(bossAbility, player);
									loggedAction = boss.NAME + " Used: " + bossAbility;
									break;
								} else if ((bossAbility) instanceof PickOnYou) {
									boss.ability(bossAbility, this.boss);
									loggedAction = boss.NAME + " Used: " + bossAbility + "\nMr. Krebs Wit went up!";
									break;
								}
							}
						case 1:
							player.takeDmg(boss.attack());
							loggedAction = boss.NAME + " attacked " + player.NAME;
							break;
						default:
							break;
						}
						notifySubscribers();
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
		subscribers.clear();
		GameEngine.displayEndBattle(this, leveledUp);
	}

}
