package view;

import model.State;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class Game2048View extends AbsGameView {
	// Data Members
	private Point startDrag, endDrag;
	
	// Method which initializes the game board
	@Override
	public void initGameBoard(State state) {
		// Game board
		this.board = new Game2048Board(boardGroup, SWT.WRAP,state.getBoardSize(), state.getBoard());
		this.board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		this.board.setFocus();

		// TODO: find elegant way to display the board for the first time!!
		shell.setMinimumSize(521, 421);
		
		display.addFilter(SWT.KeyUp, new Listener()
	    {
	        @Override
	        public void handleEvent(Event e)
	        {
	            if (e.widget instanceof Game2048Tile && isChild(board, (Control) e.widget)) 
	            {
	            	
	            	switch (e.keyCode) {
					case (SWT.ARROW_UP):
						userCommand = GameAction.UP;
						break;
					case (SWT.ARROW_DOWN):
						userCommand = GameAction.DOWN;
						break;
					case (SWT.ARROW_LEFT):
						userCommand = GameAction.LEFT;
						break;
					case (SWT.ARROW_RIGHT):
						userCommand = GameAction.RIGHT;
						break;
					default:
						break;
					}

					if ((e.keyCode == SWT.ARROW_UP)
							|| (e.keyCode == SWT.ARROW_DOWN)
							|| (e.keyCode == SWT.ARROW_LEFT)
							|| (e.keyCode == SWT.ARROW_RIGHT)) {
						// raise a flag of a change
						setChanged();
						// actively notify all observers
						// and invoke their update method
						notifyObservers();
					}
	            }
	        }
	    });

		display.addFilter(SWT.MouseDown, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				if (e.widget instanceof Game2048Tile && isChild(board, (Control) e.widget)) 
				{
				 startDrag = new Point(e.x, e.y); 
				 endDrag = startDrag;
				}
			}
		});
		display.addFilter(SWT.MouseUp, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				if (e.widget instanceof Game2048Tile && isChild(board, (Control) e.widget)) 
				{
				endDrag = new Point(e.x, e.y); 
            	
            	int horizontalDiff=endDrag.x-startDrag.x;
            	int verticalDiff=endDrag.y-startDrag.y;
            	
            	//no movement was made
            	if(horizontalDiff==0 && verticalDiff==0)
            		return;
            	//determine what kind of movement was made
            	if( Math.abs(horizontalDiff)>Math.abs(verticalDiff)) //horizontal
            	{
            		if (horizontalDiff>0) //right
            			userCommand = GameAction.RIGHT;
            		else //left
            			userCommand = GameAction.LEFT;
            	}	
            	else //vertical
            	{
            		if (verticalDiff<0) //up
            			userCommand = GameAction.UP;
            		else //down
            			userCommand = GameAction.DOWN;
            	} 
            		
                startDrag = null;  
                endDrag = null;  
              
                // raise a flag of a change
				setChanged();
				// actively notify all observers
				// and invoke their update method
				notifyObservers();
				}
			}
		});
	
	     
	}
	
	@Override
	protected void setShellText() {
		shell.setText("2048 Game");
	}
	
}