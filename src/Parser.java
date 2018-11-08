import java.io.*;

public class Parser {
    public static Nodes[] parse (String PATH) {
        File file = new File(PATH);
        BufferedReader bufferedReader = null;
        int n = 0; //numNodes
        int d = 0; //inter-request delay
        int c = 0; //cs-execution time
        int numOfRequest = 0;
        boolean found = false;
        String line;
        int index;
        boolean empty;

        //read numNode n, inter-request delay d, cs-execution time c, #ofrequest
        try {
            //open file
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            
            //get numNodes
            while (!found) {
                line = bufferedReader.readLine();
                empty = false;
                //format line to just have what we want
                if (line.contains("#")) {
                    index = line.indexOf("#");
                    line = line.substring(0, index);
                }
                if (line.isEmpty()) {
                    empty = true;
                }
                line = line.trim();
                //create nodes
                if (!empty) {
                    //parse info
                    index = line.indexOf(" ");
                    n = Integer.parseInt(line.substring(0, index));
                    line = line.substring(index).trim();
                    index = line.indexOf(" ");
                    d = Integer.parseInt(line.substring(0, index));
                    line = line.substring(index).trim();
                    index = line.indexOf(" ");
                    c = Integer.parseInt(line.substring(0, index));
                    line = line.substring(index).trim();
                    numOfRequest = Integer.parseInt(line);
                    found = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //populate node information
        Nodes[] array_of_nodes = new Nodes[n];
        try {
            int valid_lines = 0;
            while (valid_lines != n) {
                line = bufferedReader.readLine();
                empty = false;
                //format line to just have what we want
                if (line.contains("#")) {
                    index = line.indexOf("#");
                    line = line.substring(0, index);
                }
                if (line.isEmpty()) {
                    empty = true;
                }
                line = line.trim();
                //create nodes
                if (!empty) {
                    //parse info
                    Nodes node = new Nodes();
                    index = line.indexOf(" ");
                    node.setNodeId(Integer.parseInt(line.substring(0, index)));
                    line = line.substring(index).trim();
                    index = line.indexOf(" ");
                    node.setHostName(line.substring(0, index) + ".utdallas.edu");
                    line = line.substring(index).trim();
                    node.setPortNumber(Integer.parseInt(line));
                    for (int i = 0; i < n; i++){
                        if (i != node.getNodeId()) {
                            node.addNodalConnections(i);
                        }
                    }
                    array_of_nodes[valid_lines] = node;
                    valid_lines++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array_of_nodes;
    }
}