package model;

public interface Model {
	// Methods
	public void moveUp();
	public void moveDown();
	public void moveLeft();
	public void moveRight();
	public int[][] getBoard();
	public int getScore();
	public State getPrevState(); //do actions to get to prev state
	public boolean saveGame(State currState, String path); //save game to xml file
	State loadGame(String path);//load game from path. path must be xml file
	State newGame(int boardSize); //generate new game
}
