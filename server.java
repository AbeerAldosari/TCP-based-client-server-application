import java.io.*;  // For DataOutputStream , DataInputStream
import java.net.*; // For ServerSocket , Socket

public class Server {

    // Declare instance variables
    ServerSocket welcomeSocket; // Socket for handshaking process for all clients
    DataOutputStream toClient; // Data transfer to client
    DataInputStream fromClient; // Data received from client

    // Constructor to initialize the server and start listening on a given port number
    public Server(int portNum) throws Exception {
        // Create a new ServerSocket on the given port number
        welcomeSocket = new ServerSocket(portNum);

        // Print server is in a passive mode and is waiting for clients
        System.out.println("Server in a passive mode.");
        System.out.println("Server is listening on port " + portNum);

        // Keep accepting new clients indefinitely
        while (true) {
            System.out.println("Waiting for a client to connect...");

            // Create a new Socket for each client that connects to the server
            Socket clientSocket = welcomeSocket.accept();
            System.out.println("Server accepted a new client.");

            // Keep receiving messages from the client until the connection is closed
            while (!clientSocket.isClosed()) {
                // Create input and output streams for the current client socket
                toClient = new DataOutputStream(clientSocket.getOutputStream());
                fromClient = new DataInputStream(clientSocket.getInputStream());

                // Read the option (1, 2 or 3) from the client
                String option = fromClient.readUTF();

                // Check the option selected by the client
                if (option.equals("1")) {
                    // Send 1 to the client to inform them the number they received is 1
                    toClient.writeUTF("1");
                    System.out.println("Server in open mode.");

                    // Receive the clear message from the client
                    String msg = fromClient.readUTF();
                    System.out.println("The message received from client is: " + msg);

                    // Send back a copy of the clear message
                    toClient.writeUTF(msg);

                } else if (option.equals("2")) {
                    // Send 2 to the client to inform them the number they received is 2
                    toClient.writeUTF("2");
                    System.out.println("Server in secure mode.");

                    // Receive the secure message from the client
                    String secmsg = fromClient.readUTF();
                    System.out.println("The secure message received from client is: " + secmsg);

                    // Send back a copy of the secure message
                    toClient.writeUTF(secmsg);

                } else if (option.equals("3")) {
                    // Close the client socket and break out of the loop
                    System.out.println("Application is closed.");
                    clientSocket.close();
                    break;
                }
            }
        }
    }

    // Main method to create a new Server instance on a given port number
    public static void main(String[] args) throws Exception {
        int portNum = 2233; // Server port number
        Server server = new Server(portNum);
    }
}