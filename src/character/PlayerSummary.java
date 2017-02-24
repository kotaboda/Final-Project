package character;

import javafx.beans.property.IntegerProperty;
import models.Coordinates;

public class PlayerSummary {
	
	public final int playerHealth;
	public final int maxHealth;
	public final String playerName;
	public final IntegerProperty hpProperty;
	public final IntegerProperty maxHPProperty;
	public final Coordinates coordinates;

	
	public PlayerSummary(Player p){
		playerName = p.name;
		playerHealth = p.getCurrentHealth();
		maxHealth = p.getMaxHealth();
		hpProperty = p.hpProperty;
		maxHPProperty = p.maxHPProperty;
		coordinates = p.getCoordinates();
	}
}
