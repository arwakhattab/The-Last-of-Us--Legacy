package model.collectibles;

import engine.Game;
import model.characters.Hero;
import model.characters.Zombie;
import model.world.CharacterCell;

public class Vaccine implements Collectible {

	public Vaccine() {
		
	}
	
	public void pickUp(Hero h) {
		h.getVaccineInventory().add(this);
	}

	public void use(Hero h) {
		h.getVaccineInventory().remove(this);
		Zombie cured = (Zombie) h.getTarget();
		Game.zombies.remove(cured);
		Hero newHero = Game.availableHeroes.remove((int) (Game.availableHeroes.size()*Math.random()));
		newHero.setLocation(cured.getLocation());
		((CharacterCell) Game.map[newHero.getLocation().x][newHero.getLocation().y]).setCharacter(newHero);
		Game.heroes.add(newHero);
	}
	
}
