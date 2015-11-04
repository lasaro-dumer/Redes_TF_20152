import java.net.UnknownHostException;

public interface ITFNetworkAddress {
    public TFNetworkElement getOwner();
    public void setIPrefix(String ipprefix) throws UnknownHostException;
    public String getIP();
    public String getIPPrefix();
    public int getNetCIDR();
    public String getNetwork();
    public String getMAC();
    public void setMAC(String MAC);
    public String getName();
    public void setName(String name);
    public boolean isSameNetwork(String otherIP,int otherCIDR);
}
