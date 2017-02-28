package battleSystem;

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
import models.Coordinates;

public class Battle implements Subscribable<Battle>{
	protected Player player;
	protected Enemy[] enemies;
	private Coordinates place = new Coordinates(0,0);
	protected ArrayList<Listener<Battle>> subscribers = new ArrayList<Listener<Battle>>();
	private Ability playerNextAbility = null;
	private Character playerTarget = null;
	
	public Battle(Player player, Enemy...enemies) {
		this.enemies = enemies;
		this.player = player;
	}
	
	public void start() {
		//TODO(andrew): loop based on turn list, and get data using a Listener interface talking to the game engine or GUI, then use that data to do the battle
		//NOTE(andrew): add an listener interface that will take a battle as a parameter in the update method, and then change variables up at the top
		Character[] turnList = createTurnList();
		
		boolean battleOngoing = false;
		do {
			for(int i = 0 ; i < turnList.length ; i++) {
				if(turnList[i] instanceof Player) {
					GameEngine.playerBattleInput(this);
					if(playerTarget != null){
						playerNextAbility.use(playerTarget);
					}else{
						//TODO(andrew): do we need to add a use method that takes in no target for buff abilities?
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
	
	private Character[] createTurnList() {
		int playerCount = 1;
		Character[] turnList = new Character[playerCount+enemies.length];
		
		Arrays.sort(turnList);
		
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
