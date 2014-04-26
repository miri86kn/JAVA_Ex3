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
		UNDO,
		GAME_OVER,
		GAME_WIN
	};
	
	public void displayBoard(State data);
	public GameAction getUserCommand();
	public void displayScore();
}
