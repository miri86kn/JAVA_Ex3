package controller;

import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

public class Presenter implements Observer {
	// Data Members
	private View ui;
	private Model model;
	
	// Methods
	
	// Presenter constructor
	public Presenter(Model model, View ui) {
		this.model = model;
		this.ui = ui;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
}
