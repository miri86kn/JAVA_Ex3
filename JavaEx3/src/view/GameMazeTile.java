package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class GameMazeTile extends Canvas {
	// Data Members
	private Color color; //tile color
	private int value;
	
	public final int EMPTY = 0;
	public final int WALL = 1;
	public final int PLAYER = 2;
	public final int EXIT = 3;

	private Image playerImage;
	private Image exitImage;
	private Image wallImage;
	
	public GameMazeTile(Composite parent, int style) {
		super(parent, style);
		this.value = 0;
		
		playerImage =  new Image(getDisplay(), "resources\\Images\\kenny.jpg");  
		exitImage =  new Image(getDisplay(), "resources\\Images\\South_Park_sign_logo.png");
		wallImage = new Image(getDisplay(), "resources\\Images\\wall.jpg");
		
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				Image scaled ;
				if(value == PLAYER)
				{	
					scaled =  new Image(getDisplay(), playerImage.getImageData().scaledTo(getSize().x,getSize().y)); 
					e.gc.drawImage(scaled, 0, 0);
				}
				else if(value == WALL)
				{
					 scaled =  new Image(getDisplay(), wallImage.getImageData().scaledTo(getSize().x,getSize().y)); 
					e.gc.drawImage(scaled, 0, 0);
				}
				else if(value == EXIT)
				{
					 scaled =  new Image(getDisplay(), exitImage.getImageData().scaledTo(getSize().x,getSize().y)); 
					e.gc.drawImage(scaled, 0, 0);
				}
				
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
			case EMPTY:
				tileColor= new Color(getDisplay(), 255, 255, 255);
			    break;
			case WALL:
				tileColor= new Color(getDisplay(), 0, 0, 0);
			    break;
			case PLAYER:
				tileColor= new Color(getDisplay(), 0, 0, 0);
				//tileColor= new Color(getDisplay(), 232, 23, 23);
			    break; 
			case EXIT:
				tileColor= new Color(getDisplay(), 255, 255, 255);
			    break; 
			
			default:
				tileColor= new Color(getDisplay(), 13, 237, 18);
			  break;
		}
		setBackground(tileColor);
	}
	
	



	
}
