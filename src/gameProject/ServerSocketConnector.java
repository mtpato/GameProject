package gameProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * @author michaelpato
 *
 * this class extends socket connector the only real dif is that a server needs to 
 * init the socket ans start listening. after that everything is exactly 
 * like a normal socket connector
 */
public class ServerSocketConnector extends SocketConnector{

    ServerSocket serverSocket;
    
    /**
     * this is the constructor for the server socket connector it really just calls 
     * super but deosnt bother passing a host cause it doesnt need it 
     * 
     * @param port
     */
    public ServerSocketConnector(int port) {
        super(port, null);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see MiniRSA.SocketConnector#initCon()
     */
    @Override
    public boolean initCon() {
        try {
            serverSocket = new ServerSocket(port);
                     
            socket = serverSocket.accept();
            
           // input = new Scanner(socket.getInputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            return true;
        } catch (UnknownHostException e) {
            System.err.println("Error connecting to port " + port + "! Unknown host.");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("Error connecting to port.");
            e.printStackTrace();
            return false;
        }
    }
    

}
