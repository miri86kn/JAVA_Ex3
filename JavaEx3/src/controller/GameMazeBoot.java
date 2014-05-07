/*
 * Authors:
 * Name: Rotem Adhoh ID: 301738845
 * Name: Maria Kusinka ID: 313926677
 */

package controller;

import model.GameMazeModel;
import view.GameMazeView;

public class GameMazeBoot {

	public static void main(String[] args) {

        //Test generation of new maze
		GameMazeModel model =new GameMazeModel(8);
		GameMazeView ui = new GameMazeView();
		
		Presenter p=new Presenter(model,ui);
		model.addObserver(p);
		ui.addObserver(p);
		//ui.run();
		new Thread(ui).start();
	}
}
