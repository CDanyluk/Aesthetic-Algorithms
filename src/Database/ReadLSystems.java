package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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
         System.out.print(results.getString("ImageName") + "\n" +
        		 		results.getString("StartPoints") + "\n" +
        		 		results.getString("StartString") + "\n" +
        		 		results.getString("Rules") + "\n" +
        		 		results.getInt("Recursions") + "\n" +
        		 		results.getInt("Length") + "\n" +
        		 		results.getInt("Angle") + "\n" +
        		 		results.getDouble("Score") + "\n\n\n" 
        		 );
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
	 
	 
	 public List<Cells> readTopTen()  throws ClassNotFoundException, SQLException {
		 //Create a list of cells, we are gonna return this at the end
		 List<Cells> cellList = new ArrayList<Cells>();
		 //Normal database stuff
		 Class.forName("org.sqlite.JDBC");
         Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
         Statement stat = con.createStatement();
         String command = "SELECT  * FROM  LSystems ORDER BY Score DESC LIMIT 10";
         ParseCells parse = new ParseCells();
         if (stat.execute(command)) {
        	ResultSet results = stat.getResultSet();
      		while (results.next()) {
      	        System.out.println(results.getString("ImageName") + " " + results.getString("Score"));
      		}
         }
         stat.close();
         return cellList;
	}
	 
	 public  List<HashMap<String, String>> getTopTen()  throws ClassNotFoundException, SQLException {
		 //Create a list of cells, we are gonna return this at the end
		 List<HashMap<String, String>> info = new ArrayList<HashMap<String, String>>();
		 //Normal database stuff
		 Class.forName("org.sqlite.JDBC");
         Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
         Statement stat = con.createStatement();
         String command = "SELECT  * FROM  LSystems ORDER BY Score DESC LIMIT 10";
         if (stat.execute(command)) {
        	ResultSet results = stat.getResultSet();
        	while(results.next()){
        		HashMap<String, String> info2 = new HashMap<String, String>();
        		info2.put("Rules", results.getString("Rules"));
        		info2.put("StartString", results.getString("StartString"));
        		info2.put("Recursions", results.getString("Recursions"));
        		info2.put("Length", results.getString("Length"));
        		info2.put("Angle", results.getString("Angle"));
        		info.add(info2);
        	}
         }
         stat.close();
         return info;
	}
	 
	 
	
}
