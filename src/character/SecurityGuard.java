package character;

import abilities.MinionAssault;
import abilities.PlayEnchantment;

public class SecurityGuard extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8572599965260907613L;

	public SecurityGuard() {
		super("Security Guard", 600, 72, new MinionAssault(), new PlayEnchantment());

	}

}
