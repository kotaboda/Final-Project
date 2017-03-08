package abilities;

import abilityInterfaces.BuffAbility;
import character.Character;
import characterEnums.ModifiableFields;
import characterEnums.Stats;

public class AddValue extends BuffAbility {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6245588048802577832L;
	private boolean used = false;
	
	public AddValue() {
		super("Add Value");
	}
	
	@Override
	public boolean use(Character user, Character... targets) {
		boolean successful = true;
		
		if(!used) {
			int temp = user.getStat(Stats.INTELLIGIENCE);
			user.getStats().remove(Stats.INTELLIGIENCE);
			user.getStats().put(Stats.INTELLIGIENCE, temp+5);
			user.modifyField(ModifiableFields.HITPOINTS, 50);
			used = true;
		} else {
			successful = false;
		}
		return successful;
	}

	@Override
	public String getDescription() {
		return "Adds value to the battle by increasing his teaching effectiveness.";
	}

}