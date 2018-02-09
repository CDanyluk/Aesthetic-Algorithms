package Database;

import java.sql.*;
import java.util.Scanner;

// Most useful for reading material from a database
// Make sure that sqlite-jdbc.3.8.11.2.jar is in the Java Build Path
public class Read {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();

        boolean done = false;
        Scanner input = new Scanner(System.in);
        while (!done) {
            System.out.print("Enter command: ");
            String cmd = input.nextLine();
            if (cmd.equals("quit")) {
                done = true;
            } else {
                System.out.print("Enter # of columns expected: ");
                int columns = input.nextInt();
                input.nextLine();
                if (stat.execute(cmd)) {
                    ResultSet results = stat.getResultSet();
                    while (results.next()) {
                        for (int c = 1; c <= columns; ++c) {
                            System.out.print(results.getString(c) + "\t");
                        }
                        System.out.println();
                    } 
                }
            }
        }
	input.close();
    }
    
    public void readCells() throws ClassNotFoundException, SQLException {
    	  Class.forName("org.sqlite.JDBC");
          Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
          Statement stat = con.createStatement();
          String command2 = "SELECT ImageName FROM Cells;";
          String command = "SELECT score FROM Cells;";
          if (stat.execute(command)) {
          		ResultSet results = stat.getResultSet();
          		while (results.next()) {
                     System.out.print(results.getDouble("score"));
          			 System.out.println();
          		}
          }
    }
         
}
