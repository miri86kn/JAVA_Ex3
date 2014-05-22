package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Stack;

import Entities.GameAction;
import Entities.SlimState;

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
		{
			if(!this.stateStack.isEmpty())
				this.currState = this.stateStack.pop();
			else
			{
				if(stateStack.size() == 0)
					this.stateStack.push(currState.Clone());
				return;
			}
		}
		else
			this.currState =s;
		
		if(stateStack.size() == 0)
			this.stateStack.push(currState.Clone());
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
	public void solve(String ip, int port, int numOfMoves)
	{
		//convert current state to slim state
		Entities.SlimState slimState = new SlimState(currState.getBoard(), currState.getScore());
		
		//send slim state to server
		try {
			RunSolver(ip, port, slimState, numOfMoves);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] srgs) throws Exception 
	{
		Socket  myServer = new Socket(InetAddress.getLocalHost(), 5000);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(myServer.getInputStream()));
		PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String line;
		//read input
		while(!(line = inFromUser.readLine()).equals("exit"))
		{
			outToServer.println(line); //notify server
			outToServer.flush();
		}
		outToServer.println(line);
		
		//close all stuff
		inFromUser.close();
		inFromServer.close();
		outToServer.close();
		myServer.close();
	}
	
	public  void RunSolver(String ip, int port, SlimState state, int numOfMoves) throws Exception 
	{
		Socket  myServer = new Socket(InetAddress.getByName(ip), port);
		
		ObjectOutputStream outToServer = new ObjectOutputStream(myServer.getOutputStream());
		ObjectInputStream inFromServer = new ObjectInputStream(myServer.getInputStream());				
		
		
		SlimState s;
		s = state.clone();
		
		outToServer.writeObject(s);
		outToServer.flush();
		
		ArrayList<GameAction> actionsList = new ArrayList<GameAction>();
		
		if (numOfMoves>0) 
		{
			for(int i=0; i<numOfMoves; i++)
			{
				GameAction nextMove;
				nextMove = (GameAction)inFromServer.readObject();
				actionsList.add(nextMove);
				//System.out.println("Server returned: " + nextMove.getName());
				switch(nextMove)	
				{
					case DOWN:
						moveDown();
						break;
					case LEFT:
						moveLeft();
						break;
					case UP:
						moveUp();
						break;
					case RIGHT:
						moveRight();
						break;
					default:
				}
				s =new SlimState(currState.getBoard(), currState.getScore());
			}
		}
		// Send state with -1 score to end communication with server
		s.setScore(-1);
		outToServer.writeObject(s);
		outToServer.flush();
		
		//TODO: write to log
		System.out.println("Client finished");
		
		//close resources
		outToServer.close();
		myServer.close();
	
	}
}
