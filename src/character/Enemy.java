package character;

import characterEnums.Stats;

public class Enemy extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4495778022214568163L;

	public Enemy(String name, int tileSheetNum) {
		super(name, tileSheetNum);
		// TODO Auto-generated constructor stub
	}
	
	public Enemy() {
		super();
	}
	@Override
	public int takeDmg(int dmg) {
		hitPoints -= (dmg-getStat(Stats.ENDURANCE) + 2);
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		hpProperty.set(hitPoints);
		return dmg;
	}

	@Override
	public int attack() {
		int damage = 0;
		damage = getStat(Stats.INTELLIGIENCE);
		return damage;
	}

	
	

}
