package battleSystem;

import java.util.Random;

import abilities.AddValue;
import abilities.AssignATeamAssignment;
import abilities.ShowAPowerpoint;
import application.GameEngine;
import character.Boss;
import character.Character;
import character.Player;
import enums.Character.InventoryAction;
import interfaces.ability.Ability;
import itemSystem.Item;

public class PayBattle extends BossBattle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9056371708169981166L;

	public PayBattle(Player player, Boss boss) {
		super(player, boss);
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
						switch (r.nextInt(5)) {
						case 0:
						case 1:
						case 2:
							if (boss.getAbilities().size() != 0 &&
									(((ShowAPowerpoint) boss.getAbilities().get(0)).getTimesForUse() > 0)) {
								Ability nextAbility = boss.getAbilities().get(0);
								loggedAction = turnList[i].NAME + ": Used " + nextAbility;
								notifySubscribers();
								boss.ability(nextAbility, player);
								break;
							}else if((((AssignATeamAssignment) boss.getAbilities().get(1)).getTimesForUse() > 0)){
								Ability nextAbility = boss.getAbilities().get(1);
								loggedAction = turnList[i].NAME + ": Used " + nextAbility;
								notifySubscribers();
								boss.ability(nextAbility, player);
								break;
							}else if(!((AddValue) boss.getAbilities().get(2)).getUsedBuff()){
								Ability nextAbility = boss.getAbilities().get(2);
								loggedAction = turnList[i].NAME + ": Used " + nextAbility;
								notifySubscribers();
								boss.ability(nextAbility, player);
								break;
							}else{
								Ability nextAbility = boss.getAbilities().get(3);
								loggedAction = turnList[i].NAME + ": Used " + nextAbility;
								notifySubscribers();
								boss.ability(nextAbility, player);
								break;
							}
						case 3:
						case 4:
							loggedAction = turnList[i].NAME + ": Attacked " + player.NAME;
							notifySubscribers();
							player.takeDmg(boss.attack());
							break;
						default:
							break;
						}
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
