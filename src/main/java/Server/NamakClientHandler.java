package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class NamakClientHandler implements Runnable {
    private NamakServerSide namakServerSide; //server object
    private PrintWriter outMessage; //outgoing message
    private Scanner inMessage; //incoming message
    private static final String HOST = "localhost";
    private static final int PORT = 3443;
    private Socket clientSocket = null;
    private static int namakClientCount = 0; //amount of clients

    //constructor to get client socket and server
    public NamakClientHandler(Socket socket, NamakServerSide server){
        try{
            namakClientCount++;
            this.namakServerSide = server;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try{
            while (true){
                //server sends messages to clients
                namakServerSide.sendMessagetoAllClients("New user joined the chat!");
                namakServerSide.sendMessagetoAllClients("User amount: " + namakClientCount);
                break;
            }
            while (true){
                //when a client sends a message
                if (inMessage.hasNext()) {
                    String namakClientMessage = inMessage.nextLine();
                    if (namakClientMessage.equalsIgnoreCase("##session##end##")) {
                        break;
                    }
                    System.out.println(namakClientMessage); //test message output to console
                    namakServerSide.sendMessagetoAllClients(namakClientMessage);
                    Thread.sleep(100); //stream stop
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            this.namakClose();
        }
    }
    //senging a message
    public void sendMsgtoClients(String msg){
        try{
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Client leaves the chat
    public void namakClose() {
        //deleting client from the list
        namakServerSide.removeNamakClient(this);
        namakClientCount--;
        namakServerSide.sendMessagetoAllClients("User amount: " + namakClientCount);
    }
}