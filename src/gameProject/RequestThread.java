package gameProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Michael T Pato
 *
 */
public class RequestThread extends Thread{
    private ErrorLogger log;
	
    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;
    private boolean connected = true;
    private Set<Integer> sharedUsers; 
    
    
    public RequestThread(Socket socket, Set<Integer> activeUsers) {
    	this.socket = socket;
    	sharedUsers = activeUsers;
    	
    }
    
    public void run() {
    	
    	initIO();
    	
    	listen();
    }

	private void initIO() {
		log = new ErrorLogger("errorLog.txt", this.getClass().toString());//init the error logger	
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			log.log("problem creating the input buffer trace: " + e.toString());
			e.printStackTrace();
		}
    	try {
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			log.log("problem creating the output buffer trace: " + e.toString());
			
			e.printStackTrace();
		}
	}
    
    
          
    
    private void listen() {
    	String msg;
    	while(connected) {
    		try {
    			if(in.ready()) {
    				msg = in.readLine();
    				
    				handleRequest(msg);
    			}
				
			} catch (IOException e) {
				log.log("problem reading line from socket. trace: " + e.toString());
				
				e.printStackTrace();
			}
	
    	}
    	
    	
    }
    
    public void sendMsg(String msg) {
        if (msg != null && !msg.equals("")) {
            try{
                out.write(msg);
                out.write(("\n"));
                out.flush();
            }
            catch(Exception e)
            {
            	
            	log.log("problem talking to socket. trace: " + e.toString());
				
                e.printStackTrace();
            }
        } else {
        	log.log("msg was null when sending.");
			
        }
    }

	private void handleRequest(String msg) {
		String[] parsedMsg = parseMsg(msg);

		System.out.println("IN SERVER: " + msg);
		
		if(parsedMsg[0].equals("login")) {
			checkLogin(parsedMsg[1]);
		} else if(parsedMsg[0].equals("newUser")) {
			createNewUser(parsedMsg[1]);
		}
		
		
		
	}

	private void createNewUser(String string) {
		// TODO Auto-generated method stub
		
	}

	private void checkLogin(String string) {
		String[] parsedLog = string.split(",");
		
		boolean correct;
		int userID = -1;
		
		
		if(parsedLog[1].equals("pass")) {//check DATABASE here 
			userID = 100;//SET WITH USERID FORM DB
			correct = true;
		} else {
			correct = false;
		}
		
		
		if(correct ) {	
			sharedUsers.add(userID);			
			sendMsg("done");// use done for all andshake
			
		} else {
			sendMsg("error");
			//close connection 
		}
		
	}

	private String[] parseMsg(String msg) {
		String[] parsedMsg;
		
		if (msg.contains(":")) {
			parsedMsg = msg.split(":");
		} else {
			parsedMsg = new String[1];
			parsedMsg[0] = msg;
		}
		

		
		return parsedMsg;
	}  
	
	
	
}
