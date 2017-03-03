package character;

import characterEnums.Stats;
import models.Coordinates;

public class Exercise extends Enemy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 804702981939770628L;
	
	public Exercise() {
		super("Exercise", 0);
		coordinates = new Coordinates(5,5);
		this.stats.put(Stats.MOTIVATION, 9);
		this.stats.put(Stats.INTELLIGIENCE, 9);
		this.stats.put(Stats.WIT, 10);
		this.stats.put(Stats.ENDURANCE, 9);
		this.stats.put(Stats.STAMINA, 10);
	}
}
