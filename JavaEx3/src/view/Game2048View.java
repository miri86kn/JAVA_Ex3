package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class Game2048View extends Observable implements View, Runnable {
	
	
	
	// Data Members
	Display display;
	Shell shell;
	Game2048Board board;
	GameAction userCommand;
	Label scoreLbl;
	Label bestScoreLbl;
	String gameFile;
	
	// Constants
	private static final int LABEL_DATA_WIDTH = 60;

	// Methods
	
	// Method which initializes all components
	private void initComponents() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(3, false));
		shell.setSize(500, 400);
		shell.setMinimumSize(500, 400);
		shell.setText("2048 Game");
	    
		// Initialize the menus
		initMenus();
		
		// Group which contains all option buttons
	    Group buttonGroup = new Group(shell, SWT.SHADOW_OUT);
	    buttonGroup.setLayout(new GridLayout(1, true));
	    buttonGroup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
	    
	    // Undo button
		Button undoButton = new Button(buttonGroup, SWT.PUSH);
		undoButton.setText("Undo Move");
		undoButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		// Restart button
		Button restartButton = new Button(buttonGroup, SWT.PUSH);
		restartButton.setText("Restart Game");
		restartButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		// Load button
		Button loadButton = new Button(buttonGroup, SWT.PUSH);
		loadButton.setText("Load Game");
		loadButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		// Save button
		Button saveButton = new Button(buttonGroup, SWT.PUSH);
		saveButton.setText("Save Game");
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
						gameFile = loadGameFile();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {}
				});
		
		// Determine undo button select action
		undoButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				bestScoreLbl.setText("8823456");
				//bestScoreTxt.pack();
				//bestScoreGroup.pack();
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
				gameFile = loadGameFile();
				
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
	    
	    // Integrate created menu into shell
	    shell.setMenuBar(menuBar);
	}
	
	// Method which initializes the board group
	private void initBoardGroup() {
		// Group which wrap game board components
	    Group boardGroup = new Group(shell, SWT.SHADOW_OUT);
	    boardGroup.setLayout(new GridLayout(2, true));
	    boardGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
	    // Grid data for both score labels
	    GridData labelData = new GridData();
		labelData.widthHint = LABEL_DATA_WIDTH;					/* default width */
		labelData.horizontalAlignment = SWT.FILL;	/* grow to fill available width */
	    
		// Group which contains score text
	    final Group scoreGroup = new Group(boardGroup, SWT.SHADOW_OUT);
	    scoreGroup.setText("SCORE");
	    scoreGroup.setLayout(new GridLayout(1, true));
	    scoreGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
	    
	    // Score text 
	    this.scoreLbl = new Label(scoreGroup, SWT.WRAP);
	    scoreLbl.setText("0");
	    scoreLbl.setLayoutData(labelData);
	 	
	    // Group which contains best score label
	    final Group bestScoreGroup = new Group(boardGroup, SWT.SHADOW_OUT);
	    bestScoreGroup.setText("BEST");
	    bestScoreGroup.setLayout(new GridLayout(1, true));
	    bestScoreGroup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
	    
	 	// Best score label
	    this.bestScoreLbl = new Label(bestScoreGroup, SWT.WRAP);
	    bestScoreLbl.setText("0");
		bestScoreLbl.setLayoutData(labelData);
	    
	    /*final Text bestScoreTxt = new Text(bestScoreGroup, SWT.BORDER);
	    bestScoreTxt.setText("0");
	    bestScoreTxt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));*/
		
		// Game board
		this.board = new Game2048Board(boardGroup, SWT.WRAP,4);
		this.board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		this.board.setFocus();
		
		// TODO: update userCommand according to enum index
		board.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent arg0) {}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.keyCode) {
				case (SWT.ARROW_UP):
					userCommand = GameAction.UP;
					//scoreLbl.setText("1000");
					break;
				case (SWT.ARROW_DOWN):
					userCommand = GameAction.DOWN;
					//scoreLbl.setText("2000");
					break;
				case (SWT.ARROW_LEFT):
					userCommand = GameAction.LEFT;
					//scoreLbl.setText("3000");
					break;
				case (SWT.ARROW_RIGHT):
					userCommand = GameAction.RIGHT;
					//scoreLbl.setText("4000");
					break;
				default:
					break;
				}
				
				if ( (arg0.keyCode == SWT.ARROW_UP) || (arg0.keyCode == SWT.ARROW_DOWN) || (arg0.keyCode == SWT.ARROW_LEFT) || (arg0.keyCode == SWT.ARROW_RIGHT) ) {
					// raise a flag of a change
					setChanged();
					// actively notify all observers
					// and invoke their update method
					notifyObservers();
				}
			}
		});
		
		
		/*board.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				System.out.println("mouseUp");
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (arg0.stateMask == SWT.MouseEnter) {
					System.out.println("mouseDown");
				}
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				System.out.println("mouseDubleClick");
				
			}
		});*/
		
		
		/*Listener listener = new Listener() {
		      public void handleEvent(Event event) {
		        switch (event.type) {
		        case ((SWT.MouseDown) ):
		          System.out.println("down:" + event);
		          break;
		        case SWT.MouseMove:
		          System.out.println("move:"+event);
		          break;
		        case SWT.MouseUp:
		          System.out.println("Up:"+event);
		          break;
		        }
		      }
		    };
		    board.addListener(SWT.MouseDown, listener);
		    board.addListener(SWT.MouseUp, listener);
		    */
	}
	
	//TODO
	private void saveGame() {
		
	}
	
	// Method which opens file dialog and returns selected game file name
	private String loadGameFile() {
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		fd.setText("Select Game File");
		fd.setFilterPath(System.getProperty("user.home"));
		String[] filterExt = { ".xml", "*.*" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		return selected;
	}
	
	//TODO
	// Method which starts new game
	private void newGame() {
		userCommand = GameAction.RESTART;
		// raise a flag of a change
		setChanged();
		// actively notify all observers
		// and invoke their update method
		notifyObservers(); ///bbb
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

	// ...

	@Override
	public void displayBoard(int[][] data) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				// ..
				board.redraw();
			}
		});
	}

	//TODO
	@Override
	public GameAction getUserCommand() {
		return userCommand;
	}

	@Override
	public void displayScore() {
		// TODO Auto-generated method stub

	}
}