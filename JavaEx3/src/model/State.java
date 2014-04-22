package model;

public  class State {
	
	int[][] board;
	int score;
	
	public int[][] getBoard() {
		return board;
	}
	public void setBoard(int[][] board) {
		this.board = board;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	//constractor -- create new state
	public State(int boardSize) {
		this.board = new int[boardSize][boardSize];
		this.score = 0; //at the beginning of the game score is 0
	}
	
	public int getBoardSize()
	{
		return this.board.length;
	}
	
}
