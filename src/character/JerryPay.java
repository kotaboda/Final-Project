
package character;

import abilities.AddValue;
import abilities.AssignATeamAssignment;
import abilities.ShowAPowerpoint;
import abilityInterfaces.Ability;

public class JerryPay extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4892872452932024516L;

	public JerryPay() {
		super("Jerry Pay", 1000, 73, (new Ability[] {
				(new ShowAPowerpoint()), (new AssignATeamAssignment()), (new AddValue())
		}));
	}
}
