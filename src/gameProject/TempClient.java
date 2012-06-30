package gameProject;

public class TempClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SocketConnector con = new SocketConnector(4356, "localhost");
		
		con.initCon();
		con.sendMsg("YAY IT WORKS");
		System.out.println(con.getReply());
		con.sendMsg("GLAD YOU HEAR ME");

	}

}
