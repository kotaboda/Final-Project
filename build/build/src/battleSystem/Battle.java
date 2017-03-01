package battleSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import abilityInterfaces.Ability;
import application.GameEngine;
import character.Character;
import character.Enemy;
import character.Player;
import character.PlayerSummary;
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
		this.enemies = enemies;
		this.player = player;
	}
	
	public void start() {
		//TODO(andrew): loop based on turn list, and get data using a Listener interface talking to the game engine or GUI, then use that data to do the battle
		//NOTE(andrew): add an listener interface that will take a battle as a parameter in the update method, and then change variables up at the top
		Character[] turnList = createTurnList();
		
		boolean battleOngoing = true;
		do {
			for(int i = 0 ; i < turnList.length ; i++) {
				if(turnList[i] instanceof Player) {
					GameEngine.playerBattleInput(this);
					if(playerNextAbility != null){
						player.ability(playerNextAbility, playerTarget);
					}else if(playerNextItemUse != null){
						//TODO(andrew): this method will take playerTarget, which needs to be possible to be the player itself, so in the GameGui class we need
							//to allow selection of the player itself.
						playerNextItemUse.use(playerTarget);
					}else if(playerTarget != null){
						playerTarget.takeDmg(player.attack());
					}
				} else {
					player.takeDmg(turnList[i].attack());
				}
				
			}
		}while(battleOngoing);
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

	public PlayerSummary getPlayerSummary() {
		return new PlayerSummary(player);
	}
	
}
