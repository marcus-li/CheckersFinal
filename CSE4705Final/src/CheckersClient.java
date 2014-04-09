

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
			    readAndEcho(); // start message
			    readAndEcho(); // ID query
			    String user = _sc.nextLine();
			    writeMessageAndEcho(user); // user ID
			    
			    readAndEcho(); // password query 
			    String pass = _sc.nextLine();
			    writeMessage(pass);  // password

			    readAndEcho(); // opponent query
			    
			    writeMessageAndEcho(_opponent);  // opponent

			    setGameID(readAndEcho().substring(5,9)); // game 
			    setColor(readAndEcho().substring(6,11));  // color
			    System.out.println("I am playing as "+getColor()+ " in game number "+ getGameID());
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



	public static void main(String[] argv){
	String readMessage;
	CheckersClient myClient = new CheckersClient();
	
    }

    public String readAndEcho() throws IOException
    {
	String readMessage = _in.readLine();
	System.out.println("read: "+readMessage);
	return readMessage;
    }

    public void writeMessage(String message) throws IOException
    {
	_out.print(message+"\r\n");  
	_out.flush();
    }
 
    public void writeMessageAndEcho(String message) throws IOException
    {
	_out.print(message+"\r\n");  
	_out.flush();
	System.out.println("sent: "+ message);
    }
			       
    public  Socket openSocket(){
	//Create socket connection, adapted from Sun example
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
    
    
    
    
    
    
    public Socket getSocket(){
    	return _socket;
        }

        public PrintWriter getOut(){
    	return _out;
        }

        public BufferedReader getIn(){
    	return _in;
        }
         
        public void setGameID(String id){
    	_gameID = id;
        }
        
        public String getGameID() {
    	return _gameID;
        }

        public void setColor(String color){
    	_myColor = color;
        }
        
        public String getColor() {
    	return _myColor;
        }
}