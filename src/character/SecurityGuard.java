package character;

import abilities.MinionAssault;
import abilities.PlayEnchantment;
import abilityInterfaces.Ability;

public class SecurityGuard extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8572599965260907613L;
	
	public SecurityGuard() {
		super("Security Guard", 600, 72, (new Ability[] {
			(new MinionAssault()), (new PlayEnchantment())	
		}));
	}

}
