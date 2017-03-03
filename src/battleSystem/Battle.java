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
	private Ability playerNextAbility = null;
	private Usable playerNextItemUse = null;
	private boolean isCompleted = false;
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
		Character[] turnList = createTurnList();
		
		boolean battleOngoing = true;
		boolean allEnemiesDead = false;
		do {
			for(int i = 0 ; i < turnList.length ; i++) {
				//NOTE(andrew): initialize this to true, but it will be validated before it is used
				allEnemiesDead = true;
				if(turnList[i] instanceof Player) {
					GameEngine.playerBattleInput(this);
					if(playerNextAbility != null){
						//NOTE(andrew): If the player selected an ability this branch should run
						player.ability(playerNextAbility, playerTarget);
						loggedAction = player.NAME + ": Used " + playerNextAbility;
						notifySubscribers();
					}else if(playerNextItemUse != null){
						//TODO(andrew): this method will take playerTarget, which needs to be possible to be the player itself, so in the GameGui class we need
							//to allow selection of the player itself.
						//NOTE(andrew): this branch runs if the player uses an item
						playerNextItemUse.use(turnList[i]);
						loggedAction = player.NAME + ": Used " + ((Item)playerNextItemUse);
						notifySubscribers();
					}else if(playerTarget != null){
						//NOTE(andrew): this branch runs if the player selected attack
						playerTarget.takeDmg(player.attack());
						loggedAction = player.NAME + ": Attacked " + playerTarget.NAME;
						notifySubscribers();
					}
				} else {
					//NOTE(andrew): this branch runs if it's the enemies turn, this should probably be changed, not totally sure, because 
						//this AI is linear, the enemy will always attack
					if(turnList[i].getHPProperty().get() > 0){
						player.takeDmg(turnList[i].attack());
						//System.out.println("EnemyAttacked");
					}
				}
				//NOTE(andrew): check if the player is dead
				if(player.getHPProperty().get() <= 0){
					battleOngoing = false;
				}
				//NOTE(andrew): this validates that the enemies are dead
				for(int j = 0; j < enemies.length; j++){
					if(enemies[j].getHPProperty().get() > 0){
						allEnemiesDead = false;
					}
				}
				if(allEnemiesDead){
					battleOngoing = false;
					//System.out.println(Thread.currentThread().getName());
					for(int j = 0 ; j < enemies.length ; j++) {
						Item[] loot = enemies[j].getInventoryContents();
						player.modifyInventory(InventoryAction.GIVE, loot);
					}
					break;
				}
				
			}
		}while(battleOngoing);
		//System.out.println(Thread.currentThread().getName());
		isCompleted = true;
		GameEngine.displayEndBattle(this);
	}
	
	public boolean isCompleted(){
		return isCompleted;
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
		//NOTE(andrew): The turn list is initialized to an arraylist because it is much easier to manage initially. it is later cast to 
			//a traditional array
		ArrayList<Character> initial = new ArrayList<>();
		initial.add(player);
		for(int i = 1; i < enemies.length + 1; i++){
			if(enemies[i - 1].getHPProperty().get() > 0){
				initial.add(enemies[i - 1]);
			}
			
		}
		
		Character[] turnList = initial.toArray(new Character[0]);
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


	public String getLoggedAction() {
		return loggedAction;
	}
	
}
