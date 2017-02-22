package battleSystem;

import java.util.ArrayList;

import character.Enemy;
import character.Player;
import characterInterfaces.Listener;
import characterInterfaces.Subscribable;

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
	}
	
	private Character[] createTurnList() {
		return null;
	}

	@Override
	public void addSubscriber(Listener<Battle> sub) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSubscriber(Listener<Battle> sub) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifySubscribers() {
		// TODO Auto-generated method stub
		
	}
	
}
