package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import exceptions.GameActionException;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

public class Game {
	
	public static Cell [][] map = new Cell[15][15];
	public static ArrayList <Hero> availableHeroes = new ArrayList<Hero>();
	public static ArrayList <Hero> heroes =  new ArrayList<Hero>();
	public static ArrayList <Zombie> zombies =  new ArrayList<Zombie>();
		
	public static void loadHeroes(String filePath)  throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Hero hero=null;
			switch (content[1]) {
			case "FIGH":
				hero = new Fighter(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]), Integer.parseInt(content[3]));
				break;
			case "MED":  
				hero = new Medic(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]), Integer.parseInt(content[3])) ;
				break;
			case "EXP":  
				hero = new Explorer(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]), Integer.parseInt(content[3]));
				break;
			}
			availableHeroes.add(hero);
			line = br.readLine();
			
		}
		br.close();
	}
	
	public static void startGame(Hero h) {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				map[i][j] = null;
			}
		}
		int vaccineCount = 0;
		int supplyCount = 0;
		int trapCount = 0;
		int zombieCount = 0;
		while(vaccineCount < 5) {	
		    int x = (int)(15*Math.random());
			int y = (int)(15*Math.random());
			if (!(x == 0 && y == 0) && map[x][y] == null) {
				map[x][y] = new CollectibleCell(new Vaccine());
				vaccineCount++;
			}
		}
		while(supplyCount < 5) {	
		    int x = (int)(15*Math.random());
			int y = (int)(15*Math.random());
			if (!(x == 0 && y == 0) && map[x][y] == null) {
				map[x][y] = new CollectibleCell(new Supply());
				supplyCount++;
			}
		}
		while(trapCount < 5) {	
		    int x = (int)(15*Math.random());
			int y = (int)(15*Math.random());
			if (!(x == 0 && y == 0) && map[x][y] == null) {
				map[x][y] = new TrapCell();
				trapCount++;
			}
		}
		while(zombieCount < 10) {	
		    int x = (int)(15*Math.random());
			int y = (int)(15*Math.random());
			if (!(x == 0 && y == 0) && map[x][y] == null) {
				Zombie newZombie = new Zombie();
				newZombie.setLocation(new Point(x, y));
				map[x][y] = new CharacterCell(newZombie);
				zombies.add(newZombie);
				zombieCount++;
			}
		}
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (map[i][j] == null) {
					map[i][j] = new CharacterCell(null);
				}
			}
		}
		availableHeroes.remove(h);
		h.setLocation(new Point(0, 0));
		((CharacterCell) map[0][0]).setCharacter(h);
		heroes.add(h);
		for (int i = 0; i <= 1; i++) {
			for (int j = 0; j <= 1; j++) {
				map[i][j].setVisible(true);
			}
		}
	}
	public static boolean allVaccinesCollectedAndUsed() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (map[i][j] instanceof CollectibleCell && ((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine) {
					return false;
				}
			}
		}
		for (int i = 0; i < heroes.size(); i++) {
			Hero currHero = heroes.get(i);
			if (!currHero.getVaccineInventory().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkWin() {
		return allVaccinesCollectedAndUsed() && heroes.size() >= 5;
	}
	
	public static boolean checkGameOver() {
		return (allVaccinesCollectedAndUsed() && heroes.size() < 5) || heroes.size() == 0;
	}
	
	public static void endTurn() {
		for (int i = 0; i < zombies.size(); i++) {
			Zombie currZombie = zombies.get(i);
			try {
				currZombie.attack();
				if (currZombie.getCurrentHp() == 0) {
					i--;
				}
			} 
			catch (GameActionException e) {

			}
		}
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15 ; j++) {
				map[i][j].setVisible(false);
			}
		}
		for (int i = 0; i < heroes.size(); i++) {
			Hero currHero = heroes.get(i);
			currHero.setActionsAvailable(currHero.getMaxActions());
			currHero.setTarget(null);
			currHero.setSpecialAction(false);
			int x = currHero.getLocation().x;
			int y = currHero.getLocation().y;
			for (int j = x - 1; j <= x + 1; j++) {
				for (int k = y - 1; k <= y + 1; k++) {
					if (j >= 0 && k >= 0 && j <= 14 && k <= 14) {
						Game.map[j][k].setVisible(true);
					}
				}
			}
		}
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
	}

}
