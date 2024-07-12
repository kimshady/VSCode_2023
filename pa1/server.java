import java.net.*;
import java.io.*;

public class server{
    //initialize socket and input, output streams
    private Socket socket = null;
    
    private ServerSocket server = null;

    private BufferedWriter ServerResponse = null;
    private BufferedReader ServerInput = null;
    
    private boolean isNum = true;
    private boolean terminated = false;
    private String message = "Hello!";

    //port constructor
    public server (int port){
        //begin server and await connection
        try {
            server = new ServerSocket(port); //deploy server on port_number
        
            socket = server.accept(); //accept client request
            System.out.println("get connection from ... " + socket.getRemoteSocketAddress().toString());
     
            ServerResponse = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ServerInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            //keep awaiting messages until terminated
            while (!terminated){
                try{
                    
                        ServerResponse.write(message); //send initial output to connected client
                        ServerResponse.newLine();
                        ServerResponse.flush();
                    
                
                    try{
                        String line = ServerInput.readLine(); //read in client's command line arguments
                        String [] args = line.split(" "); //split between command and numbers
                        isNum = true;
                        
                        for (int i = 1; i < args.length; i++){
                            try{
                                Integer.parseInt(args[i]); //make sure command follows format
                            }
                            catch (NumberFormatException x){ //change boolean
                                isNum = false;
                            }
                        }
                        if (args[0].equals("bye")) {//client is exiting
                            message = "receive: -5";
                            System.out.println("get: " + line + ", return -5");
                        } 
                        else if (args[0].equals("terminate")) { //client is terminating
                            message = "receive: -5";
                            terminated = true;
                            System.out.println("get: " + line + ", return -5");
                        } 
                        else if(!isNum){
                            message = "receive: -4";
                            System.out.println("get: " +line+ ", return -4");                          
                        }
                        else if(!args[0].equals("add")&&!args[0].equals("subtract")&&!args[0].equals("multiply")){
                            message = "receive: -1";
                            System.out.println("get: " +line + ", return -1");
                            
                        }
                        else if (args.length < 3){
                            message = "receive: -2";
                            System.out.println("get: " +line+ ", return -2");      
                        }
                        else if (args.length > 5){
                            message = "receive: -3";
                            System.out.println("get: " +line+ ", return -3");                             
                        }
                        else if(args.length >= 3 && args.length <=5 && isNum == true){
                            if (args[0].equals("add")){ // add operand command case
                                int sum = 0;
                                for (int i = 1; i < args.length; i++){
                                    sum += Integer.parseInt(args[i]);
                                }
                                message = "receive: " + sum;
                                System.out.println("get: " + line + ", return: " + sum);
                            } 
                            else if (args[0].equals("subtract")) { // subtract operand command case
                                int difference = Integer.parseInt(args[1]);
                                for (int i = 2; i < args.length; i++){
                                    difference -= Integer.parseInt(args[i]);
                                }
                                message = "receive: " + difference;
                                System.out.println("get: " + line + ", return: " + difference);
                            } 
                            else if (args[0].equals("multiply")) { // multiply operand command case
                                int product = Integer.parseInt(args[1]);
                                for (int i = 2; i < args.length; i++){
                                    product *= Integer.parseInt(args[i]);
                                }
                                message = "receive: " + product;
                                System.out.println("get: " + line + ", return: " + product);     
                            } 
                        }
                        else{
                            message = "receive: -1";
                            System.out.println("get: " + line + ", return -1"); 
                        }
                    }
                    catch (IOException i){
                        System.out.println(i);
                    }
                
                }
                //not terminated, still accepting
                catch(NullPointerException p){
                    
                        socket.close();
                        ServerInput.close();
                        ServerResponse.close();
                
                        socket = server.accept();
                        System.out.println("get connection from ... " + socket.getRemoteSocketAddress().toString());

                        ServerInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        ServerResponse = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    //reset message for new client
                    message = "Hello!";
                }
            }
            System.out.println("Connection closed");
        }
        catch (IOException i){
            System.out.println(i);
        }
    }

    public static void main(String args[]){
        if (args.length != 1){
            System.out.println("Invalid arguments. Required format: 'java server [port_number]'");
        }
        else{ 
            server mmm_yummers = new server (Integer.valueOf(args[0]));
        }    
    }
}

