package battleSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import abilityInterfaces.Ability;
import abilityInterfaces.GroupAbility;
import application.GameEngine;
import character.Character;
import character.Enemy;
import character.Player;
import characterEnums.InventoryAction;
import itemSystem.Item;
import itemSystem.Usable;
import models.Coordinates;
import publisherSubscriberInterfaces.Listener;
import publisherSubscriberInterfaces.Subscribable;

public class Battle implements Subscribable<Battle>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3115042468203904551L;
	protected boolean leveledUp = false;
	protected Player player;
	protected Enemy[] enemies;
	private Coordinates place = new Coordinates(0, 0);
	protected ArrayList<Listener<Battle>> subscribers = new ArrayList<Listener<Battle>>();
	protected Ability playerNextAbility = null;
	protected Usable playerNextItemUse = null;
	protected boolean isCompleted = false;
	protected String loggedAction = null;
	protected Character playerTarget = null;
	protected int creditsDropped;
	private ArrayList<Item> itemsDropped = new ArrayList<Item>();

	public Battle(Player player, Enemy... enemies) {
		if (player == null) {
			throw new IllegalArgumentException("The player in a battle cannot be null");
		}
		if (enemies == null) {
			throw new IllegalArgumentException("The enemies in a battle cannot be null");
		}
		if (enemies.length == 0) {
			throw new IllegalArgumentException("Battle must have at least 1 enemy.");
		}
		this.enemies = enemies;
		this.player = player;
	}
	
	protected Battle(Player player){
		if (player == null) {
			throw new IllegalArgumentException("The player in a battle cannot be null");
		}
		this.player = player;
	}

	public void setPlayerNextItemUse(Usable playerNextItemUse) {
		this.playerNextItemUse = playerNextItemUse;
		this.playerNextAbility = null;
	}
	
	protected void playerTakesTurn(){
		GameEngine.playerBattleInput(this);
		if (playerNextAbility != null) {
			// NOTE(andrew): If the player selected an ability this
			// branch should run
			boolean successfulAbilityUse = false;
			if(playerNextAbility instanceof GroupAbility){
				successfulAbilityUse = player.ability(playerNextAbility, enemies);
			}else{
				successfulAbilityUse = player.ability(playerNextAbility, playerTarget);
			}
			if(successfulAbilityUse){
				loggedAction = player.NAME + ": Used " + playerNextAbility;							
			} else{
				loggedAction = player.NAME + ": Used " + playerNextAbility + " but it failed!";				
			}
			notifySubscribers();
		} else if (playerNextItemUse != null) {
			// TODO(andrew): this method will take playerTarget,
			// which needs to be possible to be the player itself,
			// so in the GameGui class we need
			// to allow selection of the player itself.
			// NOTE(andrew): this branch runs if the player uses an
			// item
			playerNextItemUse.use(player);
			loggedAction = player.NAME + ": Used " + (playerNextItemUse);
			notifySubscribers();
		} else if (playerTarget != null) {
			// NOTE(andrew): this branch runs if the player selected
			// attack
			playerTarget.takeDmg(player.attack());
			loggedAction = player.NAME + ": Attacked " + playerTarget.NAME;
			notifySubscribers();
		}
	}

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
						itemsDropped.addAll(Arrays.asList(loot));
						if(player.giveCredits(enemies[j].getCreditDrop())){
							leveledUp = true;
						}
						creditsDropped += enemies[j].getCreditDrop();
					}

					break;
				}

			}
		} while (battleOngoing);
		subscribers.clear();
		isCompleted = true;
		GameEngine.displayEndBattle(this, leveledUp);
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public Enemy[] getEnemies() {
		return enemies;
	}

	public void setEnemies(Enemy... enemies) {
		this.enemies = enemies;
	}

	public Coordinates getCoordinates() {
		return place;
	}

	public void setPlayerNextAbility(Ability playerNextAbility) {
		this.playerNextAbility = playerNextAbility;
		this.playerNextItemUse = null;
	}

	public void setPlayerTarget(Character playerTarget) {
		this.playerTarget = playerTarget;
	}

	private Character[] createTurnList() {
		// NOTE(andrew): The turn list is initialized to an arraylist because it
		// is much easier to manage initially. it is later cast to
		// a traditional array
		ArrayList<Character> initial = new ArrayList<>();
		initial.add(player);
		initial.addAll(Arrays.asList(enemies));
		Character[] turnList = initial.toArray(new Character[0]);
		Arrays.sort(turnList, Character::compareWit);

		return turnList;
	}

	@Override
	public void addSubscriber(Listener<Battle> sub) {
		if (sub != null) {
			sub.update();
			subscribers.add(sub);
		}
	}

	@Override
	public void removeSubscriber(Listener<Battle> sub) {
		if (sub != null) {
			subscribers.remove(sub);
		}
	}

	@Override
	public void notifySubscribers() {
		for (Listener<Battle> sub : subscribers) {
			sub.update();
		}
	}

	public Player getPlayer() {
		return player;
	}

	public String getLoggedAction() {
		return loggedAction;
	}

	public int getCreditsDropped() {
		return creditsDropped;
	}

	public Item[] getItemDrops() {
		return itemsDropped.toArray(new Item[0]);
	}

}
