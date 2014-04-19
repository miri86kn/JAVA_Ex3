package model;

public interface Model {
	// Methods
	public void moveUp();
	public void moveDown();
	public void moveLeft();
	public void moveRight();
	public int[][] getBoard();
	public int getScore();
}
