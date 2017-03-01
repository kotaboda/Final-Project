package itemSystem;

import java.io.Serializable;

public abstract class Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6732819968550686679L;
	protected String name;
	protected String description;
	
	public Item(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public String toString() {
		return name;
	}
}
