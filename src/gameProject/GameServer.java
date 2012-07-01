package gameProject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author Michael T Pato
 * 
 * the Game server handles requests from clients and spinning off threads
 * to maintain communication with each client 
 *
 */
public class GameServer {
	
	private int port = 4356;	
	private ServerSocket serverSocket;
	private ErrorLogger log;
	private boolean running;
	private HashMap<Integer,Game> games;//all current running games
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GameServer().run();

	}

	/**
	 * runs the server
	 */
	public void run() {
		log = new ErrorLogger("errorLog.txt", this.getClass().toString());//init the error logger
		
		running = true;
		System.out.println("RUNNING");
		
		
		initServer();
		
	}

	/**
	 * creates a Server Socket listening on the port and then,
	 * while the program is running, it listens for connections 
	 * and spins off threads to handle communication with each 
	 * client 
	 */
	private void initServer() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.log("problem creating server socket trace: " + e.toString());
			e.printStackTrace();
		}
        
		int ids = 0;//testing
		while(running) {
			
			System.out.println("waiting for con");
	        try {
				Socket socket = serverSocket.accept();
				
				System.out.println("got con");
				

				RequestThread r = new RequestThread(socket, ids);
				r.start();
				ids++;//testing
				
				
			} catch (IOException e) {
				log.log("problem accepting connection trace: " + e.toString());
				e.printStackTrace();
			}
		}
		

		
	}
	

}
