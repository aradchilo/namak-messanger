package Client;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NamakClientWindow {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 3443;

    private Socket clientSocket; //client socket
    private Scanner inMessage;
    private PrintWriter outMessage;

    //Form elements
    private JTextField jtfMessage;
    private JTextField jtfName;
    private JTextArea jtaTextAreaMessage;

    private String namakClientName = ""; //client name
    //getting client name
    public String getNamakClientName() {
        return this.namakClientName;
    }

    //Constructor
}
