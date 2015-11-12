public class TFRouterTableEntry {
    private String networkIPPrefix;
    private String nextHopIPPrefix;
    private String nextHopIP;
    private int portNumber;
    private TFSwitch LAN;
    private TFRouter nextHop;
    public TFPort port;

    public TFRouterTableEntry(String networkIPPrefix,String nextHopIPPrefix,int portNumber){
        this.networkIPPrefix = networkIPPrefix;
        this.nextHopIPPrefix = nextHopIPPrefix;
        this.nextHopIP = nextHopIPPrefix.equals("0.0.0.0") ? nextHopIPPrefix : nextHopIPPrefix.substring(0, nextHopIPPrefix.indexOf("/"));
        this.portNumber = portNumber;
        this.port = null;
        this.LAN = null;
    }

    public TFSwitch getLAN() {
        return LAN;
    }

    public void setLAN(TFSwitch lan) {
        LAN = lan;
    }

    public TFRouter getNextHop() {
        return nextHop;
    }

    public void setNextHop(TFRouter nextHop) {
        this.nextHop = nextHop;
    }

    public String getNetworkIPPrefix(){
        return this.networkIPPrefix;
    }

    public String getNextHopIPPrefix(){
        return this.nextHopIPPrefix;
    }

    public String getNextHopIP(){
        return this.nextHopIP;
    }

    public String toString(){
        return networkIPPrefix +"|"+ nextHopIPPrefix +"|"+ portNumber;
    }
}
