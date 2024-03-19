package views;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Supply;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import engine.Game;

public class View {
	
	public ArrayList<Button> startMenu = new ArrayList<Button>();
	public ArrayList<ToggleButton> selectHeroMenu = new ArrayList<ToggleButton>();
	public ArrayList<Button> selectHeroControls = new ArrayList<Button>();
	public ArrayList<ToggleButton> heroButtons = new ArrayList<ToggleButton>();
	public Button[][] cellButtons = new Button[15][15];
	public ArrayList<Button> actionButtons = new ArrayList<Button>();
	public ArrayList<Button> moveButtons = new ArrayList<Button>();
	public ArrayList<Label> labels = new ArrayList<Label>();
	public ToggleGroup heroToggleGroup = new ToggleGroup();
	public ToggleGroup selectHeroToggleGroup = new ToggleGroup();
	public Label attributes;
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	
	public Font getCustomFont(int size) {
		InputStream fontStream = classLoader.getResourceAsStream("Pixel Font.ttf");
		Font font = Font.loadFont(fontStream, size);
		try {
			fontStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return font;
		
	}
	
	public Parent startScreen(Stage primaryStage) {
		Font smallFont = getCustomFont(26);
		Font largeFont = getCustomFont(40);
		StackPane root = new StackPane();
		VBox parent = new VBox();
		VBox child = new VBox();
		Label gameTitle = new Label();
		Text t = new Text("The Last of Us: Legacy");
		t.setFill(Color.ANTIQUEWHITE);
		t.setFont(largeFont);
		gameTitle.setGraphic(t);
		gameTitle.setAlignment(Pos.CENTER);
		Button start = new Button();
		Text t1 = new Text("Start Game");
		t1.setFill(Color.ANTIQUEWHITE);
		t1.setFont(smallFont);
		start.setGraphic(t1);
		start.setStyle("-fx-background-color: transparent;");
		start.setPrefSize(400, 50);
		startMenu.add(start);
		/*Button howTo = new Button();
		Text t2 = new Text("How to Play");
		t2.setFill(Color.ANTIQUEWHITE);
		t2.setFont(smallFont);
		howTo.setGraphic(t2);
		howTo.setStyle("-fx-background-color: transparent;");
		howTo.setPrefSize(400, 50);
		startMenu.add(howTo);
		Button settings = new Button();
		Text t3 = new Text("Settings");
		t3.setFill(Color.ANTIQUEWHITE);
		t3.setFont(smallFont);
		settings.setGraphic(t3);
		settings.setStyle("-fx-background-color: transparent;");
		settings.setPrefSize(400, 50);
		startMenu.add(settings);*/
		Button exit = new Button();
		Text t4 = new Text("Exit");
		t4.setFill(Color.ANTIQUEWHITE);
		t4.setFont(smallFont);
		exit.setGraphic(t4);
		exit.setStyle("-fx-background-color: transparent;");
		exit.setPrefSize(400, 50);
		startMenu.add(exit);
		child.setAlignment(Pos.CENTER);
		child.getChildren().addAll(start, exit);
		parent.getChildren().addAll(gameTitle, child);
		parent.setSpacing(120);
		parent.setAlignment(Pos.CENTER);
		InputStream backgroundStream = classLoader.getResourceAsStream("Start Background.png");
		Image img = new Image(backgroundStream);
		try {
			backgroundStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageView background = new ImageView(img);
		background.fitWidthProperty().bind(primaryStage.widthProperty());
		background.fitHeightProperty().bind(primaryStage.heightProperty());
		root.getChildren().addAll(background, parent);
		return root;
	}
	
	public Parent selectHeroScreen(Stage primaryStage) {
		try {
			Game.loadHeroes("Heroes.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		StackPane root = new StackPane();
		VBox parent = new VBox();
		GridPane child1 = new GridPane();
		HBox child2 = new HBox();
		for (int i = 0; i < 8; i++) {
			Hero h = Game.availableHeroes.get(i);
			ToggleButton b = new ToggleButton();
			b.setStyle("-fx-background-color: transparent;");
			b.setToggleGroup(selectHeroToggleGroup);
			b.setPrefSize(130, 110);
			selectHeroMenu.add(b);
			if (i < 4) {
				child1.addRow(0, b);
			}
			else {
				child1.addRow(1, b);
			}
			InputStream backgroundStream = classLoader.getResourceAsStream(h.getName() + " 1.png");
			Image img = new Image(backgroundStream);
			try {
				backgroundStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ImageView background = new ImageView(img);
			background.setFitWidth(100);
			background.setFitHeight(120);
			selectHeroMenu.get(i).setGraphic(background);
		}
		
		attributes = new Label();
		attributes.setGraphic(new Text(""));
		attributes.setPrefSize(150, 80);
		Font font = getCustomFont(14);
		Button select = new Button();
		Text t1 = new Text("Select");
		t1.setFill(Color.GRAY);
		t1.setFont(font);
		select.setGraphic(t1);
		select.setStyle("-fx-background-color: transparent;");
		select.setPrefSize(150, 100);
		Button back = new Button();
		Text t2 = new Text("Back");
		t2.setFill(Color.ANTIQUEWHITE);
		t2.setFont(font);
		back.setGraphic(t2);
		back.setStyle("-fx-background-color: transparent;");
		back.setPrefSize(150, 100);
		selectHeroControls.add(select);
		selectHeroControls.add(back);
		child2.getChildren().addAll(select, back);
		parent.getChildren().addAll(attributes, child1, child2);
		parent.setAlignment(Pos.CENTER);
		parent.setSpacing(50);
		child1.setAlignment(Pos.CENTER);
		child1.setHgap(5);
		child1.setVgap(5);
		child2.setAlignment(Pos.CENTER);
		child2.setSpacing(20);
		InputStream backgroundStream = classLoader.getResourceAsStream("Start Background.png");
		Image img = new Image(backgroundStream);
		try {
			backgroundStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageView background = new ImageView(img);
		background.fitWidthProperty().bind(primaryStage.widthProperty());
		background.fitHeightProperty().bind(primaryStage.heightProperty());
		root.getChildren().addAll(background, parent);
		return root;
	}
	
	public void attributeLabel(Hero currHero) {
		String type;
		if (currHero instanceof Fighter) {
			type = "Fighter";
		}
		else if (currHero instanceof Medic) {
			type = "Medic";
		}
		else {
			type = "Explorer";
		}
		String info = "Name: " + currHero.getName();
		info += "\nType: " + type;
		info += "\nMax  HP: " + currHero.getMaxHp();
		info += "\nAttack Damage: " + currHero.getAttackDmg() + "   ";
		info += "\nMax Actions: " + currHero.getActionsAvailable();
		Text t = new Text(info);
		t.setFill(Color.ANTIQUEWHITE);
		t.setFont(getCustomFont(16));
		attributes.setGraphic(t);
	}
	
	public Parent gameScreen() {
		StackPane root = new StackPane();
		Label exceptions = new Label();
		HBox parent = new HBox();
		Label currentHeroAttributes = new Label();
		currentHeroAttributes.setPrefSize(360, 150);
		GridPane child2 = new GridPane();
		VBox child3 = new VBox();
		VBox heroes = new VBox();
		VBox moves = new VBox();
		HBox movesRow = new HBox();
		GridPane actions = new GridPane();
		for (int i = 0; i < 6; i++) {
			ToggleButton b = new ToggleButton();
			b.setPrefSize(500, 100);
			b.setToggleGroup(heroToggleGroup);
			b.setStyle("-fx-background-color: transparent;");
			heroButtons.add(b);
			heroes.getChildren().add(b);
		}
		for (int i = 0; i < 4; i++) {
			Button b = new Button();
			b.setPrefSize(250,50);
			actionButtons.add(b);
			if (i < 2) {
				actions.addRow(0, b);
			}
			else {
				actions.addRow(1, b);
			}
		}
		Font font = getCustomFont(12);
		Button up = new Button();
		Text txt1 = new Text("Up");
		txt1.setFill(Color.GRAY);
		txt1.setFont(font);
		up.setGraphic(txt1);
		up.setStyle("-fx-background-color: transparent;");
		up.setPrefSize(60, 30);
		Button down = new Button();
		Text txt2 = new Text("Down");
		txt2.setFill(Color.GRAY);
		txt2.setFont(font);
		down.setGraphic(txt2);
		down.setStyle("-fx-background-color: transparent;");
		down.setPrefSize(60, 30);
		Button left = new Button();
		Text txt3 = new Text("Left");
		txt3.setFill(Color.GRAY);
		txt3.setFont(font);
		left.setGraphic(txt3);
		left.setStyle("-fx-background-color: transparent;");
		left.setPrefSize(80, 30);
		Button right = new Button();
		Text txt4 = new Text("Right");
		txt4.setFill(Color.GRAY);
		txt4.setFont(font);
		right.setGraphic(txt4);
		right.setStyle("-fx-background-color: transparent;");
		right.setPrefSize(90, 30);
		moveButtons.add(up);
		moveButtons.add(down);
		moveButtons.add(left);
		moveButtons.add(right);
		movesRow.getChildren().addAll(left, right);
		movesRow.setAlignment(Pos.CENTER);
		movesRow.setSpacing(60);
		moves.getChildren().addAll(up, movesRow, down);
		moves.setAlignment(Pos.CENTER);
		//moves.setBottom(down);
		//moves.setLeft(left);
		//moves.setRight(right);
		Text t1 = new Text("Attack");
		t1.setFill(Color.GRAY);
		t1.setFont(font);
		actionButtons.get(0).setGraphic(t1);
		actionButtons.get(0).setStyle("-fx-background-color: transparent;");
		Text t2 = new Text("Cure");
		t2.setFill(Color.GRAY);
		t2.setFont(font);
		actionButtons.get(1).setGraphic(t2);
		actionButtons.get(1).setStyle("-fx-background-color: transparent;");
		Text t3 = new Text("Use Special");
		t3.setFill(Color.GRAY);
		t3.setFont(font);
		actionButtons.get(2).setGraphic(t3);
		actionButtons.get(2).setStyle("-fx-background-color: transparent;");
		Text t4 = new Text("End Turn");
		t4.setFill(Color.ANTIQUEWHITE);
		t4.setFont(font);
		actionButtons.get(3).setGraphic(t4);
		actionButtons.get(3).setStyle("-fx-background-color: transparent;");
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				Button b = new Button();
				b.setStyle("-fx-background-color: transparent;");
				b.setOpacity(0);
				b.setPrefSize(60, 60);
				cellButtons[14-i][j] = b;
				child2.addRow(i, b);
			}
		}
		labels.add(currentHeroAttributes);
		labels.add(exceptions);
		InputStream backgroundStream = classLoader.getResourceAsStream("GameBG.jpeg");
		Image img = new Image(backgroundStream);
		try {
			backgroundStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageView background = new ImageView(img);
		background.setFitWidth(1700);
		background.setFitHeight(1100);
		//background.fitWidthProperty().bind(primaryStage.widthProperty());
		//background.fitHeightProperty().bind(primaryStage.heightProperty());
		child3.getChildren().addAll(heroes, moves, actions);
		child3.setSpacing(50);
		child2.setAlignment(Pos.CENTER);
		parent.getChildren().addAll(currentHeroAttributes, child2, child3);
		parent.setAlignment(Pos.CENTER);
		//parent.setSpacing(100);
		root.getChildren().addAll(background, parent, exceptions);
		root.setStyle("-fx-background-color: #000011;");
		return root;
	}
	
	public void playingMap() {
		for (int i = 0; i < 6; i++) {
			if (i < Game.heroes.size()) {
				InputStream backgroundStream = classLoader.getResourceAsStream(Game.heroes.get(i).getName() + " 2.png");
				Image img = new Image(backgroundStream);
				try {
					backgroundStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ImageView background = new ImageView(img);
				background.setFitWidth(70);
				background.setFitHeight(92);
				heroButtons.get(i).setGraphic(background);
			}
			else {
				heroButtons.get(i).setGraphic(null);
			}
		}
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				Cell currCell = Game.map[i][j];
				Button currButton = cellButtons[i][j];
				if (!currCell.isVisible()) {
					currButton.setStyle("-fx-background-color: #000000;");
					currButton.setOpacity(0.2);
					currButton.setGraphic(null);
				}
				else {
					currButton.setStyle("-fx-background-color: transparent;");
					currButton.setOpacity(1);
					if (currCell instanceof TrapCell) {
						currButton.setGraphic(null);
					}
					else if (currCell instanceof CollectibleCell) {
						if (((CollectibleCell) currCell).getCollectible() instanceof Supply) {
							InputStream backgroundStream = classLoader.getResourceAsStream("Supply.png");
							Image img = new Image(backgroundStream);
							try {
								backgroundStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							ImageView background = new ImageView(img);
							background.setFitWidth(40);
							background.setFitHeight(40);
							currButton.setGraphic(background);
						}
						else {
							InputStream backgroundStream = classLoader.getResourceAsStream("Vaccine.png");
							Image img = new Image(backgroundStream);
							try {
								backgroundStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							ImageView background = new ImageView(img);
							background.setFitWidth(40);
							background.setFitHeight(40);
							currButton.setGraphic(background);
						}
					}
					else if (currCell instanceof CharacterCell && ((CharacterCell) currCell).getCharacter() == null) {
						currButton.setGraphic(null);
					}
					else {
						if (currCell instanceof CharacterCell && ((CharacterCell) currCell).getCharacter() instanceof Zombie) {
							InputStream backgroundStream = classLoader.getResourceAsStream("Zombie.png");
							Image img = new Image(backgroundStream);
							try {
								backgroundStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							ImageView background = new ImageView(img);
							background.setFitWidth(40);
							background.setFitHeight(40);
							currButton.setGraphic(background);
						}
						else {
							InputStream backgroundStream = classLoader.getResourceAsStream(((CharacterCell) currCell).getCharacter().getName() + " 2.png");
							Image img = new Image(backgroundStream);
							try {
								backgroundStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							ImageView background = new ImageView(img);
							background.setFitWidth(40);
							background.setFitHeight(40);
							currButton.setGraphic(background);
						}
					}
				}
			}
		}
	}
	
	public void updateHeroAttributes(Hero currHero) {
		String type;
		if (currHero instanceof Fighter) {
			type = "Fighter";
		}
		else if (currHero instanceof Medic) {
			type = "Medic";
		}
		else {
			type = "Explorer";
		}
		String info = "Name: " + currHero.getName();
		info += "\nType: " + type;
		info += "\nCurrent HP: " + currHero.getCurrentHp();
		info += "\nAttack Damage: " + currHero.getAttackDmg();
		info += "\nAction Points: " + currHero.getActionsAvailable();
		info += "\nSupplies: " + currHero.getSupplyInventory().size();
		info += "\nVaccines: " + currHero.getVaccineInventory().size();
		Text t = new Text(info);
		t.setFill(Color.ANTIQUEWHITE);
		t.setFont(getCustomFont(12));
		labels.get(0).setGraphic(t);
	}
	
	public Node hoverAttributes(Hero currHero) {
		String type;
		if (currHero instanceof Fighter) {
			type = "Fighter";
		}
		else if (currHero instanceof Medic) {
			type = "Medic";
		}
		else {
			type = "Explorer";
		}
		String info = "Name: " + currHero.getName();
		info += "\nType: " + type;
		info += "\nCurrent HP: " + currHero.getCurrentHp();
		info += "\nAttack Damage: " + currHero.getAttackDmg() + "   ";
		info += "\nMax Actions: " + currHero.getActionsAvailable();
		Text t = new Text(info);
		t.setFill(Color.ANTIQUEWHITE);
		t.setFont(getCustomFont(12));
		return t;
	}
	
	public void updateExceptions(String s) {
		Text t = new Text(s);
		t.setFill(Color.ANTIQUEWHITE);
		t.setFont(getCustomFont(12));
		labels.get(1).setGraphic(t);
		FadeTransition fade = new FadeTransition();
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setDuration(Duration.millis(3000));
		fade.setNode(labels.get(1));
		fade.play();
		labels.get(1).setAlignment(Pos.CENTER);
	}
	
	public Popup endGamePopup() {
		Popup popup = new Popup();
		popup.setX(700);
		popup.setY(500);
		VBox root = new VBox();
		Label message = new Label();
		Text t1 = new Text();
		t1.setFill(Color.ANTIQUEWHITE);
		t1.setFont(getCustomFont(12));
		message.setGraphic(t1);
		labels.add(message);
		Button exit = new Button();
		Text t2 = new Text("Exit");
		t2.setFill(Color.ANTIQUEWHITE);
		t2.setFont(getCustomFont(12));
		exit.setGraphic(t2);
		exit.setStyle("-fx-background-color: transparent;");
		root.setStyle("-fx-background-color: #000022;");
		actionButtons.add(exit);
		root.getChildren().addAll(message, exit);
		popup.getContent().add(root);
		return popup;
		
	}
}
