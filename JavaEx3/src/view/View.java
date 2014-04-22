package view;

import model.State;

public interface View {
	// Methods
	
	public static enum GameAction {
		UP,
		DOWN,
		RIGHT,
		LEFT,
		RESTART,
		LOAD,
		SAVE,
		UNDO		
	};
	
	public void displayBoard(State data);
	public GameAction getUserCommand();
	public void displayScore();
}
