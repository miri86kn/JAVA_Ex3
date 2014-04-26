package common;

public class Settings {
	
	public class MazeSettings{
		
		public  int EMPTY ;
		public  int WALL;
		public  int START ;
		public  int EXIT ;
		
		public  int HORIZONTAL_MOVE_COAST ;
		public  int DIAGONAL_MOVE_COAST;
		
	};
	
	MazeSettings MAZE_SETTINGS;
	
	public Settings() {
		
		MAZE_SETTINGS = new MazeSettings();
	}
	
	
}
