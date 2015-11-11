public class TFRouterTableEntry {
    private String networkIPPrefix;
    private String nextHopIP;
    private int portNumber;
    private TFSwitch LAN;
    public TFPort port;

    public TFRouterTableEntry(String networkIPPrefix,String nextHopIP,int portNumber){
        this.networkIPPrefix = networkIPPrefix;
        this.nextHopIP = nextHopIP;
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

    public String getNetworkIPPrefix(){
        return this.networkIPPrefix;
    }
    
    public String toString(){
        return networkIPPrefix +"|"+ nextHopIP +"|"+ portNumber;
    }
}
