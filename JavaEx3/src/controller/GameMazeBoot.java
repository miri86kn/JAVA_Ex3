package controller;



import view.GameMazeView;
import model.GameMazeModel;
import model.State;

public class GameMazeBoot {

	public static void main(String[] args) {

		
		//saveSettings("C:\\Users\\miri_k\\git\\JavaRep\\JavaEx3\\settings.xml");
		//loadSettings("C:\\Users\\miri_k\\git\\JavaRep\\JavaEx3\\settings.xml");
		
		
		
        //Test generation of new maze
		GameMazeModel model =new GameMazeModel(8);
		/*State stt = model.getCurrtState();
		int[][] board = stt.getBoard();
		
		for(int i=0; i<board.length; i++)
		{
			for(int j=0; j<board.length; j++)
			{
				if (board[i][j] >=0 )
					System.out.print(" ");
			 System.out.print(board[i][j]);
			 System.out.print(" ");
			}
			 System.out.print("\n");
		}*/
		

		GameMazeView ui = new GameMazeView();
		
		Presenter p=new Presenter(model,ui);
		model.addObserver(p);
		ui.addObserver(p);
		ui.run();
	}
	
	/*
	public static void saveSettings(String path) {
		
		
		//create xml from current game state
		XStream xstream = new XStream();
		String xml = xstream.toXML(new Settings()); //this.currState
		
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(path));
		    out.write(xml);
		    out.close();
		} 
		catch (IOException e) {
		    e.printStackTrace();
		} 		
	}

	
	public static void loadSettings(String path) {
		try {
		XStream xstream = new XStream();
		InputStream in = new FileInputStream(path);
		Settings loadedGame = (Settings)xstream.fromXML(in);
		
		} 
		catch (IOException e) {
		    e.printStackTrace();
		}
	}
	*/

}
