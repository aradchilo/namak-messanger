package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NamakServerSide {
    static final int PORT = 3443; //port for the server
    private ArrayList<NamakClientHandler> namakConnectedClients = new ArrayList<NamakClientHandler>(); // list of clients connected to the server
    public NamakServerSide(){
        Socket clientSocket = null; // client socket is a stream to connect the server by the adress and the port
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(PORT); // server socket for a certain port
            System.out.println("Server is on!");
            // Infinite cycle to connect to the server
            while(true){
                clientSocket = serverSocket.accept(); // waiting for connecting
                NamakClientHandler namakClient = new NamakClientHandler(clientSocket, this); // connected to the server handler
                namakConnectedClients.add(namakClient);
                new Thread(namakClient).start(); // to handle each connection in a new stream
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // sending a message to all connected clients
    public void sendMessagetoAllClients(String namakMsg) {
        for (NamakClientHandler o : namakConnectedClients){
            o.sendMsgtoClients(namakMsg);
        }
    }
    // removing disconnected client
    public void removeNamakClient (NamakClientHandler client){
        namakConnectedClients.remove(client);
    }
}