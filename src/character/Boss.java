package character;

import java.util.ArrayList;
import java.util.Arrays;

import application.GameEngine;
import enums.Character.Stats;
import interfaces.ability.Ability;
import interfaces.ability.AttackAbility;
import interfaces.ability.BuffAbility;
import interfaces.tile.Interactable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
		GameEngine.playTakeDamageAnimation(takeDamageAnimation, this);
		int damage = dmg-getStat(Stats.ENDURANCE);

		if (damage <= 0) {
			hitPoints -= 1;
		} else {
			hitPoints -= damage;
		}
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		hitPoints = hitPoints > maxHitPoints ? maxHitPoints : hitPoints;
		hpProperty.set(hitPoints);

		return dmg;
	}

	@Override
	public int attack() {
		GameEngine.playAttackAnimation(attackAnimation, this);
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
