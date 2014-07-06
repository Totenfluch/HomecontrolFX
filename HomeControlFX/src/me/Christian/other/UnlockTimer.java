package me.Christian.other;

import java.util.TimerTask;

import me.Christian.pack.Main;

public class UnlockTimer extends TimerTask {
	int n;
	public UnlockTimer(int i){
		this.n = i;
	}

	@Override
	public void run() {
		if(n < 8){
			if(!Main.Output_Lockcross[n].isVisible()){
				Main.Output_isLocked[n] = false;
			}
		}else{
			Main.Console_Button_islocked = false;
		}
	}
}
