package character;

import java.io.IOException;
import java.io.ObjectInputStream;

import abilities.AssignFraculator;
import abilities.Lecture;
import abilities.PlayDivision;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class JoshKrebs extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1532878735222428440L;

	public JoshKrebs() {
		super("Josh Krebs", 1500, 74, new AssignFraculator(), new PlayDivision(), new Lecture());
		levelUp(14);
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
		
		//TODO Replace image paths with correct images
		battleImage = new Image(getClass().getResourceAsStream("/images/guardicon.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/guarddamage.png"));
		attackAnimation = new Image(getClass().getResourceAsStream("/images/guardattack.png"));
		
	}
}
