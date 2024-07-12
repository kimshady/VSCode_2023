import java.io.*;
import java.net.*;
import java.util.*;
public class UDPclient
{
static InetAddress dest;
public static void main(String [] args) throws Exception
{

    DatagramSocket clskt = new DatagramSocket();
    Scanner input = new Scanner (System.in);
    int port =input.nextInt();
    System.out.println("Enter Destination Host name");
    String hostname=input.next();
    dest.getByName(hostname);
    int packetcount=0;
    System.out.println("Enter The path of the file you want to send");
    String path = input.next(); 
    File initialFile = new File(path);
            FileInputStream targetStream = new FileInputStream(initialFile);
    int filesize=targetStream.available();
    int neededpackets =(int)Math.ceil((double)(filesize/1024));
     byte [] data= new byte[1024];
     // counting bytes
     for (int i=0;i<1024;i++)
     {
         data[i]=(byte)targetStream.read();
     }
     //create a packet
    DatagramPacket clpkt=new DatagramPacket(data,data.length,dest,port);
    packetcount++;
    clskt.send(clpkt);
    if(packetcount >neededpackets)
        clskt.close();
   }

 }