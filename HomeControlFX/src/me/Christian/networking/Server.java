package me.Christian.networking;

import java.io.*;
import java.net.*;
import java.util.*;

import me.Christian.other.OtherStuff;

public class Server implements Runnable
{
	private static int portz;
	private ServerSocket ss;

	@SuppressWarnings("rawtypes")
	public static Hashtable outputStreams = new Hashtable();
	//private boolean AcceptSocket = true;
	
	public static void startServer( int port ) throws IOException {
		portz = port;
		(new Thread(new Server())).start();
	}
	@SuppressWarnings("unchecked")
	public void run() {

		try {
			ss = new ServerSocket( portz );
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Listening on " + ss);

		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//AcceptSocket = true;


			/*for (Object key : outputStreams.keySet()) {
				if(OtherStuff.SocketStringToIPSting(key.toString()).equals(OtherStuff.SocketStringToIPSting(s.toString()))){
					AcceptSocket = false;
					try {
						ServerFrame.doc.insertString(ServerFrame.doc.getLength(), OtherStuff.TheNormalTime() +"  " +s.toString() + " Denied - Multiple Instances opened\n", ServerFrame.notes );
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}

					Server.reply(s, "broadcast Denied-Connection-Multiple-windows-opened");
					System.out.println("Denied: " + s.toString());
					s.close();
				}
				System.out.println("Checking match of:" + OtherStuff.SocketStringToIPSting(s.toString()) + " AND " + OtherStuff.SocketStringToIPSting(key.toString()));
			}

			if(AcceptSocket == true){

				try {
					ServerFrame.doc.insertString(ServerFrame.doc.getLength(), OtherStuff.TheNormalTime() +  " Connection from "+s +"\n", ServerFrame.keyWord);
					ServerFrame.addConnectedUser(s.toString());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}*/
			
				
				DataOutputStream dout = null;
				try {
					dout = new DataOutputStream( s.getOutputStream() );
				} catch (IOException e) {
					e.printStackTrace();
				}
				Server.reply(s, "You are connected to Homecontrol-Server-1");
					
			
				outputStreams.put( s, dout );
				new ServerThread( this, s );
			}
		}
	
	
	@SuppressWarnings("rawtypes")
	static Enumeration getOutputStreams() {
		return outputStreams.elements();
	}
	
	@SuppressWarnings("rawtypes")
	public static void sendToAll( String message ) {
		synchronized( outputStreams ) {
			for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {
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
			System.out.println("Error #2");
			try {
				s.close();
				if(outputStreams.contains(s.toString())){
					outputStreams.remove(s);
					System.out.println("Error #1");
				}
			} catch( IOException ie ) {
				ie.printStackTrace();
				System.out.println("Error closing connection");
			}
			System.out.println("Error #3");
			System.out.println(OtherStuff.TheNormalTime() + " Removing connection to "+s);

		}
	}
}
