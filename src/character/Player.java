package character;

public class Player extends Character {

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
	
	public int attack(AttackType TYPE){
		//TODO Come up with final implementation
		switch(TYPE){
		
		}
		
		return 15;
	}

}
