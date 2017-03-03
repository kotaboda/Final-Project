package battleSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import abilityInterfaces.Ability;
import application.GameEngine;
import character.Character;
import character.Enemy;
import character.Player;
import characterEnums.InventoryAction;
import publisherSubscriberInterfaces.Listener;
import publisherSubscriberInterfaces.Subscribable;
import itemSystem.Item;
import itemSystem.Usable;
import models.Coordinates;

public class Battle implements Subscribable<Battle>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3115042468203904551L;
	protected Player player;
	protected Enemy[] enemies;
	private Coordinates place = new Coordinates(0,0);
	protected ArrayList<Listener<Battle>> subscribers = new ArrayList<Listener<Battle>>();
	private boolean isDone = false;
	private Ability playerNextAbility = null;
	private Usable playerNextItemUse = null;
	private String loggedAction = null;

	//TODO(andrew): this might need to be an array or array list
	private Character playerTarget = null;
	
	public Battle(Player player, Enemy...enemies) {
		if(enemies.length == 0){
			throw new IllegalArgumentException("Battle must have at least 1 enemy.");
		}
		this.enemies = enemies;
		this.player = player;
	}
	
	public void setPlayerNextItemUse(Usable playerNextItemUse) {
		this.playerNextItemUse = playerNextItemUse;
		this.playerNextAbility = null;
	}
	public void start() {
		//TODO(andrew): loop based on turn list, and get data using a Listener interface talking to the game engine or GUI, then use that data to do the battle
		//NOTE(andrew): add an listener interface that will take a battle as a parameter in the update method, and then change variables up at the top
		//TODO(andrew): the createTurnList() method is probably not working correctly, it needs to not put dead enemies into the 
		Character[] turnList = createTurnList();
		
		boolean battleOngoing = true;
		boolean allEnemiesDead = false;
		do {
			for(int i = 0 ; i < turnList.length ; i++) {
				allEnemiesDead = true;
				if(turnList[i] instanceof Player) {
					GameEngine.playerBattleInput(this);
					if(playerNextAbility != null){
						player.ability(playerNextAbility, playerTarget);
						loggedAction = player.NAME + ": Used " + playerNextAbility;
						notifySubscribers();
					}else if(playerNextItemUse != null){
						//TODO(andrew): this method will take playerTarget, which needs to be possible to be the player itself, so in the GameGui class we need
							//to allow selection of the player itself.
						playerNextItemUse.use(turnList[i]);
						loggedAction = player.NAME + ": Used " + ((Item)playerNextItemUse);
						notifySubscribers();
					}else if(playerTarget != null){
						playerTarget.takeDmg(player.attack());
						loggedAction = player.NAME + ": Attacked " + playerTarget.NAME;
						notifySubscribers();
					}
				} else {
					player.takeDmg(turnList[i].attack());
				}
				if(player.getHPProperty().get() <= 0){
					battleOngoing = false;
				}
				for(int j = 0; j < enemies.length; j++){
					if(enemies[j].getHPProperty().get() > 0){
						allEnemiesDead = false;
					}
				}
				if(allEnemiesDead){
					battleOngoing = false;
					int credits = 0;
					for(int j = 0 ; j < enemies.length ; j++) {
						Item[] loot = enemies[j].getInventoryContents();
						credits += enemies[j].getCreditDrop();
						player.modifyInventory(InventoryAction.GIVE, loot);
					}
					player.giveCredits(credits);
					break;
				}
			}
		}while(battleOngoing);
		isDone = true;
		GameEngine.displayEndBattle(this);
	}
	
	public Enemy[] getEnemies() {
		return enemies;
	}
	
	public void setEnemies(Enemy...enemies) {
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
		int playerCount = 1;
		Character[] turnList = new Character[playerCount+enemies.length];
		turnList[0] = player;
		for(int i = 1; i < enemies.length + 1; i++){
			turnList[i] = enemies[i - 1];
			
		}
		Arrays.sort(turnList, Character::compareWit);
		
		return turnList;
	}

	@Override
	public void addSubscriber(Listener<Battle> sub) {
		if(sub != null) {
			sub.update();
			subscribers.add(sub);
		}
	}

	@Override
	public void removeSubscriber(Listener<Battle> sub) {
		if(sub != null) {
			subscribers.remove(sub);
		}
	}

	@Override
	public void notifySubscribers() {
		for(Listener<Battle> sub : subscribers) {
			sub.update();
		}
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isDone() {
		// TODO Auto-generated method stub
		return isDone;
	}

	public String getLoggedAction() {
		return loggedAction;
	}
	
}
