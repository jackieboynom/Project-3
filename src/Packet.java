import java.io.Serializable;
import java.util.Date;

public class Packet implements Serializable {
    private int broadcastNode;
    private int sourceId;
    private String msg;
    private Date time;

    public void buildPacket(int broadcast, int sourceId, String msg) {
        this.broadcastNode = broadcast;
        this.sourceId = sourceId;
        this.msg = msg;
    }

    public void buildPacket(int sourceId, String msg) {
        this.sourceId = sourceId;
        this.msg = msg;
        this.time = new Date();
    }

    public int getBroadcastNode() {
        return this.broadcastNode;
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public String getMsg() {
        return this.msg;
    }

    public long getTime() {
        return this.time.getTime();
    }

    @Override
    public String toString() {
        return "Message [broadcastNode=" + this.broadcastNode + ", sourceId=" + this.sourceId + ", msg=" + this.msg + ", time=" + this.time + "]";
    }
}