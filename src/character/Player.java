package character;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import abilities.Procrastinate;
import abilities.PullAnAllNighter;
import abilityInterfaces.Ability;
import abilityInterfaces.AttackAbility;
import abilityInterfaces.BuffAbility;
import characterEnums.Direction;
import characterEnums.Stats;
import enums.Genders;
import itemSystem.Coffee;
import itemSystem.Doritos;
import itemSystem.MountainDew;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class Player extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3349758486478712145L;

	// private final Image IMAGE = new Image();
	private final ArrayList<Ability> ABILITIES = new ArrayList<>();
	private Direction directionFacing = Direction.DOWN;
	private final Genders sex;
	// private final int COLUMNS = 5;
	// private final int COUNT = 10;
	// private final int WIDTH = 32;
	// private final int HEIGHT = 32;

	public Player(String name, Genders gender, int tileSheetNum) {
		super(name, tileSheetNum);
		ABILITIES.add(new PullAnAllNighter());
		ABILITIES.add(new Procrastinate());
		this.stats.put(Stats.INTELLIGIENCE, 100);
		this.stats.put(Stats.MOTIVATION, 100);
		this.hitPoints = stats.get(Stats.MOTIVATION) * 10;
		this.energy = stats.get(Stats.STAMINA) * 10;
		energyProperty.set(energy);
		maxEnergyProperty.set(energy);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		inv.addAllItems(new Coffee(), new Doritos(), new MountainDew());
		switch (gender) {
		case BOY:
			sex = Genders.BOY;
			worldImage = new Image(getClass().getResourceAsStream("/images/MaleWalk.png"));
			break;
		case GIRL:
			sex = Genders.GIRL;
			worldImage = new Image(getClass().getResourceAsStream("/images/FemaleWalk.png"));
			break;
		default:
			sex = Genders.BOY;
			break;

		}
	}

	public Player() {
		super();
		sex = Genders.BOY;
		ABILITIES.add(new PullAnAllNighter());
		ABILITIES.add(new Procrastinate());
		this.stats.put(Stats.INTELLIGIENCE, 100);
		this.stats.put(Stats.MOTIVATION, 100);
		this.hitPoints = stats.get(Stats.MOTIVATION) * 10;
		this.energy = stats.get(Stats.STAMINA) * 10;
		energyProperty.set(energy);
		maxEnergyProperty.set(energy);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		inv.addAllItems(new Coffee(), new Doritos(), new MountainDew());
	}

	@Override
	public int takeDmg(int dmg) {
		hitPoints -= (dmg - getStat(Stats.ENDURANCE));
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

	public Image getWorldImage() {
		// TODO Auto-generated method stub
		return worldImage;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		hpProperty = new SimpleIntegerProperty(hitPoints);
		maxHPProperty = new SimpleIntegerProperty(hitPoints);
		energyProperty = new SimpleIntegerProperty(energy);
		maxEnergyProperty = new SimpleIntegerProperty(maxEnergy);
		switch (sex) {
		case BOY:
			worldImage = new Image(getClass().getResourceAsStream("/images/MaleWalk.png"));
			break;
		case GIRL:
			worldImage = new Image(getClass().getResourceAsStream("/images/FemaleWalk.png"));
			break;
		default:
			break;
		}
	}

}
