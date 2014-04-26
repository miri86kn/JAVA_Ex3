package view;

import java.awt.FontMetrics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class Tile extends Canvas{

	private Color color; //tile color
	private int value;
	
	

	public Tile(Composite parent, int style, Color color, int val){
		super(parent, style);
		this.color = color;
		this.value = val;
		
		Font f = getFont();
		Font nf = new Font(getDisplay(), f.getFontData()[0].getName(), 16, SWT.BOLD);
		
		setFont(nf);
		
		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				org.eclipse.swt.graphics.FontMetrics fm = e.gc.getFontMetrics();
				int width = fm.getAverageCharWidth();
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
	}
	

	public void changeBackgroundValue(Color color){
		setBackground(color);
	}
}
