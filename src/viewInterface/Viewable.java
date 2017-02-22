package viewInterface;

import battleSystem.Battle;
import itemSystem.Inventory;

public interface Viewable {

	
	public void displayMainMenu();
	public void displayPauseMenu();
	public void displayBattleView(Battle b);
	public void displayGeneralView();
	public void displayInventoryView(Inventory inv);
	public void displayCharacterManager();
	public void displayLootManager();
	
}
