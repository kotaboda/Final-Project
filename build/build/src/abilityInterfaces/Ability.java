package abilityInterfaces;

import java.io.Serializable;

import character.Character;

public abstract class Ability implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8165600092077902345L;
	public String name;
	
	public String getName(){
		return name;
	}
	
	public abstract void use(Character targets);
	public abstract String getDescription();
	
	@Override
	public String toString(){
		return name;
	}

}
