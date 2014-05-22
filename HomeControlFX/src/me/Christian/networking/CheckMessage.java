package me.Christian.networking;


import java.net.Socket;

import me.Christian.other.OtherStuff;


public class CheckMessage {
	private static final String login = "/login";


	public static void forcmd(Socket socket, String pcname, String cmd, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10 ){
		if(cmd.equals(login)){
			/*if(OtherStuff.ValidUserPassCombination(arg1, arg2)){
					Server.reply(socket, "Authorized");
				}else{
					Server.reply(socket, "broadcast incorrect_credicals");
				}*/

		}


		System.out.println(OtherStuff.TheNormalTime() +  " CMD from: "+socket + " : " + pcname + " " + cmd + " " + arg1 + "  " + arg2 + " " + arg3 + " " + arg4 + " " + arg5 );

	}
}
