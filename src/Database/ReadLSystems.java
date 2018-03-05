package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Automata.Cells;

public class ReadLSystems {
	
	//ImageName TEXT, StartPoints TEXT, StartString TEXT, Rules TEXT, Recursions INTEGER, Length Integer, Angle INTEGER, Score REAL

	public void readRow() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
		String command = "SELECT * FROM LSystems";
		if (stat.execute(command)) {
     		ResultSet results = stat.getResultSet();
     		while (results.next()) {
                System.out.print(results.getString("ImageName") + ", ");
                System.out.print(results.getString("StartPoints") + ", ");
                System.out.print(results.getString("StartString") + ", ");
                System.out.print(results.getString("Rules") + ", ");
                System.out.print(results.getString("Recursions") + ", ");
                System.out.print(results.getInt("Length") + ", ");
                System.out.print(results.getDouble("Angle") + "\n");
                System.out.print(results.getDouble("Score") + "\n");
     		}
		}
		stat.close();
	}
	
	public void readImageName() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
        String command = "SELECT ImageName FROM Cells;";
        if (stat.execute(command)) {
        		ResultSet results = stat.getResultSet();
        		while (results.next()) {
                   System.out.print(results.getString("ImageName"));
        		   System.out.println();
        		}
        }
        stat.close();
	}
	
	 public void readScore() throws ClassNotFoundException, SQLException {
   	  	 Class.forName("org.sqlite.JDBC");
         Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
         Statement stat = con.createStatement();
         String command = "SELECT Score FROM LSystems;";
         if (stat.execute(command)) {
         		ResultSet results = stat.getResultSet();
         		while (results.next()) {
                    System.out.print(results.getDouble("Score"));
         			System.out.println();
         		}
         }
         stat.close();
	 }
	 
	 public void readBest() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
         Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
         Statement stat = con.createStatement();
         String command = "SELECT * FROM LSystems L WHERE (SELECT MAX(Score) FROM LSystems);";
         stat.execute(command);
         ResultSet results = stat.getResultSet();
         System.out.print(results.getString("ImageName"));
         stat.close();
         //return results.getString("ImageName");
	 }
	 
	 public void deleteAll() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
	     Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
	     Statement stat = con.createStatement();
	     String command = "DELETE FROM LSystems;";
	     System.out.println("LSystems has been cleared");
	     stat.execute(command);
	     stat.close();
	 }
	 
	 public void deleteBad() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
	     Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
	     Statement stat = con.createStatement();
	     String command = "DELETE FROM LSystems WHERE Score = 0.0;";
	     System.out.println("LSystems with a bad score have been removed from database");
	     stat.execute(command);
	     stat.close();
	 }
	 
	 public int oneOrTwo() {
			if (Math.random() < 0.5) {
			    return 0;
			}else {
				return 1;
			}
		}
	
}
