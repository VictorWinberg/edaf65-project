package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class BlitzTimer extends Thread {
	Boolean player1;
	int player1Time, player2Time;

	public BlitzTimer(int time) {
		player1 = true;
		this.player2Time = time;
		this.player1Time = time;
	}
	public void switchTurn() {
		player1 = !player1;
	}

	public boolean isplayer1Turn() {
		return player1;
	}


	public void run() {
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (player1) {
					if (player1Time == 0) {
						// insert white.lost or some shit here
					} else {
						player1Time--;
					}
				} else {
					if (player2Time == 0) {
						// black.lost
					} else {
						player2Time--;
					}
					
				}
				System.out.println("Player1 : " + player1Time +"    Player2: " + player2Time);
			}
		};
		new Timer(1000, taskPerformer).start();
	}
}