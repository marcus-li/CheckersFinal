package client;

import utilities.*;
import gui.CheckersFrame;
import java.io.*;
import java.net.*;
import java.util.Scanner;


public class WorkingClientNotForTesting {

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
	
	private int[] _pieces = new int[36];//black -1,-2 white +1, +2 (2 are kings)
	
	   //initiate new client
//		public static void main(String[] args){
//			Translate.initialize();//initialize our translation function
//			//new CheckersClient();	
//			new InternalServer();
//			}
  
    public WorkingClientNotForTesting(double[] weights){ 	
    		_pieces = new int[]{0,-1,-1,-1,-1,-1,-1,-1,-1,0,-1,-1,-1,-1,0,0,0,0,0,0,0,0,0,1,1,1,1,0,1,1,1,1,1,1,1,1};
    		_GUI = new CheckersFrame();
    		_GUI.updatePieces(_pieces);
    		setupCredentials();
    		_socket = openSocket();
    		connectToServer(weights);
    }

 
	private void connectToServer(double[] weights)
		{
			try{
				
			    readAndEcho();						// SAMv1.0
			    readAndEcho();						// ?Username
			    writeMessageAndEcho(_user); 		//send username to server
			    readAndEcho(); 						// ?Password
			    writeMessageAndEcho(_password);  	// send password to server
			    readAndEcho(); 						// ?Opponent
			    System.out.println("\tserver opponent = 0");
			    String opponent = _sc.nextLine();
			    writeMessageAndEcho(opponent);  	// opponent
			    
			    _gameID = readAndEcho().substring(5,9); // game 
			    _myColor = readAndEcho().substring(6,11).toLowerCase();  // color
			    int myPiece = 1;
			    
			    	
			    System.out.println("I am playing as "+ _myColor + " in game number "+ _gameID);
			    //Initialize our player
			    ComputerPlayer myPlayer = new ComputerPlayer(weights,_myColor);
			    
			    if(_myColor.equals("black"))
			    	{
			    	myPiece = -1;	
			    	int[] bestMove = myPlayer.getBestMove(_pieces);
			    	readAndEcho();//read move prompt
			    	String toSend = Translate.nativeMoveToServer(bestMove);
			    	System.out.println(toSend);
			    	writeMessageAndEcho(Translate.nativeMoveToServer(bestMove)); //sent: (5:1):(4:0)
			    	_pieces = MoveGenerator.result(myPiece,bestMove, _pieces);
			    	readAndEcho();//read server confirmation of move
			    	_GUI.updatePieces(_pieces);
			    	
			    	}
			    
			    while(true)
			    	{
			    		
						Thread.sleep(500);
			    		//========= read computer move ,translate it to our move, store it back.
						String lastRead = readAndEcho();
						if(lastRead.contains("Result")) //read: Move prompt
			    			{
			    				print("client " + _myColor );
			    				break;
			    			}
			    		_pieces = MoveGenerator.result(-myPiece, Translate.serverMoveToNative(lastRead), _pieces);
			    		_GUI.updatePieces(_pieces);
			    		//=====================================================================
			    		readAndEcho();//prompt for move
			    		//after move prompt prepare our move
			    		int[] bestMove = myPlayer.getBestMove(_pieces);
			    		if(bestMove==null)
			    			break;
			    		_pieces = MoveGenerator.result(myPiece,bestMove, _pieces);
			    		Thread.sleep(500);
				    	String toSend = Translate.nativeMoveToServer(bestMove);
				    	//send client move to server
				    	writeMessageAndEcho(toSend);
				    	//read server ack back
				    	if(readAndEcho().contains("Result")) //read: Move prompt
			    			print("winner determined");
				    	_GUI.updatePieces(_pieces);
			    	}
			    
			    print("terminated");
			    _socket.close();
			       
			    
			} catch  (IOException e) {
				
			    System.out.println("Failed in read/close");
			    System.exit(1);
			} catch (InterruptedException e)
				{
					
					e.printStackTrace();
				}
			
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
     } 
	catch (UnknownHostException e) {  System.out.println("Unknown host: " + _machine);  System.exit(1);  }
	catch  (IOException e) {   e.printStackTrace();     System.exit(1);  }
    return _socket;
  }
    
    //choose which user to connect with
    private void setupCredentials()
		{
			System.out.println("User 1: 5, Pass: 238056\nUser 2: 6, Pass: 993592");
	    	System.out.println("Connect as User 1 or User 2? (Enter 1 or 2)");
	    	if(_sc.nextLine().equals("2"))
	    		{
	    			_user = "6";
	    			_password = "993592";
	    		}
	    	else
	    		System.out.println("connecting with default credentials");	
		}
    

    
    //simple print function
    public static void print(String s)
    	{
    		System.out.println(s);
    	}
    


}