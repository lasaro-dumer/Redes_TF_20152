import java.net.UnknownHostException;

public class TFPort implements ITFNetworkAddress{

    public int number;
    private TFNetworkAddress address;
    private TFRouter owner;

    public TFPort(TFRouter owner) {
        this.address = new TFNetworkAddress();
        this.setName(owner.name);
        this.owner = owner;
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

    public int getNetCIDR(){
        return address.getNetCIDR();
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

    public boolean isSameNetwork(String otherIP,int otherCIDR){
        return address.isSameNetwork(otherIP,otherCIDR);
    }

    public String toString() {
        return "[" + number + "]MAC: " + getMAC() + " IP/Prefix:" + getIPPrefix();
    }

    public TFNetworkElement getOwner() {
        return owner;
    }
}
