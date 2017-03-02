package battleSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import abilityInterfaces.Ability;
import application.GameEngine;
import character.Character;
import character.Enemy;
import character.Player;
import characterInterfaces.Listener;
import characterInterfaces.Subscribable;
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
	//TODO(andrew): this might need to be an array or array list
	private Character playerTarget = null;
	
	public Battle(Player player, Enemy...enemies) {
		if(enemies.length == 0){
			throw new IllegalArgumentException("Battle must have at least 1 enemy.");
		}
		this.enemies = enemies;
		this.player = player;
	}
	
	public void start() {
		//NOTE(andrew): add an listener interface that will take a battle as a parameter in the update method, and then change variables up at the top
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
					}else if(playerNextItemUse != null){
						//TODO(andrew): this method will take playerTarget, which needs to be possible to be the player itself, so in the GameGui class we need
							//to allow selection of the player itself.
						//NOTE(andrew): this branch runs if the player uses an item
						playerNextItemUse.use(playerTarget);
					}else if(playerTarget != null){
						//NOTE(andrew): this branch runs if the player selected attack
						playerTarget.takeDmg(player.attack());
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
					break;
				}
			}
		}while(battleOngoing);
		//System.out.println(Thread.currentThread().getName());
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
			sub.update(null);
			//TODO not sure why listener is needed, if someone else wants to modify this?
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
			sub.update(null);
		}
	}

	public Player getPlayer() {
		return player;
	}
	
}
