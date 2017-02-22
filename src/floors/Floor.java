package floors;

import battleSystem.Battle;
import battleSystem.BossBattle;
import character.Boss;
import character.Enemy;
import character.Player;
import drawinterfaces.Paintable;
import javafx.scene.image.Image;
import tiles.Tile;

public abstract class Floor implements Paintable{
	
	private Tile[][] tiles;
	private Boss boss;
	private Enemy[] enemies;
	private BossBattle bossBattle;
	private Battle[] battles;
	

	public Tile[][] getTiles(){
		return this.tiles;
	}
	
	public void startBattle(Enemy e, Player p){
		
	}
	
	public void startBossBattle(Player p){
		
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
