package me.Christian.networking;

import java.io.*;
import java.net.*;

import javafx.concurrent.Task;
import me.Christian.other.OtherStuff;

public class ServerThread extends Task<Void>
{

	private Server server;
	private Socket socket;

	public Socket checkip(String ip){
		Socket sp = null;
		if(socket.toString().contains(ip)){
			sp = socket;
		}else{
			sp = null;
		}
		return sp;
	}

	public ServerThread( Server server, Socket socket ) {
		this.server = server;
		this.socket = socket;
	}

	protected Void call() throws Exception {
		try {
			DataInputStream din = new DataInputStream( socket.getInputStream() );

			while (true) {
				String message = din.readUTF();
				if(message.contains("/")){
					String[] commands = message.split(" ");
					String user = "*";
					String cmd = "*";
					String arg1 = "*";
					String arg2 = "*";
					String arg3 = "*";
					String arg4 = "*";
					String arg5 = "*";
					String arg6 = "*";
					String arg7 = "*";
					String arg8 = "*";
					String arg9 = "*";
					String arg10 = "*";
					if(commands.length == 1){
						user = commands[0];
					}else if(commands.length == 2){
						user = commands[0];
						cmd = commands[1];
					}else if(commands.length == 3){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
					}else if(commands.length == 4){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
						arg2 = commands[3];
					}else if(commands.length == 5){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
						arg2 = commands[3];
						arg3 = commands[4];
					}else if(commands.length == 6){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
						arg2 = commands[3];
						arg3 = commands[4];
						arg4 = commands[5];
					}else if(commands.length == 7){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
						arg2 = commands[3];
						arg3 = commands[4];
						arg4 = commands[5];
						arg5 = commands[6];
					}else if(commands.length == 8){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
						arg2 = commands[3];
						arg3 = commands[4];
						arg4 = commands[5];
						arg5 = commands[6];
						arg6 = commands[7];
					}else if(commands.length == 9){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
						arg2 = commands[3];
						arg3 = commands[4];
						arg4 = commands[5];
						arg5 = commands[6];
						arg6 = commands[7];
						arg7 = commands[8];
					}else if(commands.length == 10){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
						arg2 = commands[3];
						arg3 = commands[4];
						arg4 = commands[5];
						arg5 = commands[6];
						arg6 = commands[7];
						arg7 = commands[8];
						arg8 = commands[9];
					}else if(commands.length == 11){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
						arg2 = commands[3];
						arg3 = commands[4];
						arg4 = commands[5];
						arg5 = commands[6];
						arg6 = commands[7];
						arg7 = commands[8];
						arg8 = commands[9];
						arg9 = commands[10];
					}else if(commands.length == 12){
						user = commands[0];
						cmd = commands[1];
						arg1 = commands[2];
						arg2 = commands[3];
						arg3 = commands[4];
						arg4 = commands[5];
						arg5 = commands[6];
						arg6 = commands[7];
						arg7 = commands[8];
						arg8 = commands[9];
						arg9 = commands[10];
						arg10 = commands[11];
					}else{
						System.out.println("Message from "+ socket + "is too long or too short!");
					}

					CheckMessage.forcmd(socket, user, cmd, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);

				}else{
					System.out.println(OtherStuff.TheNormalTime() + " Invalid Message by: " + socket.toString() + "\n" + OtherStuff.TheNormalTime() + " Message: " + message);
				}


			}
		} catch( EOFException ie ) {
		} catch( IOException ie ) {
			ie.printStackTrace();
		} finally {
			server.removeConnection( socket );
			System.out.println("Closing connection " + socket.toString());
		}
		return null;
	}
}