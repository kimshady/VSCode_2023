import java.net.*;
import java.io.*;

public class client{
    private Socket socket = null;
    
    private BufferedReader ClientLine = null;
    private BufferedWriter ClientRequest = null;

    private BufferedReader ServerResponse = null;

    private boolean terminated = false;
    private String output = null;


    public client(String address, int port){
        //start connection
        try{
            socket = new Socket(address, port); 

            ClientLine = new BufferedReader(new InputStreamReader(System.in)); //User command line
            ClientRequest = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //client's request to server
            
            ServerResponse = new BufferedReader(new InputStreamReader(socket.getInputStream())); //read server's response

            output = ServerResponse.readLine();
            //check server's response
            if (output.contains("-5")){
                System.out.println("receive: exit.");
                terminated = true;
            }
            else if (output.contains("-1")) System.out.println("receive: incorrect operation command.");
            else if (output.contains("-2")) System.out.println("receive: number of inputs is less than two.");
            else if (output.contains("-3")) System.out.println("receive: number of inputs is more than four.");
            else if (output.contains("-4")) System.out.println("receive: one or more of the inputs contain(s) non-number(s).");
            
            else System.out.println(output);
            
            while (!terminated){
                String line = "";
                
                line = ClientLine.readLine(); 
                ClientRequest.write(line);
                ClientRequest.newLine();
                ClientRequest.flush();
                
                output = ServerResponse.readLine();
                //check server's response
            if (output.contains("-5")){
                System.out.println("receive: exit.");
                terminated = true;
            }
            else if (output.contains("-1")) System.out.println("receive: incorrect operation command.");
            else if (output.contains("-2")) System.out.println("receive: number of inputs is less than two.");
            else if (output.contains("-3")) System.out.println("receive: number of inputs is more than four.");
            else if (output.contains("-4")) System.out.println("receive: one or more of the inputs contain(s) non-number(s).");
            
            else System.out.println(output);

            
            }
            //close connections
            ClientLine.close();
            ClientRequest.close();
            socket.close();
        }
        catch (UnknownHostException uhe){
            System.out.println(uhe);
        }
        catch (IOException ioe){
            System.out.println(ioe);
        }
    }

    public static void main (String args []){
        if (args.length != 2){
            System.out.println("Invalid arguments. Required format: 'java client [serverURL] [port_number]'");
        }
        else{
            client hungy = new client (args[0], Integer.valueOf(args[1])); //create the client
        }
    }
}