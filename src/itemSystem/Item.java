package itemSystem;

public abstract class Item implements Usable{
	
	public final String name;
	
	public Item(String name) {
		this.name = name;
	}
}
