package gameProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Michael T Pato
 * 
 */
public class RequestThread extends Thread {
	//errorlogger
	private ErrorLogger log;

	//Communication
	private BufferedReader in;
	private BufferedWriter out;
	private Socket socket;
	
	//shared state
	private Set<Integer> sharedUsers;
	
	//db stuff
	private Connection dbCon = null;
	
	//threads local variables 
	private int userID = -1;
	private boolean quit = false;
	private boolean signedIn = false;
	private GameModel model;
	
	

	public RequestThread(Socket socket, Set<Integer> activeUsers) {
		this.socket = socket;
		sharedUsers = activeUsers;

	}
	
	private Random rand = new Random();

	public void run() {

		
		
		initIO();
		initDBCon("theGame", "212273625", "jdbc:mysql://localhost/tile");
		
		
		sendMsg("done");//IO set up need to find game 	

		findGame();

		
		//sendMsg("done");
		//while(model == null)
		sendMsg("done");//let the client know that the connection was successful 
		
		
		
		listen();
		
		//once game is quit
		closeSocketCon();
		closeDBCon();
	}

	private void findGame() {

		String msg = null;
		while (msg == null) {

			try {
				if (in.ready()) {
					msg = in.readLine();

					if (msg.equals("tileGame")) {
						model = new TileModel();
						System.out.println("made Tile Game Model");

					} else if (msg.equals("whatever")) {
						// some other games model
						// this shoudl work great
					}

				}

			} catch (IOException e) {
				log.log("problem reading line from socket. trace: "
						+ e.toString());

				e.printStackTrace();
			}

		}

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

				msg = in.readLine();

				
				if(msg != null) {
					handleRequest(msg);
				} else {
					quit = true;
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
		
		

		
		//check signedIn boolean before doing anything but create user and login
		if (parsedMsg[0].equals("quit")) {
			System.out.println("IN SERVER: " + msg);
			
			quitGame();
			validMsg = true;
		} else if(!signedIn) {
			System.out.println("IN SERVER: " + msg);
			
			if (parsedMsg[0].equals("keyLogin")) {
				checkAuthKey(parsedMsg[1]);
				validMsg = true;
			} else if (parsedMsg[0].equals("login")) {
				checkLogin(parsedMsg[1]);
				validMsg = true;
			} else if (parsedMsg[0].equals("newUser")) {
				createNewUser(parsedMsg[1]);
				validMsg = true;
			}
		} else if(signedIn) {
			System.out.println("IN SERVER: " + userID + " - "+ msg);
			
			if (parsedMsg[0].equals("getGames")) {
				getGames("");
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
			} else if (parsedMsg[0].equals("deleteGame")) {
				deleteGame(parsedMsg[1]);
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
	 * this checks for login with the authentication key
	 * 
	 * 
	 * FORMAT:
	 * keyLogin:userID,authKey
	 * 
	 * 
	 * EXAMPLE:
	 * keyLogin:mike,1h2i2bb23n48d9snd9nf9snf
	 * 
	 * @param string
	 */
	private void checkAuthKey(String string) {
		String[] parsedLog = string.split(",");

		boolean correct;
		int userID = authenticateKey(parsedLog);

		if (userID > -1) {// check DATABASE here
			correct = true;
		} else {
			correct = false;
		}

		if (correct) {
			sharedUsers.add(userID);
			this.userID = userID;
			signedIn = true;
			
			sendMsg("done:" + userID);// use done for all andshake

		} else {
			sendMsg("error");
			// close connection
		}
		
	}
	
	private int authenticateKey(String[] parsedLog) {

		int userID = -1;

		ResultSet r = selectDB(
				"select users.userID from users join login on users.userID=login.userID where userName = ?  AND authKey = PASSWORD(?);",
				parsedLog[0], parsedLog[1]);

		try {
			if (r.next()) {
				userID = r.getInt("userID");
			}

		} catch (SQLException e) {
			log.log("problem with ResultsSet checking for userID in authenticateKey trace: "
					+ e.toString());
			e.printStackTrace();
		}

		return userID;

	}

	private void deleteGame(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * the creates a new game with a known or random user
	 * 
	 * format:
	 * userName,userName,userName
	 * 
	 * 
	 * example:
	 * mike,test2,test3
	 * 
	 * 
	 * ISSUE: only takes one userID right now this can be easily fixed
	 * most of the rest of the code is already generalizable for any number 
	 * of players 
	 * 
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
				
				Set<Integer> users = new HashSet<Integer>();
				users.add(opUserID);
				users.add(this.userID);
				
				if(createGame(users)) {
					sendMsg("done:gameCreated");
				} else {
					sendMsg("error:problemCreatingGame");
				}
				
				
			}
			
		}
		
	
		
	}

	

	/**
	 * this method creates the game, updates the games table, then updates the usersToGames
	 * table
	 * @param users, the list of userIDs to make the game with
	 * @return true if it worked false if it didnt 
	 */
	private boolean createGame(Set<Integer> users) {
		

		
		//create game 
		
		System.out.println(model);
		GameState s = model.createNewGame(users);
		
		model.printState(s);
		
		//put game in DB
		
		String compState = model.compressGameState(s);
		
		//System.out.println(compState);
		
		if(!updateDB("insert into games (state, startTime, finished, game) VALUES (?, NOW(), 0, ?)", compState, model.whatGame())) {
			log.log("problem inserting new game in to DB");
		}
		
		
		ResultSet r = selectDB("select gameID from games where state = ?", compState);

		
		int gameID = -1;
		
		try {
			if (r.next()) {
				System.out.println(r.getInt("gameID")); // return error user exists
				gameID = r.getInt("gameID");
				sendMsg("gameID:" + gameID);
			}

		} catch (SQLException e) {
			log.log("problem with ResultsSet trying to get new game ID trace: "
					+ e.toString());
			e.printStackTrace();
		}
		
		if(gameID > -1) {
			for(int u: users) {
				if(!updateDB("insert into usersToGames (userID, gameID) VALUES (?, ?)", 
						String.valueOf(u), String.valueOf(gameID))) {
					log.log("problem inserting new game in to usersToGames");
				}
			}
		} else {
			log.log("problem creating game");
			return false;
		}

		
		
		return true;
		
	}

	private void quitGame() {
		quit = true;
		sendMsg("done");
		
	}

	/**
	 * 
	 */
	private void signOut() {
		signedIn = false;
		sharedUsers.remove(userID);
		this.userID = -1;
		sendMsg("done");
	}

	/**
	 * this method executes a more and then sends 
	 * the new state to the user 
	 * 
	 * in Format:
	 * gameID,move
	 * 
	 * 
	 * @param string
	 */
	private void makeMove(String input) {
		//parse command
		String[] in = input.split(",");
		
		
		
		//get the state from the DB
		
		String compState = getCompString(in[0]);
		
		
		//parse the string from the DB
		
		GameState state = model.parseGameState(compState);
		
		//model.printState(state);
		//make the move
		
		state = model.makeMove(state, in[1]);
		//model.printState(state);
		
		//compress the state
		
		compState = model.compressGameState(state);
		
		//store it in the BD
		
		if(model.isOver(state)) {
			if(!updateDB("UPDATE games SET state = ?,finished=? WHERE gameID = ?", compState, "1", in[0])) {
				log.log("problem inserting updating game in make move");
			}
		} else {
			if(!updateDB("UPDATE games SET state = ? WHERE gameID = ?", compState, in[0])) {
				log.log("problem inserting updating game in make move");
			}
		}
		
		
		//send it back to the user???????
		sendMsg(compState);//MAYBE DONT DO
		
		

		
		
	}

	private String getCompString(String gameID) {
		ResultSet r = selectDB("SELECT state FROM games WHERE gameID = ?", gameID );

		
		try {
			if(r.next()) {
				return r.getString("state");
				
			} else {
				return null;
			}

		} catch (SQLException e) {
			log.log("problem with ResultsSet getting game state trace: "
					+ e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this function gets the game state from the database and 
	 * then sends it to the client
	 * 
	 * 
	 * @param string the gameID
	 */
	private void getGameState(String gameID) {
		
		String compState = getCompString( gameID);
		
		if(compState != null) {
			sendMsg("state:" + compState);
			
		} else {
			sendMsg("error:notFound");
		}
		
		
	}

	/**
	 * this method finds all the active games for this user in this game 
	 * 
	 * @param string
	 */
	private void getGames(String string) {
		HashMap<Integer,String> games = new  HashMap<Integer,String>();
		
		ResultSet r = selectDB("SELECT gameID,users.userName,users.userID " +
				"FROM users JOIN usersTogames " +
				"ON users.userID = usersTogames.userID " +
				"WHERE gameID IN ( " +
				"SELECT usersToGames.gameID " +
				"FROM usersToGames " +
				"JOIN games on games.gameID = usersTogames.gameID " +
				"WHERE usersToGames.userID = ? " +
				"AND games.finished = ? " +
				"AND games.game = ? " +
				")", String.valueOf(userID), String.valueOf(0), model.whatGame());

		try {
			int gameID;
			while (r.next()) {
				gameID = r.getInt("gameID");
				if(games.containsKey(gameID)) {
					games.put(gameID, games.get(gameID) + "|" + r.getString("userName") + "-" + r.getInt("userID"));
				} else {
					games.put(gameID, r.getString("userName") + "-" + r.getInt("userID"));
				}
				
				
			}

		} catch (SQLException e) {
			log.log("problem with ResultsSet gettinggames info in getGames trace: "
					+ e.toString());
			e.printStackTrace();
		}
		
		
		//System.out.println(games);
		
		String gamesString = makeGamesString(games);
		
		System.out.println(gamesString);
		
		sendMsg(gamesString);
		
	}

	/**
	 * this function builds the string to send to the client with 
	 * the info about what games they have active 
	 * 
	 * ISSUE: right now it just sends the games and the players in them 
	 * might want to make it send more later like stats about the game 
	 * but that might be more data than its worth.
	 * 
	 * format:
	 * games:gameID|userName-userID|userName-userID...,gameID|userName-userID|userName-userID...
	 * 
	 * Example:
	 * games:15|mike-1|test3-2,17|mike|test3|test10
	 * 
	 * @param games
	 * @return
	 */
	private String makeGamesString(HashMap<Integer, String> games) {
		
		StringBuilder buf = new StringBuilder("games:");
		
		for(int gID: games.keySet()) {
			buf.append(gID + "|" + games.get(gID)+ ",");
		}
		
		/*for(String s: games.values()) {
			buf.append(s + ",");
		}*/
		
		buf.deleteCharAt(buf.length() - 1);
		
		
		return buf.toString();
	}

	/**
	 * this method creates a new user 
	 * 
	 * it assumes that on client side we checked that:
	 * 		the email is an email
	 * 		that username follows the UN rules. only letters and numbers
	 * 		and check that the pass is strong enough
	 * 		AS in has 3 of letters, caps, numbers, symbols
	 * 		and is at least 7 long
	 * 
	 * format: 
	 * username,password,email
	 * 
	 * example:
	 * mike,testPass,testEmail@email.com
	 * 
	 *  
	 * 
	 * @param string
	 */
	private void createNewUser(String s) {
		boolean working = true;

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
	 * format:
	 * user,password
	 * 
	 * example:
	 * mike,testPass
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
			
			String authKey = generateAuthKey();
			
			storeAuthKey(userID,authKey);
			
			sendMsg("done:" + userID + ":" + authKey);// use done for all andshake

		} else {
			sendMsg("error");
			// close connection
		}

	}

	private void storeAuthKey(int userID, String authKey) {
		updateDB("UPDATE login SET authKey = PASSWORD(?) WHERE userID = ?",
				authKey, String.valueOf(userID));
		
	}

	/**
	 * this method generates and authentication key to
	 * send back to the user 
	 * 
	 * @return
	 */
	private String generateAuthKey() {
		SecureRandom sRan = new SecureRandom();

		String key = new BigInteger(130, sRan).toString(32);

		return key;
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
