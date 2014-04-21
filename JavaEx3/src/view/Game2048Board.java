package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class Game2048Board extends Board {
	// Constants
	private static final int ROWS = 4;
	private static final int COLS = 4;

	// Methods

	// Game2048Board Constructor
	public Game2048Board(Composite parent, int style) {
		super(parent, style); // call Board Constructor
		
		/*boardData = new int[][] { { 4, 0, 0, 3 }, { 8, 0, 16, 32 },
				{ 64, 0, 8, 2 }, { 128, 0, 0, 64 } };*/

		// Initialize the 2d array
		boardData = new int[ROWS][COLS];
		
		// Set board color
		Color boardColor = new Color(parent.getDisplay(), 187, 173, 160);
		this.setBackground(boardColor);
		boardColor.dispose();

		// add the paint listener
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				setLayout(new GridLayout(COLS, true));
				setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

				// Determine board dimensions 
				int maxX = getSize().x - 1; // Board width
				int maxY = getSize().y - 1; // Board height
				
				// Initialize location values
				int xLocation = 0;
				int yLocation = 0;
				int tileHeight = maxY / ROWS; 
				int tileWidth = maxX / COLS;

				if (boardData != null) {
					// paint the board
					for (int i = 0; i < boardData.length; i++) {
						for (int j = 0; j < boardData[0].length; j++) {
							// Draw rectangle in proper location
							Rectangle rect = new Rectangle(xLocation, yLocation, tileWidth, tileHeight);
							e.gc.drawRectangle(rect);
							
							// Draw string according to the value of the corresponding boardData array
							String cellStr = String.valueOf(boardData[i][j]);
							e.gc.drawString(cellStr,( (rect.x + tileWidth) - (tileWidth / 2) ),( (rect.y + tileHeight) - (tileHeight / 2) ));
							
							// Increment tile X location by its width value
							xLocation += (tileWidth);
						}
						// After painting a whole row
						xLocation = 0; // Initialize X location
						yLocation += (tileHeight); // Increment tile Y location by its height value
					}
				}
			}
		});
	}
}
