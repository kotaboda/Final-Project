package abilities;

import abilityInterfaces.AttackAbility;
import character.Character;

public class AssignFraculator extends AttackAbility {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3145717363225657336L;
	private boolean used = false;
	
	public AssignFraculator() {
		super("Assign Fraculator");
	}
	
	@Override
	public boolean use(Character user, Character... targets) {
		boolean successful = true;
		
		if(!used) {
			int init = user.attack();
			targets[0].takeDmg(init+30);
			used = true;
		} else {
			successful = false;
		}
		return successful;
	}

	@Override
	public String getDescription() {
		return "Assign the lab equivalent of doomsday to wreck the student's life.";
	}

}
