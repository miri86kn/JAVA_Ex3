package view;



import model.State;

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
	// Data Members
	int rows;
	int cols;

	// Constants
	private static final int ONE_DIGIT_FONT_SIZE = 26;
	private static final int TWO_DIGIT_FONT_SIZE = 26;
	private static final int THREE_DIGIT_FONT_SIZE = 22;
	private static final int FOUR_DIGIT_FONT_SIZE = 18;
	private static final int LINE_WIDTH = 6;

	// Methods

	// Game2048Board Constructor
	public Game2048Board(Composite parent, int style, int size, int[][] data) {
		super(parent, style); // call Board Constructor

		// Initialize board dimensions
		this.rows = size;
		this.cols = size;

		// Initialize the 2d array
		boardData = data;

		// Set board color
		Color boardColor = new Color(getDisplay(), 187, 173, 160);
		this.setBackground(boardColor);
		//this.setForeground(boardColor);
		boardColor.dispose();

		// add the paint listener
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {

				setLayout(new GridLayout(cols, true));
				setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

				// Determine board dimensions 
				int maxX = getSize().x - ((cols+1)*(LINE_WIDTH)); // Board width minus borders
				int maxY = getSize().y - ((rows+1)*(LINE_WIDTH)); // Board height minus borders

				// Initialize location values
				int xLocation = LINE_WIDTH;
				int yLocation = LINE_WIDTH;
				int tileHeight = maxY / rows; 
				int tileWidth = maxX / cols;

				if (boardData != null) {
					// paint the board
					for (int i = 0; i < boardData.length; i++) {
						for (int j = 0; j < boardData[0].length; j++) {
							
							
							// Set rectangles color
							Color rectColor;
							switch (boardData[i][j]) {
								case 0:
									rectColor= new Color(getDisplay(), 204, 192, 179);
								    break;
								case 2:
									rectColor= new Color(getDisplay(), 238, 228, 218);
								    break;
								case 4:
									rectColor= new Color(getDisplay(), 237, 224, 200);
								    break; 
								case 8:
									rectColor= new Color(getDisplay(), 237, 194, 147);
								    break; 
								case 16:
									rectColor= new Color(getDisplay(), 220, 117, 75);
								    break; 
								case 32:
									rectColor= new Color(getDisplay(), 220, 83, 53);
								    break;
								default:
									rectColor= new Color(getDisplay(), 220, 186, 49);
								  break;
							}
							
							e.gc.setBackground(rectColor);

							// Draw rectangle in proper location
							Rectangle rect = new Rectangle(xLocation, yLocation, tileWidth, tileHeight);
   							//make border round
							e.gc.fillRoundRectangle(xLocation, yLocation, tileWidth, tileHeight, 10, 10);
   							//e.gc.fillRectangle(rect);
							rectColor.dispose();

							// Draw string according to the corresponding value in boardData array
							String cellStr;
							if (boardData[i][j] == 0)
								cellStr = "";
							else
								cellStr = String.valueOf(boardData[i][j]);
							
							// Set string font properties
							int fontSize = getFontSize(cellStr); 
							Font font = new Font(getDisplay(), "Tahoma", fontSize, SWT.BOLD);
					        e.gc.setFont(font);
					        e.gc.setForeground(new Color(getDisplay(), 119, 110, 101));
							
					        int strHeight = font.getFontData()[0].getHeight();
					        int fontWidth = (strHeight/2);
					        int strWidht = fontWidth*(cellStr.length());
							//int stringWidthMargin = ( (fontSize / 2) +2 );
					        int stringWidthMargin = ((tileWidth - strWidht)/2) - (strWidht/3);
							int stringHeightMargin = ((tileHeight - strHeight)/2) - (strHeight/3);
							
							//e.gc.drawString(cellStr,( (rect.x + tileWidth) - (tileWidth / 2) -stringWidthMargin ),( (rect.y + tileHeight) - (tileHeight / 2) -stringHeightMargin ));
							e.gc.drawString(cellStr,( xLocation + stringWidthMargin ),(yLocation + stringHeightMargin) );
							
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
	
	// Calculate font size according to number of digits
	private int getFontSize(String cellStr) {
		int fontSize = 0;
		switch (cellStr.length()) {
		case 1:
			fontSize = ONE_DIGIT_FONT_SIZE;
			break;
		case 2:
			fontSize = TWO_DIGIT_FONT_SIZE;
			break;
		case 3:
			fontSize = THREE_DIGIT_FONT_SIZE;
			break;
		case 4:
			fontSize = FOUR_DIGIT_FONT_SIZE;
			break;
		default:
			break;
		}
		return fontSize;
	}

	public void redraw(State state) {
		setBoardData(state.getBoard());
		this.redraw();
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