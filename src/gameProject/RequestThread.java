package gameProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Michael T Pato
 * 
 */
public class RequestThread extends Thread {
	private ErrorLogger log;

	private BufferedReader in;
	private BufferedWriter out;
	private Socket socket;
	private boolean connected = true;
	private int userID = -1;
	private boolean quit = false;

	private boolean signedIn = false;
	
	private Set<Integer> sharedUsers;

	private Connection dbCon = null;

	public RequestThread(Socket socket, Set<Integer> activeUsers) {
		this.socket = socket;
		sharedUsers = activeUsers;

	}
	
	private Random rand = new Random();

	public void run() {

		initIO();
		initDBCon("theGame", "212273625", "jdbc:mysql://localhost/tile");

		listen();
		
		//once game is quit
		closeSocketCon();
		closeDBCon();
	}

	private void closeSocketCon() {
        try {
        	
        	if(socket.isConnected()) {
        		 socket.close();
        	}
        
            
        } catch (IOException e) {
			log.log("problem closing socket. trace: " + e.toString());
            e.printStackTrace();
        }
		
	}

	/**
	 * 
	 */
	private void closeDBCon() {
		if (dbCon != null) {
			try {
				dbCon.close();
				System.out.println("Database connection terminated");
			} catch (Exception e) {
				log.log("problem closing database con trace: " + e.toString());
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param userName
	 * @param password
	 * @param url
	 */
	private void initDBCon(String userName, String password, String url) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			dbCon = DriverManager.getConnection(url, userName, password);
			System.out.println("Database connection established");

		} catch (Exception e) {
			log.log("Cannot connect to database server trace: " + e.toString());
			System.err.println("Cannot connect to database server");
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	private void initIO() {
		log = new ErrorLogger("errorLog.txt", this.getClass().toString());// init
																			// the
																			// error
																			// logger
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			log.log("problem creating the input buffer trace: " + e.toString());
			e.printStackTrace();
		}
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (IOException e) {
			log.log("problem creating the output buffer trace: " + e.toString());

			e.printStackTrace();
		}
	}

	/**
     * 
     */
	private void listen() {
		String msg;
		while (socket.isConnected() && !quit) {
			try {
				if (in.ready()) {
					msg = in.readLine();

					handleRequest(msg);
				}

			} catch (IOException e) {
				log.log("problem reading line from socket. trace: "
						+ e.toString());

				e.printStackTrace();
			}

		}

	}

	/**
	 * @param msg
	 */
	public void sendMsg(String msg) {
		if (msg != null && !msg.equals("")) {
			try {
				out.write(msg);
				out.write(("\n"));
				out.flush();
			} catch (Exception e) {

				log.log("problem talking to socket. trace: " + e.toString());

				e.printStackTrace();
			}
		} else {
			log.log("msg was null when sending.");

		}
	}

	/**
	 * this method handles the requests from the client. parsing the msg and
	 * then calling the right method to handle it
	 * 
	 * @param msg
	 */
	private void handleRequest(String msg) {
		String[] parsedMsg = parseMsg(msg);

		boolean validMsg = false;
		
		System.out.println("IN SERVER: " + msg);

		
		//check signedIn boolean before doing anything but create user and login
		if (parsedMsg[0].equals("quit")) {
			quitGame();
			validMsg = true;
		} else if(!signedIn) {
			if (parsedMsg[0].equals("login")) {
				checkLogin(parsedMsg[1]);
				validMsg = true;
			} else if (parsedMsg[0].equals("newUser")) {
				createNewUser(parsedMsg[1]);
				validMsg = true;
			}
		} else if(signedIn) {
			if (parsedMsg[0].equals("getGames")) {
				getGames(parsedMsg[1]);
				validMsg = true;
			} else if (parsedMsg[0].equals("newGame")) {
				makeNewGame(parsedMsg[1]);
				validMsg = true;
			} else if (parsedMsg[0].equals("gameState")) {
				getGameState(parsedMsg[1]);
				validMsg = true;
			} else if (parsedMsg[0].equals("makeMove")) {
				makeMove(parsedMsg[1]);
				validMsg = true;
			} else if (parsedMsg[0].equals("signOut")) {
				signOut();
				validMsg = true;
			}
						
		}
		
		
		if(!validMsg) {
			log.log("SECURITY------INVALID MSG RECIEVED OVER SOCKET ---- CLOSING CON!!! msg \"" + msg + "\" ");
		}
		
		

	}

	/**
	 * the creates a new game with a known or random user
	 * 
	 * @param s
	 */
	private void makeNewGame(String s) {
		//String[] args = s.split(",");
		
		String opUser = s;
		int opUserID = -1;
		
		
		if (opUser.equals("randomOp")) {
			//do random later for not worry about known player
			//int index = rand.nextInt(sharedUsers.size());
			System.out.println("gettign random player");
			
			//opUserID = 
		} else {
			ResultSet r = selectDB("select userID from users where userName = ?", opUser );
			
			
			try {
				if (r.next()) {
					opUserID = r.getInt("userID");
				} else {
					sendMsg("error:noUser");
				}

			} catch (SQLException e) {
				log.log("problem with ResultsSet checking for opUserID in authenticate trace: "
						+ e.toString());
				e.printStackTrace();
			}
			
			
			if(opUserID > -1) {
				if(createGame(opUserID, this.userID)) {
					sendMsg("done:gameCreated");
				} else {
					sendMsg("error:problemCreatingGame");
				}
				
				
			}
			
		}
		
	
		
	}

	

	private boolean createGame(int opUserID, int userID) {
			
		//ok here i need to create the game along will the info for both players 
		//updating all the database tables and what not. 
		
		
		return false;
		
	}

	private void quitGame() {
		quit = true;
		
	}

	/**
	 * 
	 */
	private void signOut() {
		signedIn = false;
		sharedUsers.remove(userID);
		this.userID = -1;
	}

	/**
	 * @param string
	 */
	private void makeMove(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param string
	 */
	private void getGameState(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param string
	 */
	private void getGames(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param string
	 */
	private void createNewUser(String s) {
		boolean working = true;
		
		
		
		// on client check that the email is an email
		// that username follows the UN rules. only letters and numbers
		// and check that the pass is strong enough
		// AS in has 3 of letters, caps, numbers, symbols
		// and is atleast 7 long

		// the array is [username,password,email]
		String[] args = s.split(",");

		// check if username exists

		ResultSet r = selectDB("select userID from users where userName = ?",
				args[0]);

		try {
			if (r.next()) {
				sendMsg("error"); // return error user exists
				working = false;
			}

		} catch (SQLException e) {
			log.log("problem with ResultsSet checking for userID in createNewUser trace: "
					+ e.toString());
			e.printStackTrace();
		}
		
		
		//update the users table

		if (working) {
			working = updateDB("insert into users (username, email) VALUES (?, ?)",
					args[0], args[2]);
		}
		
		
		//update the login table

		if (working) {

			r = selectDB("select userID from users where userName = ?", args[0]);

			int id;

			try {
				if (r.next()) {
					id = r.getInt("userID");
					working = updateDB("insert into login (userID, password) VALUES (?, PASSWORD(?))",
							String.valueOf(id), args[1]);

				}

			} catch (SQLException e) {
				log.log("problem with ResultsSet checking for userID in createNewUser trace: "
						+ e.toString());
				e.printStackTrace();
			}
			
			
			//alert the client as to the result

			if (working) {
				sendMsg("done");
			} else {
				sendMsg("error");
			}

		}

	}

	/**
	 * this method checks the login info if the info is worng is tells the
	 * client there was a problem with the login info. if it is write it adds
	 * the userID to the current users and send confirmation that the con has
	 * been made to the client.
	 * 
	 * @param string
	 */
	private void checkLogin(String string) {
		String[] parsedLog = string.split(",");

		boolean correct;
		int userID = authenticate(parsedLog);

		if (userID > -1) {// check DATABASE here
			correct = true;
		} else {
			correct = false;
		}

		if (correct) {
			sharedUsers.add(userID);
			this.userID = userID;
			signedIn = true;
			sendMsg("done");// use done for all andshake

		} else {
			sendMsg("error");
			// close connection
		}

	}

	/**
	 * checks if the userName exists and then checks the password. if everything
	 * checks out it returns the userID. otherwise it returns -1;
	 * 
	 * 
	 * @param parsedLog
	 * @return userID if correct and exists, -1 is incorrect or doesnt exist
	 */
	private int authenticate(String[] parsedLog) {

		int userID = -1;

		ResultSet r = selectDB(
				"select users.userID from users join login on users.userID=login.userID where userName = ?  AND password = PASSWORD(?);",
				parsedLog[0], parsedLog[1]);

		try {
			if (r.next()) {
				userID = r.getInt("userID");
			}

		} catch (SQLException e) {
			log.log("problem with ResultsSet checking for userID in authenticate trace: "
					+ e.toString());
			e.printStackTrace();
		}

		return userID;

	}

	private boolean updateDB(String stmt, String... args) {

		try {
			// make query
			PreparedStatement p = dbCon.prepareStatement(stmt);

			// set variables

			for (int i = 0; i < args.length; i++) {
				p.setString(i + 1, args[i]);
			}

			// get results
			int worked = p.executeUpdate();

			if (worked > 0) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			log.log("could not get result set form DB trace: " + e.toString());
			e.printStackTrace();
		}

		return false;

	}

	private ResultSet selectDB(String stmt, String... args) {
		ResultSet r = null;

		try {
			// make query
			PreparedStatement p = dbCon.prepareStatement(stmt);

			// set variables

			for (int i = 0; i < args.length; i++) {
				p.setString(i + 1, args[i]);
			}

			// get results
			r = p.executeQuery();
			return r;

		} catch (Exception e) {
			log.log("could not get result set form DB trace: " + e.toString());
			e.printStackTrace();
		}

		return r;

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
