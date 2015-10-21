public class TFRouterTableEntry {
    public String netDest;
    //TODO: create a object and store the IP in another String
    public String nextHop;
    public TFPort port;

    public TFRouterTableEntry(){
    }

    public String toString(){
        return netDest+"|"+nextHop+"|"+port.number;
    }
}
