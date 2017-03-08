package abilities;

import abilityInterfaces.AttackAbility;
import character.Character;

public class MinionAssault extends AttackAbility {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8041420699082019236L;

	public MinionAssault() {
		super("Minion Assault");
	}

	@Override
	public boolean use(Character user, Character... targets) {
		int main = user.attack()+2;
		int dmg = main/4;
		targets[0].takeDmg(dmg);
		targets[0].takeDmg(dmg);
		targets[0].takeDmg(dmg);
		targets[0].takeDmg(dmg);
		return true;
	}

	@Override
	public String getDescription() {
		return "Summon all of your minion cards' strength for a massive assault!";
	}

}
