package application;

import engine.Game;
import exceptions.GameActionException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.characters.Direction;
import model.characters.Hero;
import model.world.CharacterCell;
import views.View;


public class Main extends Application {
	
	public View view = new View();
	public Parent startScreen;
	public Parent selectHeroScreen;
	public Parent gameScreen = view.gameScreen();
	public Popup endGame = view.endGamePopup();
	
	public void start(Stage primaryStage) throws Exception {      
		startScreen = view.startScreen(primaryStage);
		selectHeroScreen = view.selectHeroScreen(primaryStage);
		//gameScreen = view.gameScreen(primaryStage);
		primaryStage.setTitle("The Last of Us: Legacy"); 
		Scene scene = new Scene(startScreen, 800, 600);
		startScreenLogic(primaryStage, scene);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
	
	public void startScreenLogic(Stage primaryStage, Scene scene) {
		Font oldFont = ((Text) view.startMenu.get(0).getGraphic()).getFont();
		Font newFont = view.getCustomFont(28);
		for (int i = 0; i < 2; i++) {
			int j = i; 
			view.startMenu.get(i).setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					((Text) view.startMenu.get(j).getGraphic()).setFont(newFont);
				}
			});
			view.startMenu.get(i).setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					((Text) view.startMenu.get(j).getGraphic()).setFont(oldFont);
				}
			});
		}
		view.startMenu.get(0).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				scene.setRoot(selectHeroScreen);
				selectHeroScreenLogic(primaryStage, scene);
			}
		});
		view.startMenu.get(1).setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) {
				javafx.application.Platform.exit();
			}
		});
	}
	
	public void selectHeroScreenLogic(Stage primaryStage, Scene scene) {
		Font oldFont = ((Text) view.selectHeroControls.get(0).getGraphic()).getFont();
		Font newFont = view.getCustomFont(16);
		for (int i = 0; i < 8; i++) {
			Hero startingHero = Game.availableHeroes.get(i);
			view.selectHeroMenu.get(i).setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					if (view.selectHeroToggleGroup.getSelectedToggle() == null) {
						view.attributeLabel(startingHero);
					}
				}
			});
			view.selectHeroMenu.get(i).setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					if (view.selectHeroToggleGroup.getSelectedToggle() == null) {
						((Text) view.attributes.getGraphic()).setText("");
					}
				}
			});
		}
		for (int i = 0; i < 8; i++) {
			Hero startingHero = Game.availableHeroes.get(i);
			view.selectHeroMenu.get(i).setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					if (view.selectHeroToggleGroup.getSelectedToggle() != null) {
						view.attributeLabel(startingHero);
						((Text) view.selectHeroControls.get(0).getGraphic()).setFill(Color.ANTIQUEWHITE);
					}
					else {
						((Text) view.attributes.getGraphic()).setText("");
						((Text) view.selectHeroControls.get(0).getGraphic()).setFill(Color.GRAY);
					}
					view.selectHeroControls.get(0).setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent arg0) {
							if (view.selectHeroToggleGroup.getSelectedToggle() != null) {
								scene.setRoot(gameScreen);
								Game.startGame(startingHero);
								view.playingMap();
								gameScreenLogic(primaryStage, scene);
							}
						}
					});
				}
			});
		}
		view.selectHeroControls.get(1).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				scene.setRoot(startScreen);
			}
		});
		view.selectHeroControls.get(0).setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				if (view.selectHeroToggleGroup.getSelectedToggle() != null) {
					((Text) view.selectHeroControls.get(0).getGraphic()).setFont(newFont);
				}
			}
		});
		view.selectHeroControls.get(0).setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				if (view.selectHeroToggleGroup.getSelectedToggle() != null) {
					((Text) view.selectHeroControls.get(0).getGraphic()).setFont(oldFont);
				}
			}
		});
		view.selectHeroControls.get(1).setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				((Text) view.selectHeroControls.get(1).getGraphic()).setFont(newFont);
			}
		});
		view.selectHeroControls.get(1).setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				((Text) view.selectHeroControls.get(1).getGraphic()).setFont(oldFont);
			}
		});
	}
	
	public void gameScreenLogic(Stage primaryStage, Scene scene) {
		heroButtonLogic(primaryStage, scene);
		view.actionButtons.get(3).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				Game.endTurn();
				view.labels.get(1).setText("");
				view.playingMap();
				view.labels.get(0).setGraphic(null);
				Toggle b = view.heroToggleGroup.getSelectedToggle();
				if (b != null) {
					b.setSelected(false);
				}
				((Text) view.actionButtons.get(0).getGraphic()).setFill(Color.GRAY);
				((Text) view.actionButtons.get(1).getGraphic()).setFill(Color.GRAY);
				((Text) view.actionButtons.get(2).getGraphic()).setFill(Color.GRAY);
				((Text) view.moveButtons.get(0).getGraphic()).setFill(Color.GRAY);
				((Text) view.moveButtons.get(1).getGraphic()).setFill(Color.GRAY);
				((Text) view.moveButtons.get(2).getGraphic()).setFill(Color.GRAY);
				((Text) view.moveButtons.get(3).getGraphic()).setFill(Color.GRAY);
				heroButtonLogic(primaryStage, scene);
				endGamePopupLogic(primaryStage, scene);
			}
		});
		Font oldFont = ((Text) view.actionButtons.get(3).getGraphic()).getFont();
		Font newFont = view.getCustomFont(14);
		view.actionButtons.get(3).setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				((Text) view.actionButtons.get(3).getGraphic()).setFont(newFont);
			}
		});
		view.actionButtons.get(3).setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				((Text) view.actionButtons.get(3).getGraphic()).setFont(oldFont);
			}
		});
	}
	
	public void heroButtonLogic(Stage primaryStage, Scene scene) {
		for (int i = 0; i < 6; i++) {
			if (i >= Game.heroes.size()) {
				break;
			}
			int a = i;
			Hero currHero = Game.heroes.get(i);
			view.heroButtons.get(i).setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					if (view.heroToggleGroup.getSelectedToggle() != null && ((ToggleButton) view.heroToggleGroup.getSelectedToggle()).getGraphic() != null) {
						((Text) view.actionButtons.get(0).getGraphic()).setFill(Color.ANTIQUEWHITE);
						((Text) view.actionButtons.get(1).getGraphic()).setFill(Color.ANTIQUEWHITE);
						((Text) view.actionButtons.get(2).getGraphic()).setFill(Color.ANTIQUEWHITE);
						((Text) view.moveButtons.get(0).getGraphic()).setFill(Color.ANTIQUEWHITE);
						((Text) view.moveButtons.get(1).getGraphic()).setFill(Color.ANTIQUEWHITE);
						((Text) view.moveButtons.get(2).getGraphic()).setFill(Color.ANTIQUEWHITE);
						((Text) view.moveButtons.get(3).getGraphic()).setFill(Color.ANTIQUEWHITE);
					}
					else {
						((Text) view.actionButtons.get(0).getGraphic()).setFill(Color.GRAY);
						((Text) view.actionButtons.get(1).getGraphic()).setFill(Color.GRAY);
						((Text) view.actionButtons.get(2).getGraphic()).setFill(Color.GRAY);
						((Text) view.moveButtons.get(0).getGraphic()).setFill(Color.GRAY);
						((Text) view.moveButtons.get(1).getGraphic()).setFill(Color.GRAY);
						((Text) view.moveButtons.get(2).getGraphic()).setFill(Color.GRAY);
						((Text) view.moveButtons.get(3).getGraphic()).setFill(Color.GRAY);
					}
					int b = a;
					Hero h = currHero;
					view.updateHeroAttributes(currHero);
					currHero.setTarget(null);
					
					Font oldFont = ((Text) view.actionButtons.get(0).getGraphic()).getFont();
					Font newFont = view.getCustomFont(14);
					view.actionButtons.get(0).setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent arg0) {
							if (view.heroToggleGroup.getSelectedToggle() != null) {
								try {
									currHero.attack();
									if (currHero.getTarget().getCurrentHp() == 0) {
										currHero.setTarget(null);
									}
									view.updateExceptions("You attacked the zombie.");
									view.playingMap();
									view.updateHeroAttributes(currHero);
									endGamePopupLogic(primaryStage, scene);
								}
								catch (GameActionException e) {
									view.updateExceptions(e.getMessage());
								}
							}
						}
					});
					view.actionButtons.get(0).setOnMouseEntered(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							if (view.heroToggleGroup.getSelectedToggle() != null && ((ToggleButton) view.heroToggleGroup.getSelectedToggle()).getGraphic() != null) {
								((Text) view.actionButtons.get(0).getGraphic()).setFont(newFont);
							}
						}
					});
					view.actionButtons.get(0).setOnMouseExited(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							if (view.heroToggleGroup.getSelectedToggle() != null && ((ToggleButton) view.heroToggleGroup.getSelectedToggle()).getGraphic() != null) {
								((Text) view.actionButtons.get(0).getGraphic()).setFont(oldFont);
							}
						}
					});
					view.actionButtons.get(1).setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent arg0) {
							if (view.heroToggleGroup.getSelectedToggle() != null) {
								try {
									currHero.cure();
									currHero.setTarget(null);
									view.playingMap();
									view.updateHeroAttributes(currHero);
									endGamePopupLogic(primaryStage, scene);
								}
								catch (GameActionException e) {
									view.updateExceptions(e.getMessage());
								}
							}
						}
					});
					view.actionButtons.get(1).setOnMouseEntered(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							if (view.heroToggleGroup.getSelectedToggle() != null && ((ToggleButton) view.heroToggleGroup.getSelectedToggle()).getGraphic() != null) {
								((Text) view.actionButtons.get(1).getGraphic()).setFont(newFont);
							}
						}
					});
					view.actionButtons.get(1).setOnMouseExited(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							if (view.heroToggleGroup.getSelectedToggle() != null && ((ToggleButton) view.heroToggleGroup.getSelectedToggle()).getGraphic() != null) {
								((Text) view.actionButtons.get(1).getGraphic()).setFont(oldFont);
							}
						}
					});
					view.actionButtons.get(2).setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent arg0) {
							if (view.heroToggleGroup.getSelectedToggle() != null) {
								try {
									currHero.useSpecial();
									currHero.setTarget(null);
									view.playingMap();
									view.updateHeroAttributes(currHero);
									endGamePopupLogic(primaryStage, scene);
								}
								catch (GameActionException e) {
									view.updateExceptions(e.getMessage());
								}
							}
						}
					});
					view.actionButtons.get(2).setOnMouseEntered(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							if (view.heroToggleGroup.getSelectedToggle() != null && ((ToggleButton) view.heroToggleGroup.getSelectedToggle()).getGraphic() != null) {
								((Text) view.actionButtons.get(2).getGraphic()).setFont(newFont);
							}
						}
					});
					view.actionButtons.get(2).setOnMouseExited(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							if (view.heroToggleGroup.getSelectedToggle() != null && ((ToggleButton) view.heroToggleGroup.getSelectedToggle()).getGraphic() != null) {
								((Text) view.actionButtons.get(2).getGraphic()).setFont(oldFont);
							}
						}
					});
					for (int i = 0; i < 4; i++) {
						Button currButton = view.moveButtons.get(i);
						currButton.setOnAction(new EventHandler<ActionEvent>() {
							public void handle(ActionEvent arg0) {
								if (view.heroToggleGroup.getSelectedToggle() != null) {
									Direction d;
							    	switch (((Text) currButton.getGraphic()).getText()) {
							        case "Up":
							        	d = Direction.UP;
							        	break;
							        case "Down":
							        	d = Direction.DOWN;
							        	break;
							        case "Left":
							        	d = Direction.LEFT;
							        	break;
							        case "Right":
							        	d = Direction.RIGHT;
							        	break;
							        default:
							        	return;
							        }
								    try {
							    		int oldHP = h.getCurrentHp();
							    		h.move(d);
							    		int newHP = h.getCurrentHp();
							    		view.playingMap();
							    		view.updateHeroAttributes(currHero);
							    		if (newHP < oldHP) {
							    			view.updateExceptions("You stepped on a trap!");
							    		}
								   		endGamePopupLogic(primaryStage, scene);
								   	}
								   	catch (GameActionException e) {
								   		view.updateExceptions(e.getMessage());
							    	}
								}
							}
						});
						currButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
							public void handle(MouseEvent arg0) {
								if (view.heroToggleGroup.getSelectedToggle() != null && ((ToggleButton) view.heroToggleGroup.getSelectedToggle()).getGraphic() != null) {
									((Text) currButton.getGraphic()).setFont(newFont);
								}
							}
						});
						currButton.setOnMouseExited(new EventHandler<MouseEvent>() {
							public void handle(MouseEvent arg0) {
								if (view.heroToggleGroup.getSelectedToggle() != null && ((ToggleButton) view.heroToggleGroup.getSelectedToggle()).getGraphic() != null) {
									((Text) currButton.getGraphic()).setFont(oldFont);
								}
							}
						});
					}
					for (int j = 0; j < 15; j++) {
						for (int k = 0; k < 15; k++) {
							int x = j;
							int y = k;
							view.cellButtons[j][k].setOnAction(new EventHandler<ActionEvent>() {
								public void handle(ActionEvent arg0) {
									if (Game.map[x][y] instanceof CharacterCell) {
										currHero.setTarget(((CharacterCell) Game.map[x][y]).getCharacter());
									}
								}
							});
						}
					}
				}
			});
		}
		for (int i = 0; i < 6; i++) {
			int j = i;
			if (i >= Game.heroes.size()) {
				view.heroButtons.get(i).setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent arg0) {
						view.heroButtons.get(j).setGraphic(null);
					}
				});
				view.heroButtons.get(i).setOnMouseExited(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent arg0) {
						view.heroButtons.get(j).setGraphic(null);
					}
				});
			}
			else {
				Node oldGraphic = view.heroButtons.get(i).getGraphic();
				Node newGraphic = view.hoverAttributes(Game.heroes.get(i));
				view.heroButtons.get(i).setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent arg0) {
						view.heroButtons.get(j).setGraphic(newGraphic);
					}
				});
				view.heroButtons.get(i).setOnMouseExited(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent arg0) {
						view.heroButtons.get(j).setGraphic(oldGraphic);
					}
				});
			}
		}
	}
	
	public void endGamePopupLogic(Stage primaryStage, Scene scene) {
		view.actionButtons.get(4).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				endGame.hide();
				javafx.application.Platform.exit();
			}	
		});
		if (Game.checkWin()) {
			((Text)view.labels.get(2).getGraphic()).setText("Congratulations! You won!");
			endGame.show(primaryStage);
			
		}
		else if (Game.checkGameOver()){
			((Text)view.labels.get(2).getGraphic()).setText("You lost :(\nBetter Luck Next Time!");
			endGame.show(primaryStage);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
