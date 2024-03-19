package model.characters;

import java.awt.Point;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import model.world.Cell;
import model.world.CharacterCell;

public class Zombie extends Character {
	static int ZOMBIES_COUNT = 1;
	
	public Zombie() {
		super("Zombie " + ZOMBIES_COUNT, 40, 10);
		ZOMBIES_COUNT++;
	}
	
	public Hero findAdjacentHero() {
		int x = this.getLocation().x;
		int y = this.getLocation().y;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && j >= 0 && i <= 14 && j <= 14 ) {
					Cell currCell = Game.map[i][j];
					if (currCell instanceof CharacterCell) {
						Character adjacent = ((CharacterCell) currCell).getCharacter();
						if (adjacent instanceof Hero) {
							return (Hero) adjacent;
						}
					}
				}
			}
		}
		return null;
	}
	
	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		this.setTarget(this.findAdjacentHero());
		if (this.getTarget() != null) {
			super.attack();
			this.setTarget(null);
		}
	}
	
	public void onCharacterDeath() {
		while (true) {	
		    int x = (int)(15*Math.random());
			int y = (int)(15*Math.random());
			Cell current = Game.map[x][y];
			if (current instanceof CharacterCell && ((CharacterCell) current).getCharacter() == null) {
				Zombie newZombie = new Zombie();
				((CharacterCell) current).setCharacter(newZombie);
				newZombie.setLocation(new Point(x, y));
				Game.zombies.add(newZombie);
				break;
			}
		}
		super.onCharacterDeath();
		Game.zombies.remove(this);
	}
	
}


