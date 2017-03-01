package character;

import characterEnums.Stats;

public abstract class Boss extends Character {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5036318833740582652L;
	private boolean isDefeated = false;

	public Boss(String name, int tileSheetNum) {
		super(name, tileSheetNum);
		// TODO Auto-generated constructor stub
	}
	
	public Boss() {
		super();
	}
	
	public boolean isDefeated() {
		return isDefeated;
	}


	@Override
	public int takeDmg(int dmg) {
		// TODO Auto-generated method stub
		int damage = dmg-getStat(Stats.ENDURANCE);
		hitPoints -= damage;
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		hpProperty.set(hitPoints);
		if(hitPoints <= 0){
			hitPoints = 0;
			isDefeated = true;
		}
		return damage;
	}

	@Override
	public int attack() {
		int damage = 0;
		damage = getStat(Stats.INTELLIGIENCE);
		return damage;
	}

	@Override
	public String toString() {
		return "Boss [isDefeated=" + isDefeated + ", name=" + name + ", inv=" + inv + ", hitPoints=" + hitPoints
				+ ", energy=" + energy + ", level=" + level + ", floorNum=" + floorNum + "]";
	}
}
