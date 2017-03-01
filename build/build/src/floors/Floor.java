package floors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import battleSystem.Battle;
import battleSystem.BossBattle;
import character.Boss;
import character.Enemy;
import character.Player;
import drawinterfaces.Paintable;
import javafx.scene.image.Image;
import models.Coordinates;
import tileinterfaces.Collidable;
import tiles.Tile;

public abstract class Floor implements Paintable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4879286765234322293L;
	protected Tile[][] tiles;
	private Boss boss;
	private BossBattle bossBattle;
	private Battle[] battles;
	private Player player;
	private HashMap<Coordinates, Note> notes = new HashMap<>();
	

	public Tile[][] getTiles(){
		return this.tiles;
	}
	
	public void genBattles() {
		Random xD = new Random();
		int mapBorderX = getTiles()[0].length - 2;
		int mapBorderY = getTiles().length - 2;
		for(int i = 0 ; i < getBattles().length ; i++) {
			boolean validPlace = false;
			int x = 0;
			int y = 0;
			
			do {
				x = xD.nextInt(mapBorderY)+1;
				y = xD.nextInt(mapBorderX)+1;
				if(getTiles()[x][y] instanceof Collidable) {
					validPlace = true;
				} else {
					validPlace = false;
				}
			}while(!validPlace);
			
			Enemy[] enemies = new Enemy[xD.nextInt(3)+1];
			for (int j = 0; j < enemies.length; j++) {
				//TODO Dress up enemy generation here.
				enemies[j] = new Enemy();
			}
			getBattles()[i] = new Battle(getPlayer(), enemies);
			getBattles()[i].getCoordinates().setCoordinates(x, y);
		}
	}

	public void startBattle(Enemy e, Player p){
		
	}
	
	public void startBossBattle(Player p){
		
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

	public HashMap<Coordinates, Note> getNotes() {
		return notes;
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