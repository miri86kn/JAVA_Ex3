package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class Game2048Board extends Board {
	// Constants
	private static final int ROWS = 4;
	private static final int COLS = 4;
	private static final int LINE_WIDTH = 6;

	// Methods

	// Game2048Board Constructor
	public Game2048Board(Composite parent, int style) {
		super(parent, style); // call Board Constructor
		
		/*boardData = new int[][] { { 4, 0, 0, 3 }, { 8, 0, 16, 32 },
				{ 64, 0, 8, 2 }, { 128, 0, 0, 64 } };*/

		// Initialize the 2d array
		boardData = new int[ROWS][COLS];
		
		// Set board color
		Color boardColor = new Color(getDisplay(), 187, 173, 160);
		this.setBackground(boardColor);
		//this.setForeground(boardColor);
		boardColor.dispose();

		// add the paint listener
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				setLayout(new GridLayout(COLS, true));
				setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

				// Determine board dimensions 
				int maxX = getSize().x - ((COLS+1)*(LINE_WIDTH)); // Board width minus borders
				int maxY = getSize().y - ((ROWS+1)*(LINE_WIDTH)); // Board height minus borders
				
				// Initialize location values
				int xLocation = LINE_WIDTH;
				int yLocation = LINE_WIDTH;
				int tileHeight = maxY / ROWS; 
				int tileWidth = maxX / COLS;

				if (boardData != null) {
					// paint the board
					for (int i = 0; i < boardData.length; i++) {
						for (int j = 0; j < boardData[0].length; j++) {
							// Set rectangles color
							Color rectColor = new Color(getDisplay(), 237, 187, 110);
							e.gc.setBackground(rectColor);
							
							// Draw rectangle in proper location
							Rectangle rect = new Rectangle(xLocation, yLocation, tileWidth, tileHeight);
							//e.gc.drawRectangle(rect);
							e.gc.fillRectangle(rect);
							rectColor.dispose();
							
							// Set string font properties
							Font font = new Font(getDisplay(), "Tahoma", 16, SWT.BOLD);
					        e.gc.setFont(font);
					        e.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
									        
							// Draw string according to the corresponding value in boardData array
					        int stringMargin = 4;
							String cellStr = String.valueOf(boardData[i][j]);
							e.gc.drawString(cellStr,( (rect.x + tileWidth) - (tileWidth / 2) -stringMargin ),( (rect.y + tileHeight) - (tileHeight / 2) -stringMargin ));
							
							// font disposal after drawing the string
							font.dispose();
							
							// Increment tile X location by its width value
							xLocation += (tileWidth + LINE_WIDTH);
						}
						// After painting a whole row
						xLocation = LINE_WIDTH; // Initialize X location
						yLocation += (tileHeight + LINE_WIDTH); // Increment tile Y location by its height value
					}
				}
			}
		});
	}
}