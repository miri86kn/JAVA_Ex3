package controller;

import java.util.Observable;
import java.util.Observer;

import Entities.GameAction;
import model.Model;
import view.View;

public class Presenter implements Observer {
	// Data Members
	private View ui;
	private Model model;
	

	// Presenter constructor
	public Presenter(Model model, View ui) {
		this.model = model;
		this.ui = ui;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// VIEW ACTIONS:
		if (arg0 == ui) {
			GameAction command = ui.getUserCommand();
			// process user command from ui
			switch (command) {
			case UP_LEFT:
				model.moveUpLeft();
				break;
			case UP_RIGHT:
				model.moveUpRight();
				break;
			case DOWN_LEFT:
				model.moveDownLeft();
				break;
			case DOWN_RIGHT:
				model.moveDownRight();
				break;
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
			case SOLVE:
				  model.solve();
				break;
			case CONNECT:
				if (arg1 != null) //{ ip, port }
				{ 
					Object[] args = (Object[])arg1;
					if(args.length == 2)
					{
					  String ip =(String)args[0];
					  int port = (int)args[1];
					  try {
						model.connectToServer(ip, port);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
				break;
			case DISCONNECT:
				
				try {
					model.disconnectServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			
		}
		// MODEL ACTIONS:
		else if (arg0 == model) {
			ui.displayBoard(model.getCurrtState());
			
			if (arg1 != null && arg1.toString() == "WIN") {
				ui.gameWin();
			}
	
			if (arg1 != null && arg1.toString() == "GAME_OVER") {
				ui.gameOver();
			}
		}

	}

	public int getUserCommand() {
		return 0;
	}

}
