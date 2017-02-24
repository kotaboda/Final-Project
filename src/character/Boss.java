package character;

public abstract class Boss extends Character {
	
	private boolean isDefeated = false;

	public Boss(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public Boss() {
		super();
	}
	
	public boolean isDefeated() {
		return isDefeated;
	}


	@Override
	public int takeDmg(int dmg) {
		// TODO Auto-generated method stub
		
		if(hitPoints <= 0){
			hitPoints = 0;
			isDefeated = true;
		}
		return 0;
	}

	@Override
	public int attack(Character target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "Boss [isDefeated=" + isDefeated + ", name=" + name + ", inv=" + inv + ", hitPoints=" + hitPoints
				+ ", energy=" + energy + ", level=" + level + ", floorNum=" + floorNum + "]";
	}
}
