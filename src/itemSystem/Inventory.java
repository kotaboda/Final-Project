package itemSystem;

import java.util.ArrayList;

public class Inventory {

	private ArrayList<Item> items = new ArrayList<Item>();
	public final int invMax;

	public Inventory() {
		invMax = 20;
	}

	public Inventory(int invMax,Item...itemsToAdd ) {
		addAllItems(itemsToAdd);

		this.invMax = invMax;
	}

	public Item[] getItems() {
		return items.toArray(new Item[items.size()]);
	}

	public boolean addItem(Item item) {
		boolean successful = false;

		if (!isMaxed()) {
			successful = true;
			items.add(item);
		}

		return successful;
	}

	public boolean addAllItems(Item... itemsToAdd) {
		boolean successful = true;
		if (itemsToAdd.length + items.size() >= invMax) {
			successful = false;
		} else {
			for (Item thing : itemsToAdd) {
				addItem(thing);
			}
		}
		return successful;
	}
	
	public void clearInventory(){
		items.clear();
	}

	public boolean removeAllItems(Item...items) {
		
		for(Item item : items){
			if(!this.items.contains(item)){
				return false;
			}
		}
		
		for(int i = 0; i < items.length; i++){
			removeItem(items[i]);
		}
		
		return true;
	}
	
	public void removeItem(Item item) {
		items.remove(item);
	}
	
	public boolean isMaxed() {
		boolean max = false;
		
		if(items.size() == invMax) {
			max = true;
		}
		
		return max;
	}
	
	@Override
	public String toString() {
		StringBuilder bob = new StringBuilder("\nInventory:");
		
		for(Item thing : items) {
			bob.append("\n");
			bob.append(thing);
		}
		
		return bob.toString();
	}
}
