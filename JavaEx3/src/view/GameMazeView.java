package view;



import model.State;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class GameMazeView extends AbsGameView {
	
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

	}

	@Override
	protected void setShellText() {
		shell.setText("Maze Game");
	}
	
	
}