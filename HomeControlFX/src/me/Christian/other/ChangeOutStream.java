package me.Christian.other;

import java.io.PrintStream;
import java.util.logging.Logger;

import javafx.application.Platform;
import me.Christian.pack.Main;

public class ChangeOutStream {
	public ChangeOutStream(){
		System.setOut(new PrintStream(System.out) {
			public void println(String s) {
				if(Platform.isFxApplicationThread()){
					Main.Console.appendText(OtherStuff.TheSimpleNormalTime() + ": "+ s+"\n");
					Logger log = Logger.getLogger("");
					log.info(s);
				}
			}
		});
	}
}