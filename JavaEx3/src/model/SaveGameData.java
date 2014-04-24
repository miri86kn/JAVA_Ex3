package model;

import java.util.Stack;

public class SaveGameData {
	
	private State currentState;
	private Stack<State> stateStack;
	
	public void setCurrentState(State currentState, Stack<State> stateStack) {
		this.currentState = currentState;
		this.stateStack = stateStack;
	}

	public State getCurrentState() {
		return currentState;
	}

	public SaveGameData(State currentState, Stack<State> stateStack) {
		super();
		this.currentState = currentState;
		this.stateStack = stateStack;
	}

	public Stack<State> getStateStack() {
		return stateStack;
	}

	public void setStateStack(Stack<State> stateStack) {
		this.stateStack = stateStack;
	}
}
