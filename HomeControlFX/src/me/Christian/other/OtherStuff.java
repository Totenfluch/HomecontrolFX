package me.Christian.other;

import java.awt.Desktop;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;

import me.Christian.pack.Main;

public class OtherStuff {
	public static Hashtable<String, String> UserDatabase = new Hashtable<String, String>();
	public static Hashtable<String, String> PrivateKeys = new Hashtable<String, String>();

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

	public static void addToPrintQueue(String string){
		int x = 0;
		while(Main.todoprint[x] != ""){
			x++;
		}
		Main.todoprint[x] = string;
		Main.todoprintsize = x;
	}

	public static void addToCmdQueue(String string){
		int x = 0;
		while(Main.todocmd[x] != ""){
			x++;
		}
		Main.todocmd[x] = string;
		Main.todocmdsize = x;
	}

	public static void initDatabase(){
		UserDatabase.put("Totenfluch", "s123C");
		UserDatabase.put("Soulrescuer", "s321C");
	}

	public static String GeneratePrivateKey(){
		Random r = new Random(); 
		String a = "";
		for(int i=0; i<32; i++){
			final char randomChar = (char) (97 + r.nextInt(25)); // 97 is the ascii table position for character 'a'. 
			int x=(Math.random()<0.5)?0:1;
			String n = "";
			n+=randomChar;
			if(x==1){
				n = n.toUpperCase();
			}
			a += n;
		}
		return a;
	}

}
