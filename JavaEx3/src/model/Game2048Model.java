package model;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Random;
import java.util.Stack;

import com.thoughtworks.xstream.XStream;


public class Game2048Model extends Observable implements Model, Runnable {

	private Stack<State> stateStack;
	private State currState;
	private int boardSize;
	
	public final int EMPTY_CELL = 0;
			
	public Game2048Model(int boardSize) {
		
		this.stateStack = new Stack<State>(); //create stack of state/moves
		this.boardSize = boardSize;

		//TODO: board size bust be even
		
		newGame();//create new game
	}

	@Override
	//move all board up
	public void moveUp() {
		boolean boardChanged = pushAllUp();
		for(int i=0; i<this.boardSize-1; i++)
		{
			for(int j=0; j<this.boardSize; j++)
			{
				//cell and previous cell are the same number --> add
				if (this.currState.board[i][j] == this.currState.board[i+1][j])
				{
					this.currState.board[i][j] += this.currState.board[i+1][j]; //add cells
					this.currState.setScore(this.currState.getScore() + this.currState.board[i+1][j]); //add to game score
					this.currState.board[i+1][j] = this.EMPTY_CELL;
					pushAllUp();
					boardChanged = pushAllUp() || boardChanged;
				}
			}
		}
		boardChanged = pushAllUp() || boardChanged;
		if(boardChanged)
			addRandomNumber();
		this.stateStack.add(this.currState);
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers(); 
				
	}

	@Override
	public void moveDown() {
		boolean boardChanged = pushAllDown();
		for(int i=this.boardSize-1; i>0; i--)
		{
			for(int j=0; j<this.boardSize; j++)
			{
				//cell and next cell are the same number --> add
				if (this.currState.board[i][j] == this.currState.board[i-1][j])
				{
					this.currState.board[i][j] += this.currState.board[i-1][j]; //add cells
					this.currState.setScore(this.currState.getScore() + this.currState.board[i-1][j]); //add to game score
					this.currState.board[i-1][j] = this.EMPTY_CELL;
					boardChanged= pushAllDown() || boardChanged;
				}
			}
		}
		boardChanged=pushAllDown() || boardChanged;
		if (boardChanged)
			addRandomNumber();
		
		this.stateStack.add(this.currState);
		
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers(); 
	}

	@Override
	public void moveLeft() {
		boolean boardChanged = pushAllLeft();
		for(int i=0; i<this.boardSize; i++)
		{
			for(int j=0; j<this.boardSize-1; j++)
			{
				//cell and previous cell are the same number --> add
				if (this.currState.board[i][j] == this.currState.board[i][j+1])
				{
					this.currState.board[i][j] += this.currState.board[i][j+1]; //add cells
					this.currState.setScore(this.currState.getScore() + this.currState.board[i][j+1]); //add to game score
					this.currState.board[i][j+1] = this.EMPTY_CELL;
					boardChanged = pushAllLeft() || boardChanged;
				}
			}
		}
		boardChanged=  pushAllLeft() || boardChanged ;
		if (boardChanged)
			addRandomNumber();
		this.stateStack.add(this.currState);
		
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers(); 
	}

	@Override
	public void moveRight() {
		boolean boardChanged = pushAllRigth();
		for(int i=0; i<this.boardSize; i++)
		{
			for(int j=this.boardSize-1; j>0; j--)
			{
				//cell and previous cell are the same number --> add
				if (this.currState.board[i][j] == this.currState.board[i][j-1])
				{
					this.currState.board[i][j] += this.currState.board[i][j-1]; //add cells
					this.currState.setScore(this.currState.getScore() + this.currState.board[i][j-1]); //add to game score
					this.currState.board[i][j-1] = this.EMPTY_CELL;
					boardChanged = pushAllRigth() || boardChanged ;
				}
			}
		}
		boardChanged=  pushAllRigth() ||  boardChanged;
		if (boardChanged) 
			addRandomNumber();
		
		this.stateStack.add(this.currState);
		
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers();
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
	public void getPrevState(){
		this.currState = this.stateStack.pop();
	}

	@Override
	public void saveGame(String path) {
		//create xml from current game state
		XStream xstream = new XStream();
		String xml = xstream.toXML(this.currState);
		
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(path));
		    out.write(xml);
		    out.close();
		} 
		catch (IOException e) {
		    e.printStackTrace();
		} 		
	}

	@Override
	public void loadGame(String path) {
		try {
		XStream xstream = new XStream();
		InputStream in = new FileInputStream(path);
		State loadedState = (State)xstream.fromXML(in);
		this.currState = loadedState;
		this.stateStack.clear();
		
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers(); 
		} 
		catch (IOException e) {
		    e.printStackTrace();
		}
	}

	@Override
	//create new game
	public void newGame() {
		
		this.stateStack.clear(); //init stack that will hold states
		this.currState = new State(this.boardSize); //create initial game state
		initBoard(); //initialize the board
		stateStack.add(this.currState); //save current state
		 
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers(); 
		
		
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
			if(validLocation)
				this.currState.board[row][col] = getNewRandomNumber(); //set there new random number
		}
	}
	
	//helper func: generate new random number 2 or 4 ...
	//TODO: check game logic - need to return 2 or maybe also 4 or 8
	private int getNewRandomNumber()
	{
		return 2;
	}

	private boolean pushAllRigth()
	{
		boolean boardChanged=false;
		for(int i=0; i<boardSize; i++)
		{
			for(int j=0; j<boardSize-1; j++)
			{
				if (currState.board[i][j+1] == EMPTY_CELL)
				{	
					currState.board[i][j+1] =currState.board[i][j];
					currState.board[i][j] = EMPTY_CELL;
					if(!boardChanged) 
						boardChanged=true;
				}
			}
		}
		return boardChanged;
	}
	
	private boolean pushAllLeft()
	{
		boolean boardChanged=false;
		for(int i=0; i<boardSize; i++)
		{
			for(int j=boardSize-1; j>0; j--)
			{
				if (currState.board[i][j-1] == EMPTY_CELL)
				{	
					currState.board[i][j-1] =currState.board[i][j];
					currState.board[i][j] = EMPTY_CELL;
					if(!boardChanged) 
						boardChanged=true;
				}
			}
		}
		return boardChanged;
	}
	
	private boolean pushAllUp()
	{
		boolean boardChanged=false;
		for(int i=boardSize-1; i>0; i--)
		{
			for(int j=0; j<boardSize; j++)
			{
				if (currState.board[i-1][j] == EMPTY_CELL)
				{	
					currState.board[i-1][j] =currState.board[i][j];
					currState.board[i][j] = EMPTY_CELL;
					if(!boardChanged) 
						boardChanged=true;
				}
			}
		}
		return boardChanged;
	}
	
	private boolean pushAllDown()
	{
		boolean boardChanged=false;
		for(int i=0; i<boardSize-1; i++)
		{
			for(int j=0; j<boardSize; j++)
			{
				if (currState.board[i+1][j] == EMPTY_CELL)
				{	
					currState.board[i+1][j] =currState.board[i][j];
					currState.board[i][j] = EMPTY_CELL;
					if(!boardChanged) 
						boardChanged=true;
				}
			}
		}
		return boardChanged;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public State getCurrtState() {
		return this.currState;
	}
	
}
