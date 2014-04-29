package controller;



import view.GameMazeView;
import model.GameMazeModel;

public class GameMazeBoot {

	public static void main(String[] args) {

        //Test generation of new maze
		GameMazeModel model =new GameMazeModel(8);
		GameMazeView ui = new GameMazeView();
		
		Presenter p=new Presenter(model,ui);
		model.addObserver(p);
		ui.addObserver(p);
		ui.run();
	}
}
