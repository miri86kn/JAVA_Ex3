package view;

import Entities.GameAction;
import model.State;

public interface View {
	// Methods
	
	
	public void displayBoard(State data);
	public GameAction getUserCommand();
	public void displayScore();
	public void gameWin();
	public void gameOver();
}
