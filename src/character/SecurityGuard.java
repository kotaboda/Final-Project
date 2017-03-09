package character;

import abilities.MinionAssault;
import abilities.PlayEnchantment;
import enums.Character.Stats;
import itemSystem.Coffee;
import itemSystem.Doritos;
import itemSystem.MountainDew;
import itemSystem.Planeswalker;

public class SecurityGuard extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8572599965260907613L;

	public SecurityGuard() {
		super("Security Guard", 600, 72, new MinionAssault(), new PlayEnchantment());
		this.inv.clearInventory();
		this.inv.addItem(new Planeswalker());
		this.inv.addAllItems(new MountainDew(), new Doritos(), new Coffee());
		this.stats.clear();
		this.stats.put(Stats.MOTIVATION, 15);
		this.stats.put(Stats.INTELLIGIENCE, 13);
		this.stats.put(Stats.WIT, 15);
		this.stats.put(Stats.ENDURANCE, 13);
		this.stats.put(Stats.STAMINA, 13);
		this.hitPoints = stats.get(Stats.MOTIVATION) * 10;
		this.energy = stats.get(Stats.STAMINA) * 10;
	}

}
