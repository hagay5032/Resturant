package clientServerResturant;

import java.net.*;
import java.util.Scanner;
import java.io.*;

/**
 * This class is for creating a server that will listen to many clients.
 * Any clients that connect to the server need that the first line of his packet
 * will be one of the two options - 
 * 1) "GET_MENU"- it tells the server to send the menu back to the client.
 * 2) "SEND_ORDER"- it tells the server that the client is going to send an 
 * order. Each client is managed by a different thread- and hence this server 
 * can listening to multiple clients. 
 * 
 * @author Hagay Enoch
 * @version 8.12.2019
 *  
 */
public class Server 
{
	public static volatile int connectionNum = 0; // numerate the connections
	
    public static void main(String[] args)
    {
        ServerSocket srv = null;
        boolean listening = true;
        int port = 0;
        try 
        {
        	// reading the port from the configuration file
        	URL url = Server.class.getResource("serverConfig.config");
        	Scanner scan = new Scanner(new File(url.getPath()));
        	port = scan.nextInt();
        	scan.close();
        	
        	//initializing the server to listen on port 3334
            srv = new ServerSocket(port);

            System.out.println("Server's ready");
            Socket socket = null;
            
            while(listening)
            { 
                socket = srv.accept(); /* wait until client socket ask to 
                							connect on this port.*/
                new ServerThread(socket, connectionNum).start();
                connectionNum++;
            }         
        }
        catch(InterruptedIOException e)
        {
            System.out.println("Time out");
            e.printStackTrace();
        }
        catch(IOException e)
        { 
            e.printStackTrace();
        }
    } 
}


