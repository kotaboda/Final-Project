package tiles;

public abstract class Tile {

	private final int tileSheetNum;
	
	public Tile(int tileSheetNum){
		this.tileSheetNum = tileSheetNum;
	}
	
	public int getTileSheetNum(){
		return this.tileSheetNum;
	}
}
