package character;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import characterEnums.InventoryAction;
import characterEnums.ModifiableFields;
import characterEnums.Stats;
import itemSystem.Inventory;
import itemSystem.Item;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import models.Coordinates;
import publisherSubscriberInterfaces.Listener;
import publisherSubscriberInterfaces.Subscribable;

public abstract class Character implements Subscribable<Character>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -888637062240963044L;
	public final String name;
	protected Inventory inv;
	protected int hitPoints = 100;
	protected IntegerProperty hpProperty = new SimpleIntegerProperty();
	protected IntegerProperty maxHPProperty = new SimpleIntegerProperty();
	protected int energy = 100;
	protected int level = 1;
	protected Coordinates coordinates;
	protected int floorNum;
	private int creditReq = 150;
	private int currentCredits = 0;
	protected ArrayList<Listener<Character>> subscribers = new ArrayList<>();
	protected BufferedImage worldImage;
	protected BufferedImage battleImage;
	protected HashMap<Stats, Integer> stats = new HashMap<>();
	private int maxHitPoints = hitPoints;

	public Character(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		} else if (name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty.");
		}
		this.name = name;
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		
		//TODO Complete Paths to Image Files.
//		try {
//			worldImage = ImageIO.read(new File("Images/World Images/"));
//			battleImage = ImageIO.read(new File("Images/Battle Images/"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	public Character() {
		this.name = "test";
		floorNum = 1;
		inv = new Inventory();
		coordinates = new Coordinates(5,5);
	}

	protected void levelUp(int level) {
		if (level > 0) {
			level += level;
		}
	}

	public boolean giveCredits(int credits) {
		boolean leveledUp = false;
		currentCredits += credits;
		do {
			if (currentCredits >= creditReq) {
				leveledUp = true;
				currentCredits -= creditReq;
				levelUp(1);
				creditReq *= 2;
				if (currentCredits < creditReq) {
					break;
				}
			}
		} while (true);
		return leveledUp;
	}
	
	public int getLevel() {
		return level;
	}
	
	public IntegerProperty getHPProperty(){
		return hpProperty;
	}
	
	public IntegerProperty getMaxHPProperty(){
		return maxHPProperty;
	}
	
	public int getNumOfCredits(){
		return currentCredits;
	}
	
	public int getCreditRequirement(){
		return creditReq;
	}

	public Item[] getInventoryContents() {
		return inv.getItems();
	}

	public boolean modifyInventory(InventoryAction ACTION, Item... items) {
		boolean successful = false;
		switch (ACTION) {
		case GIVE:
			successful = inv.addAllItems(items);
			break;
		case TAKE:
			successful = inv.removeAllItems(items);
			break;
		}
		return successful;
	}

	public void modifyField(ModifiableFields FIELD, int modification) {
		switch (FIELD) {
		case HITPOINTS:
			hitPoints += modification;
			hitPoints = hitPoints < 0 ? 0 : hitPoints;
			break;
		case ENERGY:
			energy += modification;
			energy = energy < 0 ? 0 : energy;
			break;
		}
	}

	public int getStat(Stats STAT) {
		return stats.get(STAT);
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	@Override
	public void addSubscriber(Listener<Character> listener) {
		subscribers.add(listener);
	}

	@Override
	public void notifySubscribers() {
		for (Listener<Character> subscriber : subscribers) {
			subscriber.notify();
		}
	}
	@Override
	public void removeSubscriber(Listener<Character> sub) {
		subscribers.remove(sub);
	
	}
	
	public int getCurrentHealth(){
		return hitPoints;
	}
	
	public int getMaxHealth(){
		return maxHitPoints;
	}

	public int getFloorNum(){
		return floorNum;
	}
	
	public int compareTo(Character chara) {
		int num = 0;
		if(this.getStat(Stats.WIT) > chara.getStat(Stats.WIT)) {
			num = 1;
		} else if(this.getStat(Stats.WIT) < chara.getStat(Stats.WIT)) {
			num = -1;
		}
		return num;
	}
	public abstract int takeDmg(int dmg);

	public abstract int attack(Character target);

	@Override
	public String toString() {
		return "Character [name=" + name + ", hitPoints=" + hitPoints + ", energy=" + energy + ", level=" + level
				+ ", floorNum=" + floorNum + "]";
	}

}