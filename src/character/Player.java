package character;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import abilities.ExpertTimeManagement;
import abilities.Procrastinate;
import abilities.PullAnAllNighter;
import abilityInterfaces.Ability;
import abilityInterfaces.AttackAbility;
import abilityInterfaces.BuffAbility;
import application.GameEngine;
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

	private final ArrayList<Ability> ABILITIES = new ArrayList<>();
	private Direction directionFacing = Direction.DOWN;
	private final Genders sex;
	private transient Image worldIcon;
	
	public Player(String name, Genders gender, int tileSheetNum) {
		super(name, tileSheetNum);
		ABILITIES.addAll(Arrays.asList((new Procrastinate()), (new ExpertTimeManagement())));
		this.stats.put(Stats.INTELLIGIENCE, 100);
		this.stats.put(Stats.MOTIVATION, 100);
		this.hitPoints = stats.get(Stats.MOTIVATION) * 10;
		this.maxHitPoints = hitPoints;
		this.energy = stats.get(Stats.STAMINA) * 10;
		this.maxEnergy = energy;
		energyProperty.set(energy);
		maxEnergyProperty.set(energy);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		inv.addAllItems(new Coffee(), new Doritos(), new MountainDew());
		switch (gender) {
		case BOY:
			sex = Genders.BOY;
			worldImage = new Image(getClass().getResourceAsStream("/images/MaleWalk.png"));
			battleImage = new Image(getClass().getResourceAsStream("/images/malebattleicon.png"));
			worldIcon = new Image(getClass().getResourceAsStream("/images/maleicon.png"));
			takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/maletakedamage.png"));
			attackAnimation = new Image(getClass().getResourceAsStream("/images/maleattack.png"));
			break;
		case GIRL:
			sex = Genders.GIRL;
			worldImage = new Image(getClass().getResourceAsStream("/images/FemaleWalk.png"));
			battleImage = new Image(getClass().getResourceAsStream("/images/femalebattleicon.png"));
			worldIcon = new Image(getClass().getResourceAsStream("/images/femaleicon.png"));
			takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/femaletakedamage.png"));
			attackAnimation = new Image(getClass().getResourceAsStream("/images/femaleattack.png"));
			break;
		default:
			sex = Genders.BOY;
			break;

		}
	}

	public Player() {
		super();
		sex = Genders.BOY;
		ABILITIES.addAll(Arrays.asList(new PullAnAllNighter(), (new Procrastinate()), (new ExpertTimeManagement())));

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
		GameEngine.playTakeDamageAnimation(takeDamageAnimation, this);
		return dmg;
	}

	@Override
	public int attack() {
		GameEngine.playAttackAnimation(attackAnimation, this);
		int damage = 0;
		damage = getStat(Stats.INTELLIGIENCE);
		return damage;
	}

	public boolean ability(Ability ability, Character... enemies) {
		if (ability instanceof AttackAbility) {
			return ability.use(this, enemies);
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
		return worldImage;
	}
	
	public Image getWorldIcon() {
		return worldIcon;
	}
	
	@Override
	protected void levelUp(int level) {
		if (level > 0) {
			this.level += level;
			int currentMot = stats.get(Stats.MOTIVATION);
			int currentInt = stats.get(Stats.INTELLIGIENCE);
			int currentWit = stats.get(Stats.WIT);
			int currentEnd = stats.get(Stats.ENDURANCE);
			int currentSta = stats.get(Stats.STAMINA);
			this.stats.put(Stats.MOTIVATION, currentMot + 2);
			this.stats.put(Stats.INTELLIGIENCE, currentInt + 2);
			this.stats.put(Stats.WIT, currentWit + 2);
			this.stats.put(Stats.ENDURANCE, currentEnd + 2);
			this.stats.put(Stats.STAMINA, currentSta + 2);
		}
		if(this.level == 5){
			ABILITIES.add(new PullAnAllNighter());
		}
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
			battleImage = new Image(getClass().getResourceAsStream("/images/malebattleicon.png"));
			worldIcon = new Image(getClass().getResourceAsStream("/images/maleicon.png"));
			takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/maletakedamage.png"));
			attackAnimation = new Image(getClass().getResourceAsStream("/images/maleattack.png"));
			break;
		case GIRL:
			worldImage = new Image(getClass().getResourceAsStream("/images/FemaleWalk.png"));
			battleImage = new Image(getClass().getResourceAsStream("/images/femalebattleicon.png"));
			worldIcon = new Image(getClass().getResourceAsStream("/images/femaleicon.png"));
			takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/femaletakedamage.png"));
			attackAnimation = new Image(getClass().getResourceAsStream("/images/femaleattack.png"));
			break;
		default:
			break;
		}
	}

}
