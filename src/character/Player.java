package character;

import java.util.ArrayList;

import abilities.Procrastinate;
import abilities.PullAnAllNighter;
import abilityInterfaces.Ability;
import abilityInterfaces.AttackAbility;
import abilityInterfaces.BuffAbility;
import characterEnums.Direction;
import characterEnums.Stats;
import itemSystem.Coffee;
import itemSystem.Doritos;
import itemSystem.MountainDew;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class Player extends Character {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3349758486478712145L;

//	private final Image IMAGE = new Image();
	private final ArrayList<Ability> ABILITIES = new ArrayList<>();
	private Direction directionFacing = Direction.DOWN;
//	private final int COLUMNS = 5;
//	private final int COUNT = 10;
//    private final int WIDTH = 32;
//    private final int HEIGHT = 32;

	public Player(String name, int tileSheetNum) {
		super(name, tileSheetNum);
		ABILITIES.add(new PullAnAllNighter());
		ABILITIES.add(new Procrastinate());
		this.hitPoints = stats.get(Stats.MOTIVATION)*10;
		this.energy = stats.get(Stats.STAMINA)*10;
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
	}
	
	public Player() {
		super();
		ABILITIES.add(new PullAnAllNighter());
		ABILITIES.add(new Procrastinate());
		this.stats.put(Stats.INTELLIGIENCE, 100);
		this.stats.put(Stats.MOTIVATION, 100);
		this.hitPoints = stats.get(Stats.MOTIVATION)*10;
		this.energy = stats.get(Stats.STAMINA)*10;
		energyProperty.set(energy);
		maxEnergyProperty.set(energy);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		inv.addAllItems(new Coffee(), new Doritos(), new MountainDew());
	}
	


	@Override
	public int takeDmg(int dmg) {
		hitPoints -= (dmg-getStat(Stats.ENDURANCE));
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

	public boolean ability(Ability ability, Character targets) {
		if (ability instanceof AttackAbility) {
			return ability.use(this, targets);
		} else if (ability instanceof BuffAbility) {
			return ability.use(this, this);
		}
		return false;
		
	}
	

	public ObservableList<Ability> getAbilities() {
		ObservableList<Ability> abilities = FXCollections.observableArrayList();
		abilities.addAll(this.ABILITIES);
		
		return abilities;
	}

	public void setFloorNum(int i) {
		this.floorNum = i;
	}

	public Direction getDirectionFacing() {
		return directionFacing;
	}

	public void setDirectionFacing(Direction directionFacing) {
		this.directionFacing = directionFacing;
	}


}
