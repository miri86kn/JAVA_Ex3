package view;



import model.State;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class GameMazeView extends AbsGameView {
	
	private boolean upPressed, downPressed, leftPressd, rightPressed;
	private boolean diagonal;
	// Method which initializes the game board
	@Override
	public void initGameBoard(State state) {
		upPressed= downPressed= leftPressd= rightPressed=diagonal=false;
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
	            	switch (e.keyCode) {
					
	            	case (SWT.ARROW_UP):
						if(leftPressd)
						{
							userCommand = GameAction.UP_LEFT;
							//leftPressd=false;
						}
						else if(rightPressed)
						{
							userCommand = GameAction.UP_RIGHT;
							//rightPressed = false;
						}
						else if(upPressed && !(leftPressd||rightPressed))
							userCommand = GameAction.UP;
	            		upPressed=false;	
	            		break;
					case (SWT.ARROW_DOWN):
						if(leftPressd)
						{
							userCommand = GameAction.DOWN_LEFT;
							//leftPressd = false;
						}
						else if(rightPressed)
						{
							userCommand = GameAction.DOWN_RIGHT;
							//rightPressed = false;
						}
						else if(downPressed && !(leftPressd||rightPressed))
							userCommand = GameAction.DOWN;
					    downPressed=false;
						break;
					case (SWT.ARROW_LEFT):
						if(upPressed)
						{
							userCommand = GameAction.UP_LEFT;
							//upPressed=false;
						}
						else if(downPressed)
						{
							userCommand = GameAction.DOWN_LEFT;
							//downPressed= false;
						}
						else if(leftPressd && !(upPressed || downPressed))
							userCommand = GameAction.LEFT;
						leftPressd=false;
						break;
					case (SWT.ARROW_RIGHT):
						if(upPressed)
						{
							userCommand = GameAction.UP_RIGHT;
							//upPressed=false;
						}
						else if (downPressed)
						{
							userCommand = GameAction.DOWN_RIGHT;
							//downPressed=false;
						}
						else if (rightPressed && !(upPressed || downPressed))
							userCommand = GameAction.RIGHT;
					    rightPressed=false;
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
		
		//register what key was pressed to find out later horizontal movement
		display.addFilter(SWT.KeyDown, new Listener()
	    {
	        @Override
	        public void handleEvent(Event e)
	        {
	            if (e.widget instanceof GameMazeTile && isChild(board, (Control) e.widget)) 
	            {
	            	switch (e.keyCode) {
					
	            	case (SWT.ARROW_UP):
						upPressed=true;
						break;
					case (SWT.ARROW_DOWN):
						downPressed=true;
						break;
					case (SWT.ARROW_LEFT):
						leftPressd=true;
						break;
					case (SWT.ARROW_RIGHT):
						rightPressed=true;
						break;
					default:
						break;
					}
	            }
	        }
	    });

	}

	@Override
	protected void setShellText() {
		shell.setText("Maze Game");
	}
	
	
}