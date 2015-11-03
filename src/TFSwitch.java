import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Optional;

public class TFSwitch {
    public String network;
    private List<ITFNetworkAddress> hosts;

    public TFSwitch(String network){
        this.network = network;
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

    public String toString(){
        String net = getNetwork() + "00000000000000000000000000000000";
        net = net.substring(0,32);
        String hs = "";
        for (ITFNetworkAddress host : hosts) {
            hs+="\n\t"+host;
        }
        return "switch["+TFNetworkAddress.binaryIPtoStringIPv4(net)+"]"+hs;
    }
}
