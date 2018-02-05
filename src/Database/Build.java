package Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

// Most useful for creating/adding material to a database
// Make sure sqlite-jdbc-3.8.11.2.jar is in the Java build path
public class Build {
	public static void main() throws ClassNotFoundException, SQLException, IOException {
		Class.forName("org.sqlite.JDBC");
		Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
		Statement stat = con.createStatement();
		BufferedReader commands = new BufferedReader(new FileReader("resources/db.txt"));
		String command;
		while ((command = commands.readLine()) != null) {
			//System.out.println(command);
			stat.execute(command);
		}
	}
}
