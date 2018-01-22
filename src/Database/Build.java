package Database;

import java.sql.*;
import java.util.Scanner;

// Most useful for creating/adding material to a database
// Make sure sqlite-jdbc-3.8.11.2.jar is in the Java build path
public class Build {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:ChiValues");
        Statement stat = con.createStatement();

        boolean done = false;
        Scanner input = new Scanner(System.in);
        while (!done) {
            System.out.print("Enter command: ");
            String cmd = input.nextLine();
            if (cmd.equals("quit")) {
                done = true;
            } else {
                stat.execute(cmd);
            }
        }
	input.close();
    }
}