import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Optional;
import java.net.UnknownHostException;

public class TFRouter extends TFNetworkElement {
    public String name;
    private int numPorts;
    private List<TFPort> ports;
    private List<TFRouterTableEntry> routertable;

    public TFRouter(String name,int numPorts){
        this.name = name;
        this.numPorts = numPorts;
        ports = new ArrayList<TFPort>();
        routertable = new ArrayList<TFRouterTableEntry>();
    }

    public int getNumPorts(){
        return numPorts;
    }

    public void addPort(TFPort newPort){
        //TODO: validate port MAC and IP
        //TODO: validate numPorts and ports.Length
        //TODO: validate port by number
        ports.add(newPort);
    }

    public String toString(){
        String sPorts = "";
        String sTable = "";
        for (TFPort p : ports)
            sPorts += "\n\t"+p.toString();
        for (TFRouterTableEntry entry : routertable)
            sTable+= "\n\t"+entry.toString();
        return "Name:"+name+" Ports:"+numPorts + sPorts +" \n\tRouter Table"+ sTable;
    }

    public TFPort getPortByNumber(int portNumber){
        for (TFPort p : ports)
            if(p.number==portNumber)
                return p;
        return null;
    }

    public TFPort getPortByIP(String ip){
        for (TFPort p : ports)
            if(p.getIP().equals(ip))
                return p;
        return null;
    }

    public void addTableEntry(TFRouterTableEntry newEntry){
        //TODO: validate port?
        routertable.add(newEntry);
    }

    public void tuneRouterTable(Map<String,TFSwitch> switches,List<TFRouter> routers) throws UnknownHostException{
        for(TFRouterTableEntry r : routertable){
            String ipprefix = (r.getNextHopIPPrefix().equals("0.0.0.0")?r.getNetworkIPPrefix():r.getNextHopIPPrefix());
            String binaryIP = TFNetworkAddress.extractNetwork(ipprefix);
            TFSwitch e = switches.get(binaryIP);
            r.setLAN(e);
            Optional<TFRouter> opt = routers.stream().filter(rt -> rt.hasIP(r.getNextHopIP())).findFirst();
            if(opt.isPresent())
                r.setNextHop(opt.get());
        }
    }

    public boolean hasIP(String ip){
        for (TFPort p : ports)
            if(p.getIP().equals(ip))
                return true;
        return false;
    }

    public TFRouterTableEntry getRouteTo(String ip) throws UnknownHostException{
        for(TFRouterTableEntry r : routertable){
            if(r.getNetworkIPPrefix().equals(ip)){
                return r;
            }
        }
        return null;
    }

    public ARPPackage createARPRequest(int portNumber,String IP_dst){
        TFPort port = getPortByNumber(portNumber);
        return new ARPPackage(port.getMAC(),port.getIP(),IP_dst);
    }

    public ARPPackage doARPRequest(ARPPackage request){
        TFPort port = getPortByIP(request.IP_dst);
        setARPMACResponse(port.getMAC());
        setARPIPResponse(port.getIP());
        return super.doARPRequest(request);
    }

    public ICMPPackage doICMPRequest(ICMPPackage request) throws Exception{
        String dstNetwork = TFNetworkAddress.extractNetworkAsIPv4(request.IP_dst,request.CIDR_dst);
        TFRouterTableEntry route = getRouteTo(dstNetwork+"/"+request.CIDR_dst);
        ITFNetworkAddress port = route.port;
        boolean sameNetwork = port.isSameNetwork(request.IP_dst,request.CIDR_dst);
        String MAC_dst = this.searchMAC(request.IP_dst);
        ARPPackage arpRequest = null;
        ARPPackage arpResponse = null;
        int newTTL = request.TTL - 1;
        if(newTTL<=0){
            return new ICMPPackage(ICMPPackage.ICMPType.ICMPTimeExceeded,request.MAC_dst,request.MAC_src,request.IP_src,request.CIDR_src,request.IP_dst,request.CIDR_dst,8);
        }

        if(MAC_dst == null){
            if(sameNetwork){
                arpRequest = new ARPPackage(port.getMAC(),port.getIP(),request.IP_dst);
                arpResponse = route.getLAN().doARPRequest(arpRequest);
            }else{
                arpRequest = new ARPPackage(port.getMAC(),port.getIP(),route.getNextHopIP());
                arpResponse = route.getLAN().doARPRequest(arpRequest);
            }
            if(arpResponse!=null){
                addArpEntry(arpResponse.IP_src,arpResponse.MAC_src);
                MAC_dst = arpResponse.MAC_src;
            }
        }

        if(MAC_dst!=null){
            ICMPPackage icmpRequest = new ICMPPackage(port.getMAC(),MAC_dst,request.IP_src,request.CIDR_src,request.IP_dst,request.CIDR_dst);
            icmpRequest.TTL = newTTL;
            ICMPPackage icmpResponse = null;
            if(sameNetwork){
                icmpResponse = route.getLAN().doICMPRequest(icmpRequest);
            }else{
                icmpResponse = route.getNextHop().doICMPRequest(icmpRequest);
            }
            ICMPPackage response = new ICMPPackage(icmpResponse.type,request.MAC_dst,request.MAC_src,icmpResponse.IP_src,icmpResponse.CIDR_src,icmpResponse.IP_dst,icmpResponse.CIDR_dst,8);
            response.log(arpRequest.toString());
            response.log(arpResponse.toString());
            response.log(icmpRequest.toString());
            response.log(icmpResponse.toString());
            response.TTL = icmpResponse.TTL - 1;
            return response;
        }
        throw new Exception("Destination unreacheable");
    }
}
