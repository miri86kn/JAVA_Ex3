package controller;

import view.Game2048View;

public class Game2048Boot {

	public static void main(String[] args) {
		//View ui=new Game2048View();
		
		//Model model =new Game2048Model();
		Game2048View ui = new Game2048View();
		
		//Presenter p=new Presenter(model,ui);
		//model.addObserver(p);
		//ui.addObserver(p);
		ui.run();
	}

}
