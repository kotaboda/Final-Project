package viewInterface;

import battleSystem.Battle;
import character.PlayerSummary;
import floors.Floor;
import itemSystem.Inventory;

public interface Viewable {

	
	public void displayMainMenu();
	public void displayPauseMenu();
	public void displayBattleView(Battle b);
	public void displayGeneralView(Floor currentFloor);
	public void displayInventoryView(Inventory inv);
	public void displayCharacterManager();
	public void displayLootManager();
	public void displayEndBattle(Battle b);
}
