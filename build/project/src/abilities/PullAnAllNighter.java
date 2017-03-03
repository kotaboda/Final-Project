package abilities;

import abilityInterfaces.AttackAbility;
import character.Character;

public class PullAnAllNighter extends AttackAbility {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4660865307316702884L;

	public PullAnAllNighter(){
		name = "Pull An All Nighter!";
	}

	@Override
	public void use(Character targets) {
		
	}

	@Override
	public String getDescription() {
		return "Ultimate attack that will drain all energy but inflict massive damage.";
	}

}
