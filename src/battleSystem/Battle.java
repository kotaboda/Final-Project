package battleSystem;

import java.util.ArrayList;
import java.util.Arrays;

import application.GameEngine;
import character.Character;
import character.Enemy;
import character.Player;
import character.PlayerSummary;
import characterInterfaces.Listener;
import characterInterfaces.Subscribable;
import javafx.scene.control.Label;

public class Battle implements Subscribable<Battle>{
	protected Player player;
	protected Enemy[] enemies;
	protected ArrayList<Listener<Battle>> subscribers = new ArrayList<Listener<Battle>>();
	
	public Battle(Player player, Enemy...enemies) {
		this.enemies = enemies;
		this.player = player;
	}
	
	public void start() {
		Character[] turnList = createTurnList();
		
		boolean battleOngoing = false;
		do {
			for(int i = 0 ; i < turnList.length ; i++) {
				if(turnList[i] instanceof Player) {
					Character target = GameEngine.pickTarget();
					int dmg = turnList[i].attack(target);
					target.takeDmg(dmg);
				} else {
					int dmg = turnList[i].attack(player);
					player.takeDmg(dmg);
				}
			}
		}while(battleOngoing);
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
