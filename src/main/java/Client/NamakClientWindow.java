package Client;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NamakClientWindow extends JFrame{
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
    public NamakClientWindow () {
        try{
            //connecting to the server
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException ex){
            ex.printStackTrace();
        }
        //Form elements' settings
        setBounds(600, 300, 600, 500);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaTextAreaMessage = new JTextArea();
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
        add(jsp, BorderLayout.CENTER);
        //label to show the amount of clients
        final JLabel jlNumberOfClients = new JLabel("Clients' amount: ");
        add(jlNumberOfClients, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton jbSendMessage = new JButton("Send");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField("Type your message: ");
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);
        jtfName = new JTextField("Enter your name: ");
        bottomPanel.add(jtfName, BorderLayout.WEST);
        //Sending message button handler
        jbSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // send the message if the Message and Name fields are not empty
                if (!jtfMessage.getText().trim().isEmpty() && !jtfName.getText().trim().isEmpty()) {
                    namakClientName = jtfName.getText();
                    sendMsg();
                    // Focus to the message window
                    jtfMessage.grabFocus();
                }
            }
        });
        // While focus the Message field gets clean
        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessage.setText("");
            }
        });
        // While focus the Name field gets clean
        jtfName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfName.setText("");
            }
        });
        // Work with the Server in a different stream
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //inf cycle
                    while (true) {
                        // If there is an input message
                        if (inMessage.hasNext()) {
                            String inMes = inMessage.nextLine();
                            String clientsInChat = "CLients in chat = ";
                            if (inMes.indexOf(clientsInChat) == 0) {
                                jlNumberOfClients.setText(inMes);
                            } else {
                                // message output
                                jtaTextAreaMessage.append(inMes);
                                jtaTextAreaMessage.append("\n");
                            }
                        }
                    }
                } catch (Exception ex) {
                }
            }
        }).start();

        // Adding the client window closing handler
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    // Name field is not empty and not equals to default value
                    if (!namakClientName.isEmpty() && namakClientName != "Введите ваше имя: ") {
                        outMessage.println(namakClientName + " вышел из чата!");
                    } else {
                        outMessage.println("Участник вышел из чата, так и не представившись!");
                    }
                    // send a service message that the client have left the server
                    outMessage.println("##session##end##");
                    outMessage.flush();
                    outMessage.close();
                    inMessage.close();
                    clientSocket.close();
                } catch (IOException exc) {

                }
            }
        });
        // form display
        setVisible(true);
    }

    // message sending
    public void sendMsg() {
        // forming the message for sending it to the server
        String messageStr = jtfName.getText() + ": " + jtfMessage.getText();
        // sending the message
        outMessage.println(messageStr);
        outMessage.flush();
        jtfMessage.setText("");
    }
}
