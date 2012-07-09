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

		con = new SocketConnector(4356, "localhost");
		
		con.initCon();
		
		
		
		String ans = con.getReply();
	
		while(!ans.equals("done")) {
			ans = con.getReply();
		}
		
		
		con.sendMsg("tileGame");
		
		ans = con.getReply();
		
		while(!ans.equals("done")) {
			ans = con.getReply();
		}
		
		
		System.out.println("ON TO COMANDS");
		
		doPreSetMsgs();
		
		sendCommandsLoop();
		//login();
		
		//TalkerThread t = new TalkerThread(con);
		//t.start();
		/*
		while(connected) {
			
			//System.out.println(con.getReply());
			
			
			
		}*/
	}
	
	private void doPreSetMsgs() {
		con.sendMsg("login:mike,testPass");
		con.sendMsg("newGame:test3");
		
		
	}

	private void sendCommandsLoop() {

		String in; // MUST ENCRYPT
		
		
		
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
