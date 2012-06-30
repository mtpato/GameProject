package gameProject;

import java.io.IOException;
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
		log = new ErrorLogger("errorLog.txt", this.getClass().toString());
		
		running = true;
		
		
		
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
			
	        try {
				Socket socket = serverSocket.accept();
				
				
			} catch (IOException e) {
				log.log("problem accepting connection trace: " + e.toString());
				e.printStackTrace();
			}
		}
		

		
	}
	

}
