import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Nodes {
    private int nodeId;
    private String hostName;
    private int portNumber;
    private ArrayList<Integer> nodalConnections = new ArrayList<Integer>();
    private boolean discovered = false;
    private ArrayList<Integer> treeNeighbours = new ArrayList<Integer>();
    private ConcurrentHashMap<Integer, Boolean> keys = new ConcurrentHashMap<>();
    //private int interRequestDelay;
    //private int csExecutionTime;

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

    public void addKeys(int num, boolean status) {
        this.keys.put(num, status);
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

    public ConcurrentHashMap<Integer, Boolean> getKeys() {
        return this.keys;
    }

    public boolean getKeys(int i) {
        return this.keys.get(i);
    }
}