package character;

import java.util.Random;

import characterEnums.Stats;
import itemSystem.Coffee;
import itemSystem.Doritos;
import itemSystem.Inventory;
import itemSystem.MountainDew;
import itemSystem.Ramen;

public class Enemy extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4495778022214568163L;

	public Enemy(String name, int tileSheetNum) {
		super(name, tileSheetNum);
		this.stats.put(Stats.MOTIVATION, 1);
		this.stats.put(Stats.INTELLIGIENCE, 1);
		this.stats.put(Stats.WIT, 1);
		this.stats.put(Stats.ENDURANCE, 1);
		this.stats.put(Stats.STAMINA, 1);
		this.hitPoints = stats.get(Stats.MOTIVATION) * 5;
		this.energy = stats.get(Stats.STAMINA) * 5;
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		inv = genLoot();
	}

	public Enemy() {
		super();
	}

	private Inventory genLoot() {
		Inventory tempInv = new Inventory();
		Random xD = new Random();

		int howMuchLoot = xD.nextInt(3)+1;
		int item = xD.nextInt(4);
		
		for (int i = 0; i < howMuchLoot; i++) {
			switch (item) {
			case 0:
				tempInv.addItem(new Coffee());
				break;
			case 1:
				tempInv.addItem(new Doritos());
				break;
			case 2:
				tempInv.addItem(new MountainDew());
				break;
			case 3:
				tempInv.addItem(new Ramen());
				break;
			}
		}

		return tempInv;
	}

	@Override
	public int takeDmg(int dmg) {
		hitPoints -= (dmg - getStat(Stats.ENDURANCE) + 2);
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		hpProperty.set(hitPoints);
		return dmg;
	}

	@Override
	public int attack() {
		int damage = 0;
		damage = getStat(Stats.INTELLIGIENCE);
		return damage;
	}

}
