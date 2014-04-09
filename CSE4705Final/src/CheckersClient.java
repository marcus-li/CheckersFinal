

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CheckersClient {

    private final static String _user = "5";  // need legit id here
    private final static String _password = "238056";  // need password here
    private final static String _opponent = "0";
    private final String _machine  = "icarus2.engr.uconn.edu"; 
    private int _port = 3499;
    private Socket _socket = null;
    private PrintWriter _out;
    private BufferedReader _in;
    private Scanner _sc = new Scanner(System.in);
    private String _gameID;
    private String _myColor;
  
    public CheckersClient(){	
	_socket = openSocket();
	
	connectToServer();

    }

 

    private void connectToServer()
		{
			String readMessage;
			try{
				
				
			    readAndEcho(); // SAMv1.0
			    readAndEcho(); // ?Username
			    String user = _sc.nextLine();
			    writeMessageAndEcho(user); //send username to server
			    
			    readAndEcho(); // ?Password
			    String pass = _sc.nextLine();
			    writeMessage(pass);  // send password to server

			    readAndEcho(); // ?Opponent
			    System.out.println("( server opponent = 0 )");
			    String opponent = _sc.nextLine();
			    writeMessageAndEcho(opponent);  // opponent

			    _gameID = readAndEcho().substring(5,9); // game 
			    _myColor = readAndEcho().substring(6,11);  // color
			    
			    System.out.println("I am playing as "+ _myColor + " in game number "+ _gameID);
			    readMessage = readAndEcho();  
			    String response;
			    while(true)
			    	{
			    		response = _sc.nextLine();
			    		if(response.equals("quit"))
			    			{
			    			break;
			    			}
			    		if(response.equals(""))
			    			{
			    			 readMessage = readAndEcho();
			    			}
			    		else
			    			{
			    				writeMessageAndEcho(response);
			    			}
			    		
			    	}
			   
			    _socket.close();
			} catch  (IOException e) {
			    System.out.println("Failed in read/close");
			    System.exit(1);
			}
			
		}


    //initiate new client
	public static void main(String[] argv){new CheckersClient();}
	
	
	//read a message from the server and return it as a string
    public String readAndEcho() throws IOException
    {
		String readMessage = _in.readLine();
		System.out.println("read: "+readMessage);
		return readMessage;
    }

    
    //send a message to the server
    public void writeMessage(String message) throws IOException
    {
		_out.print(message+"\r\n");  
		_out.flush();
    }
 
    //send a message to the server, but also print back to the console
    public void writeMessageAndEcho(String message) throws IOException
    {
		_out.print(message+"\r\n");  
		_out.flush();
		System.out.println("sent: "+ message);
    }
			     
    
    
    //Initiate a tcp connection with the server
    public  Socket openSocket(){
	try{
       _socket = new Socket(_machine, _port);
       _out = new PrintWriter(_socket.getOutputStream(), true);
       _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
     } catch (UnknownHostException e) {
       System.out.println("Unknown host: " + _machine);
       System.exit(1);
     } catch  (IOException e) {
       System.out.println("No I/O");
       System.exit(1);
     }
     return _socket;
  }

}