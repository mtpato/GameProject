package gameProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConner {

	
    public static void main (String[] args)
    {
        Connection conn = null;

        try
        {
            String userName = "theGame";//game account user 
            String password = "212273625";//game account pass
            String url = "jdbc:mysql://localhost/carwars";
            Class.forName ("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection (url, userName, password);
            System.out.println ("Database connection established");
        
            
            /*
            *CHECK THE comments AFTER HERE 
            *get this into the server code 
            *
            *
            *
            */
            
            
            
            //make query
            PreparedStatement p = conn.prepareStatement("select * from users where nickName = ?");
        
            //set variables
            p.setString(1, "tester");
            
            //get results
            ResultSet r = p.executeQuery();
            
            //use them
        	while (r.next()) { // process results one row at a time
                int key = r.getInt("userID");
                String val = r.getString("nickName");
                String email = r.getString("email");

                System.out.println("userID = " + key);
                System.out.println("nick = " + val);
                System.out.println("email = " + email);
              }
        
        
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                    System.out.println ("Database connection terminated");
                }
                catch (Exception e) { /* ignore close errors */ }
            }
        }
    }
}
