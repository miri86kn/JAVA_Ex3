package view;



import model.State;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class GameMazeView extends AbsGameView {
	

	
	private int vertical; // -1 for up, 0 for nothing, 1 for down  
	private int horizontal; // -1 for left, 0 for nothing, 1 for right 
	private int pressedCount;
	
	// Method which initializes the game board
	@Override
	public void initGameBoard(State state) {
		
		// Game board
		this.board = new GameMazeBoard(boardGroup, SWT.WRAP,state.getBoardSize(), state.getBoard());
		this.board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		this.board.setFocus();

		// TODO: find elegant way to display the board for the first time!!
		shell.setMinimumSize(521, 421);
		
		display.addFilter(SWT.KeyUp, new Listener()
	    {
	        @Override
	        public void handleEvent(Event e)
	        {
	        	
	            if (e.widget instanceof GameMazeTile && isChild(board, (Control) e.widget)) 
	            {
	            	
	            	
	            	boolean change= true;
	            	
	            	
	            	//horizonatal: -1:left 0:nothing 1:right
	            	//vertical:    -1:up   0:nothing 1:right
	            	if (horizontal < 0 && vertical < 0) // up and left  
	            	{	
	            		userCommand = GameAction.UP_LEFT; pressedCount--;}
	            	else if(horizontal < 0 && vertical > 0) //down and left
	            	{	
	            		userCommand = GameAction.DOWN_LEFT; pressedCount--;}
	            	else if(horizontal > 0 && vertical < 0) //up and right
	            	{	
	            		userCommand = GameAction.UP_RIGHT; pressedCount--;}
	            	else if(horizontal > 0 && vertical > 0) //down and right
	            	{	
	            		userCommand = GameAction.DOWN_RIGHT; pressedCount--;}
	            	else if(horizontal > 0 && vertical == 0 && pressedCount==1) //right
	            		userCommand = GameAction.RIGHT;
	            	else if(horizontal < 0 && vertical == 0 && pressedCount==1) //left
	            		userCommand = GameAction.LEFT;
	            	else if(horizontal == 0 && vertical < 0 && pressedCount==1) //up
	            		userCommand = GameAction.UP;
	            	else if(horizontal == 0 && vertical > 0 && pressedCount==1) //down
	            		userCommand = GameAction.DOWN;
	            	else
	            	{
	            		change=false;
	            	}
	            	
	            	switch (e.keyCode) {
					
		            	case (SWT.ARROW_UP):
		            		vertical++;
		            		break;
						case (SWT.ARROW_DOWN):
						    vertical--;
							break;
						case (SWT.ARROW_LEFT):
							horizontal++;
							break;
						case (SWT.ARROW_RIGHT):
							horizontal--;
							break;
						default:
							break;
					}
	            	
					if ((e.keyCode == SWT.ARROW_UP)
							|| (e.keyCode == SWT.ARROW_DOWN)
							|| (e.keyCode == SWT.ARROW_LEFT)
							|| (e.keyCode == SWT.ARROW_RIGHT) ) {
						
						if(change)
						{
						pressedCount--;
							// raise a flag of a change
						setChanged();
						// actively notify all observers
						// and invoke their update method
						notifyObservers();
						}
					}
	            }
	        }
	    });
		
		//register what key was pressed to find out later horizontal movement
		display.addFilter(SWT.KeyDown, new Listener()
	    {
	        @Override
	        public void handleEvent(Event e)
	        {
	            if (e.widget instanceof GameMazeTile && isChild(board, (Control) e.widget)) 
	            {
	            	if (pressedCount<0)
	            		pressedCount=0;
	            	switch (e.keyCode) {
		            	case (SWT.ARROW_UP):
		            		vertical--;	
		            		break;
						case (SWT.ARROW_DOWN):
						    vertical++;
							break;
						case (SWT.ARROW_LEFT):
							horizontal--;
							break;
						case (SWT.ARROW_RIGHT):
							horizontal++;
							break;
						default:
							break;
					}
	            	if((e.keyCode == SWT.ARROW_UP)
							|| (e.keyCode == SWT.ARROW_DOWN)
							|| (e.keyCode == SWT.ARROW_LEFT)
							|| (e.keyCode == SWT.ARROW_RIGHT) )
	            		pressedCount++;
	            }
	        }
	    });

	}

	@Override
	protected void setShellText() {
		shell.setText("Maze Game");
	}
	
	
}