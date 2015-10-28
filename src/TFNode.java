public class TFNode extends ITFNetworkElement {
    public String name;
    public String MAC;
    private String IP;
    private String IPPrefix;
    public TFRouter gateway;
    public String gatewayIP;

    public TFNode(String name,String MAC,String ipprefix,String gatewayIP){
        this.name=name;
        this.MAC = MAC;
        this.setIPrefix(ipprefix);
        this.gatewayIP = gatewayIP;
        this.gateway = null;
        this.arpMACResponse = this.MAC;
        this.arpIPResponse = this.getIP();
    }

    public void setIPrefix(String ipprefix){
        this.IPPrefix = ipprefix;
        this.IP = ipprefix.substring(0,ipprefix.indexOf("/"));
    }

    public String getIP(){
        return IP;
    }

    public String getIPPrefix(){
        return IPPrefix;
    }

    public String toString(){
        return "Name:"+name+" MAC:"+MAC+" IP/Prefix:"+IPPrefix+" gatewayIP:"+gatewayIP;
    }
}
