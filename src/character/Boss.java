package character;

import java.util.ArrayList;
import java.util.Arrays;

import abilityInterfaces.Ability;
import abilityInterfaces.AttackAbility;
import abilityInterfaces.BuffAbility;
import characterEnums.Stats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tileinterfaces.Interactable;

public abstract class Boss extends Enemy implements Interactable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5036318833740582652L;
	private boolean isDefeated = false;
	private ArrayList<Ability> abilities = new ArrayList<>();

	public Boss(String name, int creditDrop, int tileSheetNum, Ability... abilities) {
		super(name, creditDrop, tileSheetNum);
		this.abilities.addAll(Arrays.asList(abilities));
	}
	
	public boolean isDefeated() {
		return isDefeated;
	}
	
	public ObservableList<Ability> getAbilities() {
		ObservableList<Ability> abilities = FXCollections.observableArrayList();
		abilities.addAll(this.abilities);
		return abilities;
	}
	
	public void ability(Ability ability, Character... targets) {
		if (ability instanceof AttackAbility) {
			for (int i = 0; i < targets.length; i++) {
				ability.use(this, targets[i]);
			}
		} else if (ability instanceof BuffAbility) {
			ability.use(this, this);
		}
		
	}
	
	@Override
	public void interact(Player player) {
		
	}
	


	@Override
	public int takeDmg(int dmg) {
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
