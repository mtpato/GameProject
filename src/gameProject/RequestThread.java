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
	private int userID;

	private Set<Integer> sharedUsers;

	private Connection dbCon = null;

	public RequestThread(Socket socket, Set<Integer> activeUsers) {
		this.socket = socket;
		sharedUsers = activeUsers;

	}

	public void run() {

		initIO();
		initDBCon("theGame", "212273625", "jdbc:mysql://localhost/tile");

		listen();

		closeDBCon();
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
		while (connected) {
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

		System.out.println("IN SERVER: " + msg);

		if (parsedMsg[0].equals("login")) {
			checkLogin(parsedMsg[1]);
		} else if (parsedMsg[0].equals("newUser")) {
			createNewUser(parsedMsg[1]);
		}

	}

	/**
	 * @param string
	 */
	private void createNewUser(String string) {

		// TODO Auto-generated method stub

	}

	/**
	 * this method checks the login info if the info is worng is 
	 * tells the client there was a problem with the login info.
	 * if it is write it adds the userID to the current users and 
	 * send confirmation that the con has been made to the client.
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

		ResultSet r = callDB("select userID from users where userName = \'"
				+ parsedLog[0] + "\'");

		try {
			if (r.next()) {
				userID = r.getInt("userID");
			}

		} catch (SQLException e) {
			log.log("problem with ResultsSet checking for userID in authenticate trace: "
					+ e.toString());
			e.printStackTrace();
		}

		if (userID > -1) {
			r = callDB("select userID from login where userID = \'" + userID
					+ "\' AND password = PASSWORD(\'" + parsedLog[1] + "\')");

			try {
				if (r.next()) {
					return userID;
				} else {
					return -1;
				}

			} catch (SQLException e) {
				log.log("problem with ResultsSet checking for userID trace: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		
		return userID;


	}

	private ResultSet callDB(String stmt, String... args) {
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
			// TODO Auto-generated catch block
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
