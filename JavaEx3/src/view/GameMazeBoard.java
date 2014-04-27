package view;

import model.State;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


public class GameMazeBoard extends AbsBoard {
	// Data Members
	int rows;
	int cols;
	GameMazeTile tiles[][];

	
	private static final int SPACE_WIDTH = 2;
	// Methods

	// Game2048Board Constructor
	public GameMazeBoard(Composite parent, int style, int size, int[][] data) {
		super(parent, style); // call Board Constructor

		// Initialize board dimensions
		this.rows = size;
		this.cols = size;

		GridLayout gl = new GridLayout();
		gl.numColumns = cols;
		gl.makeColumnsEqualWidth = true;
		gl.marginWidth = SPACE_WIDTH;
		gl.marginHeight = SPACE_WIDTH;
		gl.verticalSpacing = SPACE_WIDTH;
		gl.horizontalSpacing = SPACE_WIDTH;
		
		setLayout(gl);
		
		// Initialize the 2d array
		boardData = data;

		// Set board color
		Color boardColor = new Color(getDisplay(), 187, 173, 160);
		
		this.setBackground(boardColor);
		this.setForeground(boardColor);
		boardColor.dispose();

		tiles= new GameMazeTile[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				tiles[i][j] = new GameMazeTile(this, SWT.BORDER_SOLID);
				tiles[i][j].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				tiles[i][j].setValue(boardData[i][j]);
			}
		}
		
	}

	@Override
	public void redraw(State state) {
		setBoardData(state.getBoard());
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				tiles[i][j].setValue(boardData[i][j]);
			}
		}
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}
}