package character;

import characterEnums.Stats;
import models.Coordinates;

public class Project extends Enemy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7442909421705805038L;
	
	public Project() {
		super("Project", 300, 2);
		coordinates = new Coordinates(5,5);
		this.stats.put(Stats.MOTIVATION, 14);
		this.stats.put(Stats.INTELLIGIENCE, 13);
		this.stats.put(Stats.WIT, 12);
		this.stats.put(Stats.ENDURANCE, 14);
		this.stats.put(Stats.STAMINA, 12);
	}
}
