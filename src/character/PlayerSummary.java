package character;

import javafx.beans.property.IntegerProperty;

public class PlayerSummary {
	
	public final int playerHealth;
	public final int maxHealth;
	public final String playerName;
	public final IntegerProperty hpProperty;
	public final IntegerProperty maxHPProperty;

	
	public PlayerSummary(Player p){
		playerName = p.name;
		playerHealth = p.getCurrentHealth();
		maxHealth = p.getMaxHealth();
		hpProperty = p.hpProperty;
		maxHPProperty = p.maxHPProperty;
		
	}
}
