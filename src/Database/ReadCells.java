package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Automata.Cells;

public class ReadCells {
	
	public void readRow() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
		String command = "SELECT ImageName, Seeds, Dead, Live, Colors, Iterations, Score FROM Cells";
		if (stat.execute(command)) {
     		ResultSet results = stat.getResultSet();
     		while (results.next()) {
                System.out.print(results.getString("ImageName") + ", ");
                //System.out.print(results.getString("Seeds") + ", ");
                //System.out.print(results.getString("Dead") + ", ");
                //System.out.print(results.getString("Live") + ", ");
                //System.out.print(results.getString("Colors") + ", ");
                //System.out.print(results.getInt("Iterations") + ", ");
                System.out.print(results.getDouble("Score") + "\n");
     		}
     }
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
	}
	
	public void readSeeds() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
        String command = "SELECT Seeds FROM Cells;";
        if (stat.execute(command)) {
        		ResultSet results = stat.getResultSet();
        		while (results.next()) {
                   System.out.print(results.getString("Seeds"));
        			System.out.println();
        		}
        }
	}
	
	public void readDead() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
        String command = "SELECT Dead FROM Cells;";
        if (stat.execute(command)) {
        		ResultSet results = stat.getResultSet();
        		while (results.next()) {
                   System.out.print(results.getString("Dead"));
        			System.out.println();
        		}
        }
	}
	
	public void readLive() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
        String command = "SELECT Live FROM Cells;";
        if (stat.execute(command)) {
        		ResultSet results = stat.getResultSet();
        		while (results.next()) {
                   System.out.print(results.getString("Live"));
        			System.out.println();
        		}
        }
	}
	
	public void readColors() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
        String command = "SELECT Colors FROM Cells;";
        if (stat.execute(command)) {
        		ResultSet results = stat.getResultSet();
        		while (results.next()) {
                   System.out.print(results.getString("Colors"));
        			System.out.println();
        		}
        }
	}
	
	public void readIterations() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
        String command = "SELECT Iterations FROM Cells;";
        if (stat.execute(command)) {
        		ResultSet results = stat.getResultSet();
        		while (results.next()) {
                   System.out.print(results.getInt("Iterations"));
        			System.out.println();
        		}
        }
	}
	
	 public void readScore() throws ClassNotFoundException, SQLException {
   	  	 Class.forName("org.sqlite.JDBC");
         Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
         Statement stat = con.createStatement();
         String command = "SELECT Score FROM Cells;";
         if (stat.execute(command)) {
         		ResultSet results = stat.getResultSet();
         		while (results.next()) {
                    System.out.print(results.getDouble("Score"));
         			System.out.println();
         		}
         }
	 }
	 
	public void readBest() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
         Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
         Statement stat = con.createStatement();
         String command = "SELECT  C.Seeds, C.Score\r\n" + 
         		"FROM  Cells C\r\n" + 
         		"WHERE  (SELECT  MAX (C2.Score)\r\n" + 
         		"               FROM  Cells C2)\r\n" + 
         		"               = C.Score";
         Cells cells = new Cells(700, 700);
         ParseCells parse = new ParseCells();
         if (stat.execute(command)) {
         		ResultSet results = stat.getResultSet();
                parse.parseSeeds(results.getString("Seeds"));

         }
	 }
	 
	 public void deleteAll() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
	     Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
	     Statement stat = con.createStatement();
	     String command = "DELETE FROM Cells;";
	     System.out.println("Cells has been cleared");
	     stat.execute(command);
	     con.close();
	 }
	 
	 public void deleteBad() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
	     Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
	     Statement stat = con.createStatement();
	     String command = "DELETE FROM Cells WHERE Score = 0.0;";
	     System.out.println("Cells with a bad score have been removed from database");
	     stat.execute(command);
	     con.close();
	 }

}
