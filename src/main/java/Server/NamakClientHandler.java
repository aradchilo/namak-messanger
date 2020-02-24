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
            this.inMessage = new PrintWriter(socket.getInputStream());
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

                }
            }
        }
    }
}