import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.NumberFormatException;
import java.net.*;
import java.io.*;


public class receiver {
    //receiver var
    InetAddress url;
    int portNumber;
    Socket socket;
    BufferedReader inFromNetwork;
    DataOutputStream outToNetwork;
    boolean energizer;
    int packetsReceived;

    //projvar-------
    public static boolean VERBOSE = false;
    public static int PASS = 0;
    public static int CORRUPT = 1;
    public static int DROP = 2;
    public static int ACK0 = PASS;
    public static int ACK1 = CORRUPT;
    public static int ACK2 = DROP;
 
    public static String getAckString(int ack) {
       switch (ack) {
             case 0:  return "PASS";
             case 1:  return "CORRUPT";
             case 2:  return "DROP";
             default:
                      return "DROP";
         }
    }
 
    public static String createAck(String message, int sequenceNumber) {
       if (message.equals("-1")) {
          return message;
       }
 
       String ack = "";
       ack += sequenceNumber + " ";
       ack += getCheckSum(message) + " ";
       ack += message;
       return ack;
    }
 
    public static String createPacket(String message, int sequenceNumber, int id) {
       if (message.equals("-1")) {
          return message;
       }
 
       String packet = "";
       packet += sequenceNumber + " ";
       packet += id + " ";
       packet += getCheckSum(message) + " ";
       packet += message;
       return packet;
    }
 
    public static int getCheckSum(String packet) {
       int checkSum = 0;
       for (int i = 0; i < packet.length(); i++) {
          char letter = packet.charAt(i);
          checkSum += letter;
       }
       return checkSum;
    }
 
    public static String[] splitString(String line, String splitOn) {
       if (splitOn.equals(".")) {
          splitOn = "\\.";
       }
       return line.split(splitOn);
    }
    //--------------

    public receiver(InetAddress url, int portNumber) throws IOException {
        this.energizer = true;
        this.url = url;
        this.portNumber = portNumber;
        this.socket = new Socket(url, portNumber);
        this.inFromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outToNetwork = new DataOutputStream(socket.getOutputStream());
        this.packetsReceived = 0;
    }

    private void run() throws IOException {
        receive();
    }

    private void receive() throws IOException {
        while (energizer) {
        String packet = receivePacket(inFromNetwork);
        if (packet == null) {

        } else {
            if (packet.contains("-1")) {
                energizer = false;
            }
            String ack = checkPacket(packet);
            sendPacket(ack, outToNetwork);
        }
        }
    }

    private String checkPacket(String packet) throws IOException {
        String[] split = packet.split(" ");
        String ack = getAckString(CORRUPT);
        if (split.length == 4) {
        int sum = getCheckSum(split[3]);
        if (sum == Integer.parseInt(split[2])) {
            ack = getAckString(
            PASS);
        } else {
            ack = getAckString(
            CORRUPT);
        }
        }
        ack = createAck(ack, Integer.parseInt(split[0]));
        if (packet.contains("-1")) {
        ack += " -1";
        }
        return ack;
    }

    private String receivePacket(BufferedReader reader) throws IOException {
        String response = reader.readLine();
        packetsReceived++;
        String[] parsedResponse = splitString(response, " ");
        String output = "";
        output += "Waiting ";
        output += parsedResponse[0] + " ";
        output += packetsReceived + " ";
        output += "(" + response + ")" + " ";
        //output += checkPacket(response);
        String ack = checkPacket(response);
        String[] split = splitString(ack, " ");
        ack = split[2];
        output += ack;
        System.out.println(output);
        return response;
    }

    private void sendPacket(String packet, DataOutputStream stream) throws IOException {
        stream.writeBytes(packet + '\n');
        if (VERBOSE) { System.out.println("Sending packet: " + packet); }
    }

    public void start() throws IOException {
        run();
    }
public static void main(String[] args) {
    System.out.println("Starting Receiver");
    try {
        receiver receiver = new receiver(
        InetAddress.getByName(args[0]),
        Integer.parseInt(args[1]));
        receiver.start();
    } catch (UnknownHostException e) {
        System.out.println("Please use a valid host address");
        System.out.println(e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Please use a valid port number");
        e.printStackTrace();
    } catch(Exception e) {
        e.printStackTrace();
    }
}
}