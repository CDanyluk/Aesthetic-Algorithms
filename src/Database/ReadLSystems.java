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
	
	public List<Cells> readTopTen()  throws ClassNotFoundException, SQLException {
		 //Create a list of cells, we are gonna return this at the end
		 List<Cells> cellList = new ArrayList<Cells>();
		 //Normal database stuff
		 Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:Results.db");
        Statement stat = con.createStatement();
        String command = "SELECT  C.Dead, C.Live, C.Colors, C.Seeds, C.Iterations, C.Score FROM  Cells C ORDER BY Score DESC LIMIT 10";
        ParseCells parse = new ParseCells();
        if (stat.execute(command)) {
       	ResultSet results = stat.getResultSet();
     		while (results.next()) {
     	        //Create new cell
     	        Cells cell = new Cells(700, 700);
     	        cell.clear();
     			cell.setDead(parse.parseDead(results.getString("Dead")));
   			cell.setAlive(parse.parseAlive(results.getString("Live")));
   			cell.setColor(parse.parseColor(results.getString("Colors")));
   			//cell.setSeeds(parse.parseSeeds(results.getString("Seeds")));
   			cell.randomGraph();
   			int iter = results.getInt("Iterations");
   			cell.change(cell.getAlive(), cell.getDead());
   			if (oneOrTwo() == 0) {
   				iter--;
   			}else {
   				iter++;
   			}
   			cell.update(iter);
   			cellList.add(cell);
     		}
        }
        stat.close();
        return cellList;
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
	 
	 public int oneOrTwo() {
			if (Math.random() < 0.5) {
			    return 0;
			}else {
				return 1;
			}
		}
	
}
