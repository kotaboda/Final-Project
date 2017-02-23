package character;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

<<<<<<< HEAD
import characterEnums.InventoryAction;
=======
>>>>>>> Dakota
import characterEnums.ModifiableFields;
import characterEnums.Stats;
import characterInterfaces.Listener;
import characterInterfaces.Subscribable;
import itemSystem.Inventory;
import itemSystem.Item;
import models.Coordinates;

public abstract class Character implements Subscribable<Character>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -888637062240963044L;
	public final String name;
	protected Inventory inv;
	protected int hitPoints = 100;
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

	public Character(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		} else if (name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty.");
		}
		this.name = name;
		
		//TODO Complete Paths to Image Files.
		worldImage = ImageIO.read(new File("Images/World Images/"));
		battleImage = ImageIO.read(new File("Images/Battle Images/"));
		
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
	
	public int getNumOfCredits(){
		return currentCredits;
	}
	
	public int getCreditRequirement(){
		return creditReq;
	}

	public Item[] getInventoryContents() {
		return inv.getItems();
	}

	public boolean modifyInventory(InvAction ACTION, Item... items) {
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

	public void addSubscribers(Listener<Character> listener) {
		subscribers.add(listener);
	}

	public void notifySubscribers() {
		for (Listener<Character> subscriber : subscribers) {
			subscriber.notify();
		}
	}
	
<<<<<<< HEAD
	@Override
	public void removeSubscriber(Listener<Character> sub) {
		subscribers.remove(sub);
=======
	public int compareTo(Character chara) {
		int num = 0;
		if(this.getStat(Stats.WIT) > chara.getStat(Stats.WIT)) {
			num = 1;
		} else if(this.getStat(Stats.WIT) < chara.getStat(Stats.WIT)) {
			num = -1;
		}
		return num;
>>>>>>> Dakota
	}

	public abstract int takeDmg(int dmg);

	public abstract int attack(Character target);

	@Override
	public String toString() {
		return "Character [name=" + name + ", hitPoints=" + hitPoints + ", energy=" + energy + ", level=" + level
				+ ", floorNum=" + floorNum + "]";
	}

}

import javax.imageio.ImageIO;
import characterEnums.InventoryAction;
	}
	@Override
	public void removeSubscriber(Listener<Character> sub) {
		subscribers.remove(sub);