package abilities;

import abilityInterfaces.AttackAbility;
import character.Character;

public class PullAnAllNighter extends AttackAbility {
	
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
