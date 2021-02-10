package com.doglab.entities;

import java.awt.image.BufferedImage;

import com.doglab.main.Game;

public class CloseButton extends Button{

	private Label diceLabel;
	
	public CloseButton(double x, double y, int width, int height, double speed, BufferedImage sprite, Label l) {
		super(x, y, width, height, speed, sprite);
		diceLabel = l;
	}
	
	public void actionPerformed() {
		diceLabel.changeTickers();
		Game.entities.remove(diceLabel);
	}

}
