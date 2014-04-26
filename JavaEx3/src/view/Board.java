package view;

import org.eclipse.swt.widgets.Composite;

public abstract class Board extends Composite {
	// Data Members
	int[][] boardData; // the data of the board
	
	// Methods
	
	// Board Constructor
	public Board(Composite parent, int style) {		
		super(parent, style); // call canvas Constructor
	}

	public int[][] getBoardData() {
		return boardData;
	}

	public void setBoardData(int[][] boardData) {
		this.boardData = boardData;
	}
	
}
