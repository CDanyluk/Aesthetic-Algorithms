package Database;


import Automata.Cells;

public class DatabaseInput {
	
	Send send = new Send();
	Read read = new Read();
	
	public DatabaseInput() {}
	//Goadrich likes a string of 8 (9??) characters, TFTTFTFF for the dead and alive to repesent the cells
	//Since we KNOW there will always be 8 (9??) rules
	
	//(x,y) will become (00x00y) for example (8, 10) will be (008010)
	
	//Colors will just be a string like so "#color#color#color" easy to parse
	
	//Iterations just needs the number
	
	//Instead of getAlive write a getAliveDatabase for this purpose in cells
	
	//Write a thing to retrieve them from the database
	//Goal is to delete
	
	public void cellsToDatabase(String name, Cells cells, double score) throws Exception{
		String insert = "INSERT INTO Cells VALUES (\'" + name + "\', \'"+ cells.getSeedData() + "\', \'" + cells.getDeadData() + "\', \'" + cells.getAliveData() 
				+ "\', \'" + cells.getColorsData() + "\', \'" + cells.getIterData() + "\', \'" + score + "\')";
		send.send(insert);
	}
	
	public void lsystemsToDatabase(String name, String startPoints, String startString, String rules, int recursions, int length, int angle, int score) throws Exception{
		System.out.println(rules + "XXXX");
		String insert = "INSERT INTO LSystems VALUES (\'" + name + "\', \'"+ startPoints + "\', \'" + startString + "\', \'" + rules + "\', \'" + recursions + 
				"\', \'"+ length + "\', \'" + angle 
				+ "\', \'" + score + "\')";
		send.send(insert);
	}
	
	

}
