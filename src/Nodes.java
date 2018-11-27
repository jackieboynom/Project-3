import java.util.ArrayList;
import java.util.HashMap;

public class Nodes {
    private int nodeId;
    private String hostName;
    private int portNumber;
    private ArrayList<Integer> nodalConnections = new ArrayList<Integer>();
    private boolean discovered = false;
    private ArrayList<Integer> treeNeighbours = new ArrayList<Integer>();
<<<<<<< HEAD
    private HashMap<Integer, Boolean> keys = new HashMap<>();
=======
    private int interRequestDelay;
    private int csExecutionTime;
>>>>>>> 575e7c5c46c7fccce2fb63d7f04b36cf5df15fb1

    public void setNodeId(int id) {
        this.nodeId = id;
    }

    public void setHostName(String i) {
        this.hostName = i;
    }

    public void setPortNumber (int i) {
        this.portNumber = i;
    }

    public void addNodalConnections(int neighbour) {
        this.nodalConnections.add(neighbour);
    }

    public void setDiscovered(boolean value){
        this.discovered = value;
    }

    public void addTreeNeighbours(int num) {
        this.treeNeighbours.add(num);
    }

    public void addKeysTrue(int num) {
        this.keys.put(num, true);
    }

    public void addKeysFalse(int num) {
        this.keys.put(num, false);
    }

    public void removeKeys(int num) {
        this.keys.remove(num);
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public String getHostName() {
        return this.hostName;
    }

    public int getPortNumber() {
        return this.portNumber; 
    }

    public ArrayList<Integer> getNodalConnections() {
        return this.nodalConnections;
    }

    public boolean getDiscovered() {
        return this.discovered;
    }

    public ArrayList<Integer> getTreeNeighbours() {
        return this.treeNeighbours;
    }

<<<<<<< HEAD
    public HashMap<Integer, Boolean> getKeys() {
        return this.keys;
    }
=======
    public int getInterRequestDelay() { return this.interRequestDelay; }

    public int getCsExecutionTime() { return this.csExecutionTime; }
>>>>>>> 575e7c5c46c7fccce2fb63d7f04b36cf5df15fb1
}