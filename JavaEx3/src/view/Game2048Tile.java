package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class Game2048Tile extends Canvas {
	// Data Members
	private Color color; //tile color
	private int value;
	
	// Constants
	private static final int ONE_DIGIT_FONT_SIZE = 26;
	private static final int TWO_DIGIT_FONT_SIZE = 26;
	private static final int THREE_DIGIT_FONT_SIZE = 24;
	private static final int FOUR_DIGIT_FONT_SIZE = 18;

	public Game2048Tile(Composite parent, int style) {
		super(parent, style);
		// this.color = color;
		this.value = 0;
		
		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				int tileWidth = getSize().x;
				int tileHeight = getSize().y;
				
				// Set string font properties
				String tileStr = "" + value;
				int fontSize = getFontSize(tileStr); // Compute font size according to value
				Font font = new Font(getDisplay(), "Tahoma", fontSize, SWT.BOLD);
				e.gc.setFont(font);
				
		
				
				int strHeight = font.getFontData()[0].getHeight();
		        int charWidth = (strHeight/2); // One digit width
				int strWidth = charWidth*(tileStr.length()); // Whole string width, based on number of digits
				int stringWidthMargin = ((tileWidth - strWidth)/2) - (strWidth/3) -1;
				int stringHeightMargin = ((tileHeight - strHeight)/2) - (strHeight/3);
				
				e.gc.setForeground(new Color(getDisplay(), 119, 110, 101)); // Set font color
				
				// If value is not zero, draw the string
				if (value>0) {
					e.gc.drawString(tileStr, stringWidthMargin, stringHeightMargin);
				}
				// font disposal after drawing the string
				font.dispose();
			}
		});

	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		changeBackgroundColor();
		redraw();
	}
	
	public void changeBackgroundColor(){
		// Set background color
		Color tileColor;
		/*
		 * int r,g,b;
		r=Math.abs((255-value*10))%255;//(242)%255;
		g=228;//Math.abs((255-value*10))%255;
		b=2;//(180)%255;
		switch (value) {
		case 0://no number
			tileColor= new Color(getDisplay(), 204, 192, 179);
		    break;
		default://number->set color by value    
			tileColor= new Color(getDisplay(), r, g, b);
			break;
		}*/
		switch (value) {
			case 0:
				tileColor= new Color(getDisplay(), 204, 192, 179);
			    break;
			case 2:
				tileColor= new Color(getDisplay(), 238, 228, 218);
			    break;
			case 4:
				tileColor= new Color(getDisplay(), 237, 224, 200);
			    break; 
			case 8:
				tileColor= new Color(getDisplay(), 242, 177, 121);
			    break; 
			case 16:
				tileColor= new Color(getDisplay(), 245, 148, 99);
			    break; 
			case 32:
				tileColor= new Color(getDisplay(), 246, 124, 94);
			    break;
			case 64:
				tileColor= new Color(getDisplay(), 246, 94, 59);
			    break;
			case 128:
				tileColor= new Color(getDisplay(), 237, 207, 114);
			    break;
			case 256:
				tileColor= new Color(getDisplay(), 237, 204, 97);
			    break;
			case 512:
				tileColor= new Color(getDisplay(), 237, 200, 80);
			    break;
			case 1024:
				tileColor= new Color(getDisplay(), 237, 197, 63);
			    break;
			case 2048:
				tileColor= new Color(getDisplay(), 237, 194, 46);
			    break;
			default:
				tileColor= new Color(getDisplay(), 246, 186, 49);
			  break;
		}
		setBackground(tileColor);
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
}
	

