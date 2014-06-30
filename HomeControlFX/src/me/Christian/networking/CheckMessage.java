package me.Christian.networking;


import java.net.Socket;

import me.Christian.other.OtherStuff;

public class CheckMessage {
	private static final String ReqPrivateKey = "/login";
	private static final String AuthAction = "/AuthAction";


	public static void forcmd(Socket socket, String pcname, String cmd, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10 ){
		if(cmd.equals(ReqPrivateKey)){
			if(OtherStuff.UserCredicalsDatabase.containsKey(arg1)){
				if(OtherStuff.UserCredicalsDatabase.get(arg1).equals(arg2)){
					String temp = OtherStuff.GeneratePrivateKey();
					if(OtherStuff.PrivateKeys.contains(socket.toString())){
						OtherStuff.PrivateKeys.remove(socket.toString());
					}
					OtherStuff.PrivateKeys.put(socket.toString(), temp);
					OtherStuff.addToPrintQueue("Created *Private key* for: " + socket.getInetAddress().toString().replace("/", ""));
					Server.reply(socket, "/PrivateKey " + temp);

				}
			}
		}else if(cmd.equals(AuthAction)){
			if(OtherStuff.PrivateKeys.containsKey(socket.toString())){
				if(OtherStuff.PrivateKeys.get(socket.toString()).equals(arg1)){
					OtherStuff.addToPrintQueue("Action Authorized with PrivateKey");
					OtherStuff.addToCmdQueue(arg2);
				}
			}
		}



		OtherStuff.addToPrintQueue(" CMD from: " + pcname + " " + cmd + " " + arg1 + " " + arg2 + " " + arg3 + " " + arg4 + " " + arg5 );
	}
}

