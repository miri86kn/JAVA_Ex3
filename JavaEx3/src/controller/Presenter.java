package controller;

import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

public class Presenter implements Observer {
	// Data Members
	private View ui;
	private Model model;
	
	private static final int BOARD_SIZE = 4;

	// Presenter constructor
	public Presenter(Model model, View ui) {
		this.model = model;
		this.ui = ui;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// VIEW ACTIONS:
		if (arg0 == ui) {
			View.GameAction command = ui.getUserCommand();
			// process user command from ui
			switch (command) {
			case LEFT:
				model.moveLeft();
				break;
			case RIGHT:
				model.moveRight();
				break;
			case UP:
				model.moveUp();
				break;
			case DOWN:
				model.moveDown();
				break;
			case LOAD:
				if (arg1 != null && arg1.toString() != "")
					model.loadGame(arg1.toString());
				break;
			case RESTART:
			    model.newGame();
				break;
			case SAVE:
				if (arg1 != null && arg1.toString() != "")
				model.saveGame(arg1.toString());
				break;
			case UNDO:
				model.getPrevState();
				break;
			default:
				break;
			}
			
		}
		// MODEL ACTIONS:
		else if (arg0 == model) {
			ui.displayBoard(model.getCurrtState());
		}

	}

	public int getUserCommand() {
		return 0;
	}

}
