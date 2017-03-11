package character;

public class Krebsinator extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1846447040144482094L;

	public Krebsinator() {
		super("Krebsinator", 500, 76, new PickOnYou());
		levelUp(7);
		updateDerivedStats();
	}
}
