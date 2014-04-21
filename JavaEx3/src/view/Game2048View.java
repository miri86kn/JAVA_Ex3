package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class Game2048View extends Observable implements View, Runnable {
	// Data Members
	Display display;
	Shell shell;
	Label scoreLbl;
	Label bestScoreLbl;
	
	// Constants
	private static final int LABEL_DATA_WIDTH = 60;

	// Methods
	
	// Method which initializes all components
	private void initComponents() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(3, false));
		shell.setSize(400, 300);
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
		Board board = new Board(boardGroup, SWT.WRAP);
		board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
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
				// ...
				//canvas.redraw();
			}
		});
	}

	@Override
	public int getUserCommand() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void displayScore() {
		// TODO Auto-generated method stub

	}
}