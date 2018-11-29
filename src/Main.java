// Members: Helee Thumber (hat170030), Tanushri Singh (tts150030), Ko-Chen (Jack) Chen (kxc170002)
// Project 3

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import java.lang.Integer;

public class Main {

    public static boolean reqCS = false;
    public static boolean isInCS = false;


    public static void main(String[] args) {
        System.out.println();
        //parse config file
        String PATH = System.getProperty("user.dir");
        PATH = PATH + "/CS6378/Project-3/src/config_file.txt";
        Nodes[] array_of_nodes = Parser.parse(PATH);
        int delay = Parser.getD();
        int exTime = Parser.getC();

        //Start building spanning tree and initilize keys
        SpanningTree tree = new SpanningTree(array_of_nodes, Integer.parseInt(args[0]));
        while(Thread.activeCount() > 1){ } //just waiting for spanning tree to finish

        //start servers
        Server server = new Server(array_of_nodes, Integer.parseInt(args[0]));
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //start the requests
        Main app = new Main();
        app.start_app(server, delay, exTime);
    }

    public Main() {

    }

    public void start_app(Server server, int delay, int exTime) {
        cs_enter(server, delay);
    }

    public void cs_enter(Server s, int exTime) {
        reqCS = true;
        System.out.println("Entering CS");
        s.addToQueue();
        if (s.checkForKeys()) {
            isInCS = true;
            System.out.println("This server has all the keys");
            //log time start
            //wait for exTime
            try {
                Thread.sleep(exTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            s.getKeys();
        }
        while (!s.checkForKeys()) {
            //waiting
        }
        //do same stuff in the first if loop
        System.out.println("This server has all the keys");
        try {
            Thread.sleep(exTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cs_leave(Server s, int delay) {
        s.removeFromQueue();
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}