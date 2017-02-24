package floors;

import java.util.Random;

import battleSystem.Battle;
import battleSystem.BossBattle;
import character.Boss;
import character.Enemy;
import character.Player;
import drawinterfaces.Paintable;
import javafx.scene.image.Image;
import tileinterfaces.Collidable;
import tiles.Tile;

public abstract class Floor implements Paintable{
	
	protected Tile[][] tiles;
	private Boss boss;
	private Enemy[] enemies;
	private BossBattle bossBattle;
	private Battle[] battles;
	private Player player;
	

	public Tile[][] getTiles(){
		return this.tiles;
	}
	
	public void genEnemies() {
		Random xD = new Random();
		int mapBorderX = getTiles()[0].length - 2;
		int mapBorderY = getTiles().length - 2;
		for(int i = 0 ; i < getEnemies().length ; i++) {
			boolean notValidPlace = false;
			int x = 0;
			int y = 0;
			
			do {
				x = xD.nextInt(mapBorderY)+1;
				y = xD.nextInt(mapBorderX)+1;
				if(getTiles()[x][y] instanceof Collidable) {
					notValidPlace = false;
				} else {
					notValidPlace = true;
				}
			}while(notValidPlace);
			getEnemies()[i] = new Enemy();
			getEnemies()[i].getCoordinates().setCoordinates(x, y);
		}
	}

	public void startBattle(Enemy e, Player p){
		
	}
	
	public void startBossBattle(Player p){
		
	}
	
	public Enemy[] getEnemies() {
		return enemies;
	}
	
	public void setEnemies(Enemy...enemies) {
		this.enemies = enemies;
	}
	
	public Boss getBoss() {
		return boss;
	}
	
	public void setBoss(Boss boss) {
		this.boss = boss;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public BossBattle getBossBattle() {
		return bossBattle;
	}

	public void setBossBattle(BossBattle bossBattle) {
		this.bossBattle = bossBattle;
	}

	public Battle[] getBattles() {
		return battles;
	}

	public void setBattles(Battle[] battles) {
		this.battles = battles;
	}

	@Override
	public Image getWorldImage() {
		//TODO implement
		return null;
	}

	@Override
	public Image getBattleImage() {
		//TODO implement
		return null;
	}

	//TODO add a toString() override
	
}
