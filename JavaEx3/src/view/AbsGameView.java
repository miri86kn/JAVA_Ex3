package view;


import java.util.Observable;

import model.State;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public abstract class AbsGameView extends Observable implements View, Runnable {
		// Data Members
		Display display;
		Shell shell;
		AbsBoard board;
		GameAction userCommand;

		Group boardGroup;
		Label scoreLbl;
		Label bestScoreLbl;
	    State currState;
		
		// Constants
		private static final int LABEL_DATA_WIDTH = 60;

		// Methods
		
		// Method which initializes all components
		private void initComponents() {
			display = new Display();
			shell = new Shell(display);
			shell.setLayout(new GridLayout(3, false));
			Color shellColor =  new Color(shell.getDisplay(), 250, 248, 239);
			shell.setBackground(shellColor);
			shell.setSize(500, 400);
			shell.setMinimumSize(500, 400);
			shell.setText("2048 Game");
		    
			// Initialize the menus
			initMenus();
			
			Font font = new Font(shell.getDisplay(), "Tahoma", 10, SWT.BOLD);
			
			// Group which contains all option buttons
		    Group buttonGroup = new Group(shell, SWT.SHADOW_OUT);
		    buttonGroup.setLayout(new GridLayout(1, true));
		    buttonGroup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
			buttonGroup.setBackground(shellColor);
		    // Undo button
			Button undoButton = new Button(buttonGroup, SWT.PUSH);
			undoButton.setText("Undo Move");
			undoButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
			undoButton.setFont(font);
			Image imageBack = new Image(shell.getDisplay(), "resources\\Images\\back.png");
			undoButton.setImage(imageBack);
			
			// Restart button
			Button restartButton = new Button(buttonGroup, SWT.PUSH);
			restartButton.setText("Restart Game");
			restartButton.setFont(font);
			Image imageRestart = new Image(shell.getDisplay(), "resources\\Images\\undo.png");
			restartButton.setImage(imageRestart);
			restartButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
			
			// Load button
			Button loadButton = new Button(buttonGroup, SWT.PUSH);
			loadButton.setText("Load Game");
			loadButton.setFont(font);
			Image imageLoad = new Image(shell.getDisplay(), "resources\\Images\\folder.png");
			loadButton.setImage(imageLoad);
			loadButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
			
			// Save button
			Button saveButton = new Button(buttonGroup, SWT.PUSH);
			saveButton.setText("Save Game");
			saveButton.setFont(font);
			Image imageSave = new Image(shell.getDisplay(), "resources\\Images\\star.png");
			saveButton.setImage(imageSave);
			saveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
			
			// Initialize the main group which contains game board and score groups
			initBoardGroup();
			
			// Determine restart button select action
			restartButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					newGame();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});
			
			// Determine load button select action
					loadButton.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetSelected(SelectionEvent arg0) {
							loadGameFile();
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent arg0) {}
					});
					
			// Determine save button select action
			saveButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					saveGame();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				}
			});
			
			// Determine undo button select action
			undoButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					undoAction();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				}
			});
			
			//shell.pack();
			shell.open();
		}
		
		// Method which initializes menu components
		private void initMenus() {
			// Create the bar menu
		    Menu menuBar = new Menu(shell, SWT.BAR);
		    
		    // Create the File item's dropdown menu
		    Menu fileMenu = new Menu(menuBar);

		    // Create all the items in the bar menu
		    MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
		    fileItem.setText("File");
		    fileItem.setMenu(fileMenu);

		    // Create all the items in the File dropdown menu
		    MenuItem loadItem = new MenuItem(fileMenu, SWT.NONE);
		    loadItem.setText("Load Game");
		    MenuItem saveItem = new MenuItem(fileMenu, SWT.NONE);
		    saveItem.setText("Save Game");
		    
		    // Determine load item select action
		    loadItem.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					loadGameFile();
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});
		    
		    // Determine save item select action
		    saveItem.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					saveGame();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});
		    
		    // Create the Edit item's dropdown menu
		    Menu editMenu = new Menu(menuBar);

		    // Create all the items in the bar menu
		    MenuItem editItem = new MenuItem(menuBar, SWT.CASCADE);
		    editItem.setText("Edit");
		    editItem.setMenu(editMenu);

		    // Create all the items in the Edit dropdown menu
		    MenuItem undoItem = new MenuItem(editMenu, SWT.NONE);
		    undoItem.setText("Undo Move");
		    MenuItem restartItem = new MenuItem(editMenu, SWT.NONE);
		    restartItem.setText("Restart Game");
		    
		    // Determine undo item select action
		    restartItem.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					undoAction();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});

		    
		    // Determine restart item select action
		    restartItem.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					newGame();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});
		    
		    // Integrate created menu into shell
		    shell.setMenuBar(menuBar);
		}
		
		// Method which initializes the board group
		private void initBoardGroup() {
			// Group which wrap game board components
		    boardGroup = new Group(shell, SWT.SHADOW_OUT);
		    boardGroup.setLayout(new GridLayout(2, true));
		    boardGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		    Color shellColor =  new Color(shell.getDisplay(), 250, 248, 239);
		    boardGroup.setBackground(shellColor);
		    
		    // Grid data for both score labels
		    GridData labelData = new GridData();
			labelData.widthHint = LABEL_DATA_WIDTH;					/* default width */
			labelData.horizontalAlignment = SWT.FILL;	/* grow to fill available width */
			
			// Group which contains score text
		    final Group scoreGroup = new Group(boardGroup, SWT.SHADOW_OUT );
		    scoreGroup.setText("SCORE");
		    scoreGroup.setLayout(new GridLayout(1, true));
		    scoreGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
		    scoreGroup.setBackground( new Color(shell.getDisplay(), 250, 248, 239));
		    
		    // Score text 
		    Font font = new Font(shell.getDisplay(), "Tahoma", 16, SWT.BOLD);
		       
		    this.scoreLbl = new Label(scoreGroup, SWT.WRAP|SWT.BOLD);
		    scoreLbl.setText("0");
		    scoreLbl.setLayoutData(labelData);
		    scoreLbl.setFont(font);
		    //scoreLbl.setSize(new Point());
		    scoreLbl.setForeground(new Color(shell.getDisplay(), 119, 110, 101));
		    scoreLbl.setBackground(new Color(shell.getDisplay(), 250, 248, 239));
		    
		    // Group which contains best score label
		    final Group bestScoreGroup = new Group(boardGroup, SWT.SHADOW_OUT);
		    bestScoreGroup.setText("BEST");
		    bestScoreGroup.setLayout(new GridLayout(1, true));
		    bestScoreGroup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		    bestScoreGroup.setBackground( new Color(shell.getDisplay(), 250, 248, 239));
		 	// Best score label
		    this.bestScoreLbl = new Label(bestScoreGroup, SWT.WRAP);
		    bestScoreLbl.setText("0");
		    bestScoreLbl.setFont(font);
			bestScoreLbl.setLayoutData(labelData);
			bestScoreLbl.setBackground( new Color(shell.getDisplay(), 250, 248, 239));
			 	
		}
		
		// Method which initializes the game board
		public abstract void initGameBoard(State state);
		/*
		private void initGameBoard(State state) {
			// Game board
			this.board = new Game2048Board(boardGroup, SWT.WRAP,state.getBoardSize(), state.getBoard());
			this.board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
			this.board.setFocus();

			// TODO: find elegant way to display the board for the first time!!
			shell.setMinimumSize(501, 401);
			
			display.addFilter(SWT.KeyUp, new Listener()
		    {
		        @Override
		        public void handleEvent(Event e)
		        {
		            if (e.widget instanceof MazeTile && isChild(board, (Control) e.widget)) 
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
		*/
		
		
		// Method which opens "save file" dialog and stores file name
		private void saveGame() {
			FileDialog fd = new FileDialog(shell, SWT.SAVE);
			fd.setText("Save Game");
			fd.setFilterPath(System.getProperty("user.home"));
			String[] filterExt = { ".xml" };
			fd.setFilterExtensions(filterExt);
			String fileName = fd.open(); // Store selected file name as string
			if (fileName != null) {
				// update user command
				userCommand = GameAction.SAVE;
				// raise a flag of a change
				setChanged();
				// notify all observers about the given file name
				// and invoke their update method
				notifyObservers(fileName);
			}

		}
		
		// Method which opens file dialog and returns selected game file name
		private void loadGameFile() {
			FileDialog fd = new FileDialog(shell, SWT.OPEN);
			fd.setText("Select Game File");
			fd.setFilterPath(System.getProperty("user.home"));
			String[] filterExt = { "*.xml", "*.*" };
			fd.setFilterExtensions(filterExt);
			String selected = fd.open();
			if (selected != null) {
				// update user command
				userCommand = GameAction.LOAD;
				// raise a flag of a change
				setChanged();
				// notify all observers about the given file name
				// and invoke their update method
				notifyObservers(selected);
			}
		}
		
		// Method which starts new game
		private void newGame() {
			userCommand = GameAction.RESTART;
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers();
		}
		
		// Method which undo 1 action
		private void undoAction() {
			userCommand = GameAction.UNDO;
			// raise a flag of a change
			setChanged();
			// actively notify all observers
			// and invoke their update method
			notifyObservers();
		}
			
		@Override
		public void run() {
			initComponents();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
		}

		
		@Override
		public void displayBoard(State state) {
			currState = state;
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					if (board == null) {
						initGameBoard(currState);
					}
					else 
					{
						board.redraw(currState); // redraw board
						displayScore(); // display updates scores
					}
				}
			});
		}

		@Override
		public GameAction getUserCommand() {
			return userCommand;
		}

		@Override
		// Method that handles scores
		public void displayScore() {
			scoreLbl.setText(String.valueOf(currState.getScore())); // display current score
			bestScoreLbl.setText(String.valueOf(currState.getScore())); // TODO: display best score
		}
		
		public void setUserCommand(GameAction userCommand) {
			this.userCommand = userCommand;
		}
		
		protected static boolean isChild(Control parent, Control child)
		{
		    if (child.equals(parent))
		        return true;

		    Composite p = child.getParent();

		    if (p != null)
		        return isChild(parent, p);
		    else
		        return false;
		}
	
}
