import java.io.*;
 import java.net.*;
 import java.util.*;

 class UDPserver
   {
public static void main(String args[])throws Exception
{
    System.out.println("Enter Port number !!!");
    Scanner input = new Scanner(System.in);
    int SPort = input.nextInt();
    DatagramSocket srvskt = new DatagramSocket(SPort);
    byte[] data =new byte[1024];
    System.out.println("Enter a full file name to save data to it ?");
    String path = input.next();
    System.out.println("file : "+path+" will be created.");
    FileOutputStream  FOS = new FileOutputStream(path);
    DatagramPacket srvpkt = new DatagramPacket(data,1024);
    System.out.println("listening to Port: "+SPort);
    int Packetcounter=0;//packet counter
    while(true)
       {
           srvskt.receive(srvpkt);
           Packetcounter++;
           String words = new String(srvpkt.getData());
           InetAddress ip= srvpkt.getAddress();
           int port = srvpkt.getPort();
           System.out.println("Packet # :"+Packetcounter+"Received from Host / Port: "+ip+" / "+port);
           FOS.write(data);
           //out16.flush();
           if (Packetcounter >=100)
                 break;

      }
    FOS.close();//releasing file.
    System.out.println("Data has been written to the file !");
  }
}