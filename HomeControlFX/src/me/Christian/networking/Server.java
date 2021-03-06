package me.Christian.networking;

import java.io.*;
import java.net.*;
import java.util.*;

import me.Christian.other.OtherStuff;
import me.Christian.pack.Main;

public class Server implements Runnable
{
	private ServerSocket ss;

	public static Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();
	
	public void run() {
		
		try {
			ss = new ServerSocket( Main.portz );
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Listening on " + ss);
		System.out.println("Finished [1]: Server");

		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}

			DataOutputStream dout = null;
			try {
				dout = new DataOutputStream( s.getOutputStream() );
			} catch (IOException e) {
				e.printStackTrace();
			}
			Server.reply(s, "You are connected to Homecontrol-Server-1");

			outputStreams.put( s, dout );
	        OtherStuff.addToPrintQueue(s.getInetAddress().toString().replace("/", "") + " connected");
	        Thread n = new Thread(new ServerThread(this, s));
	        n.start();
		}
	}


	static Enumeration<DataOutputStream> getOutputStreams() {
		return outputStreams.elements();
	}

	public static void sendToAll( String message ) {
		synchronized( outputStreams ) {
			for (Enumeration<DataOutputStream> e = getOutputStreams(); e.hasMoreElements(); ) {
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try {
					dout.writeUTF(message);
				} catch( IOException ie ) { ie.printStackTrace(); }
			}
		}
	}

	public static void reply(Socket socket, String message){
		DataOutputStream dout;
		try {
			dout = new DataOutputStream( socket.getOutputStream() );
			dout.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void removeConnection( Socket s ) {
		synchronized( outputStreams ) {
			outputStreams.remove(s);
			try {
				s.close();
				if(outputStreams.contains(s.toString())){
					outputStreams.remove(s);
				}
			} catch( IOException ie ) {
				ie.printStackTrace();
				System.out.println("Error closing connection");
			}
			OtherStuff.addToPrintQueue(s.getInetAddress().toString().replace("/", "") + " diconnected");

		}
	}

}
