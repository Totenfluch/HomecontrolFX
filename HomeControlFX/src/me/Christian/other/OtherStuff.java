package me.Christian.other;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;

import me.Christian.pack.Main;

public class OtherStuff {
	public static Hashtable<String, String> UserCredicalsDatabase = new Hashtable<String, String>();
	public static Hashtable<String, String> UserFlagsDatabase = new Hashtable<String, String>();
	public static Hashtable<String, Integer> UserPermissionsDatabase = new Hashtable<String, Integer>();
	public static Hashtable<String, String> PrivateKeys = new Hashtable<String, String>();
	public static Main main = new Main();

	public static String TheNormalTime(){
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss | ");
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

	public static void addToPrintQueue(String string){
		int x = 0;
		while(Main.todoprint[x] != ""){
			x++;
		}
		Main.todoprint[x] = string;
		Main.todoprintsize = x;
		main.doqueue();
	}

	public static void addToCmdQueue(String string){
		int x = 0;
		while(Main.todocmd[x] != ""){
			x++;
		}
		Main.todocmd[x] = string;
		Main.todocmdsize = x;
		main.doqueue();
	}

	public static void initDatabase(){
		// Just Test Accounts, data will be decryted in a database later
		if(!MySQL.MySQL_enabled){
			UserCredicalsDatabase.put("Totenfluch", "s123C");
			UserPermissionsDatabase.put("Totenfluch", 1000);

			UserCredicalsDatabase.put("Soulrescuer", "s321C");
			UserPermissionsDatabase.put("Soulrescuer", 1000);

			UserCredicalsDatabase.put("root", Main.MasterPassword);
			UserPermissionsDatabase.put("root", 10000);
		}else{
			String temp[][] = MySQL.Userlist();
			for(int i = 0; i<temp.length;i++){
				UserCredicalsDatabase.put(temp[i][0], temp[i][1]);
				UserFlagsDatabase.put(temp[i][0], temp[i][2]);
				UserPermissionsDatabase.put(temp[i][0], Integer.valueOf(temp[i][3]));
			}
			UserCredicalsDatabase.put("root", Main.MasterPassword);
			UserPermissionsDatabase.put("root", 10000);
		}
	}

	public static String GeneratePrivateKey(){
		Random r = new Random(); 
		String a = "";
		for(int i=0; i<32; i++){
			final char randomChar = (char) (33 + r.nextInt(91));
			a += randomChar;
		}
		return a;
	}

	public static File jarlocation(){
		return new File(OtherStuff.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	}

}
