package gameProject;

import java.util.Scanner;

public class TempClient {

	
	boolean connected = true;
	Scanner scanner = new Scanner(System.in);
	SocketConnector con;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TempClient().run();
		//con.sendMsg("GLAD YOU HEAR ME");
		

	}

	private void run() {

		
		sendCommandsLoop();
		//login();
		
		//TalkerThread t = new TalkerThread(con);
		//t.start();
		/*
		while(connected) {
			
			//System.out.println(con.getReply());
			
			
			
		}*/
	}
	
	private void sendCommandsLoop() {

		String in; // MUST ENCRYPT
		
		con = new SocketConnector(4356, "localhost");
		
		con.initCon();
		
		
		System.out.println("what command: ");
		in = scanner.nextLine();
		
		if(!in.equals("q")) {

			con.sendMsg(in);
			
			System.out.println("reply: " + con.getReply());
			
			sendCommandsLoop();
		}

	}

	private void login() {

		
		
		String user;
		String pass; // MUST ENCRYPT
		System.out.println("what is you UID: ");
		user = scanner.nextLine();
		
		System.out.println("what is you pass: ");
		pass = scanner.nextLine();
		
		con = new SocketConnector(4356, "localhost");
		
		con.initCon();
		
		con.sendMsg("login:" + user + "," + pass);
		
		if (con.getReply().equals("done")) {
			System.out.println("con set up");
		} else {
			System.out.println("wrong pass or UID");
			login();
			
		}
	}

}
