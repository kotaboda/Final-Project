package character;

import java.util.ArrayList;

import abilities.PullAnAllNighter;
import abilityInterfaces.Ability;
import abilityInterfaces.AttackAbility;
import abilityInterfaces.BuffAbility;
import characterEnums.Stats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class Player extends Character {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3349758486478712145L;

//	private final Image IMAGE = new Image();
	private final ArrayList<Ability> ABILITIES = new ArrayList<>();
//	private final int COLUMNS = 5;
//	private final int COUNT = 10;
//    private final int WIDTH = 32;
//    private final int HEIGHT = 32;

	public Player(String name, int tileSheetNum) {
		super(name, tileSheetNum);
		ABILITIES.add(new PullAnAllNighter());
	}
	
	public Player() {
		super();
		ABILITIES.add(new PullAnAllNighter());
		this.stats.put(Stats.INTELLIGIENCE, 100);
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

	public void ability(Ability ability, Character... targets) {
		if (ability instanceof AttackAbility) {
			for (int i = 0; i < targets.length; i++) {
				ability.use(targets[i]);
			}
		} else if (ability instanceof BuffAbility) {
			ability.use(this);
		}
		
	}
	
	public void move() {
//		 final ImageView imageView = new ImageView(IMAGE);
//	        imageView.setViewport(new Rectangle2D(WIDTH, HEIGHT, WIDTH, HEIGHT));

	}

	public ObservableList<Ability> getAbilities() {
		ObservableList<Ability> abilities = FXCollections.observableArrayList();
		abilities.addAll(this.ABILITIES);
		
		return abilities;
	}

	public void setFloorNum(int i) {
		this.floorNum = i;
	}




}
