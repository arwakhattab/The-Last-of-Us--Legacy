package model.characters;

import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;

public class Fighter extends Hero{
	
	public Fighter(String name,int maxHp, int attackDmg, int maxActions) {
		super( name, maxHp,  attackDmg,  maxActions) ;
		
	}
	
	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		if (this.isSpecialAction()) {
			this.setActionsAvailable(getActionsAvailable() + 1);
		}
		super.attack();
	}
	
}
