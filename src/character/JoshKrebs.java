package character;

import abilities.AssignFraculator;
import abilities.Lecture;
import abilities.PlayDivision;

public class JoshKrebs extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1532878735222428440L;

	public JoshKrebs() {
		super("Josh Krebs", 1500, 74, new AssignFraculator(), new PlayDivision(), new Lecture());
	}
}
