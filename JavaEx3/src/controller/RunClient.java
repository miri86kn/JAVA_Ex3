/* JAVA Lesson: 29/04/2014 */

package controller;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class RunClient {

	public static void main(String[] srgs) throws Exception 
	{
		Socket  myServer = new Socket(InetAddress.getLocalHost(), 5000);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(myServer.getInputStream()));
		PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String line;
		//read input
		while(!(line = inFromUser.readLine()).equals("exit"))
		{
			outToServer.println(line); //notify server
			outToServer.flush();
		}
		outToServer.println(line);
		
		//close all stuff
		inFromUser.close();
		inFromServer.close();
		outToServer.close();
		myServer.close();
	}
}
