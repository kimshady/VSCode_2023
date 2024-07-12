import java.lang.NumberFormatException;
import java.net.*;
import java.io.*;
import java.util.*;

public class network {
    //network var
    int portNumber;
    ServerSocket socket;
    Socket receiverSocket;
    Socket senderSocket;
    BufferedReader inFromSender;
    BufferedReader inFromReceiver;
    DataOutputStream outToSender;
    DataOutputStream outToReceiver;
    boolean keepRunning;
    Random rand;

    //projvar--------
    public static boolean VERBOSE = false;
    public static int PASS = 0;
    public static int CORRUPT = 1;
    public static int DROP = 2;
    public static int ACK0 = PASS;
    public static int ACK1 = CORRUPT;
    public static int ACK2 = DROP;

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

    public static String getAckString(int ack) {
        switch (ack) {
            case 0: 
                return "PASS";
            case 1:  
                return "CORRUPT";
            case 2:  
                return "DROP";
            default:
                return "DROP";
        }
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
    //---------------

    public network(int portNumber) throws IOException {
        this.portNumber = portNumber;
        this.socket = new ServerSocket(this.portNumber);
        this.keepRunning = true;
        this.rand = new Random();
    }
    private void run() throws IOException {
        waitForReceiver();
        waitForSender();
        relay();
        end();
    }

    private void relay() throws IOException {
        String packet = "";
        boolean sent = true;
        while(keepRunning) {
            if (packet == null) {continue;}
            if (sent) {
                packet = receivePacket(inFromSender);
                if (packet.contains("DROP")) {
                sendPacket(packet, outToSender);
                System.out.println("ACK2 DROP");
                continue;
            }
            sendPacket(packet, outToReceiver);
            }
            else {
                packet = receivePacket(inFromReceiver);
                if (packet.contains("-1")) {keepRunning = false;}
                sendPacket(packet, outToSender);
            }
            sent = !sent;
        }
    }

    private String receivePacket(BufferedReader reader) throws IOException {
        String response = reader.readLine();
        String[] parsedResponse = splitString(response, " ");
        String output = "";
        if (response.contains("-1")) {
            return response;
        }
        if (parsedResponse.length == 4) {
            response = randomPacket(response);
            output += "Packet" + parsedResponse[0];
            output += " " + parsedResponse[1];
            String temp = checkPacket(response);
            String[] split = splitString(temp, " ");
            temp = split[2];
            output += " " + temp;
            System.out.println(output);
        } 
        else if (parsedResponse.length == 3) {
            output += "ACK" + parsedResponse[0];
            output += " " + parsedResponse[2];
            System.out.println(output);
        }
        return response;
    }

    private String checkPacket(String packet) throws IOException {
        String[] split = packet.split(" ");
        String ack = getAckString(CORRUPT);
        if (split.length == 4) {
            int sum = getCheckSum(split[3]);
            if (sum == Integer.parseInt(split[2])) {
                ack = getAckString(PASS);
            }
            else {
                ack = getAckString(CORRUPT);
            }
        }
        ack = createAck(ack, Integer.parseInt(split[0]));
        if (packet.contains("DROP")) {
            ack = split[0] + " ";
            ack += getCheckSum("DROP");
            ack += " DROP";
        }
        if (packet.contains("-1")) {
            ack += " -1";
        }
        return ack;
    }

    private double randomNumber() {
        return rand.nextDouble();
        }

        private String randomPacket(String packet) throws IOException {
        double number = randomNumber();
        if (number < 0.5) {
            return packet;
        } else if (number >= 0.5 && number < 0.75) {
            String[] split = splitString(packet, " ");
            packet = "";
            int check = Integer.parseInt(split[2]);
            check++;
            split[2] = Integer.toString(check);
            String temp = "";
            for (int i = 0; i < split.length - 1; i++) {
                packet += split[i] + " ";
                temp = split[i+1];
            }
            packet += temp;
            return packet;
        } else {
            String[] split = splitString(packet, " ");
            packet = "";
            packet += split[0] + " ";
            packet += getCheckSum("DROP");
            packet += " DROP";
        }
        return packet;
        }

        private void sendPacket(String packet, DataOutputStream stream) throws IOException {
            stream.writeBytes(packet + '\n');
            if (VERBOSE) {System.out.println("Sending packet: " + packet); }
        }

        private void waitForReceiver() throws IOException {
            receiverSocket = socket.accept();
            inFromReceiver = new BufferedReader(new InputStreamReader(receiverSocket.getInputStream()));
            outToReceiver = new DataOutputStream(receiverSocket.getOutputStream());

            if (VERBOSE) {System.out.println("Connected to Receiver."); }
        }

        private void waitForSender() throws IOException {
            senderSocket = socket.accept();
            inFromSender = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
            outToSender = new DataOutputStream(senderSocket.getOutputStream());

            if (VERBOSE == true) {System.out.println("Connected to Sender."); }
        }

        public void start() throws IOException {run();}

        public void end() throws IOException {
            inFromSender.close();
            inFromReceiver.close();
            outToSender.close();
            outToReceiver.close();
        }
    public static void main(String[] args) {
        System.out.println("Starting Network");
        try {
            network network = new network(
            Integer.parseInt(args[0]));
            network.start();
        } 
        catch (NumberFormatException e) {
            System.out.println("Please use a valid port number");
            e.printStackTrace();
        } 
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
}