// Members: Helee Thumber (hat170030), Tanushri Singh (tts150030), Ko-Chen (Jack) Chen (kxc170002)
// Project 3

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.Integer;

public class Main {

    static volatile boolean reqCS = false;
    static volatile boolean isInCS = false;
    static volatile boolean hasAllKeys = false;
    public static int serverNum = 0;
    public static String PATH = System.getProperty("user.dir");

    PrintWriter out;
    public static String logPath;

    public static void main(String[] args) {
        //parse config file
        String configPath = PATH + "/CS6378/Project-3/src/config_file.txt";
        Nodes[] array_of_nodes = Parser.parse(configPath);

        //initilize variables
        int delay = Parser.getD();
        int exTime = Parser.getC();
        int numOfRequest = Parser.getNumOfRequest();
        serverNum = Integer.parseInt(args[0]);
        logPath = PATH + "/CS6378/Project-3/csTimes.log";

        //Start building spanning tree and initilize keys
        SpanningTree tree = new SpanningTree(array_of_nodes, serverNum);
        while(Thread.activeCount() > 1){ } //just waiting for spanning tree to finish

        //start servers
        Server server = new Server(array_of_nodes, serverNum);
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //start the requests
        Main app = new Main();
        app.start_app(server, delay, exTime, numOfRequest);
    }

    public Main() {
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("File Created");
        out.close();
    }

    public void start_app(Server server, int delay, int exTime, int numOfRequest) {
        for (int i = 0; i < numOfRequest; i++) {
            System.out.println("Application: This is request #" + i);
            System.out.println("Application: This server is calling cs_enter()");
            cs_enter(server, exTime, delay);
            System.out.println("Application: This server is calling cs_leave()");
            cs_leave(server);
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cs_enter(Server s, int exTime, int delay) {
        s.addToQueue();
        reqCS = true;
        if (s.checkForKeys()) {
            isInCS = true;
            hasAllKeys = true;
            System.out.println("Application: This server has all the keys");
            try {
                //log time start
                out = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)));
                out.println();
                out.append(System.currentTimeMillis() + ":" + serverNum + "-Start");
                out.println();
                out.close();

                //start execution
                System.out.println("Application: Executing...");

                //wait for exTime
                Thread.sleep(exTime);
                System.out.println("Application: This server has finished executing cs");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else {
            System.out.println("Application: This server does not have all the keys");
            s.printKeys();
            s.getKeys();
        }
        while (!s.checkForKeys()) {
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println("Application: waiting for keys"); //waiting
        }
        //do same stuff in the first if loop
        isInCS = true;
        hasAllKeys = true;
        System.out.println("Application: This server has all the keys");
        try {
            //log time start
            out = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)));
            out.println();
            out.append(System.currentTimeMillis() + ":" + serverNum + "-Start");
            out.println();
            out.close();

            //start execution
            System.out.println("Application: Executing...");

            //wait for exTime
            Thread.sleep(exTime);
            System.out.println("Application: This server has finished executing cs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cs_leave(Server s) {
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)));
            out.append(System.currentTimeMillis() + ":" + serverNum + "-End");
            out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.close();

        isInCS = false;
        reqCS = false;
        s.removeFromQueue();
    }
}