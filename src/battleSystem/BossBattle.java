package battleSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import application.GameEngine;
import character.Boss;
import character.Character;
import character.Enemy;
import character.Player;
import characterEnums.InventoryAction;
import itemSystem.Item;

public class BossBattle extends Battle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5129731413157601090L;

	private Boss boss;

	public BossBattle(Player player, Boss boss, Enemy... enemies) {
		super(player, enemies);
		if (boss == null) {
			throw new IllegalArgumentException("The boss in a boss battle cannot be null.");
		}
		this.boss = boss;
		Enemy[] e = new Enemy[enemies.length + 1];
		for (int i = 0; i < enemies.length; i++) {
			e[i] = enemies[i];
		}
		e[enemies.length] = boss;
		this.setEnemies(e);
	}

	public BossBattle(Player player, Boss boss) {
		super(player);
		if (boss == null) {
			throw new IllegalArgumentException("The boss in a boss battle cannot be null.");
		}
		this.setEnemies(boss);
		this.boss = boss;
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
					GameEngine.playerBattleInput(this);
					if (playerNextAbility != null) {
						// NOTE(andrew): If the player selected an ability this
						// branch should run
						player.ability(playerNextAbility, playerTarget);
						loggedAction = player.NAME + ": Used " + playerNextAbility;
						notifySubscribers();
					} else if (playerNextItemUse != null) {
						// TODO(andrew): this method will take playerTarget,
						// which needs to be possible to be the player itself,
						// so in the GameGui class we need
						// to allow selection of the player itself.
						// NOTE(andrew): this branch runs if the player uses an
						// item
						playerNextItemUse.use(turnList[i]);
						loggedAction = player.NAME + ": Used " + (playerNextItemUse);
						notifySubscribers();
					} else if (playerTarget != null) {
						// NOTE(andrew): this branch runs if the player selected
						// attack
						playerTarget.takeDmg(player.attack());
						loggedAction = player.NAME + ": Attacked " + playerTarget.NAME;
						notifySubscribers();
					}
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
		GameEngine.displayEndBattle(this);
	}

	private Character[] createTurnList() {
		ArrayList<Character> c = new ArrayList<>(Arrays.asList(enemies));
		c.add(player);
		Character[] turnList = c.toArray(new Character[0]);
		Arrays.sort(turnList, Character::compareWit);

		return turnList;
	}
}
