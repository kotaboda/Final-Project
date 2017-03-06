package floors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import battleSystem.Battle;
import battleSystem.BossBattle;
import character.Boss;
import character.Enemy;
import character.Exercise;
import character.Lab;
import character.Player;
import character.Project;
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
	protected Boss boss;
	private BossBattle bossBattle;
	private Battle[] battles;
	private Player player;
	protected Coordinates playerStart;
	private HashMap<Coordinates, Note> notes = new HashMap<>();
	
	public Coordinates getPlayerStart() {
		return playerStart;
	}
	
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
				if(getTiles()[x][y] instanceof Collidable && getBoss().getCoordinates().equals(new Coordinates(x,y))) {
					validPlace = false;
				} else {
					validPlace = true;
				}
			}while(!validPlace);
			
			Enemy[] enemies = new Enemy[xD.nextInt(3)+1];
			for (int j = 0; j < enemies.length; j++) {
				//TODO Dress up enemy generation here.
				int e = xD.nextInt(3);
				switch(e) {
				case 0:
					enemies[j] = new Exercise();
					break;
				case 1:
					enemies[j] = new Lab();
					break;
				case 2:
					enemies[j] = new Project();
					break;
				}
			}
			getBattles()[i] = new Battle(getPlayer(), enemies);
			getBattles()[i].getCoordinates().setCoordinates(x, y);
		}
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
	
	public boolean bossIsDefeated(){
		return boss.isDefeated();
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
