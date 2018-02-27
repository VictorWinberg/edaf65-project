package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class BlitzTimer extends Thread {
    private boolean player1;
    private int player1Time, player2Time,originalTime;
    private Timer timer=null;

    public BlitzTimer() {
        player1 = true;
    }
    public void setTime(int time){
    	this.player1Time=time;
    	this.player2Time=time;
    	this.originalTime=time;
    }
    public void resetTime(){
    	this.player1Time=originalTime;
    	this.player2Time=originalTime;
    }
    public void switchTurn() {
        player1 = !player1;
    }

    public boolean isplayer1Turn() {
        return player1;
    }

    public int getTime() {
        if (player1) {
            return player1Time;
        } else {
            return player2Time;
        }
    }
    public void stopTimer(){
    	timer.stop();
    }

    public void run() {
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (player1) {
                    if (player1Time == 0) {
                       timer.stop();
                    } else {
                        player1Time--;
                    }
                } else {
                    if (player2Time == 0) {
                       timer.stop();
                    } else {
                        player2Time--;
                    }

                }
                System.out.println("Player1 : " + player1Time + "    Player2: " + player2Time);
            }
        };
       timer =  new Timer(1000, taskPerformer);
       timer.start();
    }
}