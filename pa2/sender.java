import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.NumberFormatException;
import java.net.*;
import java.io.*;


public class sender {
    //sender var
    InetAddress url;
    int portNumber;
    String fileName;
    Socket socket;
    BufferedReader reader;
    BufferedReader inFromNetwork;
    DataOutputStream outToNetwork;
    int sequenceNumber;
    int id;
    int packetsSent;
    boolean resending;
    //proj var----------
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
    //------------------
    public sender(InetAddress url, int portNumber, String fileName) throws IOException {
        this.url = url;
        this.portNumber = portNumber;
        this.fileName = fileName;
        this.socket = new Socket(this.url, this.portNumber);
        this.outToNetwork = new DataOutputStream(socket.getOutputStream());
        this.inFromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.id = 0;
        this.sequenceNumber = 0;
        this.resending = false;
        try {
        this.reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
        System.out.println(e.getMessage());
        }
    }
    private void run() throws IOException {
        String file = readFile(reader);
        if (VERBOSE) {
        System.out.println("File: " + file);
        }
        String[] messages = splitString(file, "\\.");
        for (String message : messages) {
        message += ".";
        sendMessage(message);
        }
    }

    private String receiveAck(BufferedReader reader) throws IOException {
        String ack = "";
        ack = receivePacket(reader);
        if (ack == null) {
        return null;
        }
        return ack;
    }

    private String readFile(BufferedReader reader) throws IOException {
        String file = "";
        String line = "";
        boolean keepRunning = true;
        while (keepRunning) {
        line = reader.readLine();;
        if (line == null) {
            line = "-1";
            keepRunning = false;
        }
        file += line;
        }
        return file;
    }

    private void sendMessage(String message) throws IOException {
        if (VERBOSE) {
        System.out.println("\nSending message: " + message);
        }
        String[] packets = splitString(message, " ");
        for (int i = 0; i < packets.length; i++) {
            if (packets[i].equals("")) {
                continue;
            }
            boolean success = false;
            String ack = "";
            while (!success) {
                sendPacket(packets[i], outToNetwork);
                ack = receiveAck(inFromNetwork);
                success = checkAck(ack);
                printUpdate(ack, success);
                if (!success) {
                    resending = true;
                    getSequenceNumber();
                    id--;
                }
            }
        }
    }

    private void printUpdate(String packet, boolean yes) {
        String[] parsedResponse = splitString(packet, " ");
        String output = "";
        output += "Waiting ACK";
        if (yes) {
        output += "0 ";
        } else {
        if (packet.contains("DROP")) {
            output += "2 ";
        } else {
            output += "1 ";            
        }
        }
        output += packetsSent + " ";
        output += parsedResponse[2] + " ";
        if (!yes) {
        output += "resend packet" + parsedResponse[0];
        } else {
        output += "send packet" + sequenceNumber;
        }
        System.out.println(output);
    }

    private boolean checkAck(String ack) {
        String pass = getAckString(PASS);
        if (ack.contains(pass)) {
        return true;
        }
        return false;
    }

    private String receivePacket(BufferedReader reader) throws IOException {
        String response = reader.readLine();
        return response;
    }

    private void sendPacket(String packet, DataOutputStream stream) throws IOException {
        packet = createPacket(packet, getSequenceNumber(), getId());
        packetsSent++;
        stream.writeBytes(packet + '\n');
        if (VERBOSE) { System.out.println("Sending packet: " + packet); }
    }

    private int getSequenceNumber() {
        int number = sequenceNumber;
        sequenceNumber = (sequenceNumber + 1) % 2;
        return number;
    }

    private int getId() {
        return id++;
    }

    public void start() throws IOException {
        run();
    }
public static void main(String[] args) {
    System.out.println("Starting Sender");
    try {
        sender sender = new sender(
        InetAddress.getByName(args[0]),
        Integer.parseInt(args[1]),
        args[2]);
        sender.start();
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

