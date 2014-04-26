package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import view.View.GameAction;

public class Tile extends Canvas{
	// Data Members
	private Color color; //tile color
	private int value;
	private int fontSize;
	
	// Constants
	private static final int ONE_DIGIT_FONT_SIZE = 26;
	private static final int TWO_DIGIT_FONT_SIZE = 26;
	private static final int THREE_DIGIT_FONT_SIZE = 22;
	private static final int FOUR_DIGIT_FONT_SIZE = 18;

	public Tile(Composite parent, int style) {
		super(parent, style);
		// this.color = color;
		this.value = 0;
		
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				// Set string font properties
				String tileStr = "" + value;
				int fontSize = getFontSize(tileStr);
				Font font = new Font(getDisplay(), "Tahoma", fontSize, SWT.BOLD);
				e.gc.setFont(font);
				FontMetrics fm = e.gc.getFontMetrics();
				int charHeight = fm.getHeight();
				int charWidth = fm.getAverageCharWidth(); // One digit width
				int numDigits = (tileStr.length()); // Number of digits
				int mx = getSize().x/2 - numDigits * charWidth/2;
				int my = getSize().y/2 - fm.getHeight()/2-fm.getDescent();
				
				//e.gc.setForeground(new Color(getDisplay(), 119, 110, 101));
				e.gc.setForeground(new Color(getDisplay(), 0, 0, 0));
				if (value>0) {
					e.gc.drawString(tileStr, mx, my);
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
				tileColor= new Color(getDisplay(), 237, 194, 147);
			    break; 
			case 16:
				tileColor= new Color(getDisplay(), 220, 117, 75);
			    break; 
			case 32:
				tileColor= new Color(getDisplay(), 220, 83, 53);
			    break;
			default:
				tileColor= new Color(getDisplay(), 220, 186, 49);
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
	
	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}
