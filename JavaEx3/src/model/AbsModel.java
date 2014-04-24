package model;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Stack;

import com.thoughtworks.xstream.XStream;

public abstract class AbsModel  extends Observable implements Model, Runnable{

	protected Stack<State> stateStack;
	protected State currState;
	protected int boardSize;
	
	protected final int EMPTY_CELL = 0;
	protected final int WINNIG_NUMBER = 2048;
			
	public AbsModel(){}
	
	
	public AbsModel(int boardSize) {
		
		this.stateStack = new Stack<State>(); //create stack of state/moves
		this.boardSize = boardSize;

		newGame();//create new game
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
		if (this.stateStack.isEmpty())
			return;
		
		State s = this.stateStack.pop();
		if (s.equals(this.currState))
			if(!this.stateStack.isEmpty())
				this.currState = this.stateStack.pop();
			else
				return;
		else
			this.currState =s;
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers(); 
	}

	@Override
	public void saveGame(String path) {
		
		
		//create xml from current game state
		XStream xstream = new XStream();
		String xml = xstream.toXML(new SaveGameData(this.currState, this.stateStack)); //this.currState
		
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
		SaveGameData loadedGame = (SaveGameData)xstream.fromXML(in);
		this.currState = loadedGame.getCurrentState();
		this.stateStack = loadedGame.getStateStack();
		
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
		stateStack.add(this.currState.Clone()); //save current state
		 
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers(); 
		
	}
	
	
	@Override
	public State getCurrtState() {
		return this.currState;
	}

}
