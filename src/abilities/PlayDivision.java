package abilities;

import character.Character;
import enums.Character.ModifiableFields;
import interfaces.ability.BuffAbility;

public class PlayDivision extends BuffAbility {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7103691996894286086L;
	private boolean used = false;
	
	public PlayDivision() {
		super("Play Division");
	}
	
	@Override
	public boolean use(Character user, Character... targets) {
		boolean successful = true;
		if(!used) {
			user.modifyField(ModifiableFields.HITPOINTS, 100);
			used = true;
		}else {
			successful = false;
		}
		return successful;
	}

	@Override
	public String getDescription() {
		return "Unwind with some nice Division gameplay and recover even more motivated than before!";
	}

}
