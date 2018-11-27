// Members: Helee Thumber (hat170030), Tanushri Singh (tts150030), Ko-Chen (Jack) Chen (kxc170002)
// Project 3

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import java.lang.Integer;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        //parse config file
        String PATH = System.getProperty("user.dir");
        PATH = PATH + "/CS6378/Project-3/src/config_file.txt";
        Nodes[] array_of_nodes = Parser.parse(PATH);


        //Start building spanning tree and initilize keys
        SpanningTree tree = new SpanningTree(array_of_nodes, Integer.parseInt(args[0]));

        //start server with array_of_nodes[args[0]], args[0] is passed in through launcher.sh
        //Server server = new Server(array_of_nodes, Integer.parseInt(args[0]));

        //start the requests
        cs_enter(server);
    }

    public void start_app() {
        cs_enter(server)
    }

    public static void cs_enter(Server s) {

    }

    public void cs_leave() {

    }
}