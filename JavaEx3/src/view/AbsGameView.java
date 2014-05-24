package view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.ServerData;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Entities.GameAction;

public abstract class AbsGameView extends Observable implements View, Runnable {
	// Data Members
	Display display;
	Shell shell;
	AbsBoard board;
	GameAction userCommand;
	Group boardGroup, buttonGroup;
	Label scoreLbl, bestScoreLbl, connectionStateLbl;
	State currState;
	// Button allGame, singleMove;
	Text numOfMoves, ip, port;
	Color shellColor;
	Font font;
	Button addServer, hintButton, allGame, singleMove, connectButton;
	Combo comboServers;
	boolean continueSolving; //flag
	
	// Constants
	private static final int LABEL_DATA_WIDTH = 60;
	private static final String SERVERS_FILE = "resources/serversData.xml";

	// Methods

	public AbsGameView() {
		super();
	}
	
	private Group GenerateGroup(Composite parent, int style, Layout layout,
			Object layoutData) {
		Group newGroup = new Group(parent, style);
		newGroup.setLayout(layout);
		newGroup.setLayoutData(layoutData);
		newGroup.setBackground(shellColor);
		return newGroup;
	}

	private Button GenerateButton(Composite parent, int style,
			GridData gridData, String text, String imageUrl) {
		Button newButton = new Button(parent, style);
		newButton.setText(text);
		newButton.setLayoutData(gridData);
		newButton.setFont(font);
		if (imageUrl != null) {
			Image imageBack = new Image(shell.getDisplay(), imageUrl);
			newButton.setImage(imageBack);
		}
		return newButton;
	}

	// Method which initializes all components
	private void initComponents() {

		display = new Display();
		shell = new Shell(display);
		shellColor = new Color(shell.getDisplay(), 250, 248, 239);
		shell.setLayout(new GridLayout(3, false));
		shell.setBackground(shellColor);
		shell.setSize(740, 660);
		shell.setMinimumSize(740, 660);
		setShellText();

		// Initialize the menus
		initMenus();

		font = new Font(shell.getDisplay(), "Tahoma", 10, SWT.BOLD);

		// Initialize buttons in buttonGroup
		initButtonGroupMenu();

		// Initialize server settings menu
		initServerSettingMenu();

		// Initialize the main group which contains game board and score groups
		initBoardGroup();

		shell.open();
	}

	private void initButtonGroupMenu() {
		// Group which contains all option buttons
		buttonGroup = GenerateGroup(shell, SWT.SHADOW_OUT, new GridLayout(1,
				true), new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		// Undo button
		Button undoButton = GenerateButton(buttonGroup, SWT.PUSH, new GridData(
				SWT.FILL, SWT.TOP, false, false, 1, 1), "Undo Move",
				"resources\\Images\\back.png");

		// Restart button
		Button restartButton = GenerateButton(buttonGroup, SWT.PUSH,
				new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1),
				"Restart Game", "resources\\Images\\undo.png");

		// Load button
		Button loadButton = GenerateButton(buttonGroup, SWT.PUSH, new GridData(
				SWT.FILL, SWT.TOP, false, false, 1, 1), "Load Game",
				"resources\\Images\\folder.png");

		// Save button
		Button saveButton = GenerateButton(buttonGroup, SWT.PUSH, new GridData(
				SWT.FILL, SWT.TOP, false, false, 1, 1), "Save Game",
				"resources\\Images\\star.png");

		// Hint button
		hintButton = GenerateButton(buttonGroup, SWT.PUSH, new GridData(
				SWT.FILL, SWT.TOP, false, false, 1, 1), "Get Hint",
				"resources\\Images\\star.png");

		hintButton.setEnabled(false); // set hint button disabled until server
										// details are selected

		// Determine restart button select action
		restartButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newGame();
				setBoardFocus();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
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

		hintButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// no need to validate because of ui restrictions
				boolean solveAllGame = allGame.getSelection();
				int numOfHints;
				
				if (hintButton.getText()=="Stop!")
				{
					hintButton.setText("Get Hint");
					continueSolving=false;
				}
				else
				{
					if (solveAllGame)
					{
						hintButton.setText("Stop!");
						userCommand = GameAction.SOLVE;
						// raise a flag of a change
						setChanged();
						notifyObservers();
						continueSolving=true;
						while (continueSolving)
						{
							userCommand = GameAction.SOLVE;
							// raise a flag of a change
							setChanged();
							notifyObservers();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								
								e.printStackTrace();
							}
						}
					}
					else {
						numOfHints = Integer.parseInt(numOfMoves.getText());
						for (int i = 0; i < numOfHints; i++) {
	
							userCommand = GameAction.SOLVE;
							// raise a flag of a change
							setChanged();
							notifyObservers();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
							
								e.printStackTrace();
							}
						}
					}
				}
				
				

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

	}

	private void initServerSettingMenu() {

		// Hint details
		ExpandBar bar = new ExpandBar(buttonGroup, SWT.V_SCROLL);
		// Image image = new Image(display, "yourFile.gif");

		bar.setBackground(shellColor);

		// container
		Composite composite = new Composite(bar, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 0;
		layout.verticalSpacing = 4;
		composite.setLayout(layout);

		// Solve all game or num of moves
		Label labelHintFunc = new Label(composite, SWT.NONE);
		labelHintFunc.setText("1. Select Hint Action:");

		Composite controlsLayout = new Composite(composite, SWT.NULL);
		controlsLayout.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false,
				false, 1, 1));

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		controlsLayout.setLayout(fillLayout);
		// controlsLayout.setBackground(shellColor);

		allGame = new Button(composite, SWT.RADIO);
		allGame.setText("Solve All Game");
		allGame.setFont(font);
		// allGame.setBackground(shellColor);
		allGame.setSelection(true);
		singleMove = new Button(composite, SWT.RADIO);
		singleMove.setText("Give Hints:");
		singleMove.setFont(font);
		// singleMove.setBackground(shellColor);

		numOfMoves = new Text(composite, SWT.BORDER);
		numOfMoves.setText("1");
		numOfMoves.setEnabled(false);

		Label labelServerDetails = new Label(composite, SWT.NONE);
		labelServerDetails.setText("2. Select Server Details");

		comboServers = new Combo(composite, SWT.NONE);
		ArrayList<String> serverData = getServerData();
		String[] arr = new String[serverData.size()];
		serverData.toArray(arr);
		comboServers.setItems(arr);

		Label labelNewServer = new Label(composite, SWT.NONE);
		labelNewServer.setText("3. Add New Server:");
		ip = new Text(composite, SWT.BORDER);
		port = new Text(composite, SWT.BORDER);

		// AddServer button
		addServer = GenerateButton(composite, SWT.PUSH, new GridData(SWT.FILL,
				SWT.TOP, false, false, 1, 1), "Add server", null);
		addServer.setEnabled(false);

		connectButton = GenerateButton(composite, SWT.PUSH, new GridData(
				SWT.FILL, SWT.TOP, false, false, 1, 1), "Connect", null);
		connectButton.setEnabled(false);
		connectButton.setText("Connect");
		
		connectionStateLbl = new Label(composite, SWT.NONE);
		
		ExpandItem hintSettingsExpander = new ExpandItem(bar, SWT.NONE, 0);
		hintSettingsExpander.setText("Hint Settings         :");
		hintSettingsExpander.setHeight(composite.computeSize(SWT.DEFAULT,
				SWT.DEFAULT).y);
		hintSettingsExpander.setControl(composite);
		// hintSettingsExpander.setImage(image);
		hintSettingsExpander.setExpanded(true);

		addServer.setEnabled(false);
		bar.setSpacing(8);
		

		// On Choose solve all game or solve few moves
		allGame.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				numOfMoves.setEnabled(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});
		singleMove.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				numOfMoves.setEnabled(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});

		// On Change IP & Port:
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

		// on adding new port
		// note: no need to do validation here - in case not valid data - button
		// is disabled
		addServer.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// add server data to combo
				comboServers.add(ip.getText() + " " + port.getText());

				// save server settings
				try {
					AppendServerList(ip.getText(),port.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// clear text boxes
				ip.setText("");
				port.setText("");

				// select in servers combo the newly added ip
				// new ip is always added to the end of the list
				comboServers.select(comboServers.getItemCount() - 1);
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			
			}
		});

		// on server selection changed
		comboServers.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				String selectedServer = comboServers.getItem(comboServers
						.getSelectionIndex());
				if (selectedServer != "") {
					// enable hints!!!
					connectButton.setEnabled(true);
				}

			}
		});

		// connect to server on click event
		connectButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (connectButton.getText() == "Connect"){
				String selectedServerData = comboServers.getText();
				userCommand = GameAction.CONNECT;
				// raise a flag of a change
				setChanged();
				// actively notify all observers
				// and invoke their update method
				Object[] args = { selectedServerData.split(" ")[0],
						Integer.parseInt(selectedServerData.split(" ")[1]) };
				notifyObservers(args);
				hintButton.setEnabled(true);
				connectionStateLbl.setText("connected....");
				connectButton.setText("Disconnect");
				}
				else{
					userCommand = GameAction.DISCONNECT;
					// raise a flag of a change
					setChanged();
					notifyObservers();
					hintButton.setEnabled(false);
					connectionStateLbl.setText("");
					connectButton.setText("Connect");
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			
			}
		});

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
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		// Determine save item select action
		saveItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				saveGame();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
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
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		// Determine restart item select action
		restartItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newGame();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		// Integrate created menu into shell
		shell.setMenuBar(menuBar);
	}

	// Method which initializes the board group
	private void initBoardGroup() {

		// Group which wrap game board components
		boardGroup = new Group(shell, SWT.SHADOW_OUT);
		boardGroup.setLayout(new GridLayout(2, true));
		boardGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				2, 1));
		Color shellColor = new Color(shell.getDisplay(), 250, 248, 239);
		boardGroup.setBackground(shellColor);

		// Font for score titles
		Font titleFont = new Font(shell.getDisplay(), "Tahoma", 10, SWT.BOLD);

		// Grid data for both score labels
		GridData labelData = new GridData();
		labelData.widthHint = LABEL_DATA_WIDTH; /* default width */
		labelData.horizontalAlignment = SWT.FILL; /* grow to fill available width */

		// Group which contains score label
		final Group scoreGroup = new Group(boardGroup, SWT.SHADOW_OUT);
		scoreGroup.setFont(titleFont);
		scoreGroup.setText("SCORE");
		scoreGroup.setLayout(new GridLayout(1, true));
		scoreGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false,
				1, 1));
		scoreGroup.setBackground(new Color(shell.getDisplay(), 250, 248, 239));

		// Font for score labels
		Font font = new Font(shell.getDisplay(), "Tahoma", 16, SWT.BOLD);

		// Score label
		this.scoreLbl = new Label(scoreGroup, SWT.WRAP | SWT.BOLD);
		scoreLbl.setText("0");
		scoreLbl.setLayoutData(labelData);
		scoreLbl.setFont(font);
		// scoreLbl.setSize(new Point());
		scoreLbl.setForeground(new Color(shell.getDisplay(), 119, 110, 101));
		scoreLbl.setBackground(new Color(shell.getDisplay(), 250, 248, 239));

		// Group which contains best score label
		final Group bestScoreGroup = new Group(boardGroup, SWT.SHADOW_OUT);
		bestScoreGroup.setFont(titleFont);
		bestScoreGroup.setText("BEST");
		bestScoreGroup.setLayout(new GridLayout(1, true));
		bestScoreGroup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true,
				false, 1, 1));
		bestScoreGroup.setBackground(new Color(shell.getDisplay(), 250, 248,
				239));
		// Best score label
		this.bestScoreLbl = new Label(bestScoreGroup, SWT.WRAP);
		bestScoreLbl.setText("0");
		bestScoreLbl.setFont(font);
		bestScoreLbl.setLayoutData(labelData);
		bestScoreLbl
				.setBackground(new Color(shell.getDisplay(), 250, 248, 239));

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
				} else {
					board.redraw(currState); // redraw board
					displayScore(); // display updates scores
					display.update(); // important!!!
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
		scoreLbl.setText(String.valueOf(currState.getScore())); // display
																// current score
		bestScoreLbl.setText(String.valueOf(currState.getScore())); // TODO:
																	// display
																	// best
																	// score
	}

	public void setUserCommand(GameAction userCommand) {
		this.userCommand = userCommand;
	}

	@Override
	// Method for handling game over
	public void gameOver() {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
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
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		messageBox
				.setMessage("Congratulations! You have won the game! Start new game?");
		messageBox.setText("Game Win");
		int response = messageBox.open();
		if (response == SWT.YES)
			newGame();
		else
			System.exit(0);
	}

	protected static boolean isChild(Control parent, Control child) {
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

	// get all server data
	private ArrayList<String> getServerData() {
		ArrayList<String> servers = new ArrayList<String>();
		ArrayList<ServerData> serverList = getServerList();
		for (ServerData data : serverList) {
			servers.add(data.getIp() + " " + data.getPort());
		}
		return servers;
	}

	// validate ip and port input
	private boolean validIpPort() {
		String ipStr = ip.getText();
		String portStr = port.getText();

		if (ipStr == "" || portStr == "")
			return false;
		return true;
	}
	
	// Reads server list from XML file and returns the list
	private ArrayList<ServerData> getServerList() {
		ArrayList<ServerData> serverList = new ArrayList<ServerData>();
		try {
			File fXmlFile = new File(SERVERS_FILE);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			// normalize doc
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("model.ServerData");

			for (int i = 0; i < nList.getLength(); i++) {

				Node nNode = nList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					String ip = eElement.getElementsByTagName("ip").item(0).getTextContent();
					int port = Integer.valueOf((eElement.getElementsByTagName("port").item(0).getTextContent()));
					serverList.add(new ServerData(ip, port));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serverList;
	}
	
	// Append new server list from XML file and returns the list
	private void AppendServerList(String ip, String port) throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		Document document = documentBuilder.parse(SERVERS_FILE);
		Element root = document.getDocumentElement();

		// Root Element
		Element rootElement = document.getDocumentElement();

		Collection<ServerData> svr = new ArrayList<ServerData>();
		svr.add(new ServerData(ip, Integer.valueOf(port)));

		for (ServerData i : svr) {
			// server elements
			Element server = document.createElement("model.ServerData");
			rootElement.appendChild(server);

			Element eIP = document.createElement("ip");
			eIP.appendChild(document.createTextNode(i.getIp()));
			server.appendChild(eIP);

			Element ePort = document.createElement("port");
			ePort.appendChild(document.createTextNode(Integer.toString(i
					.getPort())));
			server.appendChild(ePort);

			root.appendChild(server);
		}

		DOMSource source = new DOMSource(document);

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StreamResult result = new StreamResult(SERVERS_FILE);
		transformer.transform(source, result);
	}
}
