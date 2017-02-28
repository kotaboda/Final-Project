package abilityInterfaces;

import character.Character;

public abstract class Ability {
	public String name;
	
	public String getName(){
		return name;
	}
	
	public abstract void use(Character targets);
	
	@Override
	public String toString(){
		return name;
	}

}
