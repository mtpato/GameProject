package gameProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


//ok now i need a listening and talking pair so we can have tings run 
//async all the time no matter how much comunication 
//both sides will have this 
//let the listener always listen and the talker always talk 

//for live updating when a move is made i must keep track of all the 
//diff cons as separate  thread pairs that know about each other 
//im thinking a hashMap of GameID to game object the game object can keep track of
//who is connected and alert all the talker threads to push the new board. listener 
//threads will actually handle requests from the client 

public class AlertThread extends Thread{
    ErrorLogger log;
	
	BufferedReader input;
    BufferedWriter output;
    Socket socket;
    
    
    public AlertThread(Socket socket) {
    	this.socket = socket;
    	
    }
    
    public void run() {
    	
    	initIO();
    	

		
		

   
    }

	private void initIO() {
		log = new ErrorLogger("errorLog.txt", this.getClass().toString());//init the error logger	
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			log.log("problem creating the input buffer trace: " + e.toString());
			e.printStackTrace();
		}
    	try {
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			log.log("problem creating the output buffer trace: " + e.toString());
			
			e.printStackTrace();
		}
	}
    
    
          
    
    private void listen() {
      
    }  
	
	
	
}
