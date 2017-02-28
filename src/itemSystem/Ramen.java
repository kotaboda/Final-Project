package itemSystem;

import character.Character;
import characterEnums.ModifiableFields;

public class Ramen extends Item implements Usable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 889744114445606559L;

	public Ramen() {
		super("Ramen", "Fake Asian cuisine college students eat to survive.");
	}

	@Override
	public void use(Character target) {
		target.modifyField(ModifiableFields.HITPOINTS, 100);
	}

}
