package me.Christian.other;

import java.awt.Desktop;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.Christian.pack.Main;

public class OtherStuff {
	public static String TheNormalTime(){
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss - ");
		String time = sdf.format(cal.getTime());
		String currenttime =  time ;
		return currenttime;
	}
	public static String TheSimpleNormalTime(){
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(cal.getTime());
		String currenttime =  time ;
		return currenttime;
	}

	public static void openwebsite(String url){
		try {
			Desktop dt = Desktop.getDesktop();
			URI uri = new URI(url);
			dt.browse(uri.resolve(uri));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addToMainQueue(String string){
		int x = 0;
		while(Main.todo[x] != ""){
			x++;
		}
		Main.todo[x] = string;
		Main.todosize = x;
	}

}
