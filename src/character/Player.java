package character;

import abilityInterfaces.Ability;
import abilityInterfaces.AttackAbility;
import abilityInterfaces.BuffAbility;

public class Player extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7445111099520272415L;

	public Player(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int takeDmg(int dmg) {
		// TODO Come up with final implementation
		hitPoints -= dmg;
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		return dmg;
	}

	@Override
	public int attack() {
		// TODO Come up with final implementation
		return 10;
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
}
