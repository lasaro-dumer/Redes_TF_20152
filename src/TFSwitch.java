import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Optional;
import java.net.UnknownHostException;

public class TFSwitch {
    public String network;
    public int CIDR;
    private List<ITFNetworkAddress> hosts;

    public TFSwitch(String network,int CIDR){
        this.network = network;
        this.CIDR = CIDR;
        this.hosts = new ArrayList<ITFNetworkAddress>();
    }

    public String getNetwork(){
        return this.network;
    }

    public void addHost(ITFNetworkAddress host){
        //TODO: validate host;
        this.hosts.add(host);
    }

    public ITFNetworkAddress getHostByIP(String IP){
        Optional<ITFNetworkAddress> opt = hosts.stream().filter(e -> e.getIP().equals(IP)).findFirst();
        if(opt.isPresent())
            return opt.get();
        return null;
    }

    public ITFNetworkAddress getHostByName(String name){
        Optional<ITFNetworkAddress> opt = hosts.stream().filter(e -> e.getName().equals(name)).findFirst();
        if(opt.isPresent())
            return opt.get();
        return null;
    }

    public ARPPackage doARPRequest(ARPPackage request) throws Exception{
        ITFNetworkAddress dst = getHostByIP(request.IP_dst);
        if(dst!=null)
            if(dst instanceof TFNode)
                return ((TFNode)dst).doARPRequest(request);
            else if (dst instanceof TFPort)
                return ((TFPort)dst).getOwner().doARPRequest(request);
        throw new Exception("Destination unreacheable");
    }

    public ICMPPackage doICMPRequest(ICMPPackage request) throws Exception{
        ITFNetworkAddress dst = getHostByIP(request.IP_dst);
        if(dst!=null)
            return new ICMPPackage(ICMPPackage.ICMPType.ICMPEchoReply,dst.getMAC(),request.MAC_src,dst.getIP(),dst.getNetCIDR(),request.IP_src,request.CIDR_src,8);
        throw new Exception("Destination unreacheable");
    }

    public String getNetworkAsIPv4CIDR() throws UnknownHostException{
        return TFNetworkAddress.getNetworkAsIPv4(getNetwork())+"/"+this.CIDR;
    }

    public String toString(){
        String hs = "";
        for (ITFNetworkAddress host : hosts) {
            hs+="\n\t"+host;
        }
        String net = "ERROR";
        try{net = getNetworkAsIPv4CIDR();}catch(Exception e){}
        return "switch["+net+"]"+hs;
    }
}
