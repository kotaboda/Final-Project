package character;

import characterEnums.AttackTypes;
import publisherSubscriberInterfaces.Listener;

public class Player extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7445111099520272415L;

	public Player(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int takeDmg(int dmg) {
		//TODO Come up with final implementation
		hitPoints -= dmg;
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		return dmg;
	}

	@Override
	public int attack() {
		// TODO Come up with final implementation
		return 10;
	}
	
	public int attack(AttackTypes TYPE){
		//TODO Come up with final implementation
		switch(TYPE){
		
		}
		
		return 15;
	}

	@Override
	public void addSubscriber(Listener<Character> listener) {
		// TODO Auto-generated method stub
		
	}

}
