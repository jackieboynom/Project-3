import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    //set up variables
    private Nodes[] array_of_nodes;
    private int serverNum;
    private ArrayList<Packet> queue;

    //Set up messages
    String reqMsg = "REQ"; //requesting cs section
    String ackMsg = "ACK";
    String nackMsg = "NACK";

    Server (Nodes[] array_of_nodes, int serverNum) {
        this.array_of_nodes = array_of_nodes;
        this.serverNum = serverNum;
        this.queue = new ArrayList<>();
        Thread t = new Thread(this);
        t.start();
    }

    public void sendPacket(Socket outSocket, ObjectOutputStream out, int broadcast, int source, String message, int dest) {
        try {
            Packet packet = new Packet();
            packet.buildPacket(broadcast, source, message);
            System.out.println("Packet to be sent: " + packet + ", to: " + dest);
            outSocket = new Socket(array_of_nodes[dest].getHostName(), array_of_nodes[dest].getPortNumber());
            out = new ObjectOutputStream(outSocket.getOutputStream());
            out.writeObject(packet);
            out.flush();
            out.close();
            outSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //initialize socket and input/output stream
        ServerSocket server = null;
        Socket inSocket = null;
        Socket outSocket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;

        //set up variables
        int serverPort = this.array_of_nodes[serverNum].getPortNumber();
        String serverHostname = this.array_of_nodes[serverNum].getHostName();

        //broadcast to every node
        int messagesToSend = 5;
        try {
            boolean finished = false;
            server = new ServerSocket(serverPort);
            System.out.println("Started at Host: " + serverHostname + " Port: " + serverPort);

            do {
                System.out.println("Server: Waiting for a messages ...");
                inSocket = server.accept();
                System.out.println("Server: Packet received!");
                in = new ObjectInputStream(inSocket.getInputStream());
                Packet packet = (Packet) in.readObject();
                System.out.println("Server: Packet Received - " + packet);
                in.close();

                /*//get info out of msg
                int broadcastSource = packet.getBroadcastNode();
                int source = packet.getSourceId();
                String msg = packet.getMsg();

                //if record at broadcastSource == 0 then it means its a new message
                if (!msg.equals(ackMsg)) {
                    records[broadcastSource][0] = source;
                    if (array_of_nodes[serverNum].getTreeNeighbours().size() == 1) {
                        //send ack back to source if neighbour list is 1
                        sendPacket(outSocket, out, broadcastSource, serverNum, ackMsg, source);
                    } else {
                        //continue broadcast message
                        for (int i = 0; i < array_of_nodes[serverNum].getTreeNeighbours().size(); i++) {
                            int dest = array_of_nodes[serverNum].getTreeNeighbours().get(i);
                            if (dest != source) {
                                sendPacket(outSocket, out, broadcastSource, serverNum, msg, dest);
                                records[broadcastSource][1] += 1;
                            }
                        }
                    }
                } else if (msg.equals(ackMsg)) {
                    records[broadcastSource][1] -= 1;
                    if (records[broadcastSource][1] == 0 && broadcastSource != serverNum) {
                        //send ack since all replies recieved
                        sendPacket(outSocket, out, broadcastSource, serverNum, ackMsg, records[broadcastSource][0]);
                        System.out.println("Send ACK to parent: " + records[broadcastSource][0] + ". For server: " + serverNum);
                    }
                }*/
            } while (!finished);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean checkForKeys() {
        Nodes node = array_of_nodes[serverNum];
        synchronized (node) {
            for (int i : node.getTreeNeighbours()) {
                if (node.getKeys().get(i) == false) {
                    return false;
                }
            }
            return true;
        }
    }

    public void getKeys() {
        Packet m = new Packet();
        m.buildPacket(serverNum, serverNum, reqMsg);
        //send msg to all servers with false key
        
    }

    public void addToQueue() {
        Packet m = new Packet();
        m.buildPacket(serverNum, serverNum, reqMsg);
        queue.add(m);
    }

    public void removeFromQueue() {

    }
}
