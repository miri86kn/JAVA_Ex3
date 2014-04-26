package controller;

import model.GameMazeModel;
import model.State;

public class GameMazeBoot {

	public static void main(String[] args) {

		
        //Test generation of new maze
		GameMazeModel model =new GameMazeModel(10);
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
	}

}
