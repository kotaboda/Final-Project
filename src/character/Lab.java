package character;

import java.io.IOException;
import java.io.ObjectInputStream;

import characterEnums.Stats;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import models.Coordinates;

public class Lab extends Enemy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3646917878935608668L;
	
	public Lab() {
		super("Lab", 150, 1);
		coordinates = new Coordinates(5,5);
		this.stats.put(Stats.MOTIVATION, 11);
		this.stats.put(Stats.INTELLIGIENCE, 10);
		this.stats.put(Stats.WIT, 11);
		this.stats.put(Stats.ENDURANCE, 11);
		this.stats.put(Stats.STAMINA, 10);
		battleImage = new Image(getClass().getResourceAsStream("/images/lab.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/labtakedamage.png"));
	}
	

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();		
		hpProperty = new SimpleIntegerProperty(hitPoints);
		maxHPProperty = new SimpleIntegerProperty(hitPoints);
		energyProperty = new SimpleIntegerProperty(energy);
		maxEnergyProperty = new SimpleIntegerProperty(maxEnergy);
		battleImage = new Image(getClass().getResourceAsStream("/images/lab.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/labtakedamage.png"));
	}
}
