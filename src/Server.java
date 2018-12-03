import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.Queue;

public class Server extends Thread {
    //set up variables
    private Nodes[] array_of_nodes;
    private int serverNum;
    private Queue<Packet> queue;
    private Packet myRequest = null;

    //Set up messages
    String reqMsg = "REQ"; //requesting cs section
    String sendKey = "sendKey"; //sending key to each other

    //initialize socket and input/output stream
    ServerSocket server = null;
    Socket inSocket = null;
    Socket outSocket = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

    Server (Nodes[] array_of_nodes, int serverNum) {
        this.array_of_nodes = array_of_nodes;
        this.serverNum = serverNum;
        this.queue = new ConcurrentLinkedQueue<>();
        Thread t = new Thread(this);
        t.start();
    }

    public void sendPacket(int source, String message, int dest) {
        try {
            Packet packet = new Packet();
            packet.buildPacket(source, message);
            System.out.println("Server: Packet to be sent: " + packet + ", to: " + dest);
            Socket outSocket = new Socket(array_of_nodes[dest].getHostName(), array_of_nodes[dest].getPortNumber());
            ObjectOutputStream out = new ObjectOutputStream(outSocket.getOutputStream());
            out.writeObject(packet);
            out.flush();
            out.close();
            outSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //set up variables
        int serverPort = this.array_of_nodes[serverNum].getPortNumber();
        String serverHostname = this.array_of_nodes[serverNum].getHostName();

        try {
            boolean finished = false;
            server = new ServerSocket(serverPort);
            System.out.println("Server: Started at Host: " + serverHostname + " Port: " + serverPort);

            do {
                System.out.println("Server: Waiting for a messages ...");
                inSocket = server.accept();
                System.out.println("Server: Packet received!");
                in = new ObjectInputStream(inSocket.getInputStream());
                Packet packet = (Packet) in.readObject();
                System.out.println("Server: Packet Received - " + packet);
                in.close();

                //get reqMsg, check if own application is in crit section, if not send key to requestor, if yes, add to queue and wait until cs_leave
                if (packet.getMsg().equals(reqMsg)) {
                    if (Main.isInCS) {
                        //then add to queue and wait
                        queue.add(packet);
                        System.out.println("Server: Is in CS, Adding req to queue");
                    } else if (Main.reqCS) {
                        if (myRequest.getTime() > packet.getTime()) {
                            //send key
                            array_of_nodes[serverNum].addKeys(packet.getSourceId(), false);
                            sendPacket(serverNum, sendKey, packet.getSourceId());
                            Main.hasAllKeys = false;
                            System.out.println("Server: not in cs, req later than other req, sending key to " + packet.getSourceId());

                            //send req
                            sendPacket(serverNum, reqMsg, packet.getSourceId());
                            System.out.println("Server: Requesting key back from: " + packet.getSourceId());
                        } else {
                            queue.add(packet);
                            System.out.println("Server: Own request earlier than new request, adding to queue");
                        }
                    } else {
                        //send key
                        sendPacket(serverNum, sendKey, packet.getSourceId());
                        array_of_nodes[serverNum].addKeys(packet.getSourceId(), false);
                        Main.hasAllKeys = false;
                        System.out.println("Server: not in cs, no req, sending requested key");
                    }
                } else if (packet.getMsg().equals(sendKey)) {
                    array_of_nodes[serverNum].addKeys(packet.getSourceId(), true);
                    System.out.println("Server: We have recieved a key from " + packet.getSourceId());
                }
            } while (!finished);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToQueue() {
        Packet m = new Packet();
        m.buildPacket(serverNum, reqMsg);
        queue.add(m);
        myRequest = m;
        System.out.println("Server: Request added to queue");
    }

    public synchronized boolean checkForKeys() {
        Nodes node = array_of_nodes[serverNum];
        //System.out.println(node.getKeys());
        synchronized (node) {
            for (int i : node.getTreeNeighbours()) {
                if (node.getKeys(i) == false) {
                    return false;
                }
            }
            return true;
        }
    }

    public void getKeys() {
        //send msg to all servers with false key
        System.out.println("Server: Requesting missing keys");
        Nodes node = array_of_nodes[serverNum];
        for (int i : node.getTreeNeighbours()) {
            if (node.getKeys(i) == false) {
                sendPacket(serverNum, reqMsg, i);
                System.out.println("Server: From: " + i);
            }
        }
    }

    public void removeFromQueue() {
        for (Packet p : queue) {
            if (p.getSourceId() != serverNum) {
                array_of_nodes[serverNum].addKeys(p.getSourceId(), false);
                sendPacket(serverNum, sendKey, p.getSourceId());
                System.out.println("Server: Key has been sent to " + p.getSourceId());
                queue.remove(p);
                System.out.println("Server: Request from " + p.getSourceId() + " has been removed from queue.");
            } else {
                queue.remove(p);
                myRequest = null;
                System.out.println("Server: Own request has been removed from queue");
            }
        }
    }

    public void printKeys() {
        System.out.println(array_of_nodes[serverNum].getKeys());
    }
}
