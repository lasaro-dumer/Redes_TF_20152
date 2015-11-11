import java.net.UnknownHostException;

public class TFNode extends TFNetworkElement  implements ITFNetworkAddress{
    public int number;
    private TFNetworkAddress address;
    public TFRouter gateway;
    public String gatewayIP;
    private TFSwitch LAN;

    public TFNode(String name,String MAC,String ipprefix,String gatewayIP) throws UnknownHostException{
        this.address = new TFNetworkAddress();
        this.setMAC(MAC);
        this.setIPrefix(ipprefix);
        this.setName(name);
        this.gatewayIP = gatewayIP;
        this.gateway = null;
        this.LAN = null;
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

    public TFSwitch getLAN() {
        if(LAN == null)
            LAN = new TFSwitch(getNetwork(),getNetCIDR());
        return LAN;
    }

    public void setLAN(TFSwitch lan) {
        LAN = lan;
    }

    public String ping(String dstIP,int dstCIDR) throws Exception{
        return ping(dstIP,dstCIDR,0);
    }

    public String ping(String dstIP,int dstCIDR,int printLevel) throws Exception{
        StringBuilder sb = new StringBuilder();
        String MAC_dst = this.searchMAC(dstIP);
        boolean sameNetwork = address.isSameNetwork(dstIP,dstCIDR);        
        if(MAC_dst == null){
            ARPPackage arpRequest = null;
            ARPPackage arpResponse = null;
            if(sameNetwork){
                arpRequest = new ARPPackage(getMAC(),getIP(),dstIP);
                arpResponse = getLAN().doARPRequest(arpRequest);
            }else{
                arpRequest = new ARPPackage(getMAC(),getIP(),gatewayIP);
                arpResponse = gateway.doARPRequest(arpRequest);
            }
            if(arpResponse!=null){
                sb.append(arpRequest.toString()+"\n");
                sb.append(arpResponse.toString()+"\n");
                MAC_dst = arpResponse.MAC_src;
            }
        }

        if(MAC_dst!=null){
            ICMPPackage icmpRequest = null;
            ICMPPackage icmpResponse = null;
            if(sameNetwork){
                icmpRequest = new ICMPPackage(getMAC(),MAC_dst,getIP(),getNetCIDR(),dstIP,dstCIDR);
                icmpResponse = getLAN().doICMPRequest(icmpRequest);
            }else{
                icmpRequest = new ICMPPackage(getMAC(),MAC_dst,getIP(),getNetCIDR(),dstIP,dstCIDR);
                icmpResponse = gateway.doICMPRequest(icmpRequest);
            }
            if(icmpRequest!=null)
                sb.append(icmpRequest.toString()+"\n");
            if(icmpResponse!=null)
                sb.append(icmpResponse.toString()+"\n");
            return sb.toString();
        }else {
            throw new Exception("Ping failed");
        }
    }

    public String toString(){
        return "Name:"+getName()+" MAC:"+getMAC()+" IP/Prefix:"+getIPPrefix()+" gatewayIP:"+gatewayIP;
    }
}
