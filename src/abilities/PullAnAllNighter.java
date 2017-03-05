package abilities;

import abilityInterfaces.AttackAbility;
import character.Character;

public class PullAnAllNighter extends AttackAbility {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4660865307316702884L;

	public PullAnAllNighter(){
		super("Pull An All Nighter!");
	}

	@Override
	public boolean use(Character user, Character targets) {
		if(user.getEnergy() > 3 && user.useEnergy(user.getEnergyProperty().get())){
			targets.takeDmg(user.attack() * 3);
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Ultimate attack that will drain all energy but inflict massive damage. A minimum of 3 energy is required to use this ability.";
	}

}
