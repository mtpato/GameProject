package gameProject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
	
	private int port = 4356;	
	private ServerSocket serverSocket;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GameServer().run();

	}

	public void run() {
		initServer();
		
	}

	private void initServer() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
		
        try {
			Socket socket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void listenToPort() {
		
	}

}
