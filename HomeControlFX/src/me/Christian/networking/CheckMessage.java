package me.Christian.networking;


import java.net.Socket;

import me.Christian.other.OtherStuff;
import me.Christian.pack.Main;


public class CheckMessage {
	private static final String login = "/login";


	public static void forcmd(Socket socket, String pcname, String cmd, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10 ){
		if(cmd.equals(login)){

		}
		
		int x = 0;
		while(Main.todo[x] != ""){
			x++;
		}
		Main.todo[x] = OtherStuff.TheNormalTime() +  " CMD from: "+socket + " : " + pcname + " " + cmd + " " + arg1 + "  " + arg2 + " " + arg3 + " " + arg4 + " " + arg5 ;
		Main.todosize = x;
		System.out.println(OtherStuff.TheNormalTime() +  " CMD from: "+socket + " : " + pcname + " " + cmd + " " + arg1 + "  " + arg2 + " " + arg3 + " " + arg4 + " " + arg5 );

	}
}
