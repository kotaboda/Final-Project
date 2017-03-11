
package character;

import abilities.AddValue;
import abilities.AssignATeamAssignment;
import abilities.BoreToDeath;
import abilities.ShowAPowerpoint;

public class JerryPay extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4892872452932024516L;

	public JerryPay() {
		super("Jerry Pay", 1200, 73, new ShowAPowerpoint(), new AssignATeamAssignment(), new AddValue(),
				new BoreToDeath());
	}

}
