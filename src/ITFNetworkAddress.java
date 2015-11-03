import java.net.UnknownHostException;

public interface ITFNetworkAddress {
    public TFNetworkElement getOwner();
    public void setIPrefix(String ipprefix) throws UnknownHostException;
    public String getIP();
    public String getIPPrefix();
    public String getNetwork();
    public String getMAC();
    public void setMAC(String MAC);
    public String getName();
    public void setName(String name);
}
