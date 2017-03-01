package tiles;

import tileinterfaces.Collidable;

public class Wall extends Tile implements Collidable {

	public Wall(int tileSheetNum) {
		super(tileSheetNum);
	}
}
