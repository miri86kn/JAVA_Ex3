package view;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

import model.State;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
	    //Button allGame, singleMove;
	    Text numOfMoves, ip, port;
	    Color shellColor;
	    Font font;
	    Button addServer;
	    // Constants
		private static final int LABEL_DATA_WIDTH = 60;

		// Methods
		
		private Group GenerateGroup(Composite parent, int style, Layout layout, Object layoutData)
		{
			Group newGroup = new Group(parent, style);
			newGroup.setLayout(layout);
			newGroup.setLayoutData(layoutData);
			newGroup.setBackground(shellColor);
			return newGroup;
		}
		
		private Button GenerateButton(Composite parent, int style, GridData gridData, String text, String imageUrl)
		{
			Button newButton = new Button(parent, style);
			newButton.setText(text);
			newButton.setLayoutData(gridData);
			newButton.setFont(font);
			if (imageUrl != null)
			{Image imageBack = new Image(shell.getDisplay(), imageUrl);
			newButton.setImage(imageBack);}
			return newButton;
		}
		// Method which initializes all components
		private void initComponents() {
			
			
			display = new Display();
			shell = new Shell(display);
			shellColor =  new Color(shell.getDisplay(), 250, 248, 239);
			shell.setLayout(new GridLayout(3, false));
			shell.setBackground(shellColor);
			shell.setSize(520, 420);
			shell.setMinimumSize(520, 420);
			setShellText();
		    
			
				
			// Initialize the menus
			initMenus();
			
			font = new Font(shell.getDisplay(), "Tahoma", 10, SWT.BOLD);
			
			
			// Group which contains all option buttons
		    Group buttonGroup = GenerateGroup(shell, SWT.SHADOW_OUT, new GridLayout(1, true), new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		    
		  
			
			
		    // Undo button
			Button undoButton = GenerateButton(buttonGroup, SWT.PUSH, new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1),
					"Undo Move", "resources\\Images\\back.png");
			
			// Restart button
			Button restartButton = GenerateButton(buttonGroup, SWT.PUSH, new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1),
					"Restart Game", "resources\\Images\\undo.png");
			
			// Load button
			Button loadButton = GenerateButton(buttonGroup, SWT.PUSH, new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1),
					"Load Game", "resources\\Images\\folder.png");
					
			// Save button
			Button saveButton = GenerateButton(buttonGroup, SWT.PUSH, new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1),
					"Save Game", "resources\\Images\\star.png");
			
			// Hint button
			Button hintButton = GenerateButton(buttonGroup, SWT.PUSH, new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1),
								"Get Hint", "resources\\Images\\star.png");
						
			
			
		
			 //Hint details
			 ExpandBar bar = new ExpandBar(buttonGroup, SWT.V_SCROLL);
			 //Image image = new Image(display, "yourFile.gif");
			 
			 //container
			 Composite composite = new Composite(bar, SWT.NONE);
			 GridLayout layout = new GridLayout();
			 layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 2;
			 layout.verticalSpacing = 4;
			 composite.setLayout(layout);
			 
			 //Solve all game or num of moves
			  Label labelHintFunc = new Label(composite, SWT.NONE);
			  labelHintFunc.setText("1. Select Hint Action:");
			    
			 Composite controlsLayout = new Composite(composite, SWT.NULL);
			 controlsLayout.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
				
			  FillLayout fillLayout = new FillLayout();
			  fillLayout.type = SWT.VERTICAL;
			  controlsLayout.setLayout(fillLayout);
				 controlsLayout.setBackground(shellColor);
				
				 Button allGame = new Button(composite, SWT.RADIO);
				 allGame.setText("Solve All Game");
				 allGame.setFont(font);
				 allGame.setBackground(shellColor);
				 allGame.setSelection(true);
				 Button singleMove = new Button(composite, SWT.RADIO);
				 singleMove.setText("Give Hints:");
				 singleMove.setFont(font);
				 singleMove.setBackground(shellColor);
				 
				  numOfMoves = new Text(composite,SWT.BORDER);
				 numOfMoves.setText("1");
				 numOfMoves.setEnabled(false);
			   
			    Label labelServerDetails = new Label(composite, SWT.NONE);
			    labelServerDetails.setText("2. Select Server Details");
			    
		        Combo comboServers = new Combo(composite, SWT.NONE);
				ArrayList<String> serverData = getServerData();
				String[] arr =new String[serverData.size()];
				serverData.toArray(arr);
				comboServers.setItems(arr);
			    
				Label labelNewServer = new Label(composite, SWT.NONE);
				labelNewServer.setText("3. Add New Server:");
				 ip = new Text(composite,SWT.BORDER);
				 port = new Text(composite,SWT.BORDER);
				
				// Hint button
			    addServer = GenerateButton(composite, SWT.PUSH, new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1),
									"Add server", null);
				addServer.setEnabled(false);
			    ExpandItem hintSettingsExpander = new ExpandItem(bar, SWT.NONE, 0);
			    hintSettingsExpander.setText("Hint Settings         :");
			    hintSettingsExpander.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
			    hintSettingsExpander.setControl(composite);
			    //hintSettingsExpander.setImage(image);
                hintSettingsExpander.setExpanded(true);
                
               
			    
                bar.setSpacing(8);
         	//hintBtn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
			
			// Initialize the main group which contains game board and score groups
			initBoardGroup();
			
			// Determine restart button select action
			restartButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					newGame();
					setBoardFocus();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});
			
			// Determine load button select action
			loadButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					loadGameFile();
					setBoardFocus();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				}
			});
					
			// Determine save button select action
			saveButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					saveGame();
					setBoardFocus();
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
					setBoardFocus();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				}
			});
			
			allGame.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					numOfMoves.setEnabled(false);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			singleMove.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					numOfMoves.setEnabled(true);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			ip.addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent arg0) {
					addServer.setEnabled(validIpPort());
				}
			});
			port.addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent arg0) {
						addServer.setEnabled(validIpPort());
					
				}
			});
			shell.open();
		}
		
		private boolean validIpPort()
		{
			String ipStr = ip.getText();
			String portStr = port.getText();
			
			if (ipStr == "" || portStr=="")
				return false;
			return true;
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
		    undoItem.addSelectionListener(new SelectionListener() {
				
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
		    
		    // Font for score titles
		    Font titleFont = new Font(shell.getDisplay(), "Tahoma", 10, SWT.BOLD);
		    
		    // Grid data for both score labels
		    GridData labelData = new GridData();
			labelData.widthHint = LABEL_DATA_WIDTH;					/* default width */
			labelData.horizontalAlignment = SWT.FILL;	/* grow to fill available width */
			
			// Group which contains score label
		    final Group scoreGroup = new Group(boardGroup, SWT.SHADOW_OUT );
		    scoreGroup.setFont(titleFont);
		    scoreGroup.setText("SCORE");
		    scoreGroup.setLayout(new GridLayout(1, true));
		    scoreGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
		    scoreGroup.setBackground( new Color(shell.getDisplay(), 250, 248, 239));
		 
		    // Font for score labels
		    Font font = new Font(shell.getDisplay(), "Tahoma", 16, SWT.BOLD);
		    
		    // Score label   
		    this.scoreLbl = new Label(scoreGroup, SWT.WRAP|SWT.BOLD);
		    scoreLbl.setText("0");
		    scoreLbl.setLayoutData(labelData);
		    scoreLbl.setFont(font);
		    //scoreLbl.setSize(new Point());
		    scoreLbl.setForeground(new Color(shell.getDisplay(), 119, 110, 101));
		    scoreLbl.setBackground(new Color(shell.getDisplay(), 250, 248, 239));
		    
		    // Group which contains best score label
		    final Group bestScoreGroup = new Group(boardGroup, SWT.SHADOW_OUT);
		    bestScoreGroup.setFont(titleFont);
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
		
		// Method which opens "save file" dialog and stores file name
		private void saveGame() {
			FileDialog fd = new FileDialog(shell, SWT.SAVE);
			fd.setText("Save Game");
			fd.setFilterPath(System.getProperty("user.home"));
			String[] filterExt = { "*.xml" };
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
		
		@Override
		// Method for handling game over
		public void gameOver() {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION| SWT.YES | SWT.NO);
			messageBox.setMessage("GAME OVER: Start new game?");
			messageBox.setText("Game Over");
			int response = messageBox.open();
			if (response == SWT.YES)
				newGame();
			else
				System.exit(0);
		}
		
		@Override
		// Method for handling game winning
		public void gameWin() {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION	| SWT.YES | SWT.NO);
			messageBox.setMessage("Congratulations! You have won the game! Start new game?");
			messageBox.setText("Game Win");
			int response = messageBox.open();
			if (response == SWT.YES)
				newGame();
			else
				System.exit(0);
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
		
		// Method for resetting focus back to game board
		private void setBoardFocus() {
			if (board != null) {
				board.setFocus();
			}
		}
	
		protected abstract void setShellText();
		
		private ArrayList<String> getServerData(){
			ArrayList<String> servers = new ArrayList<String>();
			servers.add("10.160.5.85"+" "+ 5550);
			servers.add("10.160.5.82"+" "+ 5550);
			servers.add("10.160.5.81"+" "+ 5550);
			return servers;
		}
}
