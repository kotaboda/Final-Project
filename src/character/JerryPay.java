
package character;

import java.io.IOException;
import java.io.ObjectInputStream;

import abilities.AddValue;
import abilities.AssignATeamAssignment;
import abilities.BoreToDeath;
import abilities.ShowAPowerpoint;
import enums.Character.Stats;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class JerryPay extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4892872452932024516L;

	public JerryPay() {
		super("Jerry Pay", 1200, 73, new ShowAPowerpoint(), new AssignATeamAssignment(), new AddValue(),
				new BoreToDeath());
		this.stats.put(Stats.INTELLIGIENCE, 15);
		this.stats.put(Stats.MOTIVATION, 20);
		this.stats.put(Stats.WIT, 7);
		this.stats.put(Stats.ENDURANCE, 4);
		this.stats.put(Stats.STAMINA, 3);
		levelUp(9);
		updateDerivedStats();
	}
	
	private void readObject(ObjectInputStream ois){
		try {
			ois.defaultReadObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		hpProperty = new SimpleIntegerProperty(hitPoints);
		maxHPProperty = new SimpleIntegerProperty(hitPoints);
		energyProperty = new SimpleIntegerProperty(energy);
		maxEnergyProperty = new SimpleIntegerProperty(maxEnergy);
		
		//TODO Replace the image paths
		battleImage = new Image(getClass().getResourceAsStream("/images/guardicon.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/guarddamage.png"));
		attackAnimation = new Image(getClass().getResourceAsStream("/images/guardattack.png"));
		
	}

}
