import java.net.UnknownHostException;

public class TFNode extends TFNetworkElement  implements ITFNetworkAddress{
    public int number;
    private TFNetworkAddress address;
    public TFRouter gateway;
    public String gatewayIP;

    public TFNode(String name,String MAC,String ipprefix,String gatewayIP) throws UnknownHostException{
        this.address = new TFNetworkAddress();
        this.setMAC(MAC);
        this.setIPrefix(ipprefix);
        this.setName(name);
        this.gatewayIP = gatewayIP;
        this.gateway = null;
        setARPMACResponse(this.getMAC());
        setARPIPResponse(this.getIP());
    }

    public TFNetworkElement getOwner() {
        return this;
    }

    public void setIPrefix(String ipprefix) throws UnknownHostException {
        address.setIPrefix(ipprefix);
    }

    public String getIP() {
        return address.getIP();
    }

    public String getIPPrefix() {
        return address.getIPPrefix();
    }

    public String getNetwork() {
        return address.getNetwork();
    }

    public String getMAC() {
        return address.getMAC();
    }

    public void setMAC(String MAC) {
        address.setMAC(MAC);
    }

    public String getName() {
        return address.getName();
    }

    public void setName(String name) {
        address.setName(name);
    }

    public String ping(String dstIP){
        return ping(dstIP,0);
    }

    public String ping(String dstIP,int printLevel){
        StringBuilder sb = new StringBuilder();
        /*

        */
        return sb.toString();
    }

    public String toString(){
        return "Name:"+getName()+" MAC:"+getMAC()+" IP/Prefix:"+getIPPrefix()+" gatewayIP:"+gatewayIP;
    }
}
