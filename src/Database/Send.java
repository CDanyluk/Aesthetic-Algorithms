
package Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Send {
	//Class to send commands to database?
	// SELECT Field1, Field2, ... FieldN FROM TableName
	 public String get() throws ClassNotFoundException, SQLException, IOException {
	        Class.forName("org.sqlite.JDBC");
	        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
	        Statement stat = con.createStatement();
	       // String cmnd = s;
	         return stat.toString();
	    }

	 public void send(String s) throws ClassNotFoundException, SQLException, IOException {
		 Class.forName("org.sqlite.JDBC");
	        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
	        Statement stat = con.createStatement();
	        String command = s;
	        System.out.println(command);
	        stat.execute(command);
	        con.close();
	 }
	 

}