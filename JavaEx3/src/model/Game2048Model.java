package model;


import java.util.Random;

//public class Game2048Model extends Observable implements Model, Runnable {
public class Game2048Model extends AbsModel {
	
	public final int EMPTY_CELL = 0;
	public final int WINNIG_NUMBER = 2048;
	
	public Game2048Model(int boardSize){
		super(boardSize);
	}
	
	
	@Override
	//move all board up
	public void moveUp() {
		boolean boardChanged = pushAllUp();
		for(int i=0; i< this.boardSize-1; i++)
		{
			for(int j=0; j<this.boardSize; j++)
			{
				//cell and previous cell are the same number --> add
				if (this.currState.getBoard()[i][j] == this.currState.getBoard()[i+1][j])
				{
					this.currState.getBoard()[i][j] += this.currState.getBoard()[i+1][j]; //add cells
					this.currState.setScore(this.currState.getScore() + this.currState.getBoard()[i+1][j]); //add to game score
					this.currState.getBoard()[i+1][j] = this.EMPTY_CELL;
					pushAllUp();
					boardChanged = pushAllUp() || boardChanged;
				}
			}
		}
		boardChanged = pushAllUp() || boardChanged;
		
		if(boardChanged)
		{	
			addRandomNumber();
			this.stateStack.add(this.currState.Clone());
		
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers(); 
		}
		
			checkGameState();
		
		
	}

	@Override
	public void moveDown() {
		boolean boardChanged = pushAllDown();
		for(int i=this.boardSize-1; i>0; i--)
		{
			for(int j=0; j<this.boardSize; j++)
			{
				//cell and next cell are the same number --> add
				if (this.currState.getBoard()[i][j] == this.currState.getBoard()[i-1][j])
				{
					this.currState.getBoard()[i][j] += this.currState.getBoard()[i-1][j]; //add cells
					this.currState.setScore(this.currState.getScore() + this.currState.getBoard()[i-1][j]); //add to game score
					this.currState.getBoard()[i-1][j] = this.EMPTY_CELL;
					boardChanged= pushAllDown() || boardChanged;
				}
			}
		}
		boardChanged=pushAllDown() || boardChanged;
		if (boardChanged)
		{
			addRandomNumber();
			this.stateStack.add(this.currState.Clone());
		
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers(); 
		}
		
			checkGameState();
		
	}

	@Override
	public void moveLeft() {
		boolean boardChanged = pushAllLeft();
		for(int i=0; i<this.boardSize; i++)
		{
			for(int j=0; j<this.boardSize-1; j++)
			{
				//cell and previous cell are the same number --> add
				if (this.currState.getBoard()[i][j] == this.currState.getBoard()[i][j+1])
				{
					this.currState.getBoard()[i][j] += this.currState.getBoard()[i][j+1]; //add cells
					this.currState.setScore(this.currState.getScore() + this.currState.getBoard()[i][j+1]); //add to game score
					this.currState.getBoard()[i][j+1] = this.EMPTY_CELL;
					boardChanged = pushAllLeft() || boardChanged;
				}
			}
		}
		boardChanged=  pushAllLeft() || boardChanged ;
		if (boardChanged)
		{
			addRandomNumber();
			this.stateStack.add(this.currState.Clone());
		
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers();
		}
			checkGameState();
		
	}

	@Override
	public void moveRight() {
		boolean boardChanged = pushAllRigth();
		for(int i=0; i<this.boardSize; i++)
		{
			for(int j=this.boardSize-1; j>0; j--)
			{
				//cell and previous cell are the same number --> add
				if (this.currState.getBoard()[i][j] == this.currState.getBoard()[i][j-1])
				{
					this.currState.getBoard()[i][j] += this.currState.getBoard()[i][j-1]; //add cells
					this.currState.setScore(this.currState.getScore() + this.currState.getBoard()[i][j-1]); //add to game score
					this.currState.getBoard()[i][j-1] = this.EMPTY_CELL;
					boardChanged = pushAllRigth() || boardChanged ;
				}
			}
		}
		boardChanged=  pushAllRigth() ||  boardChanged;
		if (boardChanged) 
		{
			addRandomNumber();
			this.stateStack.add(this.currState.Clone());
		
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers();
		}
		
	    checkGameState();
		
	}
	
	@Override
	//helper function: to initialize board 
	public void initBoard(){
		//init all cells to be nulls
		for (int i=0; i< this.boardSize; i++ )
		{
			for (int j=0; j< this.boardSize; j++)
			{
				this.currState.getBoard()[i][j] = this.EMPTY_CELL;
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
							&& (this.currState.getBoard()[row][col] == this.EMPTY_CELL); //cell is empty
			if(validLocation)
				this.currState.getBoard()[row][col] = getNewRandomNumber(); //set there new random number
		}
	}
	
	//helper func: generate new random number 2 or 4 ...
	//TODO: check game logic - need to return 2 or maybe also 4 or 8
	private int getNewRandomNumber()
	{
		/*Random random = new Random(); //get new random number
		int randomNumber = random.nextInt(100);
		if (randomNumber%5 == 0)
			return 4;
		else
			return 2;*/
		
		// Generate number 2 with 0.9 probability, and 4 with 0.1 probability
		Random randomGenerator = new Random(System.currentTimeMillis());
		int randomValue=(randomGenerator.nextDouble()< 0.9)?2:4;
		
		return randomValue;
	}

	private boolean pushAllRigth()
	{
		boolean boardChanged=false;
		for(int i=0; i<boardSize; i++)
		{
			for(int j=0; j<boardSize-1; j++)
			{
				if (currState.getBoard()[i][j+1] == EMPTY_CELL)
				{	
					currState.getBoard()[i][j+1] =currState.getBoard()[i][j];
					currState.getBoard()[i][j] = EMPTY_CELL;
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
				if (currState.getBoard()[i][j-1] == EMPTY_CELL)
				{	
					currState.getBoard()[i][j-1] =currState.getBoard()[i][j];
					currState.getBoard()[i][j] = EMPTY_CELL;
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
				if (currState.getBoard()[i-1][j] == EMPTY_CELL)
				{	
					currState.getBoard()[i-1][j] =currState.getBoard()[i][j];
					currState.getBoard()[i][j] = EMPTY_CELL;
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
				if (currState.getBoard()[i+1][j] == EMPTY_CELL)
				{	
					currState.getBoard()[i+1][j] =currState.getBoard()[i][j];
					currState.getBoard()[i][j] = EMPTY_CELL;
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
	
	//helper function to check id game is eon or lost
	private void checkGameState(){
		if (gameWon())
		{
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers("WIN");
		}
		else if(boardIsFull())
		{
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers("GAME_OVER");
		}
	}

	private boolean gameWon(){
		for(int i=0; i< boardSize; i++)
			for(int j=0; j< boardSize; j++)
			{
				if (this.currState.getBoard()[i][j] == WINNIG_NUMBER)
					return true;
			}
		return false;
	}
	
	private boolean boardIsFull(){
		boolean emptyCellExists=false;
		boolean similarNumbersExists=false;
		for(int i=0; i< boardSize; i++)
			for(int j=0; j< boardSize && !emptyCellExists; j++)
			{
				if (this.currState.getBoard()[i][j] == EMPTY_CELL)
				{
						emptyCellExists= true;
						
				}
			}
		if (!emptyCellExists){
			for(int i=0; i< boardSize; i++)
				for(int j=0; j< boardSize-1; j++)
				{
					similarNumbersExists = (this.currState.getBoard()[i][j] == this.currState.getBoard()[i][j+1]);
							
					if (similarNumbersExists)
						return false;
				}
			for(int i=0; i<boardSize-1; i++)
				for (int j=0; j<boardSize; j++)
				{
					similarNumbersExists = (this.currState.getBoard()[i][j] == this.currState.getBoard()[i+1][j]);
					if (similarNumbersExists)
						return false;
				}
			return true;
		}
		return false;
	}


	@Override
	public void moveUpRight() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void moveUpLeft() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void moveDownRight() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void moveDownLeft() {
		// TODO Auto-generated method stub
		
	}
	
	

}

