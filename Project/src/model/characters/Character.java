package model.characters;

import java.awt.Point;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import engine.Game;
import model.world.CharacterCell;

public abstract class Character {
	
	private String name;
	private Point location;
	private int maxHp;
	private int currentHp;
	private int attackDmg;
	private Character target;

	public Character() {
		
	}
	
	public Character(String name, int maxHp, int attackDmg) {
		this.name=name;
		this.maxHp = maxHp;
		this.currentHp = maxHp;
		this.attackDmg = attackDmg;
	}
		
	public Character getTarget() {
		return target;
	}

	public void setTarget(Character target) {
		this.target = target;
	}
	
	public String getName() {
		return name;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		if(currentHp < 0) 
			this.currentHp = 0;
		else if(currentHp > maxHp) 
			this.currentHp = maxHp;
		else 
			this.currentHp = currentHp;
	}

	public int getAttackDmg() {
		return attackDmg;
	}
	
	public boolean isAdjacent(Character c) {
		int x1 = this.location.x;
		int y1 = this.location.y;
		int x2 = c.location.x;
		int y2 = c.location.y;
		for (int i = x2 - 1; i <= x2 + 1; i++) {
			for (int j = y2 - 1; j <= y2 + 1; j++) {
				if (i == x1 && j == y1) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		target.setCurrentHp(target.currentHp - attackDmg);
		target.defend(this);
		if (target.currentHp == 0) {
			target.onCharacterDeath();
		}
	}

	public void defend(Character c) {
		this.setTarget(c);
		target.setCurrentHp(target.currentHp - attackDmg/2);
		if (target.currentHp == 0) {
			target.onCharacterDeath();
		}
		this.setTarget(null);
	}

	public void onCharacterDeath() {
		((CharacterCell) Game.map[this.location.x][this.location.y]).setCharacter(null);
	}
	
}
