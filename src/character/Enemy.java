package character;

public class Enemy extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4495778022214568163L;

	public Enemy(String name, int tileSheetNum) {
		super(name, tileSheetNum);
		// TODO Auto-generated constructor stub
	}
	
	public Enemy() {
		super();
	}
	@Override
	public int takeDmg(int dmg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int attack(Character target) {
		// TODO Auto-generated method stub
		return 0;
	}

}
