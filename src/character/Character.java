package character;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javafx.scene.image.Image;
import models.Coordinates;
import publisherSubscriberInterfaces.Listener;
import publisherSubscriberInterfaces.Subscribable;
import tileinterfaces.Collidable;
import tiles.Tile;

public abstract class Character extends Tile implements Subscribable<Character>, Serializable, Collidable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -888637062240963044L;
	public final String NAME;
	protected Inventory inv = new Inventory();
	protected int hitPoints = 100;
	protected transient IntegerProperty hpProperty = new SimpleIntegerProperty(hitPoints);
	protected transient IntegerProperty maxHPProperty = new SimpleIntegerProperty(hitPoints);
	protected int energy = 100;
	protected int level = 1;
	protected Coordinates coordinates = new Coordinates(0, 0);
	protected int floorNum;
	private int creditReq = 150;
	private int currentCredits = 0;
	protected ArrayList<Listener<Character>> subscribers = new ArrayList<>();
	protected transient Image worldImage;
	protected transient Image battleImage;
	protected HashMap<Stats, Integer> stats = new HashMap<>();
	private int maxHitPoints = 100;
	protected int maxEnergy = 100;
	protected transient IntegerProperty maxEnergyProperty = new SimpleIntegerProperty(energy);
	protected transient IntegerProperty energyProperty = new SimpleIntegerProperty(energy);


	public Character(String name, int tileSheetNum) {
		super(tileSheetNum);
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		} else if (name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty.");
		}
		this.coordinates = new Coordinates(0,0);
		this.stats.put(Stats.MOTIVATION, 1);
		this.stats.put(Stats.INTELLIGIENCE, 1);
		this.stats.put(Stats.WIT, 1);
		this.stats.put(Stats.ENDURANCE, 1);
		this.stats.put(Stats.STAMINA, 1);
		this.NAME = name;
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
		super(1);
		this.NAME = "test";
		floorNum = 1;
		inv = new Inventory();
		coordinates = new Coordinates(5,5);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		this.stats.put(Stats.MOTIVATION, 1);
		this.stats.put(Stats.INTELLIGIENCE, 1);
		this.stats.put(Stats.WIT, 1);
		this.stats.put(Stats.ENDURANCE, 1);
		this.stats.put(Stats.STAMINA, 1);
	}

	protected void levelUp(int level) {
		if (level > 0) {
			this.level += level;
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
			} else {
				break;				
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
	public IntegerProperty getMaxEnergyProperty() {
		return maxEnergyProperty;
	}
	public IntegerProperty getEnergyProperty() {
		return energyProperty;
	}
	
	public int getNumOfCredits(){
		return currentCredits;
	}
	
	public Image getBattleImage() {
		return battleImage;
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
			hpProperty.set(hitPoints);
			break;
		case ENERGY:
			energy += modification;
			energy = energy < 0 ? 0 : energy;
			energyProperty.set(energy);
			break;
		}
	}

	public int getStat(Stats STAT) {
		return stats.get(STAT);
	}
	
	public HashMap<Stats, Integer> getStats() {
		return stats;
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
	
	public int getMaxEnergy() {
		return maxEnergy;
	}
	
	public int getMaxHealth(){
		return maxHitPoints;
	}

	public int getFloorNum(){
		return floorNum;
	}
	
	public int compareWit(Character chara) {
		int num = 0;
		if(this.getStat(Stats.WIT) > chara.getStat(Stats.WIT)) {
			num = 1;
		} else if(this.getStat(Stats.WIT) < chara.getStat(Stats.WIT)) {
			num = -1;
		}
		return num;
	}
	public abstract int takeDmg(int dmg);

	public abstract int attack();

	@Override
	public String toString() {
		return "Character [name=" + NAME + ", hitPoints=" + hitPoints + ", energy=" + energy + ", level=" + level
				+ ", floorNum=" + floorNum + "]";
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{		
		out.defaultWriteObject();
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();		
		hpProperty = new SimpleIntegerProperty(hitPoints);
		maxHPProperty = new SimpleIntegerProperty(hitPoints);
		energyProperty = new SimpleIntegerProperty(energy);
		maxEnergyProperty = new SimpleIntegerProperty(maxEnergy);
	}

	public boolean useEnergy(int i) {
		if (i <= energy) {
			energy -= i;
			energyProperty.set(energy);
			return true;
		}
		return false;
	}

	public int getEnergy() {
		// TODO Auto-generated method stub
		return energy;
	}

	public void gainEnergy(int i) {
		energy += i;
		energy = energy > maxEnergy ? maxEnergy : energy;
		energyProperty.set(energy);
		
	}
	
	
	
	
	
	
	
	

}