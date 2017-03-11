package character;

import enums.Character.Stats;

public class Krebsinator extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1846447040144482094L;

	public Krebsinator() {
		super("Krebsinator", 500, 76, new PickOnYou());
		this.stats.put(Stats.INTELLIGIENCE, 15);
		this.stats.put(Stats.MOTIVATION, 20);
		this.stats.put(Stats.WIT, 7);
		this.stats.put(Stats.ENDURANCE, 3);
		this.stats.put(Stats.STAMINA, 3);
		levelUp(7);
		updateDerivedStats();
	}
}
