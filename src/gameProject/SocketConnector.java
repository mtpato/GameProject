package gameProject;

import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author michaelpato
 *
 * this class does all the socket communication in this program.
 * it is basically a wrapper i wrote for the stuff needed to use 
 * sockets in java .
 */
public class SocketConnector {
    int port;
    String hostname;
    BufferedReader input;
    BufferedWriter output;
    Socket socket = null;


    /**
     * this is the constructor for the socket connector 
     * 
     * @param port
     * @param hostname
     */
    public SocketConnector(int port, String hostname) {
        this.port = port;
        this.hostname = hostname;  
    }

    /**
     * sends a message over a socket
     * 
     * @param msg
     */
    public void sendMsg(String msg) {
        if (msg != null && !msg.equals("")) {
            try{
                output.write(msg);
                output.write(("\r\n"));
                output.flush();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * gets a msg from a socket
     * 
     * @return String, the msg. returns null if it gets nothing 
     */
    public String getReply() {
        String reply;
        try {
            reply = input.readLine();
 
        } catch (Exception e) {
            return null;
        }
        return reply;
    }



    /**
     * inits a connection with the server
     * 
     * @return boolean, true if the connection was established and false if it was not 
     */
    public boolean initCon() {
        try {

            socket = new Socket(hostname, port);

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
    
    
    /**
     * checks if the socket is still open 
     * 
     * @return if the socket is still open 
     */
    boolean isOpen() {
        return !socket.isClosed();
    }
    

    /**
     * closes the connection with the server 
     */
    public void closeCon() {
        try {
            socket.close();
            
        } catch (IOException e) {
            System.err.println("Could not close socket.");
            e.printStackTrace();
        }
    }

}
