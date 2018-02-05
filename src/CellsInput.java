

import Database.Read;
import Database.Send;

public class CellsInput {
	
	Send send = new Send();
	Read read = new Read();
	
	public CellsInput() {}
	
	public void toDatabase(String name, String seed, Cells cells, double score) throws Exception{
		String insert = "INSERT INTO Cells VALUES (" + name + ", "+ seed + ", " + cells.getDead().toString() + ", " + cells.getAlive().toString() 
				+ ", " + cells.getColors().toString() + ", " + cells.getIterations() + ", " + 42 + ")";
		send.send(insert);
	
	
	
	}

}
