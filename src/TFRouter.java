import java.util.ArrayList;
import java.util.List;

public class TFRouter extends ITFNetworkElement {
    public String name;
    private int numPorts;
    private List<TFPort> ports;
    private List<TFRouterTableEntry> routertable;

    public TFRouter(String name,int numPorts){
        this.name = name;
        this.numPorts = numPorts;
        ports = new ArrayList<TFPort>();
        routertable = new ArrayList<TFRouterTableEntry>();
    }

    public int getNumPorts(){
        return numPorts;
    }

    public void addPort(TFPort newPort){
        //TODO: validate port MAC and IP
        //TODO: validate numPorts and ports.Length
        //TODO: validate port by number
        ports.add(newPort);
    }

    public String toString(){
        String sPorts = "";
        String sTable = "";
        for (TFPort p : ports)
            sPorts += "\n\t"+p.toString();
        for (TFRouterTableEntry entry : routertable)
            sTable+= "\n\t"+entry.toString();
        return "Name:"+name+" Ports:"+numPorts + sPorts +" \n\tRouter Table"+ sTable;
    }

    public TFPort getPortByNumber(int portNumber){
        for (TFPort p : ports)
            if(p.number==portNumber)
                return p;
        return null;
    }

    public void addTableEntry(TFRouterTableEntry newEntry){
        //TODO: validate port?
        routertable.add(newEntry);
    }

    public boolean hasIP(String ip){
        for (TFPort p : ports)
            if(p.getIP().equals(ip))
                return true;
        return false;
    }
}
