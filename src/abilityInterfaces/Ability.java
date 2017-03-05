package abilityInterfaces;

import java.io.Serializable;

import character.Character;

public abstract class Ability implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8165600092077902345L;
	public final String NAME;
	
	public Ability(String name){
		NAME = name;
	}
	
	public String getName(){
		return NAME;
	}
	
	public abstract boolean use(Character user, Character targets);
	public abstract String getDescription();
	
	@Override
	public String toString(){
		return NAME;
	}

}
