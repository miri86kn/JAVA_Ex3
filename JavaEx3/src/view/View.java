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
		UP_RIGHT,
		UP_LEFT,
		DOWN_LEFT,
		DOWN_RIGHT,
		LOAD,
		SAVE,
		UNDO,
		GAME_OVER,
		GAME_WIN,
		SOLVE
	};
	
	public void displayBoard(State data);
	public GameAction getUserCommand();
	public void displayScore();
	public void gameWin();
	public void gameOver();
}
