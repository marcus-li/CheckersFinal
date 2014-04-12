

import gui.CheckersFrame;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CheckersClient {

    private String _user = "5";
    private String _password = "238056";  
    private final static String _opponent = "0";
    private final String _machine  = "icarus2.engr.uconn.edu"; 
    private int _port = 3499;
    private Socket _socket = null;
    private PrintWriter _out;
    private BufferedReader _in;
    private Scanner _sc = new Scanner(System.in);
    private String _gameID;
    private String _myColor;
	private CheckersFrame _GUI;
	
	private int[] _pieces = new int[36];
  
    public CheckersClient(){
    		initializePieces();
    		_GUI = new CheckersFrame();
    		_GUI.updatePieces(_pieces);
    		
    			setupCredentials();
    			_socket = openSocket();
    			connectToServer();
    			

    }

 

	





	private void connectToServer()
		{
			String readMessage;
			try{
				
			    readAndEcho();				// SAMv1.0
			    readAndEcho();				// ?Username
			    writeMessageAndEcho(_user); //send username to server
			    readAndEcho(); 				// ?Password
			    writeMessageAndEcho(_password);  	// send password to server
			    readAndEcho(); 				// ?Opponent
			    System.out.println("\tserver opponent = 0");
			    String opponent = _sc.nextLine();
			    writeMessageAndEcho(opponent);  // opponent
			    _gameID = readAndEcho().substring(5,9); // game 
			    _myColor = readAndEcho().substring(6,11);  // color
			    
			    System.out.println("I am playing as "+ _myColor + " in game number "+ _gameID);
			    
			    //===============End Game Setup=================
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
	public static void main(String[] argv){
		new CheckersClient();
		Translate.initialize();//initialize our translation function
		}
	
	
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
    
    //choose which user to connect with
    private void setupCredentials()
		{
			System.out.println("User 1: 5, Pass: 238056\nUser2: 6, Pass: 993592");
	    	System.out.println("Connect as User 1 or User 2? (Enter 1 or 2)");
	    	if(_sc.nextLine().equals("2"))
	    		{
	    			_user = "6";
	    			_password = "993592";
	    		}
	    	else
	    		{
	    			System.out.println("connecting with default credentials");
	    		}
		}
    
    private void initializePieces()
		{
			//black pieces
			for(int i = 1 ; i <=8;i++ )
				{
					_pieces[i]=-1;
				}
			
			_pieces[10]=-1;
			_pieces[11]=-1;
			_pieces[12]=-1;
			_pieces[13]=-1;
			
			//white pieces
			for(int i = 28 ; i <=35;i++ )
				{
					_pieces[i]=1;
				}
			
			_pieces[26]=1;
			_pieces[25]=1;
			_pieces[24]=1;
			_pieces[23]=1;
			
		}
    
    //simple print function
    public static void print(String s)
    	{
    		System.out.println(s);
    	}


}