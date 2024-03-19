package model.characters;

import java.awt.Point;
import java.util.ArrayList;
import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

public abstract class Hero extends Character {
	
	private int actionsAvailable;
	private int maxActions;
	private ArrayList<Vaccine> vaccineInventory;
	private ArrayList<Supply> supplyInventory;
	private boolean specialAction;
	
	public Hero(String name,int maxHp, int attackDmg, int maxActions) {
		super(name,maxHp, attackDmg);
		this.maxActions = maxActions;
		this.actionsAvailable = maxActions;
		this.vaccineInventory = new ArrayList<Vaccine>();
		this.supplyInventory=new ArrayList<Supply>();
		this.specialAction=false;	
	}

	public boolean isSpecialAction() {
		return specialAction;
	}

	public void setSpecialAction(boolean specialAction) {
		this.specialAction = specialAction;
	}

	public int getActionsAvailable() {
		return actionsAvailable;
	}

	public void setActionsAvailable(int actionsAvailable) {
		this.actionsAvailable = actionsAvailable;
	}

	public int getMaxActions() {
		return maxActions;
	}

	public ArrayList<Vaccine> getVaccineInventory() {
		return vaccineInventory;
	}

	public ArrayList<Supply> getSupplyInventory() {
		return supplyInventory;
	}

	
	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		if (this.getTarget() == null) {
			throw new InvalidTargetException("You must choose a target to attack!");
		}
		if (this.getTarget() instanceof Hero) {
			throw new InvalidTargetException("You can't attack a hero!");
		}
		if (this.actionsAvailable == 0) {
			throw new NotEnoughActionsException("You can't attack without action points!");
		}
		if (!this.isAdjacent(this.getTarget())) {
			throw new InvalidTargetException("You can't attack a non-adjacent character!");
		}
		super.attack();
		this.actionsAvailable--;
	}
	
	public void onCharacterDeath() {
		super.onCharacterDeath();
		Game.heroes.remove(this);
	}
	
	public void move(Direction d) throws MovementException, NotEnoughActionsException {
		int newX = this.getLocation().x;
		int newY = this.getLocation().y;
		switch (d) {
			case UP:
				newX++;
				break;
			case DOWN:
				newX--;
				break;
			case LEFT:
				newY--;
				break;
			case RIGHT:
				newY++;
				break;
		}
		if (this.getCurrentHp() == 0) {
			this.onCharacterDeath();
			return;
		}
		if (this.actionsAvailable == 0) {
			throw new NotEnoughActionsException("You can't move without action points!");
		}
		if (newX < 0 || newX > 14 || newY < 0 || newY > 14) {
			throw new MovementException("You can't move outside the boundaries of the map!");
		}
		Cell newCell = Game.map[newX][newY];
		if (newCell instanceof CharacterCell && ((CharacterCell) newCell).getCharacter() != null) {
			throw new MovementException("The cell you are trying to move to is already occupied!");
		}
		this.actionsAvailable--;
		if (newCell instanceof CollectibleCell) {
			((CollectibleCell) newCell).getCollectible().pickUp(this);
			Game.map[newX][newY] = new CharacterCell(this);
		}
		else if (newCell instanceof TrapCell) {
			int trapDamage = ((TrapCell) newCell).getTrapDamage();
			this.setCurrentHp(this.getCurrentHp() - trapDamage);
			if (this.getCurrentHp() == 0) {
				this.onCharacterDeath();
			}
			else {
				Game.map[newX][newY] = new CharacterCell(this);
			}
		}
		else {
			((CharacterCell) newCell).setCharacter(this);
		}
		for (int i = newX - 1; i <= newX + 1; i++) {
			for (int j = newY - 1; j <= newY + 1; j++) {
				if (i >= 0 && j >= 0 && i <= 14 && j <= 14) {
					Game.map[i][j].setVisible(true);
				}
			}
		}
		Cell currentCell = Game.map[this.getLocation().x][this.getLocation().y];
		((CharacterCell) currentCell).setCharacter(null);
		this.setLocation(new Point(newX, newY));
	}
	
	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {
		if (this.supplyInventory.isEmpty()) {
			throw new NoAvailableResourcesException("You can't use your special action without supplies!");
		}
		this.supplyInventory.get(0).use(this);
		this.setSpecialAction(true);
	}
	
	public void cure() throws NoAvailableResourcesException, NotEnoughActionsException, InvalidTargetException {
		if (this.actionsAvailable == 0) {
			throw new NotEnoughActionsException("You can't cure a zombie without action points!");
		}
		if (this.vaccineInventory.isEmpty()) {
			throw new NoAvailableResourcesException("You can't cure a zombie without a vaccine!");
		}
		if (this.getTarget() == null) {
			throw new InvalidTargetException("You must choose a zombie to cure!");
		}
		if (this.getTarget() instanceof Hero) {
			throw new InvalidTargetException("You can't cure a hero!");
		}
		if (!this.isAdjacent(getTarget())) {
			throw new InvalidTargetException("You can't cure a non-adjacent zombie!");
		}
		this.actionsAvailable--;
		this.vaccineInventory.get(0).use(this);
		this.setTarget(null);
	}
		
}
