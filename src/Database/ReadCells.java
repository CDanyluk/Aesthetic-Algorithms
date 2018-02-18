package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import Automata.Cells;
import javafx.scene.paint.Color;

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
        stat.close();
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
        stat.close();
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
        stat.close();
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
        stat.close();
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
        stat.close();
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
         stat.close();
	 }
	 
	public boolean[][] readBestSeeds() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
         Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
         Statement stat = con.createStatement();
         String command = "SELECT  C.Seeds, C.Score\r\n" + 
         		"FROM  Cells C\r\n" + 
         		"WHERE  (SELECT  MAX (C2.Score)\r\n" + 
         		"               FROM  Cells C2)\r\n" + 
         		"               = C.Score";
         ParseCells parse = new ParseCells();
         stat.execute(command);
         ResultSet results = stat.getResultSet();
         String seed = results.getString("Seeds");
         stat.close();
         return parse.parseSeeds(seed);
	 }
	
	public Map<Integer, Boolean> readBestDead() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
         Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
         Statement stat = con.createStatement();
         String command = "SELECT  C.Dead, C.Score\r\n" + 
         		"FROM  Cells C\r\n" + 
         		"WHERE  (SELECT  MAX (C2.Score)\r\n" + 
         		"               FROM  Cells C2)\r\n" + 
         		"               = C.Score";
         ParseCells parse = new ParseCells();
         stat.execute(command);
         ResultSet results = stat.getResultSet();
         String dead = results.getString("Dead");
         stat.close();
         return parse.parseDead(dead);

	}
	
	public Map<Integer, Boolean> readBestAlive() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
         Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
         Statement stat = con.createStatement();
         String command = "SELECT  C.Live, C.Score\r\n" + 
         		"FROM  Cells C\r\n" + 
         		"WHERE  (SELECT  MAX (C2.Score)\r\n" + 
         		"               FROM  Cells C2)\r\n" + 
         		"               = C.Score";
         ParseCells parse = new ParseCells();
         stat.execute(command);
         ResultSet results = stat.getResultSet();
         String live = results.getString("Live");
         stat.close();
         return parse.parseAlive(live);

	}
	
	public HashMap<Integer, Color> readBestColors() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
        String command = "SELECT  C.Colors, C.Score\r\n" + 
        		"FROM  Cells C\r\n" + 
        		"WHERE  (SELECT  MAX (C2.Score)\r\n" + 
        		"               FROM  Cells C2)\r\n" + 
        		"               = C.Score";
        ParseCells parse = new ParseCells();
        stat.execute(command);
        ResultSet results = stat.getResultSet();
        String colors = results.getString("Colors");
        stat.close();
        return parse.parseColor(colors);
	}
	
	public int readBestIter() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
        String command = "SELECT  C.Iterations, C.Score\r\n" + 
        		"FROM  Cells C\r\n" + 
        		"WHERE  (SELECT  MAX (C2.Score)\r\n" + 
        		"               FROM  Cells C2)\r\n" + 
        		"               = C.Score";
        stat.execute(command);
        ResultSet results = stat.getResultSet();
        int iter = results.getInt("Iterations");
        stat.close();
        return iter;
	}
	
	 
	 public void deleteAll() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
	     Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
	     Statement stat = con.createStatement();
	     String command = "DELETE FROM Cells;";
	     System.out.println("Cells has been cleared");
	     stat.execute(command);
	     stat.close();
	 }
	 
	 public void deleteBad() throws ClassNotFoundException, SQLException {
		 Class.forName("org.sqlite.JDBC");
	     Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
	     Statement stat = con.createStatement();
	     String command = "DELETE FROM Cells WHERE Score = 0.0;";
	     System.out.println("Cells with a bad score have been removed from database");
	     stat.execute(command);
	     stat.close();
	 }

}
