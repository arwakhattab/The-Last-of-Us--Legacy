package model.characters;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;

public class Medic extends Hero {
	
	public Medic(String name,int maxHp, int attackDmg, int maxActions) {
		super( name, maxHp,  attackDmg,  maxActions) ;
		
		
	}
	
	public void useSpecial() throws InvalidTargetException, NoAvailableResourcesException {
		if (this.getTarget() == null) {
			throw new InvalidTargetException("You must choose a target to heal!");
		}
		if (this.getTarget() instanceof Zombie) {
			throw new InvalidTargetException("You can't heal a zombie!");
		}
		if (!this.isAdjacent(getTarget())) {
			throw new InvalidTargetException("You can't heal a non-adjacent character!");
		}
		super.useSpecial();
		this.getTarget().setCurrentHp(getMaxHp());
	}
}
