package gameProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
	
	private int port = 4356;	
	private ServerSocket serverSocket;
	private ErrorLogger log;
	private boolean running;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GameServer().run();

	}

	public void run() {
		log = new ErrorLogger("errorLog.txt", this.getClass().toString());//init the error logger
		
		running = true;
		System.out.println("RUNNING");
		
		
		initServer();
		
	}

	private void initServer() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.log("problem creating server socket trace: " + e.toString());
			e.printStackTrace();
		}
        
		
		while(running) {
			
			System.out.println("waiting for con");
	        try {
				Socket socket = serverSocket.accept();
				
				System.out.println("got con");
				talk(socket);
				
				
			} catch (IOException e) {
				log.log("problem accepting connection trace: " + e.toString());
				e.printStackTrace();
			}
		}
		

		
	}

	private void talk(Socket socket) {
	    BufferedReader input;
	    BufferedWriter output;
	    
        try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			
			System.out.println(input.readLine());
			
			output.write("OK I HEAR YOU");
			output.write(("\n"));
			output.flush();
			
			System.out.println(input.readLine());
			
			System.out.println("DONE");
			
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        
	}
	

}
