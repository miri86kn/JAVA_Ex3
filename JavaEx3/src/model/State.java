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
	public State() {
	}
	public State(int boardSize) {
		this.board = new int[boardSize][boardSize];
		this.score = 0; //at the beginning of the game score is 0
	}
	
	public int getBoardSize()
	{
		return this.board.length;
	}
	
	public State Clone()
	{
		State newS = new State();
		int[][] board = new int[this.board.length][this.board.length];
		for(int i=0; i<this.board.length; i++)
			for(int j=0; j<this.board.length; j++)
				board[i][j] = this.board[i][j];
		newS.setBoard(board);
		newS.setScore(this.score);
		return newS;
	}
	
	@Override
	public boolean equals(Object state)
	{
		
		State s =(State)state;
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board.length; j++)
				if (s.board[i][j] != board[i][j])
					return false;
		return this.score == s.score;
	}
}
