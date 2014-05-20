package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/*import model.algorithm.Action;
import model.algorithm.State;
import model.game2048.Model2048;
*/
public class ClientUtility {

	public static void Solve(State state) throws Exception 
	{
		Socket  myServer = new Socket(InetAddress.getByName("10.160.5.85"), 5550);
		
		ObjectOutputStream outToServer = new ObjectOutputStream(myServer.getOutputStream());
		ObjectInputStream inFromServer = new ObjectInputStream(myServer.getInputStream());				
		
		//Model2048 m2048 = new Model2048(4);
		//m2048.currState.getBoard()[0][1]=2;
		//m2048.currState.getBoard()[0][2]=4;
		
		//State s;
		//s = m2048.currState.Clone();
		
		//for(int i=0; i<200; i++)
		//{
		//	outToServer.writeObject(s);
		//	outToServer.flush();
		//	
		//	Action nextMove = null;
			
		//	nextMove = (Action) inFromServer.readObject();
			
		//	System.out.println("Server returned: " + nextMove.getName());
			
		//	s = nextMove.doAction(s);
		//}
		
		Thread.sleep(5 * 1000);
		
		System.out.println("Client finished");
	
	}
}
