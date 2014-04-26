package controller;

import view.Game2048View;
import model.Game2048Model;

public class Game2048Boot {

	public static void main(String[] args) {
		//View ui=new Game2048View();
		
		Game2048Model model =new Game2048Model(4);
		Game2048View ui = new Game2048View();
		
		Presenter p=new Presenter(model,ui);
		model.addObserver(p);
		ui.addObserver(p);
		ui.run();
		// TODO Auto-generated method stub
		
		/*
		 * 
		 * GameMazeModel model =new GameMazeModel(10);
				State stt = model.getCurrtState();
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
				}
				*/
	}

}
