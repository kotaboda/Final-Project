package character;

import abilities.MinionAssault;
import abilities.PlayEnchantment;
import enums.Character.Stats;
import javafx.scene.image.Image;

public class SecurityGuard extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8572599965260907613L;

	public SecurityGuard() {
		super("Security Guard", 600, 72, new MinionAssault(), new PlayEnchantment());
		this.stats.put(Stats.MOTIVATION, 18);
		this.stats.put(Stats.INTELLIGIENCE, 15);
		this.stats.put(Stats.WIT, 13);
		this.stats.put(Stats.ENDURANCE, 16);
		this.stats.put(Stats.STAMINA, 13);
		this.hitPoints = stats.get(Stats.MOTIVATION);
		this.energy = stats.get(Stats.STAMINA);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		battleImage = new Image(getClass().getResourceAsStream("/images/guardicon.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/guarddamage.png"));
		attackAnimation = new Image(getClass().getResourceAsStream("/images/guardattack.png"));
	}

}
