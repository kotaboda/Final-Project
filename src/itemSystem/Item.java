package itemSystem;

import java.io.Serializable;

public abstract class Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6732819968550686679L;
	public final String name;
	
	public Item(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
