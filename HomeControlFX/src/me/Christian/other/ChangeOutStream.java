package me.Christian.other;

import java.io.PrintStream;
import java.util.logging.Logger;

import me.Christian.pack.Main;

public class ChangeOutStream {
	public ChangeOutStream(){
		System.setOut(new PrintStream(System.out) {
			public void println(String s) {
				Main.Console.appendText(OtherStuff.TheSimpleNormalTime() + ": "+ s+"\n");
				Logger log = Logger.getLogger("");
				log.info(s);
			}
		});
	}
}
