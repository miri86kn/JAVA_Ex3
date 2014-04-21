package model;

import java.util.Random;
import java.util.Stack;

public class Model2048 implements Model {

	private Stack<State> stateStack;
	private State currState;
	private int boardSize;
	
	public final int EMPTY_CELL = 0;
	
	public Model2048(int boardSize) {
		
		this.stateStack = new Stack<State>(); //create stack of state/moves
		this.boardSize = boardSize;

		//TODO: board size bust be even
		
		newGame(boardSize);//create new game
	}

	
	
	@Override
	public void moveUp() {
		// TODO Auto-generated method stub
		for(int i=this.boardSize; i>0; i=i-2)
		{
			for(int j=0; j<this.boardSize; j++)
			{
				//cell and previous cell are the same number --> add
				if (this.currState.board[i][j] == this.currState.board[i-1][j])
				{
					this.currState.board[i-1][j] += this.currState.board[i][j]; //add cells
					this.currState.setScore(this.currState.getScore() + this.currState.board[i-1][j]); //add to game score
					this.currState.board[i][j] = this.EMPTY_CELL;
				}
				//previous cell is emty --> move up
				else if (this.currState.board[i][j] != this.EMPTY_CELL &&  this.currState.board[i-1][j] == this.EMPTY_CELL)
				{
					this.currState.board[i-1][j] = this.currState.board[i][j];
					this.currState.board[i][j] = this.EMPTY_CELL;
					
				}
			}
		}
		
		//this.stateStack.add(arg0);
	}

	@Override
	public void moveDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRight() {
		// TODO Auto-generated method stub
	}

	@Override
	//get current game board
	public int[][] getBoard() {
		return this.currState.getBoard();
	}

	@Override
	 //get current game score
	public int getScore() {
		return this.currState.getScore();
	}

	@Override
	//get previous game state
	public State getPrevState(){
		return this.stateStack.pop();
	}

	@Override
	public boolean saveGame(State currState, String path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public State loadGame(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//create new game
	public State newGame(int boardSize) {
		
		this.stateStack.clear(); //init stack that will hold states
		this.currState = this.newGame(boardSize); //create initial game state
		initBoard(); //initialize the board
		stateStack.add(this.currState); //save current state
		 
		return this.currState;
	}
	
	//helper function: to initialize board 
	private void initBoard(){
		//init all cells to be nulls
		for (int i=0; i< this.boardSize; i++ )
		{
			for (int j=0; j< this.boardSize; j++)
			{
				this.currState.board[i][j] = this.EMPTY_CELL;
			}
		}
		//add 2 random numbers to the board
		addRandomNumber(); 
		addRandomNumber(); 
	}
	
	//helper function: add random number to board
	private void addRandomNumber(){
		
		boolean validLocation = false;
		
		while (validLocation==false)
		{
			//generate random number between 0 and board size
			Random rand = new Random();
			int row = rand.nextInt(this.boardSize);
			int col = rand.nextInt(this.boardSize);
			//just check location to be sure
			validLocation = (row>=0 && row < this.boardSize && col >=0 && col<this.boardSize) //location is in the board
							&& (this.currState.board[row][col] == this.EMPTY_CELL); //cell is empty
			this.currState.board[row][col] = getNewRandomNumber(); //set there new random number
		}
	}
	
	//helper func: generate new random number 2 or 4 ...
	//TODO: check game logic - need to return 2 or maybe also 4 or 8
	private int getNewRandomNumber()
	{
		return 2;
	}

}
