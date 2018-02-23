package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class BlitzTimer extends Thread {
	Boolean white;
	int whiteTime, blackTime;

	public BlitzTimer(int time) {
		white = true;
		this.blackTime = time;
		this.whiteTime = time;
	}

	public void whiteTurn() {
		white = true;
	}

	public void blackTurn() {
		white = false;
	}
	public boolean isWhiteTurn(){
		return white;
	}
	public boolean isBlackTurn(){
		return !white;
	}
	
	public void run() {
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (white) {
					if (whiteTime == 0) {
						// insert white.lost or some shit here
					} else {
						whiteTime--;
					}
				} else {
					if (blackTime == 0) {
						// black.lost
					} else {
						blackTime--;
					}
				}
			}
		};
		new Timer(1000, taskPerformer).start();
	}
}