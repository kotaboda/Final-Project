package character;

import java.util.ArrayList;
import java.util.Arrays;

import abilityInterfaces.Ability;
import abilityInterfaces.AttackAbility;
import abilityInterfaces.BuffAbility;
import characterEnums.Stats;

public abstract class Boss extends Character {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5036318833740582652L;
	private boolean isDefeated = false;
	private ArrayList<Ability> abilities = new ArrayList<>();

	public Boss(String name, int tileSheetNum, Ability... abilities) {
		super(name, tileSheetNum);
		this.abilities.addAll(Arrays.asList(abilities));
		
		// TODO Auto-generated constructor stub
	}
	
	public Boss() {
		super();
	}
	
	public boolean isDefeated() {
		return isDefeated;
	}
	
	public void ability(Ability ability, Character... targets) {
		if (ability instanceof AttackAbility) {
			for (int i = 0; i < targets.length; i++) {
				ability.use(targets[i]);
			}
		} else if (ability instanceof BuffAbility) {
			ability.use(this);
		}
		
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
		return "Boss [isDefeated=" + isDefeated + ", name=" + NAME + ", inv=" + inv + ", hitPoints=" + hitPoints
				+ ", energy=" + energy + ", level=" + level + ", floorNum=" + floorNum + "]";
	}
}
