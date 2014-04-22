package view;

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
	
	public void displayBoard(int[][] data);
	public GameAction getUserCommand();
	public void displayScore();
}
