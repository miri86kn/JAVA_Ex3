package model;
import java.util.ArrayList;



public class GameMazeModel extends AbsModel {

	// Data Members
	
	// Constants
	public final int EMPTY = 0;
	public final int WALL = 1;
	public final int PLAYER = 2;
	public final int EXIT = 3;
	public final int SCORE_LIMIT = 1000;
	
	public final int HORIZONTAL_MOVE_COST = 10;
	public final int DIAGONAL_MOVE_COST = 15;
	
	// Methods
	
	// GameMazeModel Constructor
	public GameMazeModel(int boardSize){
		super(boardSize);
		
	}
	
	
	private void movePlayer(int toRow, int toCol)
	{
		currState.getBoard()[currState.getPlayerRow()][currState.getPlayerCol()] = EMPTY; // Mark the current player location as empty
		currState.setPlayerPosition(toRow, toCol);
		
		
		
		if (currState.getBoard()[currState.getPlayerRow()][currState.getPlayerCol()] != EXIT) {
			currState.getBoard()[currState.getPlayerRow()][currState.getPlayerCol()] = PLAYER;
			this.stateStack.add(getCurrtState().Clone()); // Add current state to state stack
		}
		
		//check if player reached exit
		if(isGameWon())
		{
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers("WIN");
		}
		
		if (isGameOver()) {
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers("GAME_OVER");
		}
	}
	
	@Override
	public void moveUp() {
		//is the move valid
		if (isMoveValid(currState.getPlayerRow()-1, currState.getPlayerCol()))
		{
			//update score
			this.currState.setScore(this.currState.getScore() + HORIZONTAL_MOVE_COST ); 
			
			//update current player position on board
			 movePlayer( currState.getPlayerRow()-1, currState.getPlayerCol());
			
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers(); 
		}
	}

	@Override
	public void moveDown() {
		//is the move valid
		if (isMoveValid(currState.getPlayerRow()+1, currState.getPlayerCol()))
		{
			//update score
			this.currState.setScore(this.currState.getScore() + HORIZONTAL_MOVE_COST );
			
			//update current player position on board
			movePlayer(currState.getPlayerRow()+1, currState.getPlayerCol());
			
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers(); 
		}
	}

	@Override
	public void moveLeft() {
		//is the move valid
		if (isMoveValid(currState.getPlayerRow(), currState.getPlayerCol()-1))
		{
			//update score
			this.currState.setScore(this.currState.getScore() + HORIZONTAL_MOVE_COST );	
			
			//update current player position on board
			movePlayer(currState.getPlayerRow(), currState.getPlayerCol()-1);
			
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers(); 
		}
	}

	@Override
	public void moveRight() {
		//is the move valid
		if (isMoveValid(currState.getPlayerRow(), currState.getPlayerCol()+1))
		{
			//update score
			this.currState.setScore(this.currState.getScore() + HORIZONTAL_MOVE_COST );
			
			//update current player position on board
			movePlayer(currState.getPlayerRow(), currState.getPlayerCol()+1);

			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers();
		}
	}

	public void moveUpRight() {
		//is the move valid
		if (isMoveValid(currState.getPlayerRow()-1, currState.getPlayerCol()+1))
		{
			//update score
			this.currState.setScore(this.currState.getScore() + DIAGONAL_MOVE_COST);
			
			//update current player position on board
			movePlayer(currState.getPlayerRow()-1, currState.getPlayerCol()+1);	
			
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers();
		}
	}

	public void moveUpLeft() {
		//is the move valid
		if (isMoveValid(currState.getPlayerRow()-1,  currState.getPlayerCol()-1))
		{
			//update score
			this.currState.setScore(this.currState.getScore() + DIAGONAL_MOVE_COST);
			
			//update current player position on board
			movePlayer(currState.getPlayerRow()-1,currState.getPlayerCol()-1);
			
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers();
		}
	}
	
	public void moveDownRight() {
		//is the move valid
		if (isMoveValid(currState.getPlayerRow()+1, currState.getPlayerCol()+1))
		{
			//update score
			this.currState.setScore(this.currState.getScore() + DIAGONAL_MOVE_COST);
			
			//update current player position on board
			movePlayer(currState.getPlayerRow()+1, currState.getPlayerCol()+1);
			
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers();
		}
	}

	public void moveDownLeft() {
		//is the move valid
		if (isMoveValid(currState.getPlayerRow()+1, currState.getPlayerCol()-1))
		{
			//update score
			this.currState.setScore(this.currState.getScore() + DIAGONAL_MOVE_COST);	
			
			//update current player position on board
			movePlayer(currState.getPlayerRow()+1, currState.getPlayerCol()-1);
			
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers();
		}
	}

	@Override
	public void initBoard() {
		//generate new maze and  push it to current state
		this.currState.setBoard(generateMazeByPrimAlgo(boardSize));
		
	}
	
	
	private boolean isMoveValid(int toRow,  int toCol)
	{
		//valid locations inside the board
		if ((toRow>=0 && toRow<boardSize ) && (toCol>=0 && toCol<boardSize ))
		{ 
			return (currState.getBoard()[toRow][toCol] != WALL); //If no wall then OK, else move is not valid
		}
		else
			return false;
			
	}
	
	private boolean isGameWon(){
		//is current player location the exit
		return currState.getBoard()[currState.getPlayerRow()][currState.getPlayerCol()] ==  EXIT;
	}
	
	private boolean isGameOver(){
		//is current score the maximum
		return ( currState.getScore() ==  SCORE_LIMIT );
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	//generate new maze based on Prim Algorythm 
	//All rights reserved to Google. 
    public int[][] generateMazeByPrimAlgo(int size)
    {
    	// dimensions of generated maze
    	int r=size;
    	int c=size;
 
    	// build maze and initialize with only walls
        StringBuilder s = new StringBuilder(c);
        for(int x=0;x<c;x++)
        	s.append('*');
        char[][] maz = new char[r][c];
        for(int x=0;x<r;x++) 
        	maz[x] = s.toString().toCharArray();
 
        // select random point and open as start node
        Point st = new Point((int)(Math.random()*r),(int)(Math.random()*c),null);
        maz[st.r][st.c] = 'S';
 
        // iterate through direct neighbors of node
        ArrayList<Point> frontier = new ArrayList<Point>();
        for(int x=-1;x<=1;x++)
        	for(int y=-1;y<=1;y++){
        		if(x==0&&y==0||x!=0&&y!=0)
        			continue;
        		try{
        			if(maz[st.r+x][st.c+y]=='.') continue;
        		}catch(Exception e){ // ignore ArrayIndexOutOfBounds
        			continue;
        		}
        		// add eligible points to frontier
        		frontier.add(new Point(st.r+x,st.c+y,st));
        	}
 
        Point last=null;
        while(!frontier.isEmpty()){
 
        	// pick current node at random
        	Point cu = frontier.remove((int)(Math.random()*frontier.size()));
        	Point op = cu.opposite();
        	try{
        		// if both node and its opposite are walls
        		if(maz[cu.r][cu.c]=='*'){
        			if(maz[op.r][op.c]=='*'){
 
        				// open path between the nodes
        				maz[cu.r][cu.c]='.';
        				maz[op.r][op.c]='.';
 
        				// store last node in order to mark it later
        				last = op;
 
        				// iterate through direct neighbors of node, same as earlier
        				for(int x=-1;x<=1;x++)
				        	for(int y=-1;y<=1;y++){
				        		if(x==0&&y==0||x!=0&&y!=0)
				        			continue;
				        		try{
				        			if(maz[op.r+x][op.c+y]=='.') continue;
				        		}catch(Exception e){
				        			continue;
				        		}
				        		frontier.add(new Point(op.r+x,op.c+y,op));
				        	}
        			}
        		}
        	}catch(Exception e){ // ignore NullPointer and ArrayIndexOutOfBounds
        	}
 
        	// if algorithm has resolved, mark end node
        	if(frontier.isEmpty())
        		maz[last.r][last.c]='E';
        }
 
        int[][] maze = new int[size][size];
        
        
        
		// print final maze
		for(int i=0;i<r;i++){
			for(int j=0;j<c;j++)
				{
					if (maz[i][j] == '*')
						maze[i][j] = WALL;
					else if (maz[i][j] == '.')
						maze[i][j] = EMPTY;
					else if (maz[i][j] == 'S')
					{
						maze[i][j] = PLAYER;
						this.currState.setPlayerPosition(i, j); //init player position at starting point
						
					}
					else if (maz[i][j] == 'E')
						maze[i][j] = EXIT;
					
				}
			
		}
		
		return maze;
    }
 
    
    
    static class Point{
    	Integer r;
    	Integer c;
    	Point parent;
    	public Point(int x, int y, Point p){
    		r=x;c=y;parent=p;
    	}
    	// compute opposite node given that it is in the other direction from the parent
    	public Point opposite(){
    		if(this.r.compareTo(parent.r)!=0)
    			return new Point(this.r+this.r.compareTo(parent.r),this.c,this);
    		if(this.c.compareTo(parent.c)!=0)
    			return new Point(this.r,this.c+this.c.compareTo(parent.c),this);
    		return null;
    	}
    }



	
	  
   

}
