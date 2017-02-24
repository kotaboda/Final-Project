package floors;

import java.util.Random;

import character.Boss;
import character.Enemy;
import character.Player;
import tileinterfaces.Collidable;
import tiles.Tile;
import tiles.TileManager;

public class Floor1 extends Floor {
	
	public Floor1() {
		genTiles();
		setBoss(new Boss("Life"){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
		});
		setPlayer(new Player("Joe"));
		//getBoss().getCoordinates().setCoordinates(9, 1);
		//getPlayer().getCoordinates().setCoordinates(9, 12);
		setEnemies(new Enemy[5]);
//		genEnemies();
		
		Random xD = new Random();
		int mapBorderX = 7;
		int mapBorderY = 12;
		for(int i = 0 ; i < getEnemies().length ; i++) {
			boolean notValidPlace = false;
			int x = 0;
			int y = 0;
			
			do {
				x = xD.nextInt(mapBorderY)+1;
				y = xD.nextInt(mapBorderX)+1;
				if(getTiles()[x - 1][y - 1] instanceof Collidable) {
					notValidPlace = false;
				} else {
					notValidPlace = true;
				}
			}while(notValidPlace);
			
			//getEnemies()[i].getCoordinates().setCoordinates(x, y);
		}
		
		genTiles();
	}
	
	public void genTiles() {
		int[][] tileRefs = {
				{56,54,54,54,54,54,54,54,54,4,57},
				{58,12,5,5,5,5,5,5,5,5,59},
				{58,5,5,5,5,5,5,5,5,5,59},
				{58,5,52,5,52,5,52,5,52,5,59},
				{58,5,55,5,55,5,55,5,55,5,59},
				{58,5,5,24,5,5,5,5,5,5,59},
				{58,5,55,5,37,5,55,5,55,5,59},
				{58,5,5,5,5,12,5,5,5,5,59},
				{58,5,55,5,55,5,55,5,37,5,59},
				{58,5,52,5,52,5,52,5,52,5,59},
				{58,5,27,5,5,5,5,5,5,5,59},
				{58,5,5,5,5,5,5,5,5,5,59},
				{58,5,5,5,5,5,5,24,5,5,3},
				{58,5,5,5,5,5,5,5,5,5,59},
				{48,53,53,53,53,53,53,53,53,53,49}
		};
		TileManager tileMan= new TileManager();
		this.tiles = new Tile[tileRefs.length][tileRefs[0].length];
		
		for(int i = 0 ; i < tileRefs.length ; i++) {
			for(int j = 0 ; j < tileRefs[i].length ; j++) {
				this.tiles[i][j] = tileMan.createTile(tileRefs[i][j]);
			}
		}
	}
}
